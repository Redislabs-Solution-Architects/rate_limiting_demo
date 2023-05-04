package com.bestarch.demo.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.bestarch.demo.util.RateLimitSessionCallback;
import com.bestarch.demo.util.Utility;

//@Order(1)
//@Component
public class RateLimitingFilter implements Filter, ErrorHandler {

	private Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

	private static String ERR_MSG = "Request limit reached for user: %s. Try after sometime";

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private Utility util;

	@PostConstruct
	public void name() {
		System.out.println();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Integer eligiblePermits = 0;
		String loggedInUser = null;
		String clientKey = null;

		if (isThrottlingRequired(req.getRequestURI())) {
			loggedInUser = util.getUsername();
			eligiblePermits = util.getPermitsForLoggedInUser(loggedInUser);
			clientKey = loggedInUser + ":" + LocalDateTime.now().getMinute();
			String permit = (String) redisTemplate.opsForValue().get(clientKey);
			if (ObjectUtils.isEmpty(permit) || (Integer.valueOf(permit) < eligiblePermits)) {
				handleRequest(clientKey);
			} else {
				logger.error(String.format(ERR_MSG, loggedInUser));
				handle(resp, loggedInUser, 429, ERR_MSG);
				return;
			}
			
		}
		chain.doFilter(request, response);
	}
	
	private boolean isThrottlingRequired(String uri) {
		if (uri.equalsIgnoreCase("/api/prospects")) {
			return true;
		}
		return false;
	}

	private void handleRequest(String clientKey) {
		redisTemplate.execute(RateLimitSessionCallback.builder().clientKey(clientKey).build());
	}
}

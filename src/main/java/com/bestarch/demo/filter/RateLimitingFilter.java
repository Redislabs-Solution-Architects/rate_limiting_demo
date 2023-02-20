package com.bestarch.demo.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.bestarch.demo.util.AppointmentUtil;
import com.bestarch.demo.util.RateLimitSessionCallback;

@Order(2)
@Component
public class RateLimitingFilter implements Filter, ErrorHandler {
	
	private Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);
	
	private static String ERR_MSG = "Request limit reached for user: %s. Try after sometime";
	
	private static String DEFAULT_USER_PREF = "ANONYMOUS_";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private AppointmentUtil appointmentUtil;

	@Value("${api.max.requests.minute:20}")
	private Integer API_MAX_REQUESTS_MINUTE;
	
	@Value("${api.default.requests.minute:5}")
	private Integer API_DEFAULT_REQUESTS_MINUTE;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Integer eligiblePermits = API_DEFAULT_REQUESTS_MINUTE;
		String loggedInUser = null;
		String clientKey = null;
		
		if (appointmentUtil.isAuthenticated()) {
			eligiblePermits = API_MAX_REQUESTS_MINUTE;
			loggedInUser = appointmentUtil.getUsername();
		} else {
			loggedInUser = DEFAULT_USER_PREF + req.getRemoteAddr();
		}
		
		clientKey = loggedInUser + ":" + LocalDateTime.now().getMinute();
		String permit = (String) redisTemplate.opsForValue().get(clientKey);
		if (ObjectUtils.isEmpty(permit) || (Integer.valueOf(permit) < eligiblePermits)) {
			handleRequest(clientKey);
		} else {
			logger.error(String.format(ERR_MSG, loggedInUser));
			handle(resp, loggedInUser, 429, ERR_MSG);
			return;
		}
		chain.doFilter(request, response);
	}

	private void handleRequest(String clientKey) {
		redisTemplate.execute(RateLimitSessionCallback.builder().clientKey(clientKey).build());
	}
}

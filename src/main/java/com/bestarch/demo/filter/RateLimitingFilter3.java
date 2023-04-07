/*
package com.bestarch.demo.filter;

import com.bestarch.demo.util.RateLimitSessionCallback;
import com.bestarch.demo.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RateLimitingFilter extends OncePerRequestFilter implements ErrorHandler {

	private Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

	private static String ERR_MSG = "Request limit reached for user: %s. Try after sometime";

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private Utility util;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Integer eligiblePermits = 0;
		String loggedInUser = null;
		String clientKey = null;

		if (util.isAuthenticated()) {
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
			chain.doFilter(request, response);
		}
		return;
	}

	private void handleRequest(String clientKey) {
		redisTemplate.execute(RateLimitSessionCallback.builder().clientKey(clientKey).build());
	}
}
*/

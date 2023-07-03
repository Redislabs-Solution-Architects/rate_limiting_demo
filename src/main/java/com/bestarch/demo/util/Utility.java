package com.bestarch.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Utility {
	
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;

	@Value("${api.max.requests.standard.minute:5}")
	private Integer STANDARD_API_MAX_REQUESTS_MINUTE;

	@Value("${api.max.requests.premium.minute:15}")
	private Integer PREMIUM_API_MAX_REQUESTS_MINUTE;
	
	public String getUsername() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}
	
	public boolean isStandard(String username) {
		String val = (String) redisTemplate.opsForHash().get("customer:"+username, "plan");
		if ("Standard".equalsIgnoreCase(val)) {
			return true;
		}
		return false;
	}
	
	public boolean isPremium(String username) {
		String val = (String) redisTemplate.opsForHash().get("customer:"+username, "plan");
		if ("Premium".equalsIgnoreCase(val)) {
			return true;
		}
		return false;
	}

	public Integer getPermitsForLoggedInUser(String loggedInUser) {
		Integer eligiblePermits;
		if (isPremium(loggedInUser)) {
			eligiblePermits = PREMIUM_API_MAX_REQUESTS_MINUTE;
		} else {
			eligiblePermits = STANDARD_API_MAX_REQUESTS_MINUTE;
		}
		return eligiblePermits;
	}

}

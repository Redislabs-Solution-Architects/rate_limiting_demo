package com.bestarch.demo.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import com.bestarch.demo.domain.StatusMessage;
import com.bestarch.demo.util.Utility;

@Controller
public class StatusController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private Utility util;

	@MessageMapping("/quota")
	@SendTo("/topic/messages")
	public StatusMessage send(@Payload String username) throws Exception {
		String clientKey = username + ":" + LocalDateTime.now().getMinute();
		String usedPermit = (String) redisTemplate.opsForValue().get(clientKey);
		Integer eligiblePermits = util.getPermitsForLoggedInUser(username);
		Integer availablePermits = eligiblePermits;
		if (!ObjectUtils.isEmpty(usedPermit)) {
			availablePermits = eligiblePermits - Integer.valueOf(usedPermit);
		}
		return StatusMessage.builder().quota(Integer.valueOf(availablePermits)).build();
	}

}

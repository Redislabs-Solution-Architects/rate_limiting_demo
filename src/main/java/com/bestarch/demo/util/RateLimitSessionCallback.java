package com.bestarch.demo.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import lombok.Builder;

@Builder
public class RateLimitSessionCallback implements SessionCallback<List<Object>> {

	private String clientKey;

	public RateLimitSessionCallback(String clientKey) {
		super();
		this.clientKey = clientKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
		int retryLimit = 10;
		long delay = 10;
		List<Object> results = null;
		int i = 0;
		while (results == null && i < retryLimit) {
			try {
				Thread.sleep(delay << i);
			} catch (InterruptedException e) {
				new RuntimeException(e);
			}
			operations.multi();
			operations.opsForValue().increment((K) clientKey);
			operations.expire((K) clientKey, 59, TimeUnit.SECONDS);
			results = operations.exec();
			i++;
		}
		return results;
	}

}

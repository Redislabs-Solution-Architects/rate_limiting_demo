package com.bestarch.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
class RedisConfiguration {
	
	@Value("${spring.redis.host:localhost}")
	private String server;

	@Value("${spring.redis.port:6379}")
	private String port;

	@Value("${spring.redis.password}")
	private String pswd;
	
	@Value("${spring.redis.auth}")
	private boolean requiresPswd;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(server, Integer.valueOf(port));
		if (requiresPswd) {
			RedisPassword rp = RedisPassword.of(pswd);
			redisStandaloneConfiguration.setPassword(rp);
		}
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}
	
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();

		template.setConnectionFactory(redisConnectionFactory());

		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		template.setValueSerializer(stringSerializer);
		template.setHashValueSerializer(stringSerializer);

		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		
		return template;
	}
	
}
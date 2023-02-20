package com.bestarch.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

	@Id
	private String username;
	private String contactNo;
	private String address;
	private Integer age;
}
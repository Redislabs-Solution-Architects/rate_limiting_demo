package com.bestarch.demo.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app")
@Setter
@Getter
@NoArgsConstructor
public class Users {
	
	private List<String> users;
	
	public boolean contains(String user) {
		return users.contains(user);
	}
	
}

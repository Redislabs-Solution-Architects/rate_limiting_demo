package com.bestarch.demo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.bestarch.demo.filter.APIKeyFilter;
import com.bestarch.demo.filter.RateLimitingFilter;

@SpringBootApplication
public class RateLimitingApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SpringApplication.run(RateLimitingApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilterBean() {
		FilterRegistrationBean <RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
		RateLimitingFilter rateLimitingFilter = new RateLimitingFilter();
		registrationBean.setFilter(rateLimitingFilter);
		registrationBean.addUrlPatterns("/api/prospects");
		registrationBean.setOrder(2); //set precedence
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<APIKeyFilter> apiKeyFilterBean() {
		FilterRegistrationBean <APIKeyFilter> registrationBean = new FilterRegistrationBean<>();
		APIKeyFilter apiKeyFilter = new APIKeyFilter();
		registrationBean.setFilter(apiKeyFilter);
		registrationBean.addUrlPatterns("/api2");
		registrationBean.setOrder(1); //set precedence
		return registrationBean;
	}
}

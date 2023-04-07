package com.bestarch.demo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.bestarch.demo.filter.RateLimitingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RateLimitingApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SpringApplication.run(RateLimitingApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<RateLimitingFilter> filterRegistrationBean() {
		FilterRegistrationBean <RateLimitingFilter> registrationBean = new FilterRegistrationBean();
		RateLimitingFilter rateLimitingFilter = new RateLimitingFilter();
		registrationBean.setFilter(rateLimitingFilter);
		registrationBean.addUrlPatterns("/prospects");
		registrationBean.setOrder(1); //set precedence
		return registrationBean;
	}
}

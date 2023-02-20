package com.bestarch.demo.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppointmentUtil {
	
	public String getUsername() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}

}

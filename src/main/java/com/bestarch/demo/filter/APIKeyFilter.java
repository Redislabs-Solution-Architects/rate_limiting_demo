package com.bestarch.demo.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Order(1)
@Component
public class APIKeyFilter implements Filter, ErrorHandler {
	
	Logger logger = LoggerFactory.getLogger(APIKeyFilter.class);

	private static String ERR_MSG = "No API key is provided";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		/*Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = req.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = req.getHeader(key);
	        map.put("header: " + key, value);
	    }
		logger.info("Request headers:::: {}", map);
		*/

		String apiKey = req.getHeader("API-KEY");
		logger.info("API key from request headers:::: {}", apiKey);
		if (ObjectUtils.isEmpty(apiKey)) {
			handle(resp, apiKey, 400, ERR_MSG);
			return;
		}
		chain.doFilter(request, response);
	}

}
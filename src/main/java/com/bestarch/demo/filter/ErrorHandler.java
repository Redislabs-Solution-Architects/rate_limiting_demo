package com.bestarch.demo.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface ErrorHandler {

	default void handle(HttpServletResponse resp, String loggedInUser, int httpCode, String errMsg) throws IOException {
		resp.setHeader("Content-Type", "application/json");
		resp.setStatus(httpCode);
		resp.getOutputStream().write(String.format(errMsg, loggedInUser).getBytes());
	}

}

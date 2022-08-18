package com.tweetapp.configs;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Parichay Gupta
 */
public class CorsFilter implements Filter {

//	Overridden method
	@Override
	public void destroy() {
	}

//	Overridden method
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * @allows cross origin
	 * @Header allow all origins mentioned as Cros
	 * @Header allow all REST methods
	 * @Header the session will last for 3600 seconds i.e. 30 mins
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		final HttpServletResponse response = (HttpServletResponse) servletResponse;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE,OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type,x-requested-with");
		response.setHeader("Access-Control-Max-Age", "3600");
		if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		} else {
			// forwarding the request to original destination
			chain.doFilter(servletRequest, servletResponse);
		}

	}

}

package com.video.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class VideoRequestFilter implements Filter {
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filter) throws IOException, ServletException {
		 req.getRequestDispatcher("/error.jsp").forward(req, resp);
		//filter.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
		
	}
}

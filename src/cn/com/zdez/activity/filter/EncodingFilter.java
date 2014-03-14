package cn.com.zdez.activity.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 实现对请求和响应的自动编码设置
 */
public class EncodingFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	public void destroy() {

	}

}

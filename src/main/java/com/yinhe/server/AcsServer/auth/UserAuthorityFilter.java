package com.yinhe.server.AcsServer.auth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yinhe.server.AcsServer.backbean.LoginUserStatusInfo;
import com.yinhe.server.AcsServer.util.Resources;

@RequestScoped
public class UserAuthorityFilter implements Filter{

	@Inject
	private LoginUserStatusInfo loginUserStatusInfo;
	@Inject
	private Logger log;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String path = httpServletRequest.getServletPath();
			log.info("[doFilter] path = " + path);
			if(checkIsNotCheckPath(path)){
				log.info("[doFilter] checkIsNotCheckPath");
				chain.doFilter(request, response);
				return;
			}
			if (null == loginUserStatusInfo) {
				log.info("[doFilter] null == loginUserStatusInfo");
				redirect(request, response, "/index.jsf");
				return;
			}
			
			if (!path.contains("/admin/")) {
				log.info("[doFilter] path invalid");
				chain.doFilter(request, response);
				return;
			}
		/*	if(path.contains("default_device.jsf")){
				redirect(request, response, "/admin/main_view.jsf");
			}
			
			if(path.contains("default_user.jsf")){
				redirect(request, response, "/admin/users.jsf");
			}
			*/
			String roleName = loginUserStatusInfo.getM_role();
			String userName = loginUserStatusInfo.getM_userName();
			
			if(Resources.isNullOrEmpty(roleName) || Resources.isNullOrEmpty(userName)){
				log.info("[doFilter] not login");
				redirect(request, response, "/index.jsf");
				return;
			}
			chain.doFilter(request, response);
			
		}
	}
	
	
	private boolean checkIsNotCheckPath(String path) {
		if (path.endsWith(".js") || path.endsWith(".css") || (path.endsWith(".jpg"))) {
			return true;
		}
		
		if (path.contains("/css") || path.contains("assets/") || path.contains("/images") || path.contains("/img")
				|| path.contains("/js") || path.contains("/highlighter")) {
			return true;
		}
		return false;
	}
	
	private void redirect(ServletRequest request, ServletResponse response, String path) throws IOException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + path);
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}


	@Override
	public void destroy() {
		
	}

}

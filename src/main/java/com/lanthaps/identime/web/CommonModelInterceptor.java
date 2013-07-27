package com.lanthaps.identime.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lanthaps.identime.service.*;

/**
 * The CommonModelInterceptor intercepts all requests after the handler has run
 * to ensure that common properties like the base URL are available to all views.  
 * @author Andrew Miller
 */
public class CommonModelInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	public void setSettingService(SettingService settingService) {
		this.settingService = settingService;
	}
	@Autowired
	public void setCsrfTokenService(CSRFTokenService tokenService) {
	  this.csrfTokenService = tokenService;
	}
	private SettingService settingService;
	private CSRFTokenService csrfTokenService;
	
	@Override public void postHandle(
			HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) {
	  if (!modelAndView.getViewName().startsWith("redirect:")) {
		  modelAndView.addObject("baseURL", settingService.loadStringSetting(SettingServiceImpl.baseURL));
		  modelAndView.addObject("siteName", settingService.loadStringSetting(SettingServiceImpl.siteName));
		  modelAndView.addObject("headerLogo", settingService.loadStringSetting(SettingServiceImpl.headerLogo));
		  modelAndView.addObject("csrfToken", csrfTokenService.getSessionToken(request.getSession()));
	  }
	}
}

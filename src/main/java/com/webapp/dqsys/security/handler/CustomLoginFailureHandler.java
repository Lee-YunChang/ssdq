package com.webapp.dqsys.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.webapp.dqsys.mngr.service.CommonService;
import com.webapp.dqsys.util.MessageUtils;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CommonService commonService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		Map<String, Object> param = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String errormsg = exception.getMessage();
		
		String author = "";
		int failCnt = 0;
		
		Map<String, Object> loginInfo = commonService.selLoginInfo(username);
		
		if(loginInfo != null) {
			author = loginInfo.get("AUTHOR_CODE").toString();
			failCnt = Integer.parseInt(loginInfo.get("LOGIN_FAIL_CNT").toString());
		}
		
		param.put("username", username);
		param.put("failCnt", failCnt + 1);
		
		if(failCnt < 5) {
			if(exception instanceof BadCredentialsException) {
				if(author.equalsIgnoreCase("ROLE_USER")) {
					commonService.updateLoginFailCnt(param);
				}
				errormsg = MessageUtils.BADCREDENTIALS;
			} else if(exception instanceof CredentialsExpiredException) {
				errormsg = MessageUtils.CREDENTIALSEXPIRED;
			} else if(exception instanceof DisabledException) {
				errormsg = MessageUtils.DISALED;
			} else if(exception instanceof LockedException) {
				errormsg = MessageUtils.LOCKED;
			} else if(exception instanceof InternalAuthenticationServiceException) {
		    	errormsg = MessageUtils.NOTUSER;
			}
		} else {
			errormsg = MessageUtils.LOGINFAILOVER;
		}

		request.setAttribute("username", username);
		request.setAttribute("password", password);
 
		request.getRequestDispatcher("/login?error=true&errormsg="+errormsg).forward(request, response);
		
	 }

}

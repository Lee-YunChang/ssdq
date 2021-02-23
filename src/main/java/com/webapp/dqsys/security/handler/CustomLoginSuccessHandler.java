package com.webapp.dqsys.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.webapp.dqsys.mngr.mapper.UserMapper;
import com.webapp.dqsys.mngr.service.CommonService;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.security.domain.SecurityMember;
import com.webapp.dqsys.util.SangsUtil;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CommonService commonService;
	
	@Autowired
    UserMapper userMapper;
	
	public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        
    	Map<String, Object> param = new HashMap<String, Object>();
    	((SecurityMember)authentication.getPrincipal()).setIp(SangsUtil.getClientIp(request));

        HttpSession session = request.getSession();
        
        Map<String, Object> userInfo = userMapper.selLoginInfo(SangsUtil.getUserName());
        int loginCnt = Integer.parseInt(userInfo.get("LOGIN_FAIL_CNT").toString());
        
        if (session != null) {
        	/*
			String redirectUrl = (String) session.getAttribute("prevPage");
            if (redirectUrl != null) {
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            } 
        	*/
        	String userId = SangsUtil.getUserName();
        	Member member = userMapper.readUser(userId);
			session.setAttribute("member", member);
			if(loginCnt == 5) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('계정이 잠겨있습니다. \\n관리자에게 문의하세요.'); location.href='/login';</script>");
				out.flush();
			} else {
				try {
					//로그인 실패 카운트 초기화
					param.put("username", SangsUtil.getUserName());
					param.put("failCnt", 0);
					commonService.updateLoginFailCnt(param);
					
					commonService.setMenuList(request, authentication);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				super.onAuthenticationSuccess(request, response, authentication);
			}
        	
    		
        } else {
        	try {
				commonService.setMenuList(request, authentication);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
    
    
}

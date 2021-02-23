package com.webapp.dqsys.mngr.interceptor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.webapp.dqsys.mngr.mapper.CommonMapper;
import com.webapp.dqsys.mngr.mapper.UserMapper;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.util.SangsUtil;

/**
 * 필요 없으면 지워도 됩니다. 
 * @author gtman5
 *
 */
@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
    UserMapper userMapper;
	
	@Autowired
    CommonMapper commonMapper;
	
	@Value("${server.language.kind}")
	private String lang; 
	
	@Value("${server.mode}")
	private String mode; 	
	
    
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		
		List<String> userAuthor= new ArrayList<>();
		List<Map<String,Object>> menuAuthor = new ArrayList<>();

        if (!StringUtils.isEmpty(request.getParameter("link"))) {
			return true;
		}

		//log.trace("===== Debug Interceptor Start =====");

        //if (log.isInfoEnabled()) { 
			if(request.getRequestURI().startsWith("/") 
					&& !request.getRequestURI().startsWith("/img") 
					&& !request.getRequestURI().startsWith("/js") 
					&& !request.getRequestURI().startsWith("/vendor")
					&& !request.getRequestURI().startsWith("/css")
					&& !request.getRequestURI().startsWith("/vendor")
					&& !request.getRequestURI().startsWith("/fonts")
					&& !request.getRequestURI().startsWith("/images")
					&& !request.getRequestURI().startsWith("/error")) {
				
				try {
					if(member == null) {
						String userId = SangsUtil.getUserName();
						if(!"".equals(userId) && userId != null) {
							member = userMapper.readUser(userId);
							session.setAttribute("member", member);
							session.setAttribute("lang", lang);
						}
						
					} else {
						userAuthor = userMapper.readAuthority(member.getUserId());
						menuAuthor = commonMapper.selectAuthor();
					}
				} catch (Exception e) {
					log.debug("session Exception: ", e.toString());
					// TODO: handle exception
				}
				
				Map<String, String> params = new HashMap<String, String>();
				
				params.put("requestUri", request.getRequestURI().toString());
				params.put("requestUrl", request.getRequestURL().toString());
				params.put("Method", request.getMethod().toString());
				
				if("dev".equals(mode)) {
					params.put("localHost", "");
					params.put("localMacAddr", "");
				}else {
					params.put("localHost", InetAddress.getLocalHost().toString());
					params.put("localMacAddr", getLocalMacAddress());
				}
				params.put("lang", lang);
				
				if(member != null) {
					params.put("userId", member.getUserId());
					params.put("insttCode", member.getInsttCode());
					
					String URI = params.get("requestUri");
					
					//사용자 권한에 따른 페이지 이동 제한
					if(!(userAuthor.get(0).equalsIgnoreCase("ADMIN"))) {
						for (int i = 0; i < menuAuthor.size(); i++) {
							String menu_url = menuAuthor.get(i).get("MENU_URL").toString();
							String author_code = menuAuthor.get(i).get("AUTHOR_CODE").toString();
							String auCode1 =""; 
							String auCode2 = "";
							
							if(author_code.equalsIgnoreCase("ADMIN,USER")){
								int idx = author_code.indexOf(",");
								auCode1 = author_code.substring(0, idx); 
								auCode2 = author_code.substring(idx+1);
								
							}
							
							if(URI.equalsIgnoreCase(menu_url)) {
								if(!(userAuthor.get(0).equalsIgnoreCase(auCode2))) {
									//메인페이지로 리다이렉트
									response.sendRedirect("/mngr/main");
									return false;
								}
							}
							
						}
						
					}
					
				}else {
					params.put("userId", "");
					params.put("insttCode", "");
				}
				log.info("RequestURI \t: {}", params.get("requestUri"));
				log.info("RequestURL \t: {}", params.get("requestUrl"));
				log.info("Method \t: {}", params.get("Method"));
				log.info("LocalHost \t: {}", params.get("localHost"));
				log.info("LocalMacAddr \t: {}", params.get("localMacAddr"));
				log.info("UserId \t: {}", params.get("userId"));
				log.info("UserId \t: {}", params.get("insttCode"));
				log.info("Lang \t: {}", params.get("lang"));
				userMapper.insertActionHistory(params);
			}
		//}

		Enumeration<String> nameEnum = request.getParameterNames();

		while (nameEnum.hasMoreElements()) {
			String name = nameEnum.nextElement();
			String[] values = request.getParameterValues(name);
			for (String value : values) {
				log.debug("Param: {}={}.", name, value);
			}
		}

		if (request instanceof MultipartRequest) {
			MultipartRequest mrequest = (MultipartHttpServletRequest)request;
			Iterator<String> fileIt = mrequest.getFileNames();

			while (fileIt.hasNext()) {
				String name = fileIt.next();
				log.debug("File: {}={}.", name, mrequest.getFile(name).getOriginalFilename());
			}
		}

		log.trace("===== Debug Interceptor End =====");

		return true;
		
	}
	
	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        super.afterCompletion(request, response, handler, ex);
    }
    
    public String getLocalMacAddress() {
     	String result = "";
    	InetAddress ip;

    	try {
    		ip = InetAddress.getLocalHost();
    		System.out.println("ip : "+ip);
    		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
    		byte[] mac = network.getHardwareAddress();
    	    System.out.println(mac.length);
    		StringBuilder sb = new StringBuilder();
    		for (int i = 0; i < mac.length; i++) {
    			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
    		}
    			result = sb.toString();
    	} catch (UnknownHostException e) {
    		e.printStackTrace();
    	} catch (SocketException e){
    		e.printStackTrace();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	    
    	return result;
     }
    
}
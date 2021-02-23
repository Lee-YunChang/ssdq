package com.webapp.dqsys.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.security.domain.SecurityMember;

public class SangsUtil {

	public static boolean isEmpty(Object obj) {
		if(obj == null)
			return true;
		
		String val = "";
		
		if(obj instanceof java.math.BigDecimal) {
			double d = ((java.math.BigDecimal)obj).doubleValue();
			val = String.valueOf(d);
		} else if(obj instanceof Integer) {
			int i = ((Integer)obj).intValue();
			val = String.valueOf(i);
		} else if(obj instanceof java.lang.Double) {
			double i = ((Double)obj).doubleValue();
			val = String.valueOf(i);
		}  else if(obj instanceof java.lang.Long) {
			double i = ((Long)obj).longValue();
			val = String.valueOf(i);
		} else {
			val = (String)obj;
		}
		if(val.equals("")){
			return true;
		} else {
		    return false;
		}
	}
	
	public static String getUserName() {
		String userName = "";
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			userName = ((SecurityMember) principal).getUsername();
			return userName;
		} else {
			return "Guest";
		}
	}
	
	public static String getInsttCode() {
		String insttCode = "";
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			insttCode = ((SecurityMember) principal).getInsttCode();
			return insttCode;
		} else {
			return null;
		}
	}
	
	public static String getAuth() {
		
		String authCode = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<GrantedAuthority> authorities = ((SecurityMember) principal).getAuthorities();
		
		// 유저 권한 체크 
		for(Object o : authorities) {
 			authCode = o.toString();
		} 
		
		if(authorities != null) {
			return authCode;
		} else {
			return null;
		}
	}
	
	public static String getMenuList() {
		
		String json = null;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			
			try {
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				ObjectMapper mapper = new ObjectMapper(); // parser
				resultMap.put("menuList", ((SecurityMember) principal).getMenuList());
				json = mapper.writeValueAsString(resultMap);
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return json;
		} else {
			return null;
		}
		
	}
	
	public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("WL-Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_CLIENT_IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getRemoteAddr();
         }
         return ip;
    }
	
	
	/**
     * null 일 때 공백 반환한다.
     * @param obj
     * @return
     */
    public static String nchk(Object obj) {
        String rtnVal = "";
        if(obj != null){
        	rtnVal = obj.toString().trim();
        }
        return rtnVal;
    }

    /**
     * null 일 때 0 반환한다.
     * @param obj
     * @return
     */
    public static String nchkn(Object obj) {
        String rtnVal = "0";
        if(obj != null && !"".equals(obj.toString().trim()))
        	rtnVal = obj.toString().trim();
        return rtnVal;
    }
    
    /**
     * null 일 때 int 0 반환한다.
     * @param obj
     * @return
     */
    public static int nchkint(Object obj) {
    	int rtnVal = 0;
    	if(obj != null)
    		rtnVal = Integer.parseInt(nchkn(obj));
    	return rtnVal;
    }
    
    /**
     * null 일 때 double 0 반환한다.
     * @param obj
     * @return
     */
    public static double nchkdouble(Object obj) {
    	double rtnVal = 0;
    	if(obj != null)
    		rtnVal = Double.parseDouble(nchkn(obj));
    	return rtnVal;
    }
    
    /**
     * null 일 때 대체 문자열 반환한다.
     * @param obj
     * @return
     */
    public static String nchknR(Object obj, String R) {
        String rtnVal = R;
        if(obj != null)
        	rtnVal = obj.toString().trim();
        return rtnVal;
    }
    /**
     * 날짜 포맷에 맞는지 체크
     * @param num
     * @return
     */
    
    public static boolean dateFormatCheck(String date, String format) {
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(format);
        dateFormatParser.setLenient(false);
        try {
            dateFormatParser.parse(date);
            return true;
        } catch (Exception Ex) {
            return false;
        }
    }
}

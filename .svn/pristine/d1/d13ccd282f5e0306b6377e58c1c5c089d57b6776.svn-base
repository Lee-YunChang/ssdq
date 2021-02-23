package com.webapp.dqsys.security.domain;

import java.util.List;

import org.springframework.security.core.userdetails.User;

public class SecurityMember extends User{
	
	private static final long serialVersionUID = 1L;
    
	private String ip;
	
	private String insttCode;
	
    private List<?> menuList;
    
	public String getInsttCode() {
		return insttCode;
	}

	public void setInsttCode(String insttCode) {
		this.insttCode = insttCode;
	}

	public SecurityMember(Member member) {
        super(member.getUserId(), member.getUserPassword(), member.getAuthorities());
        this.setInsttCode(member.getInsttCode());
    }
	
    public String getIp() {
        return ip;
    }
 
    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<?> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<?> menuList) {
		this.menuList = menuList;
	}

}

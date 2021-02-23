package com.webapp.dqsys.security.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class Member {
	
    private String insttCode;
    
    private String userId;
    
    private String userPassword;
    
    private String userNm;
    
    private String chrgDept;
    
    private String recentConectDt;
    
    private String recentConectIp;
    
    private String rm;
    
    private String useAt;
    
    private Collection<? extends GrantedAuthority> authorities;
    
    public String getInsttCode() {
		return insttCode;
	}

	public void setInsttCode(String insttCode) {
		this.insttCode = insttCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getChrgDept() {
		return chrgDept;
	}

	public void setChrgDept(String chrgDept) {
		this.chrgDept = chrgDept;
	}

	public String getRecentConectDt() {
		return recentConectDt;
	}

	public void setRecentConectDt(String recentConectDt) {
		this.recentConectDt = recentConectDt;
	}

	public String getRecentConectIp() {
		return recentConectIp;
	}

	public void setRecentConectIp(String recentConectIp) {
		this.recentConectIp = recentConectIp;
	}

	public String getRm() {
		return rm;
	}

	public void setRm(String rm) {
		this.rm = rm;
	}

	public String getUseAt() {
		return useAt;
	}

	public void setUseAt(String useAt) {
		this.useAt = useAt;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}

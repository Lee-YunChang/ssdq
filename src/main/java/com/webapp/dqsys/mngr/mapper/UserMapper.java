package com.webapp.dqsys.mngr.mapper;

import java.util.List;
import java.util.Map;

import com.webapp.dqsys.security.domain.Member;

public interface UserMapper {
 
    public Member readUser(String userId);
 
    public List<String> readAuthority(String userId);
    
    public int insertActionHistory(Map<String, String> params);

	public Map<String, Object> selLoginInfo(String username);

	public void updateLoginFailCnt(Map<String, Object> param);

	public int updateLoginInfo(Map<String, String> params);
}


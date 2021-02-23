package com.webapp.dqsys.security.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webapp.dqsys.mngr.mapper.UserMapper;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.security.domain.SecurityMember;
import com.webapp.dqsys.util.MessageUtils;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	protected Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String ROLE_PREFIX = "ROLE_";
    
    @Autowired
    UserMapper userMapper;
    
    
    /**
     * UserDetailsService를 상속받는 구현체 
     * 중요한 것은 loadUserByUsername 함수 
     * username 을 인자로 받아, 이에 해당하는 사용자가 있는지를 구현하는 부분입니다. 
     * 이 부분에서 정상적인 User를 return 하지 않으면 정상적으로 로그인이 되지 않는다.
     */
 
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    	Member member = userMapper.readUser(userId);
		
        if(member != null) {
        	if("1".equals(member.getUseAt())) {
        		// 사용자 권한 체크
        		member.setAuthorities(makeGrantedAuthority(userMapper.readAuthority(userId)));
        	}else {
        		// 비활성화
        		throw new DisabledException(MessageUtils.DISALED);
        	}
        }else {
        	// 계정이 없음
        	throw new InternalAuthenticationServiceException(MessageUtils.NOTUSER);
        }
        
    	return new SecurityMember(member);
    }
    
    
    private static List<GrantedAuthority> makeGrantedAuthority(List<String> roles){
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));
        return list;
    }
    
}


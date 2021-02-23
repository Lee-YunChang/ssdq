package com.webapp.dqsys.security.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.webapp.dqsys.security.handler.CustomLoginFailureHandler;
import com.webapp.dqsys.security.handler.CustomLoginSuccessHandler;
import com.webapp.dqsys.security.service.CustomUserDetailsService;

@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationSuccessHandler successHandler() {
		return new CustomLoginSuccessHandler("/mngr/main");
    }
    
    @Bean
    public AuthenticationFailureHandler failureHandler() {
		return new CustomLoginFailureHandler();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	System.out.println(http.toString());
    	
        http
        	.csrf().disable()
            .authorizeRequests()
            	.antMatchers(
            			"/"
            			,"/mngr/basic"
            			,"/mngr/selectInsttList"
            			,"/mngr/saveInsttInfo"
            			,"/mngr/selectUserIdChk"
            			,"/mngr/reSetData"
            	).permitAll()
            	.antMatchers("/mngr/**").authenticated()
	        	//.antMatchers("/user").hasAuthority("USER")
	            //.antMatchers("/admin").hasAuthority("ADMIN")
            .and()
	        .formLogin()
	        	.loginPage("/login")					// 로그인 폼 기간 인증
	        	.successHandler(successHandler())
	        	.failureHandler(failureHandler())
	    		.permitAll()
	    		.and()
	        .logout()
	        	.permitAll()
	        	.logoutSuccessUrl("/")
	        	.invalidateHttpSession(true)
	        	.and()
            .sessionManagement()
                .invalidSessionUrl("/login")
                .maximumSessions(5).expiredUrl("/login");
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }
 
   
 
}

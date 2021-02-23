package com.webapp.dqsys.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.webapp.dqsys.mngr.interceptor.LoggerInterceptor;


/**
 * 필요 없으면 지워도 됩니다. 
 * @author gtman5
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LoggerInterceptor loggerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);

		//if (log.isInfoEnabled()) {
			registry.addInterceptor(loggerInterceptor)
			.addPathPatterns("/**")
			.addPathPatterns("/**/*.do");
		//}
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		// 쿠키를 사용한 예제
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setCookieName("lang");
		return resolver;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
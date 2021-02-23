package com.webapp.dqsys.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
@MapperScan(basePackages = "com.webapp.dqsys.mngr.mapper")
public class DataSourceConfiguration {
	@Value("${server.mode}") 
    String serverMode;
	@Value("${spring.datasource.driver-class-name}") 
    String datasourceDriver;
	@Value("${app.mariaDB4j.databaseName}") 
	String databaseName;
	@Value("${spring.datasource.url}") 
    String datasourceUrl;
    @Value("${spring.datasource.username}") 
    String datasourceUsername;
    @Value("${spring.datasource.password}") 
    String datasourcePassword;
  
// Dev 설정
	/*
	@Bean
	@Primary
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
		return DataSourceBuilder
				.create()
				.driverClassName(datasourceDriver)
				.url(datasourceUrl)
				.username(datasourceUsername)
				.password(datasourcePassword)
				.build();
    }
    */ 
// Dev 설정 끝

// Local 설정

    @Bean
	@Primary
    public MariaDB4jSpringService mariaDB4j() {
		return new MariaDB4jSpringService();
    }
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
    DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) throws ManagedProcessException {
		mariaDB4jSpringService.getDB().createDB(databaseName);
		return DataSourceBuilder
			.create()
			//.type(HikariDataSource.class)
			.username(datasourceUsername)
			.password(datasourcePassword)
			.url(datasourceUrl + databaseName + "?serverTimezone=UTC")
			.driverClassName(datasourceDriver)
			.build();
    }

// Local 설정 끝

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
	
	    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	    sessionFactory.setDataSource(dataSource);
	    sessionFactory.setMapperLocations(
	    		new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
	    sessionFactory.setTypeAliasesPackage("com.webapp.dqsys.mngr.domain");
	    sessionFactory.setVfs(SpringBootVFS.class); 
	    return sessionFactory.getObject();
	}

	@Bean
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
	    return new SqlSessionTemplate(sqlSessionFactory);
	}
}

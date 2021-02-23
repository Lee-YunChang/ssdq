package com.webapp.dqsys.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;
/**
 * DB 연결 개발 되기 전까지 사용할 임시 클래스
 * @author gtman5
 *
 */
@Configuration
public class AnalsDataSourceConfig {

	@Bean(name = "analsDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource() {

		AnalsRoutingDataSource rds = new AnalsRoutingDataSource();
		rds.reloadTargetDataSources();
		rds.setDefaultTargetDataSource(
				DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build()
				);
        
		return rds;
	}

	@Bean(name = "analsSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("analsDataSource") DataSource dataSource) throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		Resource config = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis_config.xml");

		sqlSessionFactoryBean.setConfigLocation(config);
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/anals/*.xml"));
		sqlSessionFactoryBean.setTypeAliasesPackage("com.webapp.dqsys.mngr.domain");
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class); 
		sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "analsSqlSessionTemplate")
	public AnalsSqlSessionTemplate sqlSessionTemplate(@Qualifier("analsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new AnalsSqlSessionTemplate(sqlSessionFactory);
	}
}
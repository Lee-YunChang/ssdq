package com.webapp.dqsys.configuration;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class AnalsSqlSessionTemplate extends SqlSessionTemplate {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.datasource.hikari.idle-timeout}") 
    String idleTimeout;
	@Value("${spring.datasource.hikari.max-lifetime}") 
    String maxLifetime;
	@Value("${spring.datasource.hikari.maximum-pool-size}") 
    String maximumPoolSize;
	@Value("${spring.datasource.hikari.minimum-idle}") 
    String minimumIdle;
	@Value("${spring.datasource.hikari.connection-timeout}") 
    String connectionTimeout;

	@Resource(name = "analsDataSource")
	private AnalsRoutingDataSource analsDataSource;

	private String dbmsKnd = "";

	public AnalsSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	/**
	 * AnalsDataSource에 분석에 사용할 DataSource를 추가한다.
	 * @param dataMap
	 */
	public void addDataSource(Map<String, Object> dataMap, String strUrl) throws Exception {
		this.dbmsKnd = MapUtils.getString(dataMap, "dbmsKnd");
		if("CSV".equals(dbmsKnd)) {
			this.dbmsKnd = "MySQL";
		}
		
		boolean addCheck = false;
		boolean beDataCls = false;
		// 이전 정보와 비교하여 다를때만 추가 하도록 변경
		if(analsDataSource.getDdId() != null) {
			if(!analsDataSource.getDdId().equals(MapUtils.getString(dataMap, "dgnssDbmsId"))) {
				addCheck = true;
				beDataCls = true;
			}
		}else {
			addCheck = true;
			beDataCls = false;
		}
		if(addCheck) {
			if(beDataCls) {
				analsDataSource.getConnection().close();
			}
			analsDataSource.setDdId(MapUtils.getString(dataMap, "dgnssDbmsId"));
			dataMap.put("strUrl", strUrl);
			
	//		DataSource dataSource = DataSourceBuilder.create()
	//				.type(HikariDataSource.class)
	//				.url(strUrl)
	//				.username(MapUtils.getString(dataMap, "id"))
	//				.password(MapUtils.getString(dataMap, "password"))	
	//				.build();
			analsDataSource.getTargetDataSources().put(MapUtils.getString(dataMap, "dgnssDbmsId"), dataSource(dataMap));
			analsDataSource.reloadTargetDataSources();
		}
	}
	
	@Bean
	public DataSource dataSource(Map<String, Object> dataMap) throws Exception {
		DataSource dataSource = new HikariDataSource(hikariConfig(dataMap));
		return dataSource;
	}
	
	@Bean
	public HikariConfig hikariConfig(Map<String, Object> dataMap) {
		HikariConfig hcfg = new HikariConfig();
		System.out.println("============= HikariConfig ============");
		System.out.println("strUrl : "+MapUtils.getString(dataMap, "strUrl"));
		System.out.println("id : "+MapUtils.getString(dataMap, "id"));
		System.out.println("password : "+MapUtils.getString(dataMap, "strUrl"));
		System.out.println("idleTimeout : "+Long.parseLong(idleTimeout));
		System.out.println("maxLifetime : "+Long.parseLong(maxLifetime));
		System.out.println("maximumPoolSize : "+Long.parseLong(maximumPoolSize));
		System.out.println("minimumIdle : "+Long.parseLong(minimumIdle));
		System.out.println("connectionTimeout : "+Long.parseLong(connectionTimeout));
		System.out.println("========================================");
		hcfg.setJdbcUrl(MapUtils.getString(dataMap, "strUrl"));
		hcfg.setUsername(MapUtils.getString(dataMap, "id"));
		hcfg.setPassword(MapUtils.getString(dataMap, "password"));
	    hcfg.setIdleTimeout(Long.parseLong(idleTimeout));
	    hcfg.setMaxLifetime(Long.parseLong(maxLifetime));
		hcfg.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
		hcfg.setMinimumIdle(Integer.parseInt(minimumIdle));
		hcfg.setConnectionTimeout(Integer.parseInt(connectionTimeout));
		
		return hcfg;
	}
	
	@Override
	public <T> T selectOne(String statement) {
		try {
			return super.selectOne(dbmsKnd + "." + statement);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
//			try {
//			if(super.getConnection() != null) {
//				super.getConnection().close();
//			}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			
			super.clearCache();
		}
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		try {
			return super.selectOne(dbmsKnd + "." + statement, parameter);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
//			try {
//				if(super.getConnection() != null) {
//					super.getConnection().close();
//				}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
			super.clearCache();
		}
	}

	@Override
	public <E> List<E> selectList(String statement) {
		try {
			return super.selectList(dbmsKnd + "." + statement);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
//			try {
//				if(super.getConnection() != null) {
//					super.getConnection().close();
//				}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
			super.clearCache();
		}
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		try {
			return super.selectList(dbmsKnd + "." + statement, parameter);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {

//			try {
//				if(super.getConnection() != null) {
//					super.getConnection().close();
//				}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
			super.clearCache();
		}
	}
	
}

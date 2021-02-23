package com.webapp.dqsys.configuration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class AnalsRoutingDataSource extends AbstractRoutingDataSource {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private Map<Object, Object> targetDataSources = new HashMap<>(); // DataSource Map
	private String ddId = null; // 사용할 DDId 수동 설정

	@Override
	protected Object determineCurrentLookupKey() {
		return ddId;
	}

	/**
	 * 실제 사용할 resolvedDataSources 을 갱신 요청
	 */
	public void reloadTargetDataSources() {
		super.setTargetDataSources(targetDataSources);
		super.afterPropertiesSet();
	}

	/**
	 * 업데이트 할 DataSource Map을 조회
	 * @return
	 */
	public Map<Object, Object> getTargetDataSources() {
		return targetDataSources;
	}

	public String getDdId() {
		return ddId;
	}

	public void setDdId(String ddId) {
		this.ddId = ddId;
	}
}

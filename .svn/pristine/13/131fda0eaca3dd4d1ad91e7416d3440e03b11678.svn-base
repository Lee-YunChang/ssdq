package com.webapp.dqsys.mngr.domain;

import java.util.LinkedHashMap;

import org.springframework.jdbc.support.JdbcUtils;

public class SangsMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	@Override
	public Object put(String key, Object value) {
		return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
	}
}

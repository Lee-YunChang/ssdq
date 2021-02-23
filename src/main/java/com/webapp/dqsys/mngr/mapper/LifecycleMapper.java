package com.webapp.dqsys.mngr.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webapp.dqsys.mngr.domain.SangsMap;

public interface LifecycleMapper {

	int AjaxSelLifecycleListTotalCnt(Map<String, String> params);

	List<?> AjaxSelLifecycleList(Map<String, String> params);

	int AjaxInsLifecycleList(Map<String, String> params);

	String selInttCode(Map<String, String> params);

	String selMaxId(Map<String, String> params);

	int AjaxUdtLifecycleList(HashMap<String, String> param);

	int AjaxDelLifecycleList(HashMap<String, String> param);

	void insertLifecycleSchema(Map<String, Object> paramMap);

	List<?> AjaxSelLifecycleFiledNm(Map<String, String> params);

	List<Map<String, String>> selectAnalysisPeriod(Map<String, String> params);

	void insertLifecycleTableAnalysis(Map<String, Object> asyncMap);

	List<?> selectLifecycleSchemaList(Map<String, Object> paramMap);

	int selectLifecycleSchemaListCnt(Map<String, Object> paramMap);

	void updateLifecycleSchema(Map<String, Object> paramMap);

	SangsMap selectLifecycleSchema(Map<String, Object> paramMap);

	int selectLifecycleSummeryCnt(Map<String, Object> paramMap);

	SangsMap selectLifecycleSummery(Map<String, Object> paramMap);

	List<SangsMap> selectLifecycleTableRes(Map<String, Object> paramMap);

	int ajaxSelChkFiledNm(Map<String, String> params);

	String selMaxId();

	int ajaxSelChkModFiledNm(Map<String, String> params);


}

package com.webapp.dqsys.mngr.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webapp.dqsys.mngr.domain.SangsMap;

public interface DataStandardMapper {

	public int insertDomainInfo(Map<String, String> dataMap);

	public List<?> selectDomainList(Map<String, String> params);

	public int selectDomainListTotalCnt(Map<String, String> params);

	public int insertWordInfo(Map<String, String> params);

	public List<?> selectWordList(Map<String, String> params);

	public int selectWordListTotalCnt(Map<String, String> params);

	public void insertStandardSchema(Map<String, Object> paramMap);

	public List<SangsMap> selectStandardSchemaList(Map<String, Object> paramMap);

	public List<SangsMap> selectStandardSchemaListCnt(Map<String, Object> paramMap);

	public void insertStandardTable(Map<String, Object> paramMap);

	public void insertStandardColumn(Map<String, Object> paramMap);

	public List<Map<String, String>> selectDataStandardWord(Map<String, Object> asyncMap);

	public int updateDataStandardColumn(Map<String, Object> asyncMap);

	public String selectDataStansdardDomain(Map<String, Object> asyncMap);

	public void executeUpdateEndStandardSchema(Map<String, Object> paramMap);

	public SangsMap selectStandardSchema(Map<String, Object> paramMap);

	public List<SangsMap> selectStandardTableList(Map<String, Object> paramMap);

	public Map<String, Object> selectStandardSummery(Map<String, Object> paramMap);

	public int selectStandardColumnObsryCnt(Map<String, Object> paramMap);

	public List<SangsMap> selectStandardColumnResList(Map<String, Object> paramMap);

	public List<SangsMap> selectWordObsryList(Map<String, Object> paramMap);

	public List<SangsMap> selectDomainObsryList(Map<String, Object> paramMap);
	
	public int updateDomainInfo(Map<String, String> params);
	
	public int updateWordInfo(Map<String, String> params);
	
	public int deleteDomainInfo(HashMap<String, String> params);
	
	public int deleteWordInfo(HashMap<String, String> params);

	public Map<String, Object> selectTableCnt(Map<String, Object> paramMap);

	public String selectWordObsry(Map<String, Object> paramMap);

	public String selectDomainObsry(Map<String, Object> paramMap);

	public void updateDataStandardShema(Map<String, Object> asyncMap);

	public void insertDomainMatch(Map<String, String> param);

	public String selectDomainId();

	public List<Map<String, String>> selectMatchData(Map<String, String> params);

	public Map<String, String> domainDataList(Map<String, String> params);

	public void deleteMatch(Map<String, String> params);

	public void updateDomainMatch(Map<String, String> param);

	public List<Map<String, String>> selectDomainGroupList(Map<String, String> params);

}

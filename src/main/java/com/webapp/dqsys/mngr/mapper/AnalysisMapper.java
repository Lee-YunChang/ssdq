package com.webapp.dqsys.mngr.mapper;

import java.util.List;
import java.util.Map;

public interface AnalysisMapper {
	int selectTableAnalysisTotalCnt(Map<String, String> params);
	
	List<?> selectTableAnalysis(Map<String, String> params);

	List<?> selectSchema(Map<String, String> params);

	List<?> selectColumnAnalysis(Map<String, String> params);

	List<?> selectPattern(Map<String, String> params);

	List<?> selectAnalsGroupList(Map<String, String> params);

	List<?> selectTable(Map<String, String> params);

	String selectDbList(String dgnssDbmsId);

	int UDTPattern(Map<String, String> params);

	int selectPatternTotalCnt(Map<String, String> params);

	int UDTPatternUseAt(Map<String, String> params);
	
	String selectMaxVal(Map<String, String> params);
	
	String selectMinVal(Map<String, String> params);
	
	List<?> selectMinMaxVal(Map<String, String> params);

}

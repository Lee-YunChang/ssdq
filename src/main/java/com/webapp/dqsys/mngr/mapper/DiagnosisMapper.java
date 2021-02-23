package com.webapp.dqsys.mngr.mapper;

import java.util.List;
import java.util.Map;

import com.webapp.dqsys.mngr.domain.SangsMap;

public interface DiagnosisMapper {

	/* 진단 관리 */

	public SangsMap selectInsttForReport(Map<String, Object> paramMap);

	public SangsMap selectDgnssDbms(Map<String, Object> paramMap);

	public List<SangsMap> selectDgnssTableList(Map<String, Object> paramMap);

	public int selectDgnssTableListCnt(Map<String, Object> paramMap);

	public SangsMap selectDgnssTable(Map<String, Object> paramMap);
	
	public void updateDgnssTable(Map<String, Object> paramMap);

	public int insertDgnssTable(Map<String, Object> paramMap);

	public int updateEndDgnssTable(Map<String, Object> paramMap);

	public List<SangsMap> selectDgnssColumnList(Map<String, Object> paramMap);

	public List<SangsMap> selectDgnssColumnListForReport(Map<String, Object> paramMap);

	public int insertDgnssColumn(Map<String, Object> paramMap);

	public List<SangsMap> selectDgnssColumnResList(Map<String, Object> paramMap);

	public int selectWorkDgnssColumnResCount(Map<String, Object> paramMap);

	public int insertDgnssColumnRes(Map<String, Object> paramMap);

	public int updateDgnssColumnRes(Map<String, Object> paramMap);

	public List<SangsMap> selectFrqAnalList(Map<String, Object> paramMap);

	public int selectWorkFrqAnalCount(Map<String, Object> paramMap);

	public int insertFrqAnal(Map<String, Object> paramMap);

	public int deleteWorkFrqAnal(Map<String, Object> paramMap);

	/* 진단설정저장 관리 */

	public List<SangsMap> selectDgnssSaveList(Map<String, Object> paramMap);

	public int selectDgnssSaveListCnt(Map<String, Object> paramMap);

	public int insertDgnssSave(Map<String, Object> paramMap);
	
	public void insertScheduler(Map<String, Object> paramMap);

	
	public String selectScheduleIdNextVal(Map<String, Object> paramMap);

	public int deleteDgnssSave(Map<String, Object> paramMap);

	/* 진단오류 관리 */

	public List<SangsMap> selectDgnssErrorList(Map<String, Object> paramMap);

	public int selectDgnssErrorListCnt(Map<String, Object> paramMap);

	public int insertDgnssError(Map<String, Object> paramMap);

	/* 분석패턴 관리 */

	public List<SangsMap> selectAnalsList(Map<String, Object> paramMap);

	public List<SangsMap> selectAnalsListForReport(Map<String, Object> paramMap);
	
	public List<SangsMap> selectAnalsListToColumn(Map<String, Object> paramMap);

	public List<SangsMap> selectAnalsGroupList(Map<String, Object> paramMap);

	public List<SangsMap> selectAnalsGroupListForReport(Map<String, Object> paramMap);

	public List<SangsMap> selectResAnalsList(Map<String, Object> paramMap);
	
	public String selectColumnComment(Map<String, Object> paramMap);
	
	public List<?> selectTableDataList(Map<String, Object> paramMap);
	
	public List<?> selectAnalsTableDataList(Map<String, Object> paramMap);
	
	public List<?> selectTableColumnsList(Map<String, Object> paramMap);

	public List<SangsMap> selectScheduler();
	
	/* 같은 예약시간 체크*/
	public int insertSchedulerTimeConfirm(Map<String, Object> paramMap);
	
	
}

package com.webapp.dqsys.mngr.mapper;

import java.util.List;
import java.util.Map;

public interface CommonMapper {
	
	/* 메뉴관리 */
 
	public List<?> selectMenuList(Map<String, String> params) throws Exception;
	
	public List<?> selectUpMenuList(Map<String, String> params) throws Exception;
	
	public List<?> selectMenuGroupList(Map<String, String> params) throws Exception;
	
	public void insertGroupMenu(Map<String, String> params) throws Exception;
	
	public void updateGroupMenu(Map<String, String> params) throws Exception;
	
	public void deleteGroupMenu(Map<String, String> params) throws Exception;
	
	public void deleteGroupMenuList(Map<String, String> params) throws Exception;
	
	public void insertMenu(Map<String, String> params) throws Exception;
	
	public void updateMenu(Map<String, String> params) throws Exception;
	
	public void deleteMenu(Map<String, String> params) throws Exception;
	
	/* 메뉴 권한 관리 */
	
	public List<?> selectUserAuthList(Map<String, String> params) throws Exception;
	
	public List<?> selectUserAuthMenuList(Map<String, String> params) throws Exception;
	
	public void deleteAuthor(Map<String, String> params) throws Exception;
	
	public void insertMenuAuthList(Map<String, String> params) throws Exception;
	
	public void updateMenuAuthList(Map<String, String> params) throws Exception;
	
	
	/* 공통코드 관리 */
	
	public List<?> selectCommonCode(Map<String, String> params);

	public List<?> selectDetailCommonCode(Map<String, String> params);
	
	public void insertCommonCode(Map<String, String> params);
	
	public void updateCommonCode(Map<String, String> params);
	
	public void deleteCommonCode(Map<String, String> params);

	public void insertDetailCommonCode(Map<String, String> params);
	
	public void updateDetailCommonCode(Map<String, String> params);

	public void deleteDetailCommonCode(Map<String, String> params);
	
	public List<?> selectCodeDoubleChk(Map<String, String> params);

	public List<?> selectUserIdChk(Map<String, String> params) throws Exception;
	
	
	/* 초기 데이터 (관리자) 등록 */
	
	public int selectInsttCnt() throws Exception;
	
	public int selectInsttTotalCnt(Map<String, String> params) throws Exception;
	
	public List<?> selectInsttList(Map<String, String> params) throws Exception;
	
	public int insertUserInfo(Map<String, String> params) throws Exception;
	
	public int insertInsttInfo(Map<String, String> params) throws Exception;
	
	public int updateAnalsInssttCode(Map<String, String> params) throws Exception;
	
	
	/* 운영정보 관리 */
	
	public List<?> selectManageInsttList(Map<String, String> params) throws Exception;
	
	public List<?> selectManageList(Map<String, String> params);
	
	public void updateManageInfo(Map<String, String> params);
	
	public void updateManageInsttInfo(Map<String, String> params);
	
	/* 데이터 초기화 */
	public void deleteScheduler(Map<String, String> params);
	public void deleteFrqAnals(Map<String, String> params);
	public void deleteDgnssSave(Map<String, String> params);
	public void deleteDgnssError(Map<String, String> params);
	public void deleteDgnssColumnsRes(Map<String, String> params);
	public void deleteDgnssColumns(Map<String, String> params);
	public void deleteDnssTables(Map<String, String> params);
	public void deleteDgnssDbms(Map<String, String> params);
	public void deleteUser(Map<String, String> params);
	public void deleteInstt(Map<String, String> params);
	
	/* 메인화면 */
	/**
	 * @param params
	 * @return
	 */
	public List<?> selectDbList(Map<String, String> params);
	/**
	 * @param params
	 * @return
	 */
	public int selectDbListTotalCnt(Map<String, String> params);
	/**
	 * 진단 항목 현황
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAnalsCnt(Map<String, String> params) throws Exception;
	
	/**
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String selectDbKnd(Map<String, String> params) throws Exception;
	
	/**
	 * 기본정보
	 * @return
	 * @throws Exception
	 */
	public List<?> selectBasicInfoCnt(Map<String, String> params) throws Exception;
	
	/**
	 * 진단률
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<?> selectResTotCnt(Map<String, String> params) throws Exception;
	
	/**
	 * 항목별 불일치 건수
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<?> selectResNotMatchCntToAnalsNm(Map<String, String> params) throws Exception;
	
	/**
	 * 월별 진단 현황
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMonthResCnt(Map<String, String> params) throws Exception;
	
	/**
	 * 결과 view check
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int selectViewCnt(Map<String, String> params) throws Exception;
	
	/**
	 * 결과 view drop
	 * @param params
	 */
	public void dropResView(Map<String, String> params);
	
	/**
	 * 결과 view create
	 * @param params
	 */
	public void createResView(Map<String, String> params);

	/**
	 * 메뉴 권한 조회
	 * @return
	 */
	public List<Map<String,Object>> selectAuthor() throws Exception;
}


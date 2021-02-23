package com.webapp.dqsys.mngr.mapper;

import java.util.List;
import java.util.Map;

public interface BasicInfoMapper {

	public int insertInsttDbms(Map<String, String> params);

	public int insertInsttDbmsVer(Map<String, String> params);

	public int insertDgnssDbms(Map<String, String> params);

	public int updateDgnssDbms(Map<String, String> params);

	public int selectDgnssDbmsListCnt(Map<String, String> params);

	public int selectInsttDbmsListCnt(Map<String, String> params);

	public String selectDgnssDbmsId(Map<String, String> params);

	public String selectInsttDbmsId(Map<String, String> params);

	public int selectDgnssDbmsNmCheck(Map<String, String> params);

	public List<?> selectDbList(Map<String, String> params);

	public int selectDbListTotalCnt(Map<String, String> params);

	public List<?> selectDbInfo(Map<String, String> params);

	public List<?> selDbmsKindList(Map<String, String> params);

	public List<?> selDbmsVerList(Map<String, String> params);

	public int insertCvsFileData(Map<String, Object> params);

	public int createTableDb(Map<String, String> params);

	public int dropTableDb(Map<String, String> params);

	public String selectDgnssDbmsIdNextVal(Map<String, String> params);

	public String selectDualdata(Map<String, String> params);

	public int mysqlimportFile(Map<String, Object> params);

	public int selectInsttCnt(Map<String, Object> params);

	public int selectInsttDbmsCnt(Map<String, Object> params);

	public int selectDgnssDbmsCnt(Map<String, Object> params);

	public int selectDgnssSaveCnt(Map<String, Object> params);

	public int selectDgnssTablesCnt(Map<String, Object> params);

	public int selectFrqAnalsCnt(Map<String, Object> params);

	public int selectDgnssColumnsCnt(Map<String, Object> params);

	public int selectDgnssColumnsResCnt(Map<String, Object> params);

	public int selectAnalsCnt(Map<String, Object> params);

	public int selectDgnssErrorCnt(Map<String, Object> params);

	public List<?> selDbmsDateTypeList(Map<String, String> params);

}

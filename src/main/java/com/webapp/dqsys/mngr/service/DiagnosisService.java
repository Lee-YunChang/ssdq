package com.webapp.dqsys.mngr.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.mapper.DiagnosisMapper;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.util.MessageUtils;
import com.webapp.dqsys.util.SangsAbstractService;
import com.webapp.dqsys.util.SangsUtil;

@Service
public class DiagnosisService extends SangsAbstractService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;
	
	@Resource
	private BasicInfoService basicInfoService;
	
	@Resource
	private RuleMngService ruleMngService;

	@Resource
	private DiagnosisMapper diagnosisMapper;
	
	private List<Long> tasksList;

	// Oralce/Mysql/Maria 외부 DB
	public List<String> selectTableList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectTableList", paramMap);
	}

	public List<Map<String, Object>> selectColumnList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectColumnList", paramMap);
	}

	public List<Map<String, Object>> selectColumnList(String schemaName, String tableName, String[] columnName) throws Exception, SQLException {

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tableName", tableName);
		paramMap.put("columnList", columnName);
		paramMap.put("schemaName", schemaName);

		return sqlSession.selectList("DiagnosisMapper.selectSelColumnList", paramMap);
	}

	public String selectColumnComment(Map<String, Object> paramMap) throws Exception, SQLException {
		//return sqlSession.selectOne("DiagnosisMapper.selectColumnComment", paramMap);
		 return diagnosisMapper.selectColumnComment(paramMap);
	}

	public List<Map<String, Object>> selectDataList(Map<String, Object> paramMap, int rowCount) throws Exception, SQLException {

		paramMap.put("rowCount", rowCount);

		return sqlSession.selectList("DiagnosisMapper.selectDataList", paramMap);
	}

	public List<Map<String, Object>> selectDataList(Map<String, Object> paramMap, String[] columnName, int rowCount) throws Exception, SQLException {

		paramMap.put("columnList", columnName);
		paramMap.put("rowCount", rowCount);

		return sqlSession.selectList("DiagnosisMapper.selectSelDataList", paramMap);
	}

	public int selectRowCount(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectOne("DiagnosisMapper.selectRowCount", paramMap);
	}

	public int selectPatternCount(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectOne("DiagnosisMapper.selectPatternCount", paramMap);
	}
	
	public int selectNumberCount(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectOne("DiagnosisMapper.selectNumberCount", paramMap);
	}
	
	public List<Map<String, Object>> selectPatternList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectPatternList", paramMap);
	}
	
	public List<Map<String, Object>> selectDownLoadList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectDownLoadList", paramMap);
	}

	public int selectRangeCount(Map<String, Object> paramMap) throws Exception, SQLException {

		String analsSe = paramMap.get("analsSe").toString();
		String columnTy = paramMap.get("columnTy").toString();
		
		if(analsSe.equalsIgnoreCase("AG000601")) {
			if(!columnTy.equalsIgnoreCase("NUMBER") || !columnTy.equalsIgnoreCase("FLOAT") || !columnTy.equalsIgnoreCase("INT") || !columnTy.equalsIgnoreCase("BIGINT") || !columnTy.equalsIgnoreCase("INTEGER") || !columnTy.equalsIgnoreCase("NUMERIC")) {
				paramMap.put("beginValue", 0);
				paramMap.put("endValue", 0);
				paramMap.put("analsSe", "AG000602");
			}
		}
		
		if(analsSe.equalsIgnoreCase("AG000603")) {
			if(!columnTy.equalsIgnoreCase("DATE") || !columnTy.equalsIgnoreCase("TIMESTAMP") || !columnTy.equalsIgnoreCase("TIME") || !columnTy.equalsIgnoreCase("DATETIME")) {
				paramMap.put("beginValue", 0);
				paramMap.put("endValue", 0);
				paramMap.put("analsSe", "AG000602");
			}
		}
		
		
		if (paramMap.get("beginValue") == null) {
			paramMap.put("beginValue", "");
		}
		if (paramMap.get("endValue") == null) {
			paramMap.put("endValue", "");
		}
		
		return sqlSession.selectOne("DiagnosisMapper.selectRangeCount", paramMap);
	}
	
	public List<SangsMap> selectFrqAnalCountList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectFrqAnalCountList", paramMap);
	}

	public List<String> selectPKList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectPKList", paramMap);
	}

	public List<Map<String, Object>> selectPatternNotMatchList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectPatternNotMatchList", paramMap);
	}
	
	public List<Map<String, Object>> selectSqlNotMatchList(Map<String, Object> paramMap) throws Exception, SQLException {
		return sqlSession.selectList("DiagnosisMapper.selectSqlNotMatchList", paramMap);
	}

	public List<Map<String, Object>> selectRangeNotMatchList(Map<String, Object> paramMap) throws Exception, SQLException {

		if (paramMap.get("beginValue") == null) {
			paramMap.put("beginValue", "");
		}
		if (paramMap.get("endValue") == null) {
			paramMap.put("endValue", "");
		}

		return sqlSession.selectList("DiagnosisMapper.selectRangeNotMatchList", paramMap);
	}
	
	public List<Map<String, Object>> selectRangeMatchList(Map<String, Object> paramMap) throws Exception, SQLException {

		if (paramMap.get("beginValue") == null) {
			paramMap.put("beginValue", "");
		}
		if (paramMap.get("endValue") == null) {
			paramMap.put("endValue", "");
		}

		return sqlSession.selectList("DiagnosisMapper.selectRangeMatchList", paramMap);
	}

	// Maria 내부 DB
	public SangsMap selectInsttForReport(Map<String, Object> paramMap) {
		return diagnosisMapper.selectInsttForReport(paramMap);
	}

	public SangsMap selectDgnssDbms(Map<String, Object> paramMap) {
		return diagnosisMapper.selectDgnssDbms(paramMap);
	}

	public Map<String, Object> selectDgnssTableList(Map<String, Object> paramMap) {

		super.pagingSetMySql(paramMap);

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("list", diagnosisMapper.selectDgnssTableList(paramMap));
		resultMap.put("totalCnt", diagnosisMapper.selectDgnssTableListCnt(paramMap));

		return resultMap;
	}

	public SangsMap selectDgnssTable(Map<String, Object> paramMap) {
		return diagnosisMapper.selectDgnssTable(paramMap);
	}
	
	public void updateDgnssTable(Map<String, Object> paramMap) {
		diagnosisMapper.updateDgnssTable(paramMap);
	}

	public int insertDgnssTable(Map<String, Object> paramMap) {
		int resInt = 0;
		try {
			resInt =  diagnosisMapper.insertDgnssTable(paramMap);
		}catch(Exception e) {
			insertDgnssError(paramMap);
		}
		return resInt;
	}
	
	public void insertScheduler(Map<String, Object> paramMap) {
		
		try {
			
			String scheduleId =  null;
			
			String intscheduleId = diagnosisMapper.selectScheduleIdNextVal(paramMap);
			
			scheduleId = String.valueOf(intscheduleId);
			
			if(scheduleId.equals("") || scheduleId == null || scheduleId.equals("null")) {
				
				scheduleId = "1000";
			}
			
			paramMap.put("scheduleId", scheduleId);
			
			diagnosisMapper.insertScheduler(paramMap);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<SangsMap> selectScheduler() {
		return diagnosisMapper.selectScheduler();
	}
	
	public List<SangsMap> selectDgnssColumnList(Map<String, Object> paramMap) {
		return diagnosisMapper.selectDgnssColumnList(paramMap);
	}

	public List<SangsMap> selectDgnssColumnListForReport(Map<String, Object> paramMap) {
		return diagnosisMapper.selectDgnssColumnListForReport(paramMap);
	}

	public int insertDgnssColumn(Map<String, Object> paramMap) {
		int resInt = 0;
		try {
			resInt =  diagnosisMapper.insertDgnssColumn(paramMap);
		}catch(Exception e) {
			insertDgnssError(paramMap);
		}
		return resInt;
	}

	public List<SangsMap> selectDgnssColumnResList(Map<String, Object> paramMap) {
		return diagnosisMapper.selectDgnssColumnResList(paramMap);
	}

	public int insertDgnssColumnRes(Map<String, Object> paramMap) {
		int resInt = 0;
		try {
			resInt =  diagnosisMapper.insertDgnssColumnRes(paramMap);
		}catch(Exception e) {
			insertDgnssError(paramMap);
		}
		return resInt;
	}

	public List<SangsMap> selectFrqAnalList(Map<String, Object> paramMap) {
		return diagnosisMapper.selectFrqAnalList(paramMap);
	}

	public int insertFrqAnal(Map<String, Object> paramMap) {
		int resInt = 0;
		try {
			resInt =  diagnosisMapper.insertFrqAnal(paramMap);
		}catch(Exception e) {
			insertDgnssError(paramMap);
		}
		return resInt;
	}

	public Map<String, Object> selectDgnssSaveList(Map<String, Object> paramMap) {

		super.pagingSetMySql(paramMap);

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("list", diagnosisMapper.selectDgnssSaveList(paramMap));
		resultMap.put("totalCnt", diagnosisMapper.selectDgnssSaveListCnt(paramMap));

		return resultMap;
	}

	public int insertDgnssSave(Map<String, Object> paramMap) {
		int resInt = 0;
		try {
			resInt =  diagnosisMapper.insertDgnssSave(paramMap);
		}catch(Exception e) {
			insertDgnssError(paramMap);
		}
		return resInt;
	}
	public void insertSchedulerTimeConfirm(Map<String, Object> params,HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception, IOException{
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		  String json="";
		  String dateFmt = null;
		  int timecheck=0;
		  Map<String, Object>resultmap=new HashMap<>();
		  PrintWriter out = res.getWriter();
	        
		try {
			dateFmt = params.get("scheduleDe").toString();
			String schedule = dateFmt;
			resultmap.put("schedule", schedule);
			timecheck= diagnosisMapper.insertSchedulerTimeConfirm(resultmap);
			
			ObjectMapper mapper = new ObjectMapper();
			json=mapper.writeValueAsString(timecheck);
			
		}catch(Exception e) {
		}
		out.print(json);
		out.flush();
		out.close();
		
	}
	
	public int deleteDgnssSave(Map<String, Object> paramMap) {
		int resInt = 0;
		try {
			resInt =  diagnosisMapper.deleteDgnssSave(paramMap);
		}catch(Exception e) {
			insertDgnssError(paramMap);
		}
		return resInt;
	}

	public Map<String, Object> selectDgnssErrorList(Map<String, Object> paramMap) {

		super.pagingSetMySql(paramMap);

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("list", diagnosisMapper.selectDgnssErrorList(paramMap));
		resultMap.put("totalCnt", diagnosisMapper.selectDgnssErrorListCnt(paramMap));

		return resultMap;
	}

	public int insertDgnssError(Map<String, Object> paramMap) {

		if (StringUtils.isEmpty(MapUtils.getString(paramMap, "dgnssErrorId"))) {
			paramMap.put("dgnssErrorId", DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmssSSS")); // 스레드로 실행되므로 ID가 중복 날 수 있음. (밀리세컨드도 중복 됨)
		}
		Object key = null;
		Iterator it = paramMap.keySet().iterator();
		boolean chekPk1 = false;
		boolean chekPk2 = false;
		boolean chekPk3 = false;
		while (it.hasNext()) {
			key = it.next();
			// 에러로그 등록 파라메터의 PK 값 설정 여부 처리
			// INSTT_CODE
			if("INSTT_CODE".equals(key.toString()) || "insttCode".equals(key.toString())) {
				chekPk1 = true;
			}
			// DGNSS_DBMS_ID
			if("DGNSS_DBMS_ID".equals(key.toString()) || "dgnssDbmsId".equals(key.toString())) {
				chekPk2 = true;
			}
			// DGNSS_INFO_ID
			if("DGNSS_INFO_ID".equals(key.toString()) || "dgnssInfoId".equals(key.toString())) {
				chekPk3 = true;
			}
		}
		if(!chekPk1) {
			
			paramMap.put("INSTT_CODE", "ER01");
			paramMap.put("insttCode", "ER01");
		}
		if(!chekPk2) {
			paramMap.put("DGNSS_DBMS_ID", "ER02");
			paramMap.put("dgnssDbmsId", "ER02");
		}
		if(!chekPk3) {
			paramMap.put("DGNSS_INFO_ID", "ER03");
			paramMap.put("dgnssInfoId", "ER03");
		}
		
		return diagnosisMapper.insertDgnssError(paramMap);
	}

	public int insertDgnssError(Map<String, Object> paramMap, Exception e) {
		String errorNm = ExceptionUtils.getMessage(e);
		String errorContent = ExceptionUtils.getStackTrace(e);
		logger.error("errorNm : "+errorNm);
		logger.error("errorContent : "+errorContent);
		if(e instanceof ClassNotFoundException) {
			errorNm = MessageUtils.SYSERROR01;
		} else if(e instanceof IllegalAccessException) {
			errorNm = MessageUtils.SYSERROR01;
		} else if(e instanceof InstantiationException) {
			errorNm = MessageUtils.SYSERROR01;
		} else if(e instanceof InterruptedException) {
			errorNm = MessageUtils.SYSERROR02;
		} else if(e instanceof NoSuchMethodException){
			errorNm = MessageUtils.SYSERROR03;
		} else if(e instanceof IOException) {
			if(errorNm.indexOf("CharConversionException") > -1) {
				errorNm = MessageUtils.SYSERROR04;
			}else if(errorNm.indexOf("EOFException") > -1) {
				errorNm = MessageUtils.SYSERROR05;
			}else if(errorNm.indexOf("FileNotFoundException") > -1) {
				errorNm = MessageUtils.SYSERROR06;
			}else {
				errorNm = MessageUtils.SYSERROR01;
			}
		} else if(e instanceof RuntimeException) {
			if(errorNm.indexOf("IllegalArgumentException") > -1) {
				errorNm = MessageUtils.SYSERROR07;
			}else if(errorNm.indexOf("NumberFormatException") > -1) {
				errorNm = MessageUtils.SYSERROR08;
			}else if(errorNm.indexOf("ArrayIndexOutOfBoundsException") > -1) {
				errorNm = MessageUtils.SYSERROR09;
			}else if(errorNm.indexOf("NullPointerException") > -1) {
				errorNm = MessageUtils.SYSERROR10;
			}else {
				errorNm = MessageUtils.SYSERROR01;
			}
			
		} else {
			if(errorNm.indexOf("RecoverableDataAccessException") > -1) {
				errorNm = MessageUtils.SYSERROR11;
			}else if(errorNm.indexOf("OutOfMemoryError") > -1) {
				errorNm = MessageUtils.SYSERROR12;
			}else if(errorNm.indexOf("StackOverflowError") > -1) {
				errorNm = MessageUtils.SYSERROR13;
			}else {
				errorNm = MessageUtils.SYSERROR01;
			}
		} 
		
		String temp[] = errorNm.split("\n");
		// 오류명은 2줄로 
		if(temp.length > 5) {
			errorNm = temp[0]+"\n"+temp[1];
		}
		
		String tempCon[] = errorContent.split("\n");
		StringBuffer strBuf = new StringBuffer("");
		// 오류내용은 15줄로 
		if(tempCon.length > 15) {
			for(int i=0;i<tempCon.length;i++) {
				if(i < 15) {
				strBuf.append(tempCon[i]);
				}
			}
			errorContent = strBuf.toString();
		}
		paramMap.put("errorNm", errorNm);
		paramMap.put("errorContent", errorContent);
		return this.insertDgnssError(paramMap);
	}

	public int insertDgnssError(Map<String, Object> paramMap, String errorNm) {
		paramMap.put("errorNm", errorNm);
		return this.insertDgnssError(paramMap);
	}

	public List<SangsMap> selectAnalsList(Map<String, Object> paramMap) {
		return diagnosisMapper.selectAnalsList(paramMap);
	}

	public List<SangsMap> selectAnalsListForReport(Map<String, Object> paramMap) {
		return diagnosisMapper.selectAnalsListForReport(paramMap);
	}
	
	public List<SangsMap> selectAnalsListToColumn(Map<String, Object> paramMap) {
		return diagnosisMapper.selectAnalsListToColumn(paramMap);
	}

	public List<SangsMap> selectAnalsGroupList(Map<String, Object> paramMap) {
		return diagnosisMapper.selectAnalsGroupList(paramMap);
	}

	public List<SangsMap> selectAnalsGroupListForReport(Map<String, Object> paramMap) {
		return diagnosisMapper.selectAnalsGroupListForReport(paramMap);
	}

	public List<SangsMap> selectResAnalsList(Map<String, Object> paramMap) {
		return diagnosisMapper.selectResAnalsList(paramMap);
	}
	
//	public Future<List<Long>> asyncExecutorAnalysisAnalsId(Map<String, Object> paramMap, SangsMap analsMap) {
//		tasksList = Collections.synchronizedList(new ArrayList<Long>());
//		
//		
//		return new AsyncResult<List<Long>>(tasksList);
//	}
	
	// 스레드
	@Async("asyncExecutor")
	public void asyncExecutorAnalysisAnalsId(Map<String, Object> paramMap, SangsMap analsMap) {
		executeAnalysisAnalsId(paramMap, analsMap);
	}
	
	// 스레드 컬럼 단위 실행
	@Async("asyncExecutor")
	public void asyncExecutorAnalysisColumn(Map<String, Object> paramMap, Map<String, SangsMap> analsIdMap, String[] analsIdList) {
		executeAnalysisColumn(paramMap, analsIdMap, analsIdList);
	}
					
			
	/**
	 * @param paramMap
	 * @param analsMap
	 */
	private void executeAnalysisAnalsId(Map<String, Object> paramMap, SangsMap analsMap) {

		String analsSe = MapUtils.getString(analsMap, "analsSe");
		String analsTy = MapUtils.getString(analsMap, "analsTy");
		String dbmsKnd = MapUtils.getString(analsMap, "dbmsKnd");
		
		int mtchgCo = 0;

		try {

			switch (analsTy) {

			case "AT000100": // SQL (or Value Freq.)
				if (!StringUtils.equals(analsSe, "AG000200")) { // SQL

					String tableNm = MapUtils.getString(paramMap, "tableNm");
					String columnNm = MapUtils.getString(paramMap, "columnNm");
					String sql = MapUtils.getString(analsMap, "analsFrmla");
					sql = StringUtils.replace(sql, "COUNT(1)", "COUNT($COLUMN_NAME)");
					sql = StringUtils.replace(sql, "$TABLE_NAME", tableNm);
					if(dbmsKnd.equals("MSSQL")) {
						sql = StringUtils.replace(sql, "$COLUMN_NAME", columnNm);
					}else if(dbmsKnd.equals("MYSQL") || dbmsKnd.equals("CSV")) {
						sql = StringUtils.replace(sql, "$COLUMN_NAME", "`"+columnNm+"`");
					}else {
						sql = StringUtils.replace(sql, "$COLUMN_NAME", "\""+columnNm+"\"");
					}
					logger.info("sql: {}", sql);

					mtchgCo = sqlSession.selectOne("DiagnosisMapper.selectSQLCount", sql);

				} else { // Value Frequency

					List<SangsMap> frqList = selectFrqAnalCountList(paramMap);

					for (SangsMap frqMap : frqList) {
						paramMap.putAll(frqMap);
						diagnosisMapper.insertFrqAnal(paramMap);
					}
					diagnosisMapper.deleteWorkFrqAnal(paramMap);

					return;

				}
				break;

			case "AT000200": // 패턴
				// MSSQL은 쿼리내 패턴처리가 불가(단순패턴만 LIKE로 처리가능)
				// MSSQL 일때는 JAVA 프로그램으로 처리 하도록 변경
				if(dbmsKnd.equals("MSSQL")) {
					String chkPattern = (String)analsMap.get("analsFrmla");
					
					paramMap.put("analsFrmla", chkPattern);
					mtchgCo = pattenCheckCnt(paramMap);
					
				}else {
					paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
					//if("IP(V6)".equals(analsMap.get("analsNm"))) {
					if("030".equals(paramMap.get("analsId").toString().substring(1))) {
						// 오라클이고 IP(V6)일때 JAVA 로 처리 필요.....
						String chkPattern = "^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$";
						//String chkPattern = analsMap.get("analsFrmla").toString();
						paramMap.put("analsFrmla", chkPattern);
						mtchgCo = pattenCheckCnt(paramMap);
						
					//}else if("주소".equals(analsMap.get("analsNm"))) {
					}else if("026".equals(paramMap.get("analsId").toString().substring(1))) {
						// 숫자만 있는 컬럼일 경우 JAVA에서 처리 함.
						logger.info("database : "+paramMap.get("database"));
						logger.info("tableNm : "+paramMap.get("tableNm"));
						logger.info("columnNm : "+paramMap.get("columnNm"));
						int cnt = selectNumberCount(paramMap);
						if(cnt > 0) {
							//String chkPattern = analsMap.get("analsFrmla").toString();
							paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
							mtchgCo = pattenCheckCnt(paramMap);
						}else {
							mtchgCo = selectPatternCount(paramMap);
						}
					}else {
						mtchgCo = selectPatternCount(paramMap);
					}
				}
				break;
			case "AT000300": // 범주
				logger.info("analsSe:{}", analsMap.get("analsSe"));
				paramMap.put("analsSe", StringUtils.defaultString(MapUtils.getString(analsMap, "analsSe"), ""));
				paramMap.put("beginValue", StringUtils.defaultString(MapUtils.getString(analsMap, "beginValue"), ""));
				paramMap.put("endValue", StringUtils.defaultString(MapUtils.getString(analsMap, "endValue"), ""));
				
				mtchgCo = selectRangeCount(paramMap);
				break;

			default:
				mtchgCo = 0;
				break;
			}

			paramMap.put("mtchgCo", mtchgCo);
			diagnosisMapper.updateDgnssColumnRes(paramMap);

		} catch (Exception e) {
			e.printStackTrace();
			this.insertDgnssError(paramMap, e);
		}
/* 스레드 지연 테스트용
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}


	/**
	 * @param paramMap
	 * @param analsIdMap
	 * @param analsIdList
	 */
	private void executeAnalysisColumn(Map<String, Object> paramMap, Map<String, SangsMap> analsIdMap, String[] analsIdList) {

		String analsSe = "";
		String analsTy = "";
		String dbmsKnd = "";
		int mtchgCo = 0;

		try {
//			for (String test : analsIdList) {
//				logger.info("analsId : {} ", test);
//			}
			
			for (String analsId : analsIdList) {
				paramMap.put("analsId", analsId);
				logger.info("analsId : {} ", analsId);
				
				if (!StringUtils.equals(MapUtils.getString(analsIdMap.get(analsId), "analsSe"), "AG000200")) { 
					// Freq 가  아닌 경우 (DB에 아직 Freq 구분 플래그가 없어서 하드코딩 함)
					insertDgnssColumnRes(paramMap);
				} else {
					paramMap.put("sn", 0);
					insertFrqAnal(paramMap);
				}

				Map<String, Object> asyncMap = new ConcurrentHashMap<>();
				asyncMap.putAll(paramMap);
				SangsMap analsMap = analsIdMap.get(analsId);
				executeAnalysisAnalsId(asyncMap, analsMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.insertDgnssError(paramMap, e);
		}
/* 스레드 지연 테스트용
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}

	@Async("asyncExecutor")
	public void executeUpdateEndDgnssTable(Map<String, Object> paramMap) {

		int runCount = 0;
		int maxCount = 100000;
		int term = 3000;
		int result = -1;
		int errCnt = -1;

		try {

			while (result != 0 && runCount++ <= maxCount) {
				// 에러 건수 체크
				errCnt = diagnosisMapper.selectDgnssErrorListCnt(paramMap);
				// 에러 건수가 있으면 실패 건으로 처리 한다.
				if (errCnt > 0) {
					result = -1;
					break;
				}
				// 실행 결과가 등록 되지 않은 건 체크(기본 분석, 기본 패턴, 사용자)
				result = diagnosisMapper.selectWorkDgnssColumnResCount(paramMap);
				// 실행 결과가 등록 되지 않은 건 체크(Value Frequency)
				result += diagnosisMapper.selectWorkFrqAnalCount(paramMap);
				
				logger.info("errCnt : "+errCnt+" / result : "+result);
				// 처리 결과가 등록이 완료 되면 상태를 업데이트 하기 위해 처리
				if (result == 0) {
					break;
				}
				
				Thread.sleep(term);
			}

			if (result == 0) {
				paramMap.put("excSttus", "E");
			} else {
				// columnNm , analsId 값은 필수 값이라 삭제 하면 안됨. 삭제시 임시 값으로 설정 됨.
				paramMap.remove("columnNm"); // 테이블 오류 시는 컬럼명 안나오게
				paramMap.remove("analsId"); // 테이블 오류 시는 분석ID 안나오게
				if(errCnt == 0) this.insertDgnssError(paramMap, "Time Over.");
				paramMap.put("excSttus", "F");
			}

			diagnosisMapper.updateEndDgnssTable(paramMap);

		} catch (Exception e) {
			logger.error(e.getMessage());
			this.insertDgnssError(paramMap, e);
		}
	}
	

	public Map<String, Object> selectColumnAjax(Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException{

//		res.setContentType("text/html; charset=utf-8");
//		PrintWriter out = res.getWriter();
//		String json = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> columnNames = new ArrayList<>();
		try {
			//paramMap.put("rowCount", rowCount);
			
			List<?> columnList = sqlSession.selectList("DiagnosisMapper.selectColumnList", params);
//			List<?> dataList = sqlSession.selectList("DiagnosisMapper.selectDataList", params);
			
			Object key = null;
			for (Object o : columnList) {
				HashMap map = (HashMap) o;
				columnNames.add(map.get("COLUMN_NAME").toString());
			}
			params.put("columnList", columnNames);
			
			List<?> dataList = sqlSession.selectList("DiagnosisMapper.selectSelDataList", params);
			
//			Object key = null;
//			for(int i=0;i<dataList.size();i++) {
//				HashMap map = (HashMap) dataList.get(i);
//				Iterator it = map.keySet().iterator();
//				while (it.hasNext()) {
//					key = it.next();
//					logger.info(key + " : "+ map.get(key));
//				}
//				
//			}
			
			boolean csvAt = false;
			if("CSV".equals(params.get("conType"))) {
				csvAt = true;
			}
			
			
			resultMap.put("columnList", columnList);
			resultMap.put("dataList", dataList);
			resultMap.put("csvAt", csvAt);
			
//			ObjectMapper mapper = new ObjectMapper(); // parser
//			json = mapper.writeValueAsString(resultMap);
//			logger.info(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
//		out.print(json);
//		out.flush();
//		out.close();
	}
	
	
	public Map<String, Object> selectAnalysisAjax(String schemaName, String tableName, String[] columnName, String rowCount, HttpServletRequest req,
			HttpServletResponse res, HttpSession session) throws Exception, IOException{

//		res.setContentType("text/html; charset=utf-8");
//		PrintWriter out = res.getWriter();
//		String json = null; 
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			Map<String, Object> params = new HashMap<>();
			
			params.put("schemaName", schemaName);
			params.put("tableName", tableName);
			params.put("columnList", columnName);
			params.put("rowCount", rowCount);
			
			// 세션값에서 기관정보 , 진단대상데이터베이스정보 조회 처리
			Member member = (Member)session.getAttribute("member");
			String dgnssDbmsId = (String)session.getAttribute("DDId");
			String insttCode = SangsUtil.getInsttCode();

			if (member != null) {
				insttCode = member.getInsttCode();
			}
			if (dgnssDbmsId == null) {
				dgnssDbmsId = "";
			}
			params.put("insttCode", insttCode);
			
			String newDdId = MapUtils.getString(params, "dgnssDbmsId");

			if (StringUtils.isNotEmpty(newDdId) && !StringUtils.equals(dgnssDbmsId, newDdId)) {
				Map<String, String> param = new HashMap<>();
				param.put("dgnssDbmsId", newDdId);
				try {
					basicInfoService.setDbmsUrl(param, req, null);
					dgnssDbmsId = newDdId;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			params.put("dgnssDbmsId", dgnssDbmsId);
			
			Map<String, String> data = new HashMap();
			data.put("dgnssDbmsId", dgnssDbmsId);
			data = ruleMngService.selectDgnssDbmsInfo(data);
			params.putAll(data);
			
			List<Map<String, Object>> selectColList = sqlSession.selectList("DiagnosisMapper.selectSelColumnList", params);
			List<Map<String, Object>> selectDataList = sqlSession.selectList("DiagnosisMapper.selectSelDataList", params);
			List<SangsMap> analsList = selectAnalsList(params);
			
			boolean csvAt = false;
			if (StringUtils.startsWithIgnoreCase(MapUtils.getString(params, "tableName"), "csv")) {
				csvAt = true;
			}
			
			
			resultMap.put("selectColList", selectColList);
			resultMap.put("selectDataList", selectDataList);
			resultMap.put("analsList", analsList);
			resultMap.put("analsGroup", changeListToMap(selectAnalsGroupList(params), "groupCode"));
			resultMap.put("csvAt", csvAt);

//			ObjectMapper mapper = new ObjectMapper(); // parser
//			json = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {
			logger.error("e: erorr {} "+e);
		}
		return resultMap;
//		out.print(json);
//		out.flush();
//		out.close();
	}
	
	
	private Map<String, Object> changeListToMap(List<SangsMap> list, String key) {
		Map<String, Object> resultMap = new HashMap<>();
		for (SangsMap map : list) {
			resultMap.put(MapUtils.getString(map, key), map);
		}
		return resultMap;
	}
	
	public List<?> selectTableDataList(Map<String, Object> paramMap) {
		List<?> result = diagnosisMapper.selectTableDataList(paramMap);
		return result;
	}
	
	public List<?> selectAnalsTableDataList(Map<String, Object> paramMap) {
		List<?> result = diagnosisMapper.selectAnalsTableDataList(paramMap);
		return result;
	}
	
	public List<?> selectTableColumnsList(Map<String, Object> paramMap) {
		List<?> result = diagnosisMapper.selectTableColumnsList(paramMap);
		return result;
	}
	
	public int pattenCheckCnt(Map<String, Object> paramMap) {
		// 오라클이고 IP(V6)일때 JAVA 로 처리 필요.....
		String chkPattern = paramMap.get("analsFrmla").toString();
		Pattern p = Pattern.compile(chkPattern);
		String inStr = "";
		int index = 0;
		int dataCnt = 0;
		int ko = 10000;
		try {
			//logger.info("database : "+paramMap.get("database").toString());
			//logger.info("tableNm : "+paramMap.get("tableNm").toString());
			dataCnt = selectRowCount(paramMap);
			//logger.info("dataCnt : "+dataCnt);
			// 1048576
			if((dataCnt/ko) > 1) {
				
				int moc = (dataCnt/ko);
				int han = (dataCnt%ko);
				//logger.info("건수가 1만건 이상 : 1만건씩 나눠서 실행한다.== 몫:"+moc+" 나머지:"+han);
				// 100000 건 단위로 실행
				for(int s=0;s<=moc;s++) {
					//logger.info(s+" >> "+((s*ko))+" : "+ko);
					int startInt = ((s*ko));
					//logger.info(s+" : startInt : "+startInt+" , scope : "+ko);
					paramMap.put("startInt", startInt);
					paramMap.put("endInt", (startInt+ko));
					paramMap.put("scope", ko);
					
					// 분석 대상 데이터를 조회
					List<Map<String, Object>> pattenList = selectPatternList(paramMap);
					//logger.info(s+" : pattenList.size() >> "+pattenList.size());
					Object key = null;
					for(int i=0;i<pattenList.size();i++) {
						HashMap map = (HashMap) pattenList.get(i);
						Iterator it = map.keySet().iterator();
						StringBuffer strBuf = new StringBuffer("");
						while (it.hasNext()) {
							key = it.next();
							strBuf = new StringBuffer("");
							if(key.equals(MapUtils.getString(paramMap, "columnNm")) && map.get(key) != null) {
								//inStr = map.get(key).toString();
								strBuf.append(map.get(key).toString());
								// 조회된 데이터를 정규식으로 처리
								Matcher m = p.matcher(strBuf.toString());
								if(m.find())
								{
									index++;
								}
							}
						}
					}
				}
			}else {
				// 분석 대상 데이터를 조회
				List<Map<String, Object>> pattenList = selectPatternList(paramMap);
				Object key = null;
				for(int i=0;i<pattenList.size();i++) {
					HashMap map = (HashMap) pattenList.get(i);
					Iterator it = map.keySet().iterator();
					StringBuffer strBuf = new StringBuffer("");
					while (it.hasNext()) {
						key = it.next();
						strBuf = new StringBuffer("");
						if(key.equals(MapUtils.getString(paramMap, "columnNm")) && map.get(key) != null) {
							//inStr = map.get(key).toString();
							strBuf.append(map.get(key).toString());
							// 조회된 데이터를 정규식으로 처리
							Matcher m = p.matcher(strBuf.toString());
							if(m.find())
							{
								index++;
							}
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			insertDgnssError(paramMap);
		}
		
		//index값이 총 dataCnt를 넘으면 index값에 dataCnt 넣어준다.
		if(index > dataCnt) {
			index = dataCnt;
		}
		
		return index;
	}
	
	public List<Map<String, Object>> pattenCheckCnt(List<Map<String, Object>> valueList,List<Map<String, Object>> pattenList,  Map<String, Object> paramMap, int reportListCnt) {
		
		try {
			String chkPattern = paramMap.get("analsFrmla").toString();
			Pattern p = Pattern.compile(chkPattern);
			String inStr = "";
			int matchIndex = 0;
			int notMatchIndex = 0;
			
			Object key = null;
			for(int i=0;i<pattenList.size();i++) {
				HashMap map = (HashMap) pattenList.get(i);
				Iterator it = map.keySet().iterator();
				Map<String, Object> matchMap = new HashMap<>();
				Map<String, Object> notMatchMap = new HashMap<>();
				Map<String, Object> matchTempMap = new HashMap<>();
				inStr = "";
				boolean chekResult = false;
				while (it.hasNext()) {
					key = it.next();
					if(key.equals(paramMap.get("columnNm")) && map.get(key) != null) {
						inStr = map.get(key).toString();
						if(!"".equals(inStr) && inStr != null) {
							// 조회된 데이터를 정규식으로 처리
							Matcher m = p.matcher(inStr);
							
							if(m.find())
							{
								chekResult = true;
								matchIndex++;
							}else {
								chekResult = false;
							}
						}else {
							chekResult = false;
						}
					}else {
						inStr = "";
					}
					matchTempMap.put(key.toString(), map.get(key));
					
					
				}
				
				if(chekResult) {
					if(matchIndex <= reportListCnt) {
						matchMap.putAll(matchTempMap);
					}
				}else {
					notMatchIndex++;
					if(notMatchIndex <= reportListCnt) {
						notMatchMap.putAll(matchTempMap);
					}
				}
				if(paramMap.get("matchMm").equals("mis")) {
					if(notMatchIndex <= reportListCnt && notMatchMap.size() > 0) {
						valueList.add(notMatchMap);
					}
					if(notMatchIndex > reportListCnt) {
						break;
					}
				}else {
					if(matchIndex <= reportListCnt && matchMap.size() > 0) {
						valueList.add(matchMap);
					}
					if(matchIndex > reportListCnt) {
						break;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			insertDgnssError(paramMap);
		}
		return valueList;
	}

	public List<Object> selectColumnType(Map<String, Object> paramMap) {
		List<Object> columnType = sqlSession.selectList("DiagnosisMapper.selectColumnType",paramMap);
		return columnType;
	}
}

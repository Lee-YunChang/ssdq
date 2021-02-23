package com.webapp.dqsys.mngr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.mapper.BasicInfoMapper;
import com.webapp.dqsys.mngr.service.DiagnosisService;
import com.webapp.dqsys.mngr.service.RuleMngService;

import ch.qos.logback.classic.Logger;

@Component
public class analsScheduler  {

	 protected Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	 @Autowired
	private DiagnosisService diagnosisService;
    
	 @Autowired
	private BasicInfoMapper basicInfoMapper;
	
	 @Autowired
	 private RuleMngService ruleMngService;
	 
    
    
    @Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;
    
	//매분 15초마다 스케줄링
	@Scheduled(cron = "15 * * * * ?")
	public void schedule() throws Exception {

		try {
			//스케줄링 데이터 조회
			List<SangsMap> columnList = diagnosisService.selectScheduler();
			 
	    	Map<String, Object> paramMap = new ConcurrentHashMap<>();
	    	Map<String, String> params = new HashMap<>();
	    	
	    	HttpServletRequest req = null;
	    	HttpServletResponse res = null;
	    	ModelMap model= null;
			
	    	if(columnList.size() != 0) {
				
			
				paramMap.put("insttCode",columnList.get(0).get("insttCode"));
				paramMap.put("dgnssSaveId",columnList.get(0).get("dgnssSaveId"));
				paramMap.put("dgnssDbmsId",columnList.get(0).get("dgnssDbmsId"));
				paramMap.put("dgnssSaveNm",columnList.get(0).get("dgnssSaveNm"));
				paramMap.put("dgnssInfoId", columnList.get(0).get("dgnssDbmsId") + "_" + columnList.get(0).get("dgnssSaveId"));
				paramMap.put("tableNm",columnList.get(0).get("tableNm"));
				paramMap.put("schemaNm",columnList.get(0).get("schemaNm"));
				paramMap.put("columnNmDc",columnList.get(0).get("columnNmDc"));
				paramMap.put("analsIdDc",columnList.get(0).get("analsIdDc"));
				paramMap.put("dbmsKnd",columnList.get(0).get("dbmsKnd"));
				
				String dgnssDbmsId = (String) columnList.get(0).get("dgnssDbmsId");
				String insttCode = (String) columnList.get(0).get("insttCode");
				
				params.put("dgnssDbmsId", MapUtils.getString(paramMap, "dgnssDbmsId"));
				List<?> dataList = basicInfoMapper.selectDbInfo(params);
				paramMap.put("dataList",dataList);
				
				
				//분석 시작시간, 상태값 업데이트
				paramMap.put("excSttus","S");
				diagnosisService.updateDgnssTable(paramMap);
				//분석 db연결체크
				dbmsConCheck(paramMap,  model, dataList);
				//분석 db연결 
				setDbmsUrl(paramMap, req, res, dataList );
				
				//oracle 쿼리문 조회시 필요한 정보 세팅
				Map<String, String> data = new HashMap();
				data.put("dgnssDbmsId", dgnssDbmsId);
				data = ruleMngService.selectDgnssDbmsInfo(data);
				paramMap.putAll(data);
				
				
				String schemaName = columnList.get(0).get("schemaNm").toString();
				String tableName =  columnList.get(0).get("tableNm").toString();
				String [] columnNameList = (columnList.get(0).get("columnNmDc").toString()).split(",");
				
				List<Map<String, Object>> columnMapList = diagnosisService.selectColumnList(schemaName, tableName, columnNameList);
				
				//data count 디폴드 분석을 위한 param값 추가
				if("Oracle".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
					paramMap.put("dataCntId", "1001");
				} else if("MySQL".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
					paramMap.put("dataCntId", "3001");
				} else if("Tibero".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
					paramMap.put("dataCntId", "4001");
				} else if("MSSQL".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
					paramMap.put("dataCntId", "5001");
				} else if("CSV".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
					paramMap.put("dataCntId", "2001");
				}
				
				Map<String, SangsMap> analsIdMap = changeListToMap(diagnosisService.selectAnalsList(paramMap), "analsId");
				
				String analsIdDc = paramMap.get("analsIdDc").toString();
				analsIdDc = analsIdDc.replaceAll("\"","");
				
				String [] analsList = analsIdDc.split("],");
				
				int i = 0;
				
				for (Map<String, Object> columnMap : columnMapList) {
					
					String columnName = MapUtils.getString(columnMap, "COLUMN_NAME");
					String anals = analsList[i];
					anals = anals.replaceAll(columnName+":"+"\\[", "");
					
					if(i+1 == analsList.length) {
						anals = anals.replaceAll("\\]", "");
					}
					
					i++;
					
					String[] analsIdList = anals.split(",");
				
					if (analsIdList == null) {
						continue;
					}
		
					paramMap.put("columnNm", columnName);
					paramMap.put("columnTy", columnMap.get("DATA_TYPE"));
					if (columnMap.get("DATA_LENGTH") != null) {
						if(!"text".equals(columnMap.get("DATA_TYPE")) && !"clob".equals(columnMap.get("DATA_TYPE"))) {
							paramMap.put("columnLt", columnMap.get("DATA_LENGTH"));
						}else {
							paramMap.put("columnLt", "");
						}
					}else {
						paramMap.put("columnLt", "");
					}
					paramMap.put("trgcnt", analsIdList.length);
		
					diagnosisService.insertDgnssColumn(paramMap);
					
		
//					for (String analsId : analsIdList) {
//						paramMap.put("analsId", analsId);
//		
//						if (!StringUtils.equals(MapUtils.getString(analsIdMap.get(analsId), "analsSe"), "AG000200")) { 
//							// Freq 가  아닌 경우 (DB에 아직 Freq 구분 플래그가 없어서 하드코딩 함)
//							diagnosisService.insertDgnssColumnRes(paramMap);
//						} else {
//							paramMap.put("sn", 0);
//							diagnosisService.insertFrqAnal(paramMap);
//						}
//		
//						Map<String, Object> asyncMap = new ConcurrentHashMap<>();
//						asyncMap.putAll(paramMap);
//						// 컬럼별 분석 항목 쓰레드 실행
//						diagnosisService.executeAnalysisAnalsId(asyncMap, analsIdMap.get(analsId));
//					}
					
					// 컬럼 단위로 쓰레드 실행 
					Map<String, Object> asyncMap = new ConcurrentHashMap<>();
					asyncMap.putAll(paramMap);
					diagnosisService.asyncExecutorAnalysisColumn(asyncMap, analsIdMap, analsIdList);
			
				}
				diagnosisService.executeUpdateEndDgnssTable(paramMap);
				//return "redirect:result/list.do";
			}
    	} catch (Exception e){
    		e.printStackTrace();
    		
    	}
		
	}
	
	
	private Map<String, SangsMap> changeListToMap(List<SangsMap> list, String key) {

		Map<String, SangsMap> resultMap = new HashMap<>();

		for (SangsMap map : list) {
			resultMap.put(MapUtils.getString(map, key), map);
		}

		return resultMap;
	}
	
	
	//분석 DB 연결체크
	private void dbmsConCheck(Map<String, Object> params, ModelMap model ,List<?> dataList) throws Exception, IOException {
		boolean result = false;
		boolean check1 = false;
		boolean check2 = false;
		Map<String, String> dataMap = new HashMap();
		Connection connection = null;
		Statement statement = null;
		Statement statement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;

		String user = "";
		String password = "";
		String driverName = "";
		String url = "";
		String insttCode = "";
		int resCnt = 0;
		int schemaCnt = 0;
		int dbmsNmCnt = 0;
		String message = "";
		String type = "";
		String paramtr = "";
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Object key = null;
		Map data = new HashMap();
		
		for (int i = 0; i < dataList.size(); i++) {
			HashMap map = (HashMap) dataList.get(i);
			Iterator it = map.keySet().iterator();

			while (it.hasNext()) {
				key = it.next();
				data.put(key.toString(), map.get(key));
			}
		}
			
			dataMap.put("ip", data.get("ip").toString());
			dataMap.put("port", data.get("port").toString());
			dataMap.put("id", data.get("id").toString());
			dataMap.put("pwd", data.get("password").toString());
			if(!"MySQL".equals(data.get("dbmsKnd").toString())) {
				dataMap.put("sid", data.get("sid").toString());
			}
			dataMap.put("database", data.get("database").toString());
			//dataMap.put("schema", data.get("schema").toString());
			dataMap.put("dbmsKnd", data.get("dbmsKnd").toString());
			dataMap.put("dgnssDbmsNm", data.get("dgnssDbmsNm").toString());
			dataMap.put("dgnssDbmsId", data.get("dgnssDbmsId").toString());
			dataMap.put("paramtr", data.get("paramtr").toString());
			dataMap.put("type", "reCheck");
			dataMap.put("insttCode", params.get("insttCode").toString());

			user = dataMap.get("id").toString();
		password = dataMap.get("pwd").toString();
		
		
		
//		driverName = "oracle.jdbc.driver.OracleDriver";
//		url = "jdbc:Oracle:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":" + dataMap.get("sid");
		
		String dbmsKnd = dataMap.get("dbmsKnd").toString().toUpperCase();
		
		logger.debug("dbmsKnd : {}", dbmsKnd);
		
		if(!"".equals(dataMap.get("paramtr").toString()) && dataMap.get("paramtr") != null) {
			if("MYSQL".equals(dbmsKnd)) {
				paramtr = "&"+dataMap.get("paramtr").toString();
			}else {
				paramtr = "?"+dataMap.get("paramtr").toString();
			}
		}
		
		// ORACLE, MYSQL, TIBERO, MSSQL
		String sql = "SELECT COUNT(1) AS CNT FROM DUAL";
		String sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('"+dataMap.get("database")+"') GROUP BY OWNER";
		
		// Driver, URL 설정
		if("ORACLE".equals(dbmsKnd)) {
			driverName = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:Oracle:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":" + dataMap.get("sid")+paramtr;
			sql = "SELECT COUNT(1) AS CNT FROM DUAL";
			sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('"+dataMap.get("database")+"') GROUP BY OWNER";
		}else if("MYSQL".equals(dbmsKnd)) {
			driverName = "com.mysql.cj.jdbc.Driver";
			//url = "jdbc:mysql://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/" + dataMap.get("database")+ "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000"+paramtr;
			url = "jdbc:mysql://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/" + dataMap.get("database")+ "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000&connectTimeout=7200000&socketTimeout=7200000"+paramtr;
			sql = "SELECT COUNT(1) AS CNT FROM DUAL";
			sql2 = "SELECT COUNT(SCHEMA_NAME) AS CNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"+dataMap.get("database")+"' GROUP BY SCHEMA_NAME";
			
		}else if("TIBERO".equals(dbmsKnd)) {
			driverName = "com.tmax.tibero.jdbc.TbDriver";
			url = "jdbc:tibero:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":" + dataMap.get("sid")+paramtr;
			sql = "SELECT COUNT(1) AS CNT FROM DUAL";
			sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('"+dataMap.get("database")+"') GROUP BY OWNER";
		}else if("MSSQL".equals(dbmsKnd)) {
			// com.microsoft.sqlserver
			driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			url = "jdbc:sqlserver://" + dataMap.get("ip") + ":" + dataMap.get("port") + ";databaseName=" + dataMap.get("database");
			sql = "SELECT COUNT(1) AS CNT FROM INFORMATION_SCHEMA.TABLES";
			sql2 = "SELECT COUNT(CATALOG_NAME) AS CNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE CATALOG_NAME = '"+dataMap.get("database")+"' ";
			
		}
		
		logger.info(sql);
		logger.info(sql2);
		logger.info("driverName : "+driverName);
		logger.info("url : "+url);
		logger.info("user : "+user);
		logger.info("password : "+password);

		
		try {
			logger.info("[연결 시작]");
			
			// 로드
			Class.forName(driverName);
			
			// 연결 체크
			DriverManager.setLoginTimeout(5); // timeout 설정 : 5초 , 입력정보 오류로 접속이 안될때
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			statement2 = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet != null) {
				if( resultSet.next() ){
					resCnt = resultSet.getInt("CNT");
					if(resCnt > 0) {
						check1 = true;
						logger.info("[연결 성공]");
					}
				}
			}else {
				logger.info("[연결 실패]");
				message = "입력하신 DBMS 정보로 연결 할 수가 없습니다. 입력정보를 확인 하세요.";
			}
			if(dataMap.get("type").equals("reCheck")) {
				result = check1;
			}else {
				// schema 체크
				if(check1==true) {
					resultSet2 = statement2.executeQuery(sql2);
					if (resultSet2 != null) {
						if( resultSet2.next() ){
							schemaCnt = resultSet2.getInt("CNT");
							
							if(schemaCnt > 0) {
								check2 = true;
								logger.info("[스키마 정상]");
								message = "연결 성공";
							}else {
								check2 = false;
								logger.info("[스키마 정보 오류]");
								message = "Database 입력 정보를 확인 해 주세요.";
							}
						}else {
							check2 = false;
							logger.info("[스키마 정보 오류]");
							message = "Database 입력 정보를 확인 해 주세요.";
						}
					}else {
						check2 = false;
						logger.info("[스키마 정보 오류]");
						message = "Database 입력 정보를 확인 해 주세요.";
					}
				}
				
				// 진단대상 데이터베이스명 체크
				if(check2==true) {
					// dgnssDbmsNm
					dbmsNmCnt = basicInfoMapper.selectDgnssDbmsNmCheck(dataMap);
					if(dbmsNmCnt > 0) {
						result = false;
						logger.info("[진단대상 데이터베이스명 중복]");
						message = "이미 등록되어 있는 진단대상 데이터베이스명 입니다.";
					}else {
						result = true;
						logger.info("[진단대상 데이터베이스명 체크 완료]");
					}
				}
			}
			logger.info("message : "+message);
			resultMap.put("result", result);
			resultMap.put("message", message);

		} catch (ClassNotFoundException e) {
			resultMap.put("result", result);
			resultMap.put("message", e.getMessage());
			logger.error("[로드 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			resultMap.put("result", result);
			resultMap.put("message", e.getMessage());
			logger.error("[연결 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}finally {
			if(connection != null) {
				connection.close();
			}
			if(statement != null) {
				statement.close();
			}
			if(statement2 != null) {
				statement2.close();
			}
			
		}

	}
	
	
	//분석DB연결
	public void setDbmsUrl(Map<String, Object> params, HttpServletRequest req, HttpServletResponse res,List<?> dataList )
			throws Exception {
		String strUrl = "";
		String showUrl = "";
		String dgnssDbmsId = "";
		String schema = "";
		// 추가 
		String fileName = "";
		String conType = "";
		ModelMap model = null;
		
		Map dataMap = new HashMap();
		try {
			
			if (dataList != null && dataList.size() > 0) {
				Object key = null;

				for (int i = 0; i < dataList.size(); i++) {
					HashMap map = (HashMap) dataList.get(i);
					Iterator it = map.keySet().iterator();
					if(i== 0) {
						while (it.hasNext()) {
							key = it.next();
							dataMap.put(key.toString(), map.get(key));
						}
					}
				}
				conType = dataMap.get("dbmsKnd").toString();
				String dbmsKnd = dataMap.get("dbmsKnd").toString().toUpperCase();
				
				if("ORACLE".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:Oracle:thin:@125.7.207.6:1522:ORCL
					strUrl = "jdbc:oracle:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port")+ ":" + dataMap.get("sid");
					showUrl = strUrl;
				}else if("MYSQL".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:mysql://localhost:3389/test
					//strUrl = "jdbc:mySql://" + dataMap.get("ip") + ":" + dataMap.get("port")+ "/"+ dataMap.get("database") + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000";
					strUrl = "jdbc:mySql://" + dataMap.get("ip") + ":" + dataMap.get("port")+ "/"+ dataMap.get("database") + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000&connectTimeout=7200000&socketTimeout=7200000";
					showUrl = "jdbc:mySql://" + dataMap.get("ip") + ":" + dataMap.get("port")+ "/"+ dataMap.get("database");
				}else if("TIBERO".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:tibero:thin:@localhost:3389:tibero
					strUrl = "jdbc:tibero:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port")+ ":" + dataMap.get("sid");
					showUrl = strUrl;
				}else if("MSSQL".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:sqlserver://125.7.207.6:1433;databaseName=master;integratedSecurity=true
					strUrl = "jdbc:sqlserver://" + dataMap.get("ip") + ":" + dataMap.get("port")+ ";database="+ dataMap.get("database");
					showUrl = "jdbc:sqlserver://" + dataMap.get("ip") + ":" + dataMap.get("port")+ ";database="+ dataMap.get("database");
				}else if("CSV".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
			        String str = dataMap.get("dgnssDbmsNm").toString();
			        int idx = str.indexOf("("); 
			        fileName = str.substring(0, idx);
			        strUrl = "jdbc:mariadb://" + dataMap.get("ip") + ":" + dataMap.get("port")+ "/"+ dataMap.get("database");
			        showUrl = fileName;
					
				}
				dgnssDbmsId = dataMap.get("dgnssDbmsId").toString();
			}
			logger.debug("conDbUrl : {} ", strUrl);

			sqlSession.addDataSource(dataMap, strUrl); 
		} catch (Exception e) {
			logger.error("[DBMS 정보 조회 실패]" + e.getMessage());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	
}

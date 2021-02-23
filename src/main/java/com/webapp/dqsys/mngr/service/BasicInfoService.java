package com.webapp.dqsys.mngr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.domain.BaseFile;
import com.webapp.dqsys.mngr.mapper.BasicInfoMapper;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.util.CSVReader;
import com.webapp.dqsys.util.FileUploadUtil;
import com.webapp.dqsys.util.SangsAbstractService;
import com.webapp.dqsys.util.WebUtil;
import com.zaxxer.hikari.util.SuspendResumeLock;

@Service
public class BasicInfoService extends SangsAbstractService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

//	@Resource(name = "oracleSqlSessionTemplate")
//	private SqlSessionTemplate oracleSqlSession;
//
//	@Resource(name = "msSqlSessionTemplate")
//	private SqlSessionTemplate msSqlSession;
//
//	@Resource(name = "mySqlSessionTemplate")
//	private SqlSessionTemplate mySqlSession;
//
//	@Resource(name = "mariaSqlSessionTemplate")
//	private SqlSessionTemplate mariaSqlSession;

	@Autowired
	private BasicInfoMapper basicInfoMapper;

	@Value("${file.csv.dataDir}")
	private String csvDir;

	@Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;

	/**
	 * @param params
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void dbmsConCheck(Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {
		boolean result = false;
		boolean check1 = false;
		boolean check2 = false;
		Map dataMap = new HashMap();
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

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		// 세션정보에서 기관코드 정보 조회
		Member member = (Member) req.getSession().getAttribute("member");
		if (member != null) {
			insttCode = member.getInsttCode();
		}

		for (Object o : arrItems) {
			JSONObject item = (JSONObject) o;

			dataMap.put("ip", item.get("ip").toString());
			dataMap.put("port", item.get("port").toString());
			dataMap.put("id", item.get("id").toString());
			dataMap.put("pwd", item.get("pwd").toString());
			if (!"MySQL".equals(item.get("dbmsKnd").toString())) {
				dataMap.put("sid", item.get("sid").toString());
			}
			dataMap.put("database", item.get("database").toString());
			// dataMap.put("schema", item.get("schema").toString());
			dataMap.put("dbmsKnd", item.get("dbmsKnd").toString());
			dataMap.put("dgnssDbmsNm", item.get("dgnssDbmsNm").toString());
			dataMap.put("dgnssDbmsId", item.get("dgnssDbmsId").toString());
			dataMap.put("paramtr", item.get("paramtr").toString());
			dataMap.put("insttCode", insttCode);
			dataMap.put("type", item.get("type").toString());

		}
		user = dataMap.get("id").toString();
		password = dataMap.get("pwd").toString();

//		driverName = "oracle.jdbc.driver.OracleDriver";
//		url = "jdbc:Oracle:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":" + dataMap.get("sid");

		String dbmsKnd = dataMap.get("dbmsKnd").toString().toUpperCase();

		logger.debug("dbmsKnd : {}", dbmsKnd);

		if (!"".equals(dataMap.get("paramtr").toString()) && dataMap.get("paramtr") != null) {
			if ("MYSQL".equals(dbmsKnd)) {
				paramtr = "&" + dataMap.get("paramtr").toString();
			} else {
				paramtr = "?" + dataMap.get("paramtr").toString();
			}
		}

		// ORACLE, MYSQL, TIBERO, MSSQL
		String sql = "SELECT COUNT(1) AS CNT FROM DUAL";
		String sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('"
				+ dataMap.get("database") + "') GROUP BY OWNER";

		// Driver, URL 설정
		if ("ORACLE".equals(dbmsKnd)) {
			driverName = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:Oracle:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":" + dataMap.get("sid")
					//+ paramtr //추가적용변수 임시 해제
					;
			sql = "SELECT COUNT(1) AS CNT FROM DUAL";
			sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('" + dataMap.get("database")
					+ "') GROUP BY OWNER";
		} else if ("MYSQL".equals(dbmsKnd)) {
			driverName = "com.mysql.cj.jdbc.Driver";
			// url = "jdbc:mysql://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/" +
			// dataMap.get("database")+
			// "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&serverTimezone=UTC&validationQuery=SHOW
			// databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000"+paramtr;
			url = "jdbc:mysql://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/" + dataMap.get("database")
					+ "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000&connectTimeout=7200000&socketTimeout=7200000"
					//+ paramtr //추가적용변수 임시 해제
					;
			sql = "SELECT COUNT(1) AS CNT FROM DUAL";
			sql2 = "SELECT COUNT(SCHEMA_NAME) AS CNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '"
					+ dataMap.get("database") + "' GROUP BY SCHEMA_NAME";

		} else if ("TIBERO".equals(dbmsKnd)) {
			driverName = "com.tmax.tibero.jdbc.TbDriver";
			url = "jdbc:tibero:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":" + dataMap.get("sid")
					//+ paramtr //추가적용변수 임시 해제
					;
			sql = "SELECT COUNT(1) AS CNT FROM DUAL";
			sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('" + dataMap.get("database")
					+ "') GROUP BY OWNER";
		} else if ("MSSQL".equals(dbmsKnd)) {
			// com.microsoft.sqlserver
			driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			url = "jdbc:sqlserver://" + dataMap.get("ip") + ":" + dataMap.get("port") + ";databaseName="
					+ dataMap.get("database");
			sql = "SELECT COUNT(1) AS CNT FROM INFORMATION_SCHEMA.TABLES";
			sql2 = "SELECT COUNT(CATALOG_NAME) AS CNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE CATALOG_NAME = '"
					+ dataMap.get("database") + "' ";

		}

		logger.info(sql);
		logger.info(sql2);
		logger.info("driverName : " + driverName);
		logger.info("url : " + url);
		logger.info("user : " + user);
		logger.info("password : " + password);

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
				if (resultSet.next()) {
					resCnt = resultSet.getInt("CNT");
					if (resCnt > 0) {
						check1 = true;
						logger.info("[연결 성공]");
					}
				}
			} else {
				logger.info("[연결 실패]");
				message = "입력하신 DBMS 정보로 연결 할 수가 없습니다. 입력정보를 확인 하세요.";
			}
			if (dataMap.get("type").equals("reCheck")) {
				result = check1;
			} else {
				// schema 체크
				if (check1 == true) {
					resultSet2 = statement2.executeQuery(sql2);
					if (resultSet2 != null) {
						if (resultSet2.next()) {
							schemaCnt = resultSet2.getInt("CNT");

							if (schemaCnt > 0) {
								check2 = true;
								logger.info("[스키마 정상]");
								message = "연결 성공";
							} else {
								check2 = false;
								logger.info("[스키마 정보 오류]");
								message = "Database 입력 정보를 확인 해 주세요.";
							}
						} else {
							check2 = false;
							logger.info("[스키마 정보 오류]");
							message = "Database 입력 정보를 확인 해 주세요.";
						}
					} else {
						check2 = false;
						logger.info("[스키마 정보 오류]");
						message = "Database 입력 정보를 확인 해 주세요.";
					}
				}

				// 진단대상 데이터베이스명 체크
				if (check2 == true) {
					// dgnssDbmsNm
					dbmsNmCnt = basicInfoMapper.selectDgnssDbmsNmCheck(dataMap);
					if (dbmsNmCnt > 0) {
						result = false;
						logger.info("[진단대상 데이터베이스명 중복]");
						message = "이미 등록되어 있는 진단대상 데이터베이스명 입니다.";
					} else {
						result = true;
						logger.info("[진단대상 데이터베이스명 체크 완료]");
					}
				}
			}
			logger.info("message : " + message);
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
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (statement2 != null) {
				statement2.close();
			}

		}
		json = mapper.writeValueAsString(resultMap);
		out.print(json);
		out.flush();
		out.close();

	}

	/**
	 * @param params
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void dbmsConSave(Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model)
			throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		int dbResult = 0;
		ObjectMapper mapper = new ObjectMapper(); // parser
		String insttCode = "";

		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
			HashMap<String, String> param = new HashMap<String, String>();

			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}

			for (Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				param.put("insttCode", insttCode);
				param.put("dgnssDbmsId", item.get("dgnssDbmsId").toString());
				param.put("dgnssDbmsNm", item.get("dgnssDbmsNm").toString());
				param.put("dbmsKnd", item.get("dbmsKnd").toString());
				if (item.get("dbmsVerId") != null) {
					param.put("dbmsVerId", item.get("dbmsVerId").toString());
					param.put("dbmsVerInfo", item.get("dbmsKnd").toString() + item.get("dbmsVerId").toString());
				}
				param.put("ip", item.get("ip").toString());
				param.put("port", item.get("port").toString());
				// param.put("schema", item.get("schema").toString());
				if (item.get("sid") != null) {
					param.put("sid", item.get("sid").toString());
				}
				param.put("database", item.get("database").toString());
				param.put("paramtr", item.get("paramtr").toString());
				param.put("id", item.get("id").toString());
				param.put("password", item.get("pwd").toString());
				param.put("rm", "");
				param.put("useAt", "Y");
				// DGNSS_DBMS
				int resCount = basicInfoMapper.selectDgnssDbmsListCnt(param);

				int dbmsNmCheck = basicInfoMapper.selectDgnssDbmsNmCheck(param);
				logger.debug("dbmsNmCheck : " + dbmsNmCheck);
				
				// 기존 등록 여부 체크
				if (dbmsNmCheck > 0) {
					// DGNSS_DBMS UPDATE
					dbResult = basicInfoMapper.updateDgnssDbms(param);
				} else {
					// INSTT_DBMS , INSTT_DBMS_VER 정보 조회
					int resInt = basicInfoMapper.selectInsttDbmsListCnt(param);
					if (resInt > 0) {
						String dbmsId = basicInfoMapper.selectInsttDbmsId(param);
						param.put("dbmsId", dbmsId);
						// DGNSS_DBMS INSERT
						dbResult = basicInfoMapper.insertDgnssDbms(param);
					}
				}
			}
			if (dbResult > 0) {
				result = true;
			} else {
				result = false;
			}

		} catch (Exception e) {
			logger.error("[저장 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			json = mapper.writeValueAsString(result);
			out.print(json);
			out.flush();
			out.close();
		}

	}

	public void mariaConSave(Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		int dbResult = 0;
		String insttCode = "";

		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
			HashMap<String, String> param = new HashMap<String, String>();

			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}

			for (Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				param.put("insttCode", insttCode);
				param.put("dgnssDbmsId", item.get("dgnssDbmsId").toString());
				param.put("dgnssDbmsNm", item.get("dgnssDbmsNm").toString());
				param.put("dbmsKnd", item.get("dbmsKnd").toString());
				if (item.get("dbVer") != null) {
					param.put("dbmsVerId", item.get("dbVer").toString());
					param.put("dbmsVerInfo", item.get("dbmsKnd").toString() + item.get("dbVer").toString());
				}
				param.put("ip", item.get("ip").toString());
				param.put("port", item.get("port").toString());
				param.put("schema", item.get("schema").toString());
				param.put("sid", item.get("sid").toString());
				param.put("database", "");
				param.put("paramtr", "");
				param.put("id", item.get("id").toString());
				param.put("password", item.get("pwd").toString());
				param.put("rm", "");
				param.put("useAt", "Y");
				// DGNSS_DBMS
				int resCount = basicInfoMapper.selectDgnssDbmsListCnt(param);

				// 기존 등록 여부 체크
				if (resCount > 0) {
					dbResult = -1;
				} else {
					// INSTT_DBMS , INSTT_DBMS_VER 정보 조회
					int resInt = basicInfoMapper.selectInsttDbmsListCnt(param);

					if (resInt > 0) {
						String dbmsId = basicInfoMapper.selectInsttDbmsId(param);
						param.put("dbmsId", dbmsId);
						// DGNSS_DBMS_ID 생서
//						String dgnssDbmsId = basicInfoMapper.selectDgnssDbmsIdNextVal(param);
//						param.put("dgnssDbmsId", dgnssDbmsId);
						// DGNSS_DBMS 등록
						dbResult = basicInfoMapper.insertDgnssDbms(param);
					}
				}

			}
			if (dbResult > 0) {
				result = true;
			} else {
				result = false;
			}
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(result);

		} catch (Exception e) {
			logger.error("[저장 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
		}
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * @param params
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public void selectDbList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		String json = null;
		List<?> dataList = null;
		int totalCnt = 0;
		try {
			// 페이징 처리 설정
			super.pagingSet(params);

			String dgnssDbmsId = "";
			if (params.get("dgnssDbmsId") != null) {
				dgnssDbmsId = params.get("dgnssDbmsId").toString();
				params.put("dgnssDbmsId", dgnssDbmsId);
			} else {
				params.put("dgnssDbmsId", "");
			}
			totalCnt = basicInfoMapper.selectDbListTotalCnt(params);
			dataList = basicInfoMapper.selectDbList(params);
			resultMap.put("totalCnt", totalCnt);
			resultMap.put("result", dataList);
			resultMap.put("tabType", params.get("tabType"));
			json = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
	}

	public int selectDbListTotalCnt(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {

		return basicInfoMapper.selectDbListTotalCnt(params);
	}

	public List<?> selDbmsKindList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		try {
			dataList = basicInfoMapper.selDbmsKindList(params);
		} catch (Exception e) {
			logger.error("[조회 오류]" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());

		}
		return dataList;
	}

	public List<?> selDbmsVerList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		try {
			dataList = basicInfoMapper.selDbmsVerList(params);
		} catch (Exception e) {
			logger.error("[조회 오류]" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
		}
		return dataList;
	}

	public void selectDbInfo(Map<String, String> params, HttpServletResponse res, ModelMap model)
			throws Exception, IOException {
		List<?> dataList = null;
		try {
			dataList = basicInfoMapper.selectDbInfo(params);
			model.addAttribute("dataList", dataList);
			Map<String, String> dataMap = new HashMap<String, String>();
			Object key = null;
			for (int i = 0; i < dataList.size(); i++) {
				HashMap map = (HashMap) dataList.get(i);
				Iterator it = map.keySet().iterator();

				while (it.hasNext()) {
					key = it.next();
					model.addAttribute(key.toString(), map.get(key));
				}
			}

		} catch (Exception e) {
			logger.error("[조회 오류]" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param params
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void setDbmsUrl(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String strUrl = "";
		String showUrl = "";
		String dgnssDbmsId = "";
		String dgnssDbmsNm = "";
		String schema = "";
		// 추가
		String fileName = "";
		String conType = "";
		ModelMap model = null;

		Map dataMap = new HashMap();
		try {

			List<?> dataList = basicInfoMapper.selectDbInfo(params);

			if (dataList != null && dataList.size() > 0) {
				Object key = null;

				for (int i = 0; i < dataList.size(); i++) {
					HashMap map = (HashMap) dataList.get(i);
					Iterator it = map.keySet().iterator();
					if (i == 0) {
						while (it.hasNext()) {
							key = it.next();
							dataMap.put(key.toString(), map.get(key));
						}
					}
				}
				conType = dataMap.get("dbmsKnd").toString();
				String dbmsKnd = dataMap.get("dbmsKnd").toString().toUpperCase();

				if ("ORACLE".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:Oracle:thin:@125.7.207.6:1522:ORCL
					strUrl = "jdbc:oracle:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":"
							+ dataMap.get("sid");
					showUrl = strUrl;
				} else if ("MYSQL".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:mysql://localhost:3389/test
					// strUrl = "jdbc:mySql://" + dataMap.get("ip") + ":" + dataMap.get("port")+
					// "/"+ dataMap.get("database") +
					// "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&serverTimezone=UTC&validationQuery=SHOW
					// databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000";
					strUrl = "jdbc:mySql://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/"
							+ dataMap.get("database")
							+ "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000&connectTimeout=7200000&socketTimeout=7200000";
					showUrl = "jdbc:mySql://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/"
							+ dataMap.get("database");
				} else if ("TIBERO".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:tibero:thin:@localhost:3389:tibero
					strUrl = "jdbc:tibero:thin:@" + dataMap.get("ip") + ":" + dataMap.get("port") + ":"
							+ dataMap.get("sid");
					showUrl = strUrl;
				} else if ("MSSQL".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					// jdbc:sqlserver://125.7.207.6:1433;databaseName=master;integratedSecurity=true
					strUrl = "jdbc:sqlserver://" + dataMap.get("ip") + ":" + dataMap.get("port") + ";database="
							+ dataMap.get("database");
					showUrl = "jdbc:sqlserver://" + dataMap.get("ip") + ":" + dataMap.get("port") + ";database="
							+ dataMap.get("database");
				} else if ("CSV".equals(dbmsKnd) && dataMap.get("dbmsKnd") != null) {
					String str = dataMap.get("dgnssDbmsNm").toString();
					int idx = str.indexOf("(");
					fileName = str.substring(0, idx);
					strUrl = "jdbc:mariadb://" + dataMap.get("ip") + ":" + dataMap.get("port") + "/"
							+ dataMap.get("database");
					showUrl = fileName;

				}
				dgnssDbmsId = dataMap.get("dgnssDbmsId").toString();
				dgnssDbmsNm = dataMap.get("dgnssDbmsNm").toString();
			}
			logger.debug("conDbUrl : {} ", strUrl);

			// 대상 정보 session 설정
			req.getSession().setAttribute("conDbUrl", showUrl);
			req.getSession().setAttribute("DDId", dgnssDbmsId);

			// 추가
			req.getSession().setAttribute("conType", conType);
			req.getSession().setAttribute("conFileName", fileName);
			req.getSession().setAttribute("conTitle", dgnssDbmsNm);

			sqlSession.addDataSource(dataMap, strUrl);
		} catch (Exception e) {
			logger.error("[DBMS 정보 조회 실패]" + e.getMessage());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void readFileData(Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {
		// 소요 시간 확인 용
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		// 등록 파일 확인
		List<Object> authorList = new ArrayList<Object>();
		String attFileOutputPath = csvDir + "/";
		String fileName = "";
		List<BaseFile> fileList = null;
		try {

			if (ServletFileUpload.isMultipartContent(req)) {
				fileList = FileUploadUtil.uploadFiles(req, attFileOutputPath);
			}

			if (fileList != null && fileList.size() > 0) {
				BaseFile vo = fileList.get(0);
				fileName = vo.getFileName();
				String attFileOutputFullPath = attFileOutputPath + vo.getPhysicalName();
				logger.info("attFileOutputFullPath : "+attFileOutputFullPath);
				logger.info("checkEncoding : "+checkEncoding(attFileOutputFullPath));
				File file = new File(attFileOutputFullPath);
				
				// UTF-8 변환 처리
				if(!checkEncoding(attFileOutputFullPath)) {
					// utf8 변환 처리
					String changText = FileUtils.readFileToString(file, "MS949");
					FileUtils.writeStringToFile(file, changText, "UTF-8");
				}

				//FileUtils.
				String fileText = FileUtils.readFileToString(file, "utf8");
			    
				String[] lines = fileText.split("\n");
				char LF = 0x0A;
				char CR = 0x0D;
				// fileText.replaceAll(LF, "\n\r");
				String[] crlflines = fileText.split("" + CR + LF);
				String[] lflines = fileText.split("" + LF);
				String[] crlines = fileText.split("" + CR);
				String osKnd = "WINDOWS";
				logger.info("lines.length : " + lines.length + " / crlflines.length : " + crlflines.length);
				logger.info("lflines.length : " + lflines.length + " / crlines.length : " + crlines.length);
				// 자동으로
				if (lines.length == crlflines.length) {
					osKnd = "WINDOWS";
				} else if (lines.length == crlines.length) {
					osKnd = "MAC";
				} else if (lines.length == lflines.length) {
					osKnd = "UNIX";
				}
				logger.info("osKnd : " + osKnd);
				int idx = 0;
				// 라인만큼 리스트 생성
				for (String str : lines) {
					Map<String, Object> authorMap = new HashMap<String, Object>();

					if (idx < 10) { // 10라인으로 리스트 제한
						authorMap.put("dataMap", str);
						authorList.add(authorMap);

					}
					idx++;
				}
				resultMap.put("dataList", authorList);
				resultMap.put("fileName", fileName);
				resultMap.put("attFileOutputFullPath", attFileOutputFullPath);
				resultMap.put("osKnd", osKnd);
				logger.info("=== 파일 사이즈    : " + (file.length() / 1024) + "KB");
			}
			stopWatch.stop();
			logger.info("=== 파일 업로드 처리 시간 : " + stopWatch.getTotalTimeSeconds());
			logger.info("========================================");

		} catch (Exception e) {
			logger.error("[파일 정보 조회 실패]" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}
		json = mapper.writeValueAsString(resultMap);
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void readFilePaser(Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		// 기존 파일정보 설정
		String fileName = req.getParameter("fileName");
		String attFilePath = req.getParameter("path");
		// 입력 정보 설정
		String culLineCnt = req.getParameter("culmLineCnt"); // 헤더 라인수
		String delimiter = req.getParameter("delimiter"); // 구분자

		int titleCnt = 0;
		// 입력 받은 값을 Int 형으로 변경
		if (!"".equals(culLineCnt) && culLineCnt != null) {
			titleCnt = Integer.parseInt(culLineCnt);
		} else {
			culLineCnt = "0";
		}
		if ("".equals(delimiter) || delimiter == null) {
			// default 값 설정
			delimiter = ",";
		}
		char tempChar[] = delimiter.toCharArray();
		char separator = tempChar[0];

		logger.info("============ 파일 처리 시작 =============");
		// 등록 파일 확인
		// 대상 파일이 CSV 파일일때 처리
		List<?> authorList = null;
		String attFileOutputPath = csvDir + "/";
		ArrayList<Map> rtnList = new ArrayList<Map>();
		List<BaseFile> fileList = null;
		WebUtil wutil = new WebUtil();
		try {
			File file = new File(attFilePath);
			if (file.exists()) {
				FileInputStream csvFile = new FileInputStream(file);
				InputStreamReader readFile = new InputStreamReader(csvFile, "utf8");
				CSVReader reader = new CSVReader(readFile, separator);
				String[] nextLine = null;

				int idx = 0;

				ArrayList<Map> columnList = new ArrayList<Map>();
				ArrayList<Map> dataList = new ArrayList<Map>();

				while ((nextLine = reader.readNext()) != null) {
					Map dataMap = new HashMap();
					int i = 0;
					for (String str : nextLine) {
						Map columnMap = new HashMap();
						// str = wutil.clearXSSMinimum(str);
						if (idx < titleCnt) {
							columnMap.put("header", str);
							columnMap.put("name", "column" + i);
							columnMap.put("align", "center");
							columnList.add(columnMap);
						} else {
							if (idx == 0) {
								columnMap.put("header", "column" + i);
								columnMap.put("name", "column" + i);
								columnMap.put("align", "center");
								columnList.add(columnMap);
							}
							dataMap.put("column" + i, str);

						}

						i++;
					}
					if (idx >= titleCnt && idx < 11) {
						dataList.add(dataMap);
					}

					idx++;

					if (idx - titleCnt > 8)
						break;

				}

				resultMap.put("columnList", columnList);
				resultMap.put("dataList", dataList);
				resultMap.put("fileName", fileName);
				resultMap.put("culLineCnt", culLineCnt);
				resultMap.put("delimiter", delimiter);
				resultMap.put("attFilePath", attFilePath);
			}

		} catch (Exception e) {
			logger.error("[파일 정보 변환 실패]" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}

		json = mapper.writeValueAsString(resultMap);
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void readFilePaserSave(Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {
		// 소요 시간 확인 용
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		String date = format.format(new Date());

		// 파일정보 수신
		String fileName = params.get("fileName").toString();
		String culLineCnt = params.get("culLineCnt").toString();
		String delimiter = params.get("delimiter").toString();
		String attFilePath = params.get("attFilePath").toString();
		String osKnd = params.get("osKnd").toString();

		int titleCnt = 0;
		// 입력 받은 값을 Int 형으로 변경
		if (!"".equals(culLineCnt) && culLineCnt != null) {
			titleCnt = Integer.parseInt(culLineCnt);
		}
		if ("".equals(delimiter) || delimiter == null) {
			// default 값 설정
			delimiter = ",";
		}
		char tempChar[] = delimiter.toCharArray();
		char separator = tempChar[0];

		// 등록 파일 확인

		// 대상 파일이 CSV 파일일때 처리
		List<?> authorList = null;
		String attFileOutputPath = csvDir + "/";
		ArrayList<Map> rtnList = new ArrayList<Map>();
		List<BaseFile> fileList = null;

		String insttCode = "";
		// 세션정보에서 기관코드 정보 조회
		Member member = (Member) req.getSession().getAttribute("member");
		if (member != null) {
			insttCode = member.getInsttCode();
		}

		logger.info("===================================");
		date = format.format(new Date());
		logger.debug("=== 파일등록 시작 : " + date);
		try {

			File file = new File(attFilePath);
			if (file.exists()) {
				FileInputStream csvFile = new FileInputStream(file);
				InputStreamReader readFile = new InputStreamReader(csvFile, "utf8");
				CSVReader reader = new CSVReader(readFile, separator);
				CSVReader tmpReader = new CSVReader(new FileReader(file));
				List tmpList = tmpReader.readAll();
				int rowCount = tmpList.size();
				logger.info("===================================" + rowCount);

				String[] nextLine = null;

				int idx = 0;

				ArrayList<Map> columnList = new ArrayList<Map>();
				ArrayList<Map> dataList = new ArrayList<Map>();
				// 컬럼 정보 설정
				while ((nextLine = reader.readNext()) != null) {
					Map<String, String> dataMap = new HashMap<String, String>();
					int i = 0;
					for (String str : nextLine) {
						Map<String, String> columnMap = new HashMap<String, String>();
						if (idx < titleCnt) {
							System.out.println(i + " : " + str);
							columnMap.put("title", str);
							columnMap.put("column", "column" + i);
							columnList.add(columnMap);
						} else {
							if (idx == 0) {
								System.out.println(i + " : " + str);
								columnMap.put("title", "column" + i);
								columnMap.put("column", "column" + i);
								columnList.add(columnMap);
							}
							break;
							// dataMap.put("column" + i, str);
						}

						i++;
					}
					if (idx >= titleCnt) {
						// dataList.add(dataMap);
						break;
					}
					idx++;

				}
				// 생성될 테이블 명 설정
				String tableName = fileName.substring(0, fileName.indexOf(".")) + "_file_" + date;
				// 생성될 테이블의 컬럼 속성 설정
				String culumnInfo = "";
				StringBuffer strBuf = new StringBuffer("");
				for (int k = 0; k < columnList.size(); k++) {
					// logger.debug("columnList" + columnList.get(k));
					strBuf.append("`" + columnList.get(k).get("column") + "`" + " varchar(255) null COMMENT '"
							+ columnList.get(k).get("title") + "'");
					if (k < columnList.size() - 1) {
						strBuf.append(",");
					}
				}
				culumnInfo = strBuf.toString();
				// 테이블명과 컬럼정보를 map으로 등록
				params.put("tableName", tableName);
				params.put("culumnInfo", culumnInfo);

				// 결과 리턴값 변수
				int ct = -1;
				int idres = -1;
				int totIdres = 0;
				int dataCnt = rowCount - titleCnt;

				// CREATE TABLE
				ct = basicInfoMapper.createTableDb(params);
				date = format.format(new Date());
				logger.debug("=== 테이블 생성 : " + date);
				// 테이블 생성시
				if (ct >= 0) {
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("tableName", tableName);
					paramMap.put("columnList", columnList);
					paramMap.put("delimiter", delimiter);
					paramMap.put("fullFilePath", file.getPath());
					paramMap.put("titleCnt", titleCnt);
					paramMap.put("osKnd", osKnd);

					logger.info("============ 파일 처리 : OK");
					logger.info("============ 데이터 등록 시작 ===");
					// 파일 데이터 등록
					totIdres = basicInfoMapper.mysqlimportFile(paramMap);

//					Map<String, String> columnMap = new HashMap<String, String>();
//					Map<String, String> dataMap = new HashMap<String, String>();
//					for (int d = 0; d < dataList.size(); d++) {
//						dataMap = dataList.get(d);
//
//						ArrayList<Map> dataValList = new ArrayList<Map>();
//						for (int c = 0; c < columnList.size(); c++) {
//							Map<String, String> dataVal = new HashMap<String, String>();
//							columnMap = columnList.get(c);
//							String culumnName = columnMap.get("column");
//							String culumnVal = "";
//							culumnVal = dataMap.get(culumnName);
//							dataVal.put("culumnName", culumnName);
//							dataVal.put("dataVal", culumnVal);
//							dataValList.add(dataVal);
//						}
//						paramMap.put("dataList", dataValList);
//						// INSERT 쿼리
//						idres = basicInfoMapper.insertCvsFileData(paramMap);
//						totIdres = totIdres + idres;
//					}
					logger.info("totIdres : " + totIdres + " / dataCount : " + (dataCnt));
					if (totIdres == dataCnt) {
						logger.info("등록된 데이터에 대하여 DB 정보 등록");
						HashMap<String, String> dataParam = new HashMap<String, String>();
						// 등록된 데이터에 대하여 DB 정보 등록
						logger.info("파라메터 설정");
						// 파라메터 설정
						dataParam.put("insttCode", insttCode); // 추후에 세션에서 로컬정보를 확인해서 변경 해야 됨.
						dataParam.put("dbmsVerId", "10.0");
						dataParam.put("ip", "localhost");
						dataParam.put("port", "3308");
						dataParam.put("schema", "");
						dataParam.put("sid", "");
						dataParam.put("database", "dq_database");
						dataParam.put("id", "root");
						dataParam.put("password", "");
						dataParam.put("dbmsKnd", "CSV");
						dataParam.put("rm", "dq_database");
						dataParam.put("useAt", "1");
						dataParam.put("dgnssDbmsNm", fileName + "(" + tableName + ")");

						logger.info("파라메터 설정 완료");

						// dbmsId 조회
						logger.info("dbmsId 설정");
						String dbmsId = basicInfoMapper.selectInsttDbmsId(dataParam);
						dataParam.put("dbmsId", dbmsId);
						logger.info("dbmsId : " + dbmsId);
						logger.info("DGNSS_DBMS 등록");
						// DGNSS_DBMS 등록
						int insRes = basicInfoMapper.insertDgnssDbms(dataParam);
						if (insRes > -1) {
							logger.info("DGNSS_DBMS 등록 완료");
							result = true;
						}
					} else {
						// 등록 데이터건수가 파일정보와 다를때
						basicInfoMapper.dropTableDb(params);
					}
				} else {
					// 테이블 생성 실패시
					// model.addAttribute("resCode", "9999");
					// model.addAttribute("resMsg", "등록실퍠");
				}
			}
			String ast = "완료";
			if (!result) {
				ast = "실패";
			}
			stopWatch.stop();
			date = format.format(new Date());
			logger.info("=== 파일등록 " + ast + " : " + date);
			logger.info("=== 파일명  : " + file.getName());
			logger.info("=== 파일 사이즈    : " + (file.length() / 1024) + "KB");
			logger.info("=== 파일등록 처리 시간 : " + stopWatch.getTotalTimeSeconds());
			logger.info("========================================");
		} catch (Exception e) {
			logger.error("[파일 정보 저장 실패]" + e.getStackTrace());
			// 데이터 등록 실패시 테일블 삭제 처리
			basicInfoMapper.dropTableDb(params);
			e.printStackTrace();
			logger.debug("error : " + e.getMessage());
			model.addAttribute("resMsgDetail", e.getMessage());
		} finally {
			// 등록 결과 return
			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	private boolean checkEncoding(String path) throws Exception{
		boolean check = false;
		
		try {
			byte[] buf = new byte[4096]; 
			String fileName = path; 
			FileInputStream fis = new FileInputStream(fileName); 
			UniversalDetector detector = new UniversalDetector(null); 
			int nread; 
			
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) { 
				detector.handleData(buf, 0, nread); 
			}
			
			detector.dataEnd(); 
			
			String encoding = detector.getDetectedCharset();//파일 인코딩 체크
			logger.info("encoding : "+encoding);
			if (encoding.equalsIgnoreCase("UTF-8")) {  
				check = true;
			} else { 
				check = false; 
			} 
			detector.reset();
			fis.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public List<?> selDbmsDateTypeList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		
		try {
			
			String dbmsNm = params.get("dbmsId");
			String dbmsId = "";
			
			if(dbmsNm.equalsIgnoreCase("Oracle")) {
				dbmsId = "1";
			} else if(dbmsNm.equalsIgnoreCase("MySQL")) {
				dbmsId = "3";
			} else if(dbmsNm.equalsIgnoreCase("Tibero")) {
				dbmsId = "4";
			} else if(dbmsNm.equalsIgnoreCase("MSSQL")) {
				dbmsId = "5";
			} else {
				dbmsId = params.get("dbmsId");
			}
			
			params.put("dbmsId", dbmsId);
			
			dataList = basicInfoMapper.selDbmsDateTypeList(params);
		} catch (Exception e) {
			logger.error("[조회 오류]" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());

		}
		return dataList;
	}
	
	/**
	 * @param params
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void dbmsConUdt(Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model)
			throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		int dbResult = 0;
		ObjectMapper mapper = new ObjectMapper(); // parser
		String insttCode = "";

		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
			HashMap<String, String> param = new HashMap<String, String>();

			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}

			for (Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				param.put("insttCode", insttCode);
				param.put("dgnssDbmsId", item.get("dgnssDbmsId").toString());
				param.put("dgnssDbmsNm", item.get("dgnssDbmsNm").toString());
				param.put("dbmsKnd", item.get("dbmsKnd").toString());
				if (item.get("dbmsVerId") != null) {
					param.put("dbmsVerId", item.get("dbmsVerId").toString());
					param.put("dbmsVerInfo", item.get("dbmsKnd").toString() + item.get("dbmsVerId").toString());
				}
				param.put("ip", item.get("ip").toString());
				param.put("port", item.get("port").toString());
				// param.put("schema", item.get("schema").toString());
				if (item.get("sid") != null) {
					param.put("sid", item.get("sid").toString());
				}
				param.put("database", item.get("database").toString());
				param.put("paramtr", item.get("paramtr").toString());
				param.put("id", item.get("id").toString());
				param.put("password", item.get("pwd").toString());
				param.put("rm", "");
				param.put("useAt", "Y");
				
				// DGNSS_DBMS UPDATE
				dbResult = basicInfoMapper.updateDgnssDbms(param);
			}
			if (dbResult > 0) {
				result = true;
			} else {
				result = false;
			}

		} catch (Exception e) {
			logger.error("[저장 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			json = mapper.writeValueAsString(result);
			out.print(json);
			out.flush();
			out.close();
		}

	}

}

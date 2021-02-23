package com.webapp.dqsys.mngr.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.mapper.AnalysisMapper;
import com.webapp.dqsys.mngr.mapper.BasicInfoMapper;
import com.webapp.dqsys.mngr.mapper.RuleMngMapper;
import com.webapp.dqsys.security.domain.Member;

@Service
public class RuleMngService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;

	@Autowired
	private RuleMngMapper ruleMngMapper;

	@Autowired
	private BasicInfoMapper basicInfoMapper;
	
	@Autowired
	private AnalysisMapper analysisMapper;
	
	/**
	 * 접속 정보 조회
	 * @param dgnssId
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public Map<String, String> selectConnDbmsType(String dgnssId) throws Exception, IOException {
		return ruleMngMapper.selectConnDbmsType(dgnssId);
	}
	
	
	/**
	 * 분석 구분 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectAnalsSeList(Map<String, String> params)
			throws Exception, IOException {
		List<?> dataList = null;
		try {
			dataList = ruleMngMapper.selectAnalsSeList(params);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 분석 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectAnalsList(Map<String, String> params) throws Exception, IOException {
		List<?> dataList = null;
		try {
			dataList = ruleMngMapper.selectAnalsList(params);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}
	
	/**
	 * 공통코드 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectCmmnList(Map<String, String> params) throws Exception, IOException {
		List<?> dataList = null;
		try {
			dataList = ruleMngMapper.selectCmmnList(params);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 진단대상 DBMS 의 스키마목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectSchemasList(Map<String, String> params) throws Exception, IOException {
		List<?> dataList = null;
		
		Map<String, String> data = new HashMap();
		Map<String, Object> paramMap = new HashMap();
		String dgnssDbmsId = "";
		
		try {
			// 진단대상DBMS 내에 접근 가능한 스키마정보를 조회한다.
			dgnssDbmsId = params.get("dgnssDbmsId");

			if (dgnssDbmsId != null) {
				// dgnss_dbms 테이블에서 대상 DB 정보를 가지고 온다.
				data = selectDgnssDbmsInfo(params);
				// 대상DB정보, 검색 파라메터 설정
				paramMap.putAll(params);
				paramMap.putAll(data);
				// 대상 DB 검색
				dataList = sqlSession.selectList("AnalysisMapper.selectSchema",paramMap);
			}
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 진단대상 DBMS 의 대상 스키마 테이블 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectSchemaTablesList(Map<String, String> params) throws Exception, IOException {

		List<?> dataList = null;
		Map<String, String> data = new HashMap();
		Map<String, Object> paramMap = new HashMap();
		String dgnssDbmsId = "";
		

		try {
			dgnssDbmsId = params.get("dgnssDbmsId");

			if (dgnssDbmsId != null) {
				// dgnss_dbms 테이블에서 대상 DB 정보를 가지고 온다.
				data = selectDgnssDbmsInfo(params);
				// 대상DB정보, 검색 파라메터 설정
				paramMap.putAll(params);
				paramMap.putAll(data);
				// 대상 DB 검색
				dataList = sqlSession.selectList("AnalysisMapper.selectTable",paramMap);
			}
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 진단대상 DBMS 허용 범위값 설정 등록
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void saveRuleLimit(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		ObjectMapper mapper = new ObjectMapper(); // parser
		String json = null;
		int resCnt = -1;
		boolean result = false;
		Map dataMap = new HashMap();
		Map<String, String> data = new HashMap();

		String insttCode = ""; // 기관 코드
		String analsId = ""; // 분석 아이디
		String analsTy = "AT000300"; // 분석 타입 AT000300 : 범주
		String pttrnSe = ""; // 패턴 구분
		String analsNm = ""; // 분석 명
		String analsFrmla = ""; // 분석 식
		String dbmsKnd = ""; // DBMS 종류
		String dbmsVer = ""; // DBMS 버전
		String rm = ""; // 비고
		String useAt = "1"; // 사용 여부
		String registDt = ""; // 등록 일시
		String IUDType = "I";
		String dbmsId = ""; 
		String beginValue = "";
		String endValue = "";
		try {
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}

			// DBMS_ID DDId
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			if (dgnssDbmsId != null) {
				params.put("dgnssDbmsId", dgnssDbmsId);
				data = selectDgnssDbmsInfo(params);
				dbmsId = data.get("dbmsId").toString();
				dbmsKnd = data.get("dbmsKnd").toString();
				if(data.get("dbmsVerId") != null) {
					dbmsVer = data.get("dbmsVerId").toString();
				}
				rm = params.get("rm").toString();
				beginValue = params.get("beginValue").toString();
				endValue = params.get("endValue").toString();
				
				// 분석 ID 설정
				analsId = params.get("analsId");
				if ("".equals(analsId) || analsId == null) {
					params.put("dbmsKnd", dbmsKnd);
					long intAnalsId = ruleMngMapper.selectAnalsIdNextVal(params);
					analsId = String.valueOf(intAnalsId);
				} else {
					IUDType = "U";
				}
				
				//analsFrmla = "SELECT COUNT($COLUMN_NAME) FROM $TABLE_NAME WHERE $COLUMN_NAME  BETWEEN '"+beginValue+"' AND '"+endValue+"'";
				analsFrmla = beginValue+" ~ "+endValue;
				params.put("insttCode", insttCode);
				params.put("analsId", analsId);
				params.put("analsTy", analsTy);
				params.put("pttrnSe", pttrnSe);
				params.put("analsFrmla", analsFrmla);
				params.put("viewFrmla", analsFrmla);
				params.put("dbmsKnd", dbmsKnd);
				params.put("dbmsVer", dbmsVer);
				params.put("rm", rm);
				params.put("useAt", useAt);
				params.put("registDt", registDt);
	
				if ("U".equals(IUDType)) {
					resCnt = ruleMngMapper.updateAnalsLimit(params);
				} else {
					resCnt = ruleMngMapper.insertAnals(params);
				}
				if (resCnt > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			logger.error("[저장 오류]\n" + e.getStackTrace());
			e.printStackTrace();
		}
		json = mapper.writeValueAsString(result);
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * 진단대상 DBMS 사용자 정의 patten 등록
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void saveRulePatten(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		ObjectMapper mapper = new ObjectMapper(); // parser
		String json = null;
		int resCnt = -1;
		boolean result = false;

		String insttCode = ""; // 기관 코드
		String analsId = ""; // 분석 아이디
		String analsSe = "AG000401"; // 분석 구분 AG000401 : 사용자정의패턴
		String analsTy = "AT000200"; // 분석 타입 AT000200 : 패턴
		String pttrnSe = ""; // 
		String analsNm = ""; // 분석 명
		String analsFrmla = ""; // 분석 식
		String beginValue = ""; // 시작 값
		String endValue = ""; // 종료 값
		String dbmsKnd = ""; // DBMS 종류
		String dbmsVer = ""; // DBMS 버전
		String rm = ""; // 비고
		String useAt = "1"; // 사용 여부
		String registDt = ""; // 등록 일시
		String IUDType = "I";
		try {
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}

			// DBMS_ID DDId
			String dbmsId = (String) req.getSession().getAttribute("DDId");

			Map<String, String> dataMap = new HashMap();
			if (!"".equals(dbmsId) && dbmsId != null) {
				params.put("dgnssDbmsId", dbmsId);
				dataMap = selectDgnssDbmsInfo(params);
				dbmsKnd = dataMap.get("dbmsKnd");
				if(dataMap.get("dbmsVerId") != null) {
					dbmsVer = dataMap.get("dbmsVerId");
				}
			}

			// 분석 ID 설정
			analsId = params.get("analsId");
			if ("".equals(analsId) || analsId == null) {
				params.put("dbmsKnd", dbmsKnd);
				long intAnalsId = ruleMngMapper.selectAnalsIdNextVal(params);
				analsId = String.valueOf(intAnalsId);
			} else {
				IUDType = "U";
			}
			
			params.put("insttCode", insttCode);
			params.put("analsId", analsId);
			//params.put("analsSe", analsSe);
			params.put("analsTy", analsTy);
			params.put("pttrnSe", pttrnSe);
			params.put("viewFrmla", params.get("analsFrmla").toString());
			params.put("beginValue", beginValue);
			params.put("endValue", endValue);
			params.put("dbmsKnd", dbmsKnd);
			params.put("dbmsVer", dbmsVer);
			params.put("useAt", useAt);
			params.put("registDt", registDt);

			if ("U".equals(IUDType)) {
				resCnt = ruleMngMapper.updateAnalsPatten(params);
			} else {
				resCnt = ruleMngMapper.insertAnals(params);
			}
			if (resCnt > 0)
				result = true;
			
		} catch (Exception e) {
			logger.error("[저장 오류]\n" + e.getStackTrace());
			e.printStackTrace();
		}
		json = mapper.writeValueAsString(result);
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * 진단대상 DBMS 사용자 정의 SQL 등록
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 * @throws IOException
	 */
	public void saveRuleSql(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception, IOException {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		ObjectMapper mapper = new ObjectMapper(); // parser
		String json = null;
		int resCnt = -1;
		boolean result = false;

		String insttCode = ""; // 기관 코드
		String analsId = ""; // 분석 아이디
		String analsSe = "AG000402"; // 분석 구분 AG000400 : 사용자 정의 SQL
		String analsTy = "AT000100"; // 분석 타입 AT000100 : SQl
		String pttrnSe = ""; // 패턴 구분 AG000400 : 사용자정의패턴
		String analsNm = ""; // 분석 명
		String analsFrmla = ""; // 분석 식
		String viewFrmla = ""; // 분석 식 입력 값
		String beginValue = ""; // 시작 값
		String endValue = ""; // 종료 값
		String dbmsKnd = ""; // DBMS 종류
		String dbmsVer = ""; // DBMS 버전
		String rm = ""; // 비고
		String registDt = ""; // 등록 일시
		String IUDType = "I";
		String tables = "";
		String schema = "";
		try {
			
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}
			// DBMS_ID DDId
			String dbmsId = (String) req.getSession().getAttribute("DDId");
			Map<String, String> dataMap = new HashMap();
			if (!"".equals(dbmsId) && dbmsId != null) {
				params.put("dgnssDbmsId", dbmsId);
				dataMap = selectDgnssDbmsInfo(params);
				dbmsKnd = dataMap.get("dbmsKnd").toString();
				if(dataMap.get("dbmsVerId") != null) {
					dbmsVer = dataMap.get("dbmsVerId").toString();
				}
			}
			// 분석 ID 설정
			analsId = req.getParameter("analsId");
			if ("".equals(analsId) || analsId == null) {
				params.put("dbmsKnd", dbmsKnd);
				long intAnalsId = ruleMngMapper.selectAnalsIdNextVal(params);
				analsId = String.valueOf(intAnalsId);
			} else {
				IUDType = "U";
			}
			
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
			HashMap<String, String> param = new HashMap<String, String>();
			for(Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				param.put("tables", item.get("tables").toString());
				param.put("schema", item.get("schema").toString());
				param.put("analsNm", item.get("analsNm").toString());
				param.put("analsFrmla", item.get("analsFrmla").toString());
				param.put("viewFrmla", item.get("analsFrmla").toString());
				param.put("rm", item.get("rm").toString());
			}
			
			if("CSV".equals(dbmsKnd)) {
				
				//csv 접속 일 시 Where절 컬럼명이 코멘트이기 때문에 ColumnName 체크
				Map<String, String> csvComents = new HashMap<String, String>();
				
				//사용자 where절 
				analsFrmla = param.get("analsFrmla").toString();
				
				
				
				// ']' 기준 인덱스 체크 
				int idx = analsFrmla.indexOf("]");
				String comments = "";
				String comments1 = "";
				if(idx > 0) {
					String[] tempStr =CsvAnalsFrmlaMaker(analsFrmla,"[","]");
					for(int i = 0 ; i<tempStr.length; i++) {
						comments1 = tempStr[i];
						comments = "["+comments1+"]";
						csvComents.put("comments",comments1);
						csvComents.put("schema",param.get("schema"));
						csvComents.put("tables",param.get("tables"));
						
						String csvColName = sqlSession.selectOne("AnalysisMapper.checkCsvColumnNm",csvComents);
						if(csvColName != null && csvColName != "") {
							analsFrmla = analsFrmla.replace(comments, csvColName);
						}
					}
					param.put("analsFrmla",analsFrmla);
				}
			}
			
			// 변환 처리 결과를 다시 체크 한다.
			int intres = sqlSession.selectOne("AnalysisMapper.selectCheckSql",param);
			
			// 실행 결과
			if (intres > -1) {
				String sql1 = "SELECT COUNT(1) \n FROM " + param.get("schema").toString()+"."+param.get("tables").toString() + "\n WHERE ";
				analsFrmla = sql1 + param.get("analsFrmla").toString();
				
				params.put("insttCode", insttCode);
				params.put("analsId", analsId);
				params.put("analsSe", analsSe);
				params.put("analsTy", analsTy);
				params.put("pttrnSe", pttrnSe);
				params.put("analsNm", param.get("analsNm").toString());
				params.put("analsFrmla", analsFrmla);
				params.put("viewFrmla", sql1 + param.get("viewFrmla").toString());
				params.put("beginValue", beginValue);
				params.put("endValue", endValue);
				params.put("dbmsKnd", dbmsKnd);
				params.put("dbmsVer", dbmsVer);
				params.put("rm", param.get("rm").toString());
				params.put("registDt", registDt);

				if ("U".equals(IUDType)) {
					resCnt = ruleMngMapper.updateAnalsPatten(params);
				} else {
					resCnt = ruleMngMapper.insertAnals(params);
				}
				if (resCnt > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			logger.error("[저장 오류]\n" + e.getStackTrace());
			e.printStackTrace();
		}
		json = mapper.writeValueAsString(result);
		out.print(json);
		out.flush();
		out.close();
	}
	
	public String[] CsvAnalsFrmlaMaker(String str, String start, String end) {
		String[] returnStr = null;
		if(str.indexOf(end) > 0) {
			returnStr = str.split(end);
			for(int i=0;i<returnStr.length;i++) {
				returnStr[i] = returnStr[i].substring(returnStr[i].indexOf(start)+1);
			}
		}
		return returnStr;
	}

	/**
	 * 진단대상 DBMS Patten 체크
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 * @throws IOException
	 */
	public void checkRulePatten(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception, IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		int intres = 0;
		Map<String, String> data = new HashMap();
		Map<String, Object> paramMap = new HashMap();
		ObjectMapper mapper = new ObjectMapper(); // parser
		try {
			
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			
			// dgnss_dbms 테이블에서 대상 DB 정보를 가지고 온다.
			params.put("dgnssDbmsId", dgnssDbmsId);
			data = selectDgnssDbmsInfo(params);
			// 대상DB정보, 검색 파라메터 설정
			paramMap.putAll(params);
			paramMap.putAll(data);
			// 대상 DB 검색
			intres = sqlSession.selectOne("AnalysisMapper.selectCheckPatten",paramMap);
			// 결과 처리
			if (intres > 0) {
				result = true;
			}
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		json = mapper.writeValueAsString(result);
		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
	 * 진단대상 DB 사용자 정의 SQL 체크
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 * @throws IOException
	 */
	public void checkRuleSql(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception, IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		int intres = -1;
		Map<String, String> data = new HashMap();
		Map<String, Object> paramMap = new HashMap();
		ObjectMapper mapper = new ObjectMapper(); // parser
		
		try {
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			// dgnss_dbms 테이블에서 대상 DB 정보를 가지고 온다.
			params.put("dgnssDbmsId", dgnssDbmsId);
			data = selectDgnssDbmsInfo(params);
			// 대상DB정보, 검색 파라메터 설정
			paramMap.putAll(params);
			paramMap.putAll(data);
			
			//csv 접속 일 시 Where절 컬럼명이 코멘트이기 때문에 ColumnName 체크
			if("CSV".equals(paramMap.get("dbmsKnd"))) {
				String analsFrmla = "";
				Map<String, String> csvComents = new HashMap<String, String>();
				
				//사용자 where절 
				analsFrmla = params.get("analsFrmla");
				
				
				// ']' 기준 인덱스 체크 
				int idx = analsFrmla.indexOf("]");
				String comments = "";
				String comments1 = "";
				if(idx > 0) {
					String[] tempStr =CsvAnalsFrmlaMaker(analsFrmla,"[","]");
					for(int i = 0 ; i<tempStr.length; i++) {
						comments1 = tempStr[i];
						comments = "["+comments1+"]";
						csvComents.put("comments",comments1);
						csvComents.put("schema",params.get("schema"));
						csvComents.put("tables",params.get("tables"));
						
						String csvColName = sqlSession.selectOne("AnalysisMapper.checkCsvColumnNm",csvComents);
						if(csvColName != null && csvColName != "") {
							analsFrmla = analsFrmla.replace(comments, csvColName);
						}
					}
					paramMap.put("analsFrmla",analsFrmla);
				}
			}else if("MSSQL".equals(paramMap.get("dbmsKnd"))) {
				// MSSQL은 JAVA 에서 패턴 처리 
				
			}
			// 대상 DB 검색
			intres = sqlSession.selectOne("AnalysisMapper.selectCheckSql",paramMap);
			// 실행 결과
			if (intres > -1) {
				result = true;
			}
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		json = mapper.writeValueAsString(result);
		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
	 * 진단대상 DBMS 정보 조회
	 * @param params
	 * @return
	 */
	public Map<String, String> selectDgnssDbmsInfo(Map<String, String> params) {
		Map<String, String> data = new HashMap();

		try {
			List<?> dbList = basicInfoMapper.selectDbInfo(params);
			Map dataMap = new HashMap();
			if (dbList != null && dbList.size() > 0) {
				Object key = null;

				for (int i = 0; i < dbList.size(); i++) {
					HashMap map = (HashMap) dbList.get(i);
					Iterator it = map.keySet().iterator();

					while (it.hasNext()) {
						key = it.next();
						dataMap.put(key.toString(), map.get(key));
					}
				}

				data.put("conIp", dataMap.get("ip").toString());
				data.put("conPort", dataMap.get("port").toString());
				data.put("user", dataMap.get("id").toString());
				data.put("password", dataMap.get("password").toString());
				data.put("sId", dataMap.get("sid").toString());
				//data.put("schema", dataMap.get("schema").toString());
				data.put("database", dataMap.get("database").toString());
				data.put("dbmsId", dataMap.get("dbmsId").toString());
				data.put("dbmsKnd", dataMap.get("dbmsKnd").toString());
				if(dataMap.get("dbmsVerId") != null) {
					data.put("dbmsVerId", dataMap.get("dbmsVerId").toString());
				}
				if(dataMap.get("dbmsVerInfo") != null) {
					data.put("dbmsVerInfo", dataMap.get("dbmsVerInfo").toString());
				}

			}
		} catch (Exception e) {
			logger.error("[조회 오류]\n" + e.getStackTrace());
			e.printStackTrace();
		}

		return data;
	}
	
	public void selectColumnAnalysis(Map<String, String> params, HttpServletRequest req , HttpServletResponse res) throws  Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		List<?> dataList = null;
		
		try {
			String dbmsKnd ="";
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);
			
			if ("Oracle".equals(dbmsKnd)) {
				dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
			}else if ("MySQL".equals(dbmsKnd)) {
				dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
			}else if ("Tibero".equals(dbmsKnd)) {
				dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
			}else if ("MSSQL".equals(dbmsKnd)) {
				dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
			}else if ("CSV".equals(dbmsKnd)) {
				dataList = ruleMngMapper.selectColumnAnalysis(params);
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(dataList);
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}

}

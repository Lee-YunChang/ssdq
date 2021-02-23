package com.webapp.dqsys.mngr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.domain.BaseFile;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.mapper.AnalysisMapper;
import com.webapp.dqsys.mngr.mapper.LifecycleMapper;
import com.webapp.dqsys.util.CSVReader;
import com.webapp.dqsys.util.FileUploadUtil;
import com.webapp.dqsys.util.SangsAbstractService;
import com.webapp.dqsys.util.SangsUtil;

@Service
public class LifecycleService extends SangsAbstractService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	LifecycleMapper lifecycleMapper;
	
	@Autowired
	AnalysisMapper analysisMapper;
	
	@Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;

	@Value("${file.csv.dataDir}")
	private String csvDir;

	public void AjaxSelLifecycleList(Map<String, String> params, HttpServletResponse res) throws Exception {

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

			totalCnt = lifecycleMapper.AjaxSelLifecycleListTotalCnt(params);
			dataList = lifecycleMapper.AjaxSelLifecycleList(params);

			resultMap.put("totalCnt", totalCnt);
			resultMap.put("dataList", dataList);
			json = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {
			logger.error("[Lifecycle 항목 관리 조회 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}

	public void AjaxInsLifecycleList(Map<String, String> params, HttpServletResponse res) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("insertList"));

			HashMap<String, String> param = new HashMap<String, String>();

			for (Object o : arrItems) {
				JSONObject item = (JSONObject) o;

				param.put("analsId", lifecycleMapper.selMaxId());
				param.put("period", item.get("period").toString());
				param.put("fieldNm", item.get("fieldNm").toString());
				param.put("periodCl", item.get("periodCl").toString());
				param.put("analsTy", item.get("analsTy").toString());
				param.put("rm", item.get("rm").toString());

				// Lifecycle INSERT
				int insCnt = lifecycleMapper.AjaxInsLifecycleList(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[Lifecycle 저장 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);

			out.print(json);
			out.flush();
			out.close();
		}

	}

	public void AjaxUdtLifecycleList(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("insertList"));

			HashMap<String, String> param = new HashMap<String, String>();

			for (Object o : arrItems) {
				JSONObject item = (JSONObject) o;

				param.put("insttCode", item.get("insttCode").toString());
				param.put("analsId", item.get("analsId").toString());
				param.put("analsTy", item.get("analsTy").toString());
				param.put("fieldNm", item.get("fieldNm").toString());
				param.put("period", item.get("period").toString());
				param.put("periodCl", item.get("periodCl").toString());
				param.put("rm", item.get("rm").toString());

				// Lifecycle update
				int insCnt = lifecycleMapper.AjaxUdtLifecycleList(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[Lifecycle 수정 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);

			out.print(json);
			out.flush();
			out.close();
		}

	}

	public void AjaxDelLifecycleList(String[] params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		try {
			HashMap<String, String> param = new HashMap<String, String>();

			for (int i = 0; i < params.length; i++) {

				param.put("analsId", params[i]);

				// Lifecycle DELETE
				int insCnt = lifecycleMapper.AjaxDelLifecycleList(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[Lifecycle 삭제 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);

			out.print(json);
			out.flush();
			out.close();
		}

	}

	public void ajaxInsLifecycleCsvFile(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		File csvFile = null;
		FileInputStream fiStream = null;
		InputStreamReader isReader = null;
		CSVReader csvReader = null;
		int chk = 0;				

		try {
			// 임시저장 폴더에 파일 생성
			String attFileOutputPath = csvDir + "/";
			List<BaseFile> fileList = null;

			if (ServletFileUpload.isMultipartContent(req)) {
				fileList = FileUploadUtil.uploadFiles(req, attFileOutputPath);
			}

			// 임시저장에 등록된 파일 읽어들여 Lifecycle 정보 저장
			if (fileList != null && fileList.size() > 0) {
				BaseFile vo = fileList.get(0);
				String attFileOutputFullPath = attFileOutputPath + vo.getPhysicalName();

				csvFile = new File(attFileOutputFullPath);

				if (csvFile.exists()) {
					char tempChar[] = ",".toCharArray();
					char separator = tempChar[0];
					fiStream = new FileInputStream(csvFile);
					isReader = new InputStreamReader(fiStream, "euc-kr");
					csvReader = new CSVReader(isReader, separator);
					String[] nextLine = null;

					int lowIndex = 0;
					int colIndex = 0;
					String[] colKeyArr = { "analsTy", "period", "periodCl", "rm"};
					while ((nextLine = csvReader.readNext()) != null) {
						if (lowIndex > 0) {
							colIndex = 0;
							HashMap<String, String> param = new HashMap<String, String>();
							for (String str : nextLine) {
								if (str != null && !"".equals(str.trim())) {

									param.put("analsId", lifecycleMapper.selMaxId());
									param.put(colKeyArr[colIndex], str);
									param.put("fieldNm", param.get("analsTy")+"_"+param.get("period")+param.get("periodCl"));
								}
								++colIndex;
							}

							// 필수값 체크
							if (param.get("analsTy") != null && param.get("fieldNm") != null
									&& param.get("period") != null && param.get("periodCl") != null) {
								
								int chkCnt = lifecycleMapper.ajaxSelChkFiledNm(param);
								
								if(chkCnt > 0) {
									chk++;
								}
								
								if(chk == 0) {
									// lifecycle 저장
									int insCnt = lifecycleMapper.AjaxInsLifecycleList(param);
	
									if (insCnt > 0) {
										result = true;
									}
								} else {
									result = false;
								}
							}
						}
						++lowIndex;
					}
				}
			}
		} catch (Exception e) {
			result = false;
			logger.error("[lifecycle CSV 저장 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					logger.error("[CSVReader]");
					logger.debug("error : " + e.getMessage());
				}
			}
			if (isReader != null) {
				try {
					isReader.close();
				} catch (IOException e) {
					logger.error("[InputStreamReader]");
					logger.debug("error : " + e.getMessage());
				}
			}
			if (fiStream != null) {
				try {
					fiStream.close();
				} catch (IOException e) {
					logger.error("[FileInputStream]");
					logger.debug("error : " + e.getMessage());
				}
			}
			if (csvFile.exists()) {
				// 임시저장 폴더에 업로드된 파일 삭제
				csvFile.delete();
			}

			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);

			out.print(json);
			out.flush();
			out.close();
		}

	}

	public List<?> selectTableAnalysis(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) {
		List<?> dataList = null;

		try {
			String dbmsKnd = "";
			String ddId = params.get("ddId");

			dbmsKnd = analysisMapper.selectDbList(ddId);
			logger.debug("1. dbmsKnd : {}", params.get("dbmsKnd"));
			super.pagingSet(params);

			String tableSchema = params.get("tableSchema");
			logger.debug("2. dbmsKnd : {}", dbmsKnd);
			logger.debug("tableSchema : {}", tableSchema);

			if ("Oracle".equals(dbmsKnd)) {
				if (tableSchema == null) {

					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");

				} else {
//					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt", params);
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis", params);
				}
			} else if ("MySQL".equals(dbmsKnd)) {
				if (tableSchema == null) {

					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");

				} else {
//					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt", params);
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis", params);
				}
			} else if ("Tibero".equals(dbmsKnd)) {
				if (tableSchema == null) {

					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");

				} else {
//					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt", params);
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis", params);
				}
			} else if ("MSSQL".equals(dbmsKnd)) {
				if (tableSchema == null) {

					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");

				} else {
//					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt", params);
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis", params);
				}
			} else if ("CSV".equals(dbmsKnd)) {
				if (tableSchema == null) {

					dataList = analysisMapper.selectSchema(params);

				} else {
//					totalCnt = analysisMapper.selectTableAnalysisTotalCnt(params);
					dataList = analysisMapper.selectTableAnalysis(params);
				}
			} else {
				if (tableSchema == null) {

					dataList = analysisMapper.selectSchema(params);

				} else {
//					totalCnt = analysisMapper.selectTableAnalysisTotalCnt(params);
					dataList = analysisMapper.selectTableAnalysis(params);
				}
			}
		} catch (Exception e) {
			logger.error("[Lifecycle 분석 테이블 조회 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}

		return dataList;
	}

	public void insertLifecycleSchema(Map<String, Object> paramMap) {
		lifecycleMapper.insertLifecycleSchema(paramMap);
		
	}

	public void AjaxSelLifecycleFiledNm(Map<String, String> params, HttpServletResponse res) throws Exception{
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		String json = null;
		List<?> dataList = null;
		//int totalCnt = 0;
		try {
			//totalCnt = lifecycleMapper.AjaxSelLifecycleListTotalCnt(params);
			dataList = lifecycleMapper.AjaxSelLifecycleFiledNm(params);

			//resultMap.put("totalCnt", totalCnt);
			resultMap.put("dataList", dataList);
			json = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {
			logger.error("[Lifecycle 항목 조회 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
		
	}
	
	public void selectColumnAnalysis(Map<String, String> params,HttpServletRequest req , HttpServletResponse res) throws  Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		List<?> dataList = null;
		ArrayList arr = new ArrayList();
		Object[] str = {};

		try {
			String dbmsKnd ="";
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);

			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("tableData"));

			HashMap<String, String> param = new HashMap<String, String>();
			HashMap<String, String> param3 = new HashMap<String, String>();

			for(Object o : arrItems) {
				HashMap<String, Object> param2 = new HashMap<String, Object>();
				JSONObject item = (JSONObject) o;
				
				param.put("tableSchema", item.get("tableSchema").toString());
				param.put("tableName", item.get("tableName").toString());
			
				String tableSchema = param.get("tableSchema");
				String tableName = param.get("tableName");
				
				
				if ("Oracle".equals(dbmsKnd)) {
					if (tableSchema == null) {
						dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					}else {
						dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", param);
					}
				}else if ("MySQL".equals(dbmsKnd)) {
					if (tableSchema == null) {
						dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					}else {
						dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", param);
					}
				}else if ("Tibero".equals(dbmsKnd)) {
					if (tableSchema == null) {
						dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					}else {
						dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", param);
					}
				}else if ("MSSQL".equals(dbmsKnd)) {
					if (tableSchema == null) {
						dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					}else {
						dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", param);
					}
				}else if ("CSV".equals(dbmsKnd)) {
					if (tableSchema == null) {
						dataList = analysisMapper.selectSchema(param);
					}else {
						dataList = analysisMapper.selectColumnAnalysis(param);
					}
				}else {
					if (tableSchema == null) {
						dataList = analysisMapper.selectSchema(param);
					}else {
						dataList = analysisMapper.selectColumnAnalysis(param);
					}
				}
			
				param2.put("tableName", tableName);
				param2.put("colList", dataList);
				
				
				arr.add(param2);
				
			}
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			
			dataList = listMap;
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(arr);

		} catch (Exception e) {
			e.printStackTrace();
		}

		out.print(json);
		out.flush();
		out.close();
		
	}

	public List<Map<String, String>> selectAnalysisPeriod(Map<String, String> params) throws Exception{
		List<Map<String, String>> periodList = lifecycleMapper.selectAnalysisPeriod(params);
		return periodList;
	}

	/* @Async("asyncExecutor") */
	public void asyncExecutorDataLifecycleAnalysis(Map<String, Object> asyncMap, HttpServletResponse res, HttpServletRequest req) throws Exception{
		String[] oracleDataTypeStr = {"CHAR", "VARCHAR2", "NCHAR", "NVARCHAR2"};
		String[] oracleDataTypeDate = {"DATE", "TIMESTAMP"};
		String[] mysqlDataType = {"DATE", "TIME", "DATETIME", "TIMESTAMP"}; //"CHAR", "VARCHAR2"
		String[] tiberoDataTypeStr = {"CHAR", "VARCHAR"};
		String[] tiberoDataTypeDate = {"DATE", "TIME", "TIMESTAMP"};
		String[] mssqlDataType = {"DATE", "TIME", "DATETIME"}; //"CHAR", "VARCHAR"
		String whereParam = "";
		int totalCnt = 0;
		int mtchgCnt = 0;
		int missCnt = 0;
		boolean chk = false;
		

		try {
			String dbmsKnd ="";
			String dgnssDbmsId = asyncMap.get("dgnssDbmsId").toString();
			String analsTy = asyncMap.get("analsTy").toString();
			String columnName = asyncMap.get("columnName").toString();
			String columnType = asyncMap.get("columnType").toString();
			String chkDate = asyncMap.get("chkDate").toString().replace("-", "").trim();
			
			asyncMap.put("chkDate", chkDate);
			
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);

			if ("Oracle".equals(dbmsKnd)) {
//				for (int i = 0; i < oracleDataTypeStr.length; i++) {
//					if(columnType.equalsIgnoreCase(oracleDataTypeStr[i])) {
//						whereParam = "WHERE TO_CHAR(TO_DATE(";
//						whereParam += columnName;
//						whereParam += ",'"+analsTy+"'";
//						whereParam += "), 'YYYYMMDD')";
//						
//						asyncMap.put("whereParam", whereParam);
//						
//						chk = true;
//					}
//				}
				
				for (int i = 0; i < oracleDataTypeDate.length; i++) {
					if(columnType.equalsIgnoreCase(oracleDataTypeDate[i])) {
						whereParam = "WHERE TO_CHAR(";
						whereParam += columnName;
						whereParam += ", 'YYYYMMDD')";
						
						asyncMap.put("whereParam", whereParam);
						
						chk = true;
					} 
				}
				
				totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalColCnt", asyncMap);

				if(chk) {
					mtchgCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMtchgCnt", asyncMap);
					missCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMissCnt", asyncMap);
				}
				
			}else if ("MySQL".equals(dbmsKnd)) {
				for (int i = 0; i < mysqlDataType.length; i++) {
					if(columnType.equalsIgnoreCase(mysqlDataType[i])) {
						if("YYYYMM".equalsIgnoreCase(analsTy)) {
							whereParam = "WHERE DATE(CONCAT(";
							whereParam += columnName;
							whereParam += ", '01'))";
							
							asyncMap.put("whereParam", whereParam);
							
							chk = true;
							
						} else {
							whereParam = "WHERE DATE(";
							whereParam += columnName;
							whereParam += ")";
							
							asyncMap.put("whereParam", whereParam);
							
							chk = true;
						}
					} 
				}
				
				totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalColCnt", asyncMap);

				if(chk) {
					mtchgCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMtchgCnt", asyncMap);
					missCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMissCnt", asyncMap);
				}
			}else if ("Tibero".equals(dbmsKnd)) {
//				for (int i = 0; i < tiberoDataTypeStr.length; i++) {
//					if(columnType.equalsIgnoreCase(tiberoDataTypeStr[i])) {
//						whereParam = "WHERE TO_CHAR(TO_DATE(";
//						whereParam += columnName;
//						whereParam += ",'"+analsTy+"'";
//						whereParam += "), 'YYYYMMDD')";
//						
//						asyncMap.put("whereParam", whereParam);
//						
//						chk = true;
//					}
//				}
				
				for (int i = 0; i < tiberoDataTypeDate.length; i++) {
					if(columnType.equalsIgnoreCase(tiberoDataTypeDate[i])) {
						whereParam = "WHERE TO_CHAR(";
						whereParam += columnName;
						whereParam += ", 'YYYYMMDD')";
						
						asyncMap.put("whereParam", whereParam);
						
						chk = true;
					}
				}
				
				totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalColCnt", asyncMap);

				if(chk) {
					mtchgCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMtchgCnt", asyncMap);
					missCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMissCnt", asyncMap);
				}
			}else if ("MSSQL".equals(dbmsKnd)) {
				for (int i = 0; i < mssqlDataType.length; i++) {
					if(columnType.equalsIgnoreCase(mssqlDataType[i])) {
						if("YYYYMMDD".equalsIgnoreCase(analsTy)) {
							whereParam = "WHERE CONVERT(VARCHAR,";
							whereParam += columnName;
							whereParam += ", 112)";
						}
						if("YYMMDD".equalsIgnoreCase(analsTy)) {
							whereParam = "WHERE CONCAT('20'CONVERT(VARCHAR,";
							whereParam += columnName;
							whereParam += ", 112))";
						}
						if("YYYYMM".equalsIgnoreCase(analsTy)) {
							whereParam = "WHERE CONCAT(CONVERT(VARCHAR,";
							whereParam += columnName;
							whereParam += ", 112), '01')";
						}
						
						asyncMap.put("whereParam", whereParam);
						
						chk = true;
						
					}
				}
				
				totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalColCnt", asyncMap);

				if(chk) {
					mtchgCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMtchgCnt", asyncMap);
					missCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisMissCnt", asyncMap);
				}
			}

			asyncMap.put("totalCnt", totalCnt);
			asyncMap.put("mtchgCnt", mtchgCnt);
			asyncMap.put("missCnt", missCnt);
			
			//테이블별 전단 결과 등록
			lifecycleMapper.insertLifecycleTableAnalysis(asyncMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			//분석실패 스키마 정보 업데이트
			//asyncMap.put("excSttus", "F");
			//lifecycleMapper.updateLifecycleSchema(asyncMap);
			asyncMap.put("mtchgCnt", mtchgCnt);
			asyncMap.put("missCnt", missCnt);
			
			//테이블별 전단 결과 등록
			lifecycleMapper.insertLifecycleTableAnalysis(asyncMap);
		}
	}

	public Map<String, Object> selectLifecycleSchemaList(Map<String, Object> paramMap) {
		super.pagingSetMySql(paramMap);
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("list", lifecycleMapper.selectLifecycleSchemaList(paramMap));
		resultMap.put("totalCnt", lifecycleMapper.selectLifecycleSchemaListCnt(paramMap));

		return resultMap;
	}

	public void updateLifecycleSchema(Map<String, Object> paramMap) {
		lifecycleMapper.updateLifecycleSchema(paramMap);
		
	}

	public SangsMap selectLifecycleSchema(Map<String, Object> paramMap) {
		SangsMap schema = lifecycleMapper.selectLifecycleSchema(paramMap);
		return schema;
	}

	public Map<String, Object> selectLifecycleSummery(Map<String, Object> paramMap) {
		Map<String, Object> summery = new HashMap<String, Object>();
		
		int tableCnt = lifecycleMapper.selectLifecycleSummeryCnt(paramMap);	
		SangsMap lifecycleSummery = lifecycleMapper.selectLifecycleSummery(paramMap);	
		
		summery.put("tableCnt",tableCnt);
		summery.put("totalCo",lifecycleSummery.get("totalCo"));
		summery.put("mtchgCo",lifecycleSummery.get("mtchgCo"));
		summery.put("missCo",lifecycleSummery.get("missCo"));
		summery.put("errCo",lifecycleSummery.get("errCo"));
		summery.put("mtchgPer",lifecycleSummery.get("mtchgPer"));
		summery.put("missPer",lifecycleSummery.get("missPer"));
		summery.put("errPer",lifecycleSummery.get("errPer"));
		
		return summery;
	}

	public List<SangsMap> selectLifecycleTableRes(Map<String, Object> paramMap) {
		List<SangsMap> lifecycleTableRes = lifecycleMapper.selectLifecycleTableRes(paramMap);
		
		return lifecycleTableRes;
	}

	public void ajaxSelChkFiledNm(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			int fieldNmChk = lifecycleMapper.ajaxSelChkFiledNm(params);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(fieldNmChk);

		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}
	
	public void ajaxSelChkModFiledNm(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			int fieldNmChk = 0;
			
			int chk = lifecycleMapper.ajaxSelChkFiledNm(params);
			
			if(chk == 0) {
				fieldNmChk = 0;
			} else {
				int chk2 = lifecycleMapper.ajaxSelChkModFiledNm(params);
				
				if(chk2 == 0) {
					fieldNmChk = 1;
				} else {
					fieldNmChk = 0;
				}
			
			}
			
			
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(fieldNmChk);

		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}

	/**
	 * Lifecycle 엑셀 업로드
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception 
	 */
	public void ajaxInsLifecycleExcelFile(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		FileInputStream fis = null;
		File excelFile = null;
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int chk = 0;
		
		try {
			// 임시저장 폴더에 파일 생성
			String attFileOutputPath = csvDir + "/";
			List<BaseFile> fileList = null;

			if (ServletFileUpload.isMultipartContent(req)) {
				fileList = FileUploadUtil.uploadFiles(req, attFileOutputPath);
			}

			// 임시저장에 등록된 파일 읽어들여 도메인 정보 저장
			if (fileList != null && fileList.size() > 0) {
				BaseFile vo = fileList.get(0);
				String attFileOutputFullPath = attFileOutputPath + vo.getPhysicalName();
				// 업로드된 파일 읽어오기
				String[] colKeyArr = { "analsTy", "period", "periodCl", "rm"};
				
				excelFile = new File(attFileOutputFullPath);
				fis = new FileInputStream(excelFile);
				Workbook wb = null;
				
				String extension = FilenameUtils.getExtension(attFileOutputFullPath);
				if (StringUtils.equalsIgnoreCase(extension, "xls")) {
					System.out.println("xls");
					wb = new HSSFWorkbook(fis);
				} else if (StringUtils.equalsIgnoreCase(extension, "xlsx")) {
					System.out.println("xlsx");
					wb = new XSSFWorkbook(fis);
				}
				
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				
				int startRow = 3;
				while (rowIterator.hasNext()) {
					
					Row row = rowIterator.next();
					int rowNum = row.getRowNum();
					if (rowNum <= startRow) {
						continue;
					} /*
						 * else if (rowNum == sheet.getLastRowNum()) { continue; }
						 */
					
					Iterator<Cell> cellIterator = row.cellIterator();
					int cellIdx = 0;
					String dataMapValue = "";
					Map<String, String> dataMap = new LinkedHashMap<String, String>();
					
					//데이터 정제
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellType()) {
						case BOOLEAN:
							dataMapValue = String.valueOf(cell.getBooleanCellValue());
							break;
						case NUMERIC:
							if( HSSFDateUtil.isInternalDateFormat(cell.getCellStyle().getDataFormat()) ) {
								dataMapValue = simpleDateFormat.format(cell.getDateCellValue());
								if( !SangsUtil.dateFormatCheck(dataMapValue, "yyyy-MM-dd") ) {
									dataMapValue = "";
								}
							}
							else {
								dataMapValue = new DecimalFormat("#####.##").format(cell.getNumericCellValue());
							}
							
							break;
						case STRING:
							dataMapValue = cell.getStringCellValue();
							break;
						case FORMULA:
							dataMapValue = cell.getStringCellValue();
							break;
						default:
							dataMapValue = "";
							break;
						}
						//cell 널체크, 데이터 길이는 공백 허용
						if(cellIdx != 4 && "".equals(dataMapValue) || null == dataMapValue) {
							continue;
						}
						
						dataMap.put("analsId", lifecycleMapper.selMaxId());
						dataMap.put(colKeyArr[cellIdx], dataMapValue);
						dataMap.put("fieldNm", dataMap.get("analsTy")+"_"+dataMap.get("period")+dataMap.get("periodCl"));
						cellIdx++;
					}
					// 필수값 체크
					if (dataMap.get("analsTy") != null && dataMap.get("fieldNm") != null
							&& dataMap.get("period") != null && dataMap.get("periodCl") != null) {
						
						//항목명 중복체크
						int chkCnt = lifecycleMapper.ajaxSelChkFiledNm(dataMap);
						
						if(chkCnt > 0) {
							chk++;
						}
						
						if(chk == 0) {
							// Lifecycle 정보 저장
							int insCnt = lifecycleMapper.AjaxInsLifecycleList(dataMap);
							if (insCnt > 0) {
								result = true;
								dataMap = null;
							}
						} else {
							result = false;
							dataMap = null;
						}
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if( excelFile.exists() ) {
				excelFile.delete();  
			}
			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);

			out.print(json);
			out.flush();
			out.close();
		}
		
	}


}

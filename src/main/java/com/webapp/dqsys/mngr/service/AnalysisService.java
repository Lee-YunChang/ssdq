package com.webapp.dqsys.mngr.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.mapper.AnalysisMapper;
import com.webapp.dqsys.util.SangsAbstractService;

@Service
public class AnalysisService extends SangsAbstractService{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;

	@Autowired
	AnalysisMapper analysisMapper;
	
	@Autowired
	RuleMngService ruleMngService;
	
	/**
     * ajax테이블 구조 분석
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS	
     *
     * @param params
     * @param res
	 * @param msg 
     * @throws Exception
     */
	public void selectDbmsKnd(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws  Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String json = null;
		
		try {

			String dbmsKnd ="";
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("dbmsKnd", dbmsKnd);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.print(json);
		out.flush();
		out.close();
		
	}
	
	public void selectTableAnalysis(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws  Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String json = null;
		List<?> dataList = null;
		int totalCnt = 0;
		
		try {
			String dbmsKnd ="";
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);
			logger.debug("1. dbmsKnd : {}", params.get("dbmsKnd"));
			super.pagingSet(params);
			
			String tableSchema = params.get("tableSchema");
			logger.debug("2. dbmsKnd : {}", dbmsKnd);
			logger.debug("tableSchema : {}", tableSchema);
			
			if ("Oracle".equals(dbmsKnd)) {
				if (tableSchema == null) {
					
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					
				}else {
					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt",params);	
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis",params);
				}
			}else if ("MySQL".equals(dbmsKnd)) {
				if (tableSchema == null) {
					
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					
				}else {
					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt",params);	
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis",params);
				}
			}else if ("Tibero".equals(dbmsKnd)) {
				if (tableSchema == null) {
					
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					
				}else {
					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt",params);	
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis",params);
				}
			}else if ("MSSQL".equals(dbmsKnd)) {
				if (tableSchema == null) {
					
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
					
				}else {
					totalCnt = sqlSession.selectOne("AnalysisMapper.selectTableAnalysisTotalCnt",params);	
					dataList = sqlSession.selectList("AnalysisMapper.selectTableAnalysis",params);
				}
			}else if ("CSV".equals(dbmsKnd)) {
				if (tableSchema == null) {
					
					dataList = analysisMapper.selectSchema(params);
					
				}else {
					totalCnt = analysisMapper.selectTableAnalysisTotalCnt(params);	
					dataList = analysisMapper.selectTableAnalysis(params);
				}
			}else {
				if (tableSchema == null) {
					
					dataList = analysisMapper.selectSchema(params);
					
				}else {
					totalCnt = analysisMapper.selectTableAnalysisTotalCnt(params);	
					dataList = analysisMapper.selectTableAnalysis(params);
				}
			}
			
			resultMap.put("totalCnt", totalCnt);
			resultMap.put("result", dataList);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}
	
	/**
     * ajax테이블 리스트 조회
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS	
     *
     * @param params
     * @param req
     * @param res
     * @throws Exception
     */
//	public void selectSchema(Map<String, String> params, HttpServletResponse res) throws  Exception {
//		res.setContentType("text/html; charset=utf-8");
//		PrintWriter out = res.getWriter();
//		String json = null;
//		List<?> dataList = null;
//		
//		try {
//			dataList = analysisMapper.selectSchema(params);
//			
//			ObjectMapper mapper = new ObjectMapper(); // parser
//			json = mapper.writeValueAsString(dataList);
//			
//		} catch (Exception e) {
//
//		}
//
//		out.print(json);
//		out.flush();
//		out.close();
//	}
	
	public void selectTable(Map<String, String> params, HttpServletRequest req,HttpServletResponse res) throws  Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		List<?> dataList = null;
		
		try {
			String dbmsKnd ="";
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			logger.debug("1. dbmsKnd : {}", params.get("dbmsKnd"));
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);
			
			String tableSchema = params.get("tableSchema");
			
			logger.debug("2. dbmsKnd : {}", dbmsKnd);
			logger.debug("tableSchema : {}", tableSchema);
			
			if ("Oracle".equals(dbmsKnd)) {
				
				dataList = sqlSession.selectList("AnalysisMapper.selectTable",tableSchema);
				
			}else if ("MySQL".equals(dbmsKnd)) {
				
				dataList = sqlSession.selectList("AnalysisMapper.selectTable",tableSchema);
				
			}else if ("Tibero".equals(dbmsKnd)) {
				
				dataList = sqlSession.selectList("AnalysisMapper.selectTable",tableSchema);
				
			}else if ("MSSQL".equals(dbmsKnd)) {
				
				dataList = sqlSession.selectList("AnalysisMapper.selectTable",tableSchema);
				
			}else if ("CSV".equals(dbmsKnd)) {
				
				dataList = analysisMapper.selectTable(params);
				
			}else {
				dataList = analysisMapper.selectTable(params);
			}
			
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(dataList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
     * ajax컬럼 리스트 조회
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS	
     *
     * @param params
     * @param req
     * @param res
     * @throws Exception
     */
//	public void selectColumnAnalysis(Map<String, String> params, HttpServletResponse res) throws  Exception {
//		res.setContentType("text/html; charset=utf-8");
//		PrintWriter out = res.getWriter();
//		String json = null;
//		List<?> dataList = null;
//		
//		try {
//			dataList = analysisMapper.selectColumnAnalysis(params);
//			
//			ObjectMapper mapper = new ObjectMapper(); // parser
//			json = mapper.writeValueAsString(dataList);
//		} catch (Exception e) {
//
//		}
//
//		out.print(json);
//		out.flush();
//		out.close();
//		
//	}
	
	public void selectColumnAnalysis(Map<String, String> params,HttpServletRequest req , HttpServletResponse res) throws  Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		List<?> dataList = null;
		List<?> dataList2 = null;
		HashMap resMap = new HashMap();
		HashMap minMaxMap = new HashMap();
		StringBuffer strBuf = new StringBuffer("");
		try {
			String dbmsKnd ="";
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			
			dbmsKnd = analysisMapper.selectDbList(dgnssDbmsId);
			String tableSchema = params.get("tableSchema");
			HashMap dataMap = new HashMap();
			
			if ("Oracle".equals(dbmsKnd)) {
				if (tableSchema == null) {
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
				}else {
					dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
				}
			}else if ("MySQL".equals(dbmsKnd)) {
				if (tableSchema == null) {
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
				}else {
					dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
				}
			}else if ("Tibero".equals(dbmsKnd)) {
				if (tableSchema == null) {
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
				}else {
					dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
				}
			}else if ("MSSQL".equals(dbmsKnd)) {
				if (tableSchema == null) {
					dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
				}else {
					dataList = sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
				}
			}else if ("CSV".equals(dbmsKnd)) {
				if (tableSchema == null) {
					dataList = analysisMapper.selectSchema(params);
				}else {
					dataList = analysisMapper.selectColumnAnalysis(params);
				}
			}else {
				if (tableSchema == null) {
					dataList = analysisMapper.selectSchema(params);
				}else {
					dataList = analysisMapper.selectColumnAnalysis(params);
				}
			}
			
			
			if (tableSchema == null) {
				ObjectMapper mapper = new ObjectMapper(); // parser
				json = mapper.writeValueAsString(dataList);
			}else {
				Map<String, String> paramMap = new HashMap<String, String>();
				
				String tableName = "";
				String columnName = "";
				String dataType = "";
				String maxVal = "";
				String minVal = "";
				
				String[] resultList = null;
				String dataType2 = "";
				List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> listMap2 = new ArrayList<Map<String, Object>>();
				if (dataList != null && dataList.size() > 0) {
					dataList2 = dataList;
					
					Object key = null;
					// 컬럼명에 따른 min, max 컬럼 조회 설정
					for (int i = 0; i < dataList.size(); i++) {
						HashMap map = (HashMap) dataList.get(i);
						Iterator it = map.keySet().iterator();
						while (it.hasNext()) {
							key = it.next();
							
							if("dataType".equals(key)) {
								dataType2 = map.get(key).toString();//각 dbms별 숫자형 분기 추가예정
							}
							
							if("columnName".equals(key)) {
								columnName = map.get(key).toString();
								if(i==0) {
									//strBuf.append("MAX("+columnName+") AS MAX_"+columnName);
									if ("Oracle".equals(dbmsKnd)) {
										strBuf.append("NVL(MAX("+columnName+"), NULL) AS MAX_"+columnName);
									}else if ("MySQL".equals(dbmsKnd)) {
										strBuf.append("IFNULL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
									}else if ("Tibero".equals(dbmsKnd)) {
										if((dataType2.equalsIgnoreCase("NUMBER"))||dataType2.equalsIgnoreCase("INTEGER")||dataType2.equalsIgnoreCase("FLOAT")) {
											strBuf.append("NVL(MAX("+columnName+"), '0') AS MAX_"+columnName);
										} else {
											strBuf.append("NVL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
										}
									}else if ("MSSQL".equals(dbmsKnd)) {
										if((dataType2.equalsIgnoreCase("INT"))||dataType2.equalsIgnoreCase("NUMERIC")) {
											strBuf.append("ISNULL(MAX("+columnName+"), '0') AS MAX_"+columnName);
										} else {
											strBuf.append("ISNULL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
										}
									}else if ("CSV".equals(dbmsKnd)) {
										strBuf.append("IFNULL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
									}
								}else {
									//strBuf.append(", MAX("+columnName+") AS MAX_"+columnName);
									if ("Oracle".equals(dbmsKnd)) {
										strBuf.append(", NVL(MAX("+columnName+"), NULL) AS MAX_"+columnName);
									}else if ("MySQL".equals(dbmsKnd)) {
										strBuf.append(", IFNULL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
									}else if ("Tibero".equals(dbmsKnd)) {
										if((dataType2.equalsIgnoreCase("NUMBER"))||dataType2.equalsIgnoreCase("INTEGER")||dataType2.equalsIgnoreCase("FLOAT")) {
											strBuf.append(", NVL(MAX("+columnName+"), '0') AS MAX_"+columnName);
										} else {
											strBuf.append(", NVL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
										}
									}else if ("MSSQL".equals(dbmsKnd)) {
										if((dataType2.equalsIgnoreCase("INT"))||dataType2.equalsIgnoreCase("NUMERIC")) {
											strBuf.append(", ISNULL(MAX("+columnName+"), '0') AS MAX_"+columnName);
										} else {
											strBuf.append(", ISNULL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
										}
									}else if ("CSV".equals(dbmsKnd)) {
										strBuf.append(", IFNULL(MAX("+columnName+"), 'NULL') AS MAX_"+columnName);
									}
								}
								//strBuf.append(", MIN("+columnName+") AS MIN_"+columnName);
								if ("Oracle".equals(dbmsKnd)) {
									strBuf.append(", NVL(MIN("+columnName+"), NULL) AS MIN_"+columnName);
								}else if ("MySQL".equals(dbmsKnd)) {
									strBuf.append(", IFNULL(MIN("+columnName+"), 'NULL') AS MIN_"+columnName);
								}else if ("Tibero".equals(dbmsKnd)) {
									if((dataType2.equalsIgnoreCase("NUMBER"))||dataType2.equalsIgnoreCase("INTEGER")||dataType2.equalsIgnoreCase("FLOAT")) {
										strBuf.append(", NVL(MIN("+columnName+"), '0') AS MIN_"+columnName);
									} else {
										strBuf.append(", NVL(MIN("+columnName+"), 'NULL') AS MIN_"+columnName);
									}
								}else if ("MSSQL".equals(dbmsKnd)) {
									if((dataType2.equalsIgnoreCase("INT"))||dataType2.equalsIgnoreCase("NUMERIC")) {
										strBuf.append(", ISNULL(MIN("+columnName+"), '0') AS MIN_"+columnName);
									} else {
										strBuf.append(", ISNULL(MIN("+columnName+"), 'NULL') AS MIN_"+columnName);
									}
								}else if ("CSV".equals(dbmsKnd)) {
									strBuf.append(", IFNULL(MIN("+columnName+"), 'NULL') AS MIN_"+columnName);
								}
							}
	//						if("dataType".equals(key)) {
	//							dataType = map.get(key).toString();
	//							resultMap.put("dataType", dataType);
	//						}
						}
					}
				
					// min, max 쿼리 파라메터 구성
					paramMap.put("columnName", strBuf.toString());
					paramMap.put("tableSchema", params.get("tableSchema"));
					paramMap.put("tableName", params.get("tableName"));
					
					List<?> minMaxList = null;
					if ("Oracle".equals(dbmsKnd)) {
						// 최대 / 최소값 조회 쿼리 실행
						minMaxList = sqlSession.selectList("AnalysisMapper.selectMinMaxVal",paramMap);
					}else if ("MySQL".equals(dbmsKnd)) {
						// 최대 / 최소값 조회 쿼리 실행
						minMaxList = sqlSession.selectList("AnalysisMapper.selectMinMaxVal",paramMap);
					}else if ("Tibero".equals(dbmsKnd)) {
						// 최대 / 최소값 조회 쿼리 실행
						minMaxList = sqlSession.selectList("AnalysisMapper.selectMinMaxVal",paramMap);
					}else if ("MSSQL".equals(dbmsKnd)) {
						// 최대 / 최소값 조회 쿼리 실행
						minMaxList = sqlSession.selectList("AnalysisMapper.selectMinMaxVal",paramMap);
					}else if ("CSV".equals(dbmsKnd)) {
						// 최대 / 최소값 조회 쿼리 실행
						minMaxList = analysisMapper.selectMinMaxVal(paramMap);
					}else {
						// 최대 / 최소값 조회 쿼리 실행
						minMaxList = analysisMapper.selectMinMaxVal(paramMap);
					}
					
					if(!(minMaxList.get(0) == null)) {
						Object key1 = null;
						for (int i = 0; i < minMaxList.size(); i++) {
							HashMap map = (HashMap) minMaxList.get(i);
							Iterator it = map.keySet().iterator();
							if(i== 0) {
								while (it.hasNext()) {
									key1 = it.next();
									
									minMaxMap.put(key1, map.get(key1).toString());
								}
							}
						}
						
						// 최대/최소값 dataList add
						Object key2 = null;
						for (int k = 0; k < dataList2.size(); k++) {
							HashMap map = (HashMap) dataList2.get(k);
							Iterator it = map.keySet().iterator();
							dataMap = new HashMap();
							while (it.hasNext()) {
								key2 = it.next();
								dataMap.put(key2, map.get(key2).toString());
								if("columnName".equals(key2)) {
									String maxColumn = "MAX_"+map.get(key2).toString();
									String minColumn = "MIN_"+map.get(key2).toString();
									
									dataMap.put("highValue", minMaxMap.get(maxColumn).toString());
									dataMap.put("lowValue", minMaxMap.get(minColumn).toString());
								}
							}
							listMap.add(dataMap);
						}
						
					}
					
				}
				
				if(listMap.size() != 0) {
					dataList = listMap;
				}
				
				ObjectMapper mapper = new ObjectMapper(); // parser
				//json = mapper.writeValueAsString(dataMap);
				json = mapper.writeValueAsString(dataList);
			}
//			ObjectMapper mapper = new ObjectMapper(); // parser
//			json = mapper.writeValueAsString(dataList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		out.print(json);
		out.flush();
		out.close();
		
	}

	/**
	 * 깔끔하게 변경
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectColumnAnalysis(Map<String, String> params) {

		if (params.get("tableSchema") == null) {
			return sqlSession.selectList("AnalysisMapper.selectSchema");
		}else {
			return sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
		}
	}

	public Object selectDbList(Map<String, Object> paramMap) {
		List<?> dataList = null;
		try {
				
			dataList = sqlSession.selectList("AnalysisMapper.selectSchema");
				
		} catch (Exception e) {
		
		}
		
		return dataList;
	}
	
	public Object selectTableList(Map<String, Object> paramMap) {
		List<?> dataList = null;
		try {

			dataList = sqlSession.selectList("AnalysisMapper.selectTable", paramMap);
			
		} catch (Exception e) {
		
		}
		
		return dataList;
	}
	
	/**
     * ajax패턴 리스트 조회
     * 
     * @date	: 2019 7. 03						
     * @author	: SANGS	
     *
     * @param params
     * @param req
     * @param res
	 * @return 
     * @throws Exception
     */
	public void selectPattern(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException{
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		Map<String, String> data = new HashMap();
		
		try {
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			// dgnss_dbms 테이블에서 대상 DB 정보를 가지고 온다.
			params.put("dgnssDbmsId", dgnssDbmsId);
			
			if(params.get("dbmsKnd").equals("") || params.get("dbmsKnd") == null) {
				data = ruleMngService.selectDgnssDbmsInfo(params);
				params.putAll(data);
			}
			
			super.pagingSet(params);
			
			int totalCnt = analysisMapper.selectPatternTotalCnt(params);
			List<?> dataList = analysisMapper.selectPattern(params);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();

			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("totalCnt", totalCnt);
			resultMap.put("result", dataList);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
	}

	public Object selectAnalsGroupList(Map<String, String> params) throws  Exception {
		List<?> analsList = null;
		try {
			analsList = analysisMapper.selectAnalsGroupList(params);
		}catch (Exception e) {
			
		}
		return analsList;
	}

	public void UDTPattern(Map<String, String> params, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			int resultCnt = 0;
			resultCnt = analysisMapper.UDTPattern(params);
			Map<String, Object> resultMap = new HashMap<String, Object>();

			
			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("resultCnt", resultCnt);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}

	public void UDTPatternUseAt(Map<String, String> params, HttpServletResponse res) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		
		int updateCnt = 0;
		
		try {
			JSONArray updateArray = (JSONArray) JSONValue.parse(params.get("arrayList"));
			
			HashMap<String, String> param = new HashMap<String, String>();
			
			for(Object o : updateArray) {
				JSONObject item = (JSONObject) o;
				param.put("analsId", item.get("analsId").toString());
				param.put("useAt", item.get("useAt").toString());
				
				updateCnt = analysisMapper.UDTPatternUseAt(param);
			}
			
		
			Map<String, Object> resultMap = new HashMap<String, Object>();

			
			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("result", updateCnt);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}
		
}


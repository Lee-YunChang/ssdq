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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.configuration.AnalsSqlSessionTemplate;
import com.webapp.dqsys.mngr.domain.BaseFile;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.mapper.AnalysisMapper;
import com.webapp.dqsys.mngr.mapper.DataStandardMapper;
import com.webapp.dqsys.util.CSVReader;
import com.webapp.dqsys.util.FileUploadUtil;
import com.webapp.dqsys.util.SangsAbstractService;
import com.webapp.dqsys.util.SangsUtil;
import com.webapp.dqsys.util.WebUtil;

@Service
public class DataStandardService extends SangsAbstractService {

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
	private DataStandardMapper dataStandardMapper;

	@Autowired
	AnalysisMapper analysisMapper;

	@Value("${file.csv.dataDir}")
	private String csvDir;

	@Resource(name = "analsSqlSessionTemplate")
	private AnalsSqlSessionTemplate sqlSession;

	/**표준화 항목 관리 목록 조회
	 * @param params
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public void selectDataStandardManageList(Map<String, String> params, HttpServletResponse res)
			throws Exception, IOException {
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

			if (params.get("tabType") != null && "word".equals(params.get("tabType"))) {
				// 표준 용어
				totalCnt = dataStandardMapper.selectWordListTotalCnt(params);
				dataList = dataStandardMapper.selectWordList(params);
			} else {
				// 도메인
				totalCnt = dataStandardMapper.selectDomainListTotalCnt(params);
				dataList = dataStandardMapper.selectDomainList(params);
			}
			resultMap.put("totalCnt", totalCnt);
			resultMap.put("dataList", dataList);
			resultMap.put("tabType", params.get("tabType"));
			json = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {
			logger.error("[표준화 항목 관리 조회 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}

	/**
	 * 도메인 insert 호출
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void saveDomainInfo(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
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

				param.put("domainCl", WebUtil.clearXSSMinimum(item.get("domainCl").toString()));
				param.put("domainNm", WebUtil.clearXSSMinimum(item.get("domainNm").toString()));
				param.put("domainTy", WebUtil.clearXSSMinimum(item.get("domainTy").toString()));
				param.put("domainLt", WebUtil.clearXSSMinimum(item.get("domainLt").toString()));
				param.put("domainRm", WebUtil.clearXSSMinimum(item.get("domainRm").toString()));
				param.put("domainDbms", WebUtil.clearXSSMinimum(item.get("domainDbms").toString()));

				// Domain INSERT
				int insCnt = dataStandardMapper.insertDomainInfo(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[도메인 저장 오류]");
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

	
	/**
	 * 도메인 CSV insert 호출
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void saveDomainCsv(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
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
					String[] colKeyArr = { "domainCl","domainNm",  "domainDbms", "domainTy", "domainLt", "domainRm"  };
					while ((nextLine = csvReader.readNext()) != null) {
						if (lowIndex > 0) {
							colIndex = 0;
							HashMap<String, String> param = new HashMap<String, String>();
							for (String str : nextLine) {
								if (str != null && !"".equals(str.trim())) {
							
									if(colIndex == 2) {
										if ("oracle".equals(str) || "Oracle".equals(str) || "ORACLE".equals(str)) {
											str = "1";
										} else if ("mysql".equals(str) || "Mysql".equals(str) || "MYSQL".equals(str)) {
											str = "3";
										}else if ("tibero".equals(str) || "Tibero".equals(str) || "TIBERO".equals(str)) {
											str = "4";
										}else if ("mssql".equals(str) || "Mysql".equals(str) || "MSSQL".equals(str)) {
											str = "5";
										}
										 else {
											str = null;
										}
										
										if (str != null) {
											param.put(colKeyArr[colIndex], str);
										}
									} 
									else {
										param.put(colKeyArr[colIndex], str);
									}
								}
								++colIndex;
							}

							// 필수값 체크
							if (param.get("domainCl") != null && param.get("domainNm") != null
									&& param.get("domainTy") != null) {
								// 도메인 정보 저장
								int insCnt = dataStandardMapper.insertDomainInfo(param);
								if (insCnt > 0) {
									result = true;
								}
							}
						}
						++lowIndex;
					}
				}
			}
		} catch (Exception e) {
			result = false;
			logger.error("[도메인 CSV 저장 오류]");
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

	/**
	 * 도메인 Excel insert 호출
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	public void saveDomainExcel(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		FileInputStream fis = null;
		File excelFile = null;
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
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
				String[] colKeyArr = { "domainCl", "domainNm",  "domainDbms", "domainTy", "domainLt", "domainRm"  };
				
				excelFile = new File(attFileOutputFullPath);
				fis = new FileInputStream(excelFile);
				Workbook wb = null;
				String extension = FilenameUtils.getExtension(attFileOutputFullPath);
				if (StringUtils.equalsIgnoreCase(extension, "xls")) {
					wb = new HSSFWorkbook(fis);
				} else if (StringUtils.equalsIgnoreCase(extension, "xlsx")) {
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
						
						if ("oracle".equals(dataMapValue) || "Oracle".equals(dataMapValue) || "ORACLE".equals(dataMapValue)) {
							dataMapValue = "1";
						} else if ("mysql".equals(dataMapValue) || "Mysql".equals(dataMapValue) || "MYSQL".equals(dataMapValue)) {
							dataMapValue = "3";
						}else if ("tibero".equals(dataMapValue) || "Tibero".equals(dataMapValue) || "TIBERO".equals(dataMapValue)) {
							dataMapValue = "4";
						}else if ("mssql".equals(dataMapValue) || "Mysql".equals(dataMapValue) || "MSSQL".equals(dataMapValue)) {
							dataMapValue = "5";
						}
						
						dataMap.put(colKeyArr[cellIdx], dataMapValue);
						cellIdx++;
					}
					// 필수값 체크
					if (dataMap.get("domainCl") != null && dataMap.get("domainNm") != null
							&& dataMap.get("domainTy") != null && dataMap.get("domainDbms") != null 
							&& dataMap.get("domainLt") != null && dataMap.get("domainRm") != null) {
						// 도메인 정보 저장
						int insCnt = dataStandardMapper.insertDomainInfo(dataMap);
						if (insCnt > 0) {
							result = true;
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
	
	
	
	
	/**
	 * 표준 용어 insert 호출
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void saveWordInfo(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
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

				param.put("wordNm", WebUtil.clearXSSMinimum(item.get("wordNm").toString()));
				param.put("wordEngNm", WebUtil.clearXSSMinimum(item.get("wordEngNm").toString()));
				param.put("wordEngAb", WebUtil.clearXSSMinimum(item.get("wordEngAb").toString()));
				param.put("wordCl", WebUtil.clearXSSMinimum(item.get("wordCl").toString()));
				param.put("wordRm", WebUtil.clearXSSMinimum(item.get("wordRm").toString()));
				//param.put("wordDbms", item.get("wordDbms").toString());

				// Domain INSERT
				int insCnt = dataStandardMapper.insertWordInfo(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[표준 용어 저장 오류]");
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

	/**
	 * 표준 용어 CSV insert 호출
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void saveWordCsv(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
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

		try {
			// 임시저장 폴더에 파일 생성
			String attFileOutputPath = csvDir + "/";
			List<BaseFile> fileList = null;

			if (ServletFileUpload.isMultipartContent(req)) {
				fileList = FileUploadUtil.uploadFiles(req, attFileOutputPath);
			}

			// 임시저장에 등록된 파일 읽어들여 표준용어 정보 저장
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
					String[] colKeyArr = { "wordNm", "wordEngNm", "wordEngAb", "wordCl", "wordRm" };
					while ((nextLine = csvReader.readNext()) != null) {
						if (lowIndex > 0) {
							colIndex = 0;
							HashMap<String, String> param = new HashMap<String, String>();
							for (String str : nextLine) {
								if (str != null && !"".equals(str.trim())) {
									if (colIndex == 3) {
										if ("표준어".equals(str)) {
											str = "0";
										} else if ("동의어".equals(str)) {
											str = "1";
										} else {
											str = null;
										}

										if (str != null) {
											param.put(colKeyArr[colIndex], str);
										}
									} else {
										param.put(colKeyArr[colIndex], str);
									}
								}
								++colIndex;
							}

							// 필수값 체크
							if (param.get("wordNm") != null && param.get("wordCl") != null) {
								// 표준 용어 정보 저장
								int insCnt = dataStandardMapper.insertWordInfo(param);
								if (insCnt > 0) {
									result = true;
								}
							}
						}
						++lowIndex;
					}
				}
			}
		} catch (Exception e) {
			result = false;
			logger.error("[표준 용어 CSV 저장 오류]");
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

	
	/**
	 * 표준용어 Excel insert 호출
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	public void saveWordExcel(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		FileInputStream fis = null;
		File excelFile = null;
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
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
				String[] colKeyArr = { "wordNm", "wordEngNm", "wordEngAb", "wordCl", "wordRm" };
				
				excelFile = new File(attFileOutputFullPath);
				fis = new FileInputStream(excelFile);
				Workbook wb = null;
				String extension = FilenameUtils.getExtension(attFileOutputFullPath);
				if (StringUtils.equalsIgnoreCase(extension, "xls")) {
					wb = new HSSFWorkbook(fis);
				} else if (StringUtils.equalsIgnoreCase(extension, "xlsx")) {
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
					int aaa = sheet.getLastRowNum();
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
								dataMapValue = new DecimalFormat("#####").format(cell.getNumericCellValue());
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
						
						//cell 널체크
						if("".equals(dataMapValue) || null == dataMapValue) {
							continue;
						}
						
							if ("표준어".equals(dataMapValue)) {
								dataMapValue = "0";
							} else if ("동의어".equals(dataMapValue)) {
								dataMapValue = "1";
							} 
							dataMap.put(colKeyArr[cellIdx], dataMapValue);
							cellIdx++;
					}
					// 필수값 체크
					if (dataMap.get("wordNm") != null && dataMap.get("wordCl") != null && dataMap.get("wordEngNm") != null
							&& dataMap.get("wordEngAb") != null && dataMap.get("wordRm") != null) {
						// 도메인 정보 저장
						int insCnt = dataStandardMapper.insertWordInfo(dataMap);
						if (insCnt > 0) {
							result = true;
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
	
	
	
	
	
	/**
	 * 분석 테이블 정보조회
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public List<?> selectTableAnalysis(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
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
			logger.error("[표준화 분석 테이블 조회 오류]");
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		}

		return dataList;
	}

	
	/**
	 * 분석 대상 스키마 정보 입력
	 * @param paramMap
	 * 
	 */
	public void insertStandardSchema(Map<String, Object> paramMap) throws Exception {
		dataStandardMapper.insertStandardSchema(paramMap);
	}

	/**
	 * 표준화 분석 결과 리스트 ajax호출
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectStandardSchemaList(Map<String, Object> paramMap) {
		super.pagingSetMySql(paramMap);

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("list", dataStandardMapper.selectStandardSchemaList(paramMap));
		resultMap.put("totalCnt", dataStandardMapper.selectStandardSchemaListCnt(paramMap));

		return resultMap;
	}

	/**
	 * 
	 * @param paramMap
	 * @param tableInfoMap
	 * @param analsisItemList
	 */
	public void insertStandardTable(Map<String, Object> paramMap, Map<String, Object> tableInfoMap,
			String[] analsisItemList) {
		
		String domain = "0";
		String word = "0";
		if(analsisItemList != null) {
			for(int i = 0; i < analsisItemList.length; i++) {
				if(analsisItemList[i].equals("domain")) {
					domain = "1";
				}else if(analsisItemList[i].equals("word")) {
					word = "1";
				}
			}
		}
		
		paramMap.put("domainAt", domain);
		paramMap.put("wordAt", word);
		paramMap.putAll(tableInfoMap);
		
		dataStandardMapper.insertStandardTable(paramMap);
		
	}

	/**
	 * 
	 * @param paramMap
	 */
	public void insertStandardColumn(Map<String, Object> paramMap) {
		dataStandardMapper.insertStandardColumn(paramMap);
	}
	
	
	/**
	 * 컬럼 단위로 쓰레드 실행 표준용어,도메인 분석
	 * @param asyncMap
	 */
	@Async("asyncExecutor")
	public void asyncExecutorDataStandardColumn(Map<String, Object> asyncMap) throws Exception  {
		
		
		int result ;
		List<Map<String, String>> wordResult = dataStandardMapper.selectDataStandardWord(asyncMap);
		
		
		String columnNm = (String) asyncMap.get("columnNm");
		
		String [] domain  = columnNm.split("_");
		ArrayList<String> domainList = new ArrayList<>();

		for(String temp : domain) {
			domainList.add(temp);
		}
		
		asyncMap.put("domainList", domainList);
		
		
		String domainResult = dataStandardMapper.selectDataStansdardDomain(asyncMap);
		
		
		
//		if(asyncMap.get("wordAt").equals("0")) {
//			asyncMap.put("wordResult", "2");
//		}else if(asyncMap.get("wordAt").equals("1")) {
//			if(wordResult != null) {
//				asyncMap.put("wordResult", "1");
//			}else if(wordResult == null) {
//				asyncMap.put("wordResult", "0");
//			}
//		}
		
		if(asyncMap.get("wordAt").equals("0")) {
			asyncMap.put("wordResult", "2");
		}else if(asyncMap.get("wordAt").equals("1")) {
			if(wordResult.size() > 0) {
				asyncMap.put("wordResult", "1");
			}else if(wordResult.size() == 0) {
				asyncMap.put("wordResult", "0");
			}
		}
		
		if(asyncMap.get("domainAt").equals("0")) {
			asyncMap.put("domainResult", "2");
		}else if(asyncMap.get("domainAt").equals("1")) {
			if(domainResult != null ) {
				asyncMap.put("domainResult", "1");
			}else if(domainResult == null ) {
				asyncMap.put("domainResult", "0");
			}
		}
		result = dataStandardMapper.updateDataStandardColumn(asyncMap);
		
		if(result == 0) {
			asyncMap.put("excSttus", "F");
			dataStandardMapper.updateDataStandardShema(asyncMap);
		}
		
	}

	/**
	 * 분석결과 update
	 * @param paramMap
	 */
	public void executeUpdateEndStandardSchema(Map<String, Object> paramMap) throws Exception {
		
		dataStandardMapper.executeUpdateEndStandardSchema(paramMap);
		
	}

	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public SangsMap selectStandardSchema(Map<String, Object> paramMap) throws Exception  {
		
		return dataStandardMapper.selectStandardSchema(paramMap);
		
	}

	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<SangsMap> selectStandardTableList(Map<String, Object> paramMap) throws Exception  {
		
		return dataStandardMapper.selectStandardTableList(paramMap);
	}

	
	
	/**
	 * 분석요약정보 조회
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectStandardSummery(Map<String, Object> paramMap) throws Exception  {
		
		Map<String, Object> summery = new HashMap<String, Object>();
		Map<String, Object> tableCnt = dataStandardMapper.selectTableCnt(paramMap);	
		
		//표준용어 미준수 컬럼조회
		String wordObsry = dataStandardMapper.selectWordObsry(paramMap);	
		
		//도메인 미준수 컬럼 조회
		String domainObsry = dataStandardMapper.selectDomainObsry(paramMap);	
		
		summery.putAll(tableCnt);
		summery.put("wordObsry", wordObsry);
		summery.put("domainObsry", domainObsry);
		
		return summery;
	}

	public int selectStandardColumnObsryCnt(Map<String, Object> paramMap)  throws Exception {
		// TODO Auto-generated method stub
		return dataStandardMapper.selectStandardColumnObsryCnt(paramMap);
	}

	public List<SangsMap> selectStandardColumnResList(Map<String, Object> paramMap) throws Exception  {
		
		return dataStandardMapper.selectStandardColumnResList(paramMap);
	}

	public List<SangsMap> selectWordObsryList(Map<String, Object> paramMap) throws Exception  {

		return dataStandardMapper.selectWordObsryList(paramMap);
	}

	public List<SangsMap> selectDomainObsryList(Map<String, Object> paramMap) throws Exception  {
		// TODO Auto-generated method stub
		return dataStandardMapper.selectDomainObsryList(paramMap);
	}
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void updateDomainInfo(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
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
				
				param.put("domainId", item.get("domainId").toString());
				param.put("domainCl", item.get("domainCl").toString());
				param.put("domainNm", item.get("domainNm").toString());
				param.put("domainTy", item.get("domainTy").toString());
				param.put("domainDbms", item.get("domainDbms").toString());
				param.put("domainLt", item.get("domainLt").toString());
				param.put("domainRm", item.get("domainRm").toString());

				// Domain update
				int insCnt = dataStandardMapper.updateDomainInfo(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[도메인 수정 오류]");
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
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void updateWordInfo(Map<String, String> params, HttpServletRequest req, HttpServletResponse res)
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
				
				param.put("wordId", item.get("wordId").toString());
				param.put("wordNm", item.get("wordNm").toString());
				param.put("wordEngNm", item.get("wordEngNm").toString());
				param.put("wordEngAb", item.get("wordEngAb").toString());
				//현재 표준어,동의어 구분 X 표준어만 등록
				param.put("wordCl", "0");
				param.put("wordRm", item.get("wordRm").toString());

				// Word UPDATE
				int insCnt = dataStandardMapper.updateWordInfo(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[표준 용어 수정 오류]");
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
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void deleteDomainInfo(String[] params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		try {
			//JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("insertList"));

			HashMap<String, String> param = new HashMap<String, String>();

			for (int i=0; i < params.length;  i++) {
		
				param.put("domainId", params[i]);
				
				// Domain DELETE
				int insCnt = dataStandardMapper.deleteDomainInfo(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[표준 용어 수정 오류]");
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
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void deleteWordInfo(String[] params, HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper(); // parser

		try {
			
			HashMap<String, String> param = new HashMap<String, String>();
			
			for (int i=0; i < params.length;  i++) {
				
				param.put("wordId", params[i]);
				
				// Word DELETE
				int insCnt = dataStandardMapper.deleteWordInfo(param);

				if (insCnt > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			logger.error("[표준 용어 수정 오류]");
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

	public void insertDomainMatch(List<Map<String, String>> columnData, Map<String, Object> paramMap, HttpServletRequest req, HttpServletResponse res) {
		
		
		int columnCnt = Integer.parseInt(String.valueOf(paramMap.get("columnCnt")));
		
		Map<String, String> param = new HashMap<String, String>();
		for(int i = 0; i < columnCnt; i ++) {
			
			param.putAll(columnData.get(i));
			param.put("domainDbms", (String)paramMap.get("dbmsId"));
			param.put("dbmsId", (String)paramMap.get("dbmsId"));
			

			if(!param.get("domainId").equals("0")) {
				
				if(columnData.get(i).get("updateYn").equals("0")) {
					dataStandardMapper.insertDomainMatch(param);
				}else if(columnData.get(i).get("updateYn").equals("1")) {
					dataStandardMapper.updateDomainMatch(param);
				}


			}
			
		}
		
		
	}

	public void selectMatchData(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		
		try {
		List<Map<String,String>> matchDataList  = dataStandardMapper.selectMatchData(params);
		Map<String,String> data = new HashMap<String,String>();
		List<Map<String,String>> domainDataList = new ArrayList<>();
		List<Map<String,String>> columnList = new ArrayList<>();
		if (params.get("tableSchema") == null) {
			 columnList = sqlSession.selectList("AnalysisMapper.selectSchema");
		}else {
			 columnList =  sqlSession.selectList("AnalysisMapper.selectColumnAnalysis", params);
		}
		
		
		
		if(matchDataList.size() != 0) {
		
		
			for(int i = 0; i < columnList.size(); i++) {
				
			
				for(int  j = 0 ; j < matchDataList.size(); j++) {
					
					params.put("domainId", matchDataList.get(j).get("DOMAIN_ID"));
					
					
					data = dataStandardMapper.domainDataList(params);
					if(columnList.get(i).get("columnName").equals(matchDataList.get(j).get("COLUMN_NM"))) {
						
						//도메인 매칭 insert,update flag값 세팅
						data.put("updateYn", "1");
						data.put("row", String.valueOf(i));
						data.put("MATCH_ID",matchDataList.get(j).get("MATCH_ID"));
						domainDataList.add(data);
					}
					
				}
				
			}
		} else {
			
			data.put("updateYn", "0");
			domainDataList.add(data);
			
		}

		ObjectMapper mapper = new ObjectMapper(); // parser
		
		json = mapper.writeValueAsString(domainDataList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		out.print(json);
		out.flush();
		out.close();
		
	}

	public void deleteMatch(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) {
		
		dataStandardMapper.deleteMatch(params);
		
	}

	public List<?> selectDomainGroupList(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws IOException {

		List<?> domainDataList = new ArrayList<>();
		try {
			domainDataList = dataStandardMapper.selectDomainGroupList(params);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return domainDataList;
	}
}

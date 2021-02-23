package com.webapp.dqsys.mngr.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisOrientation;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData.Series;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.service.AnalysisService;
import com.webapp.dqsys.mngr.service.BasicInfoService;
import com.webapp.dqsys.mngr.service.DiagnosisService;
import com.webapp.dqsys.mngr.service.RuleMngService;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.util.SangsUtil;

@Controller
public class DiagnosisController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private DiagnosisService diagnosisService;

	@Resource
	private BasicInfoService basicInfoService;

	@Resource
	private AnalysisService analysisService;

	@Resource
	private RuleMngService ruleMngService;
	
	private Future<List<Long>> numberListTask;

	/**
	 * 불일치 데이터 다운로드 임시 저장 경로
	 */
	@Value("${file.diagnosis.dataDir}")
	private String dataDir;
	
	/**
	 * 리포트 파일 임시 저장 경로
	 */
	@Value("${file.report.dataDir}")
	private String reportDir;
	
	/**
	 * 분석결과 보기 페이지당 리스트 건수
	 */
	@Value("${analysis.view.listCnt}")
	private int viewListCnt;
	
	/**
	 * 불일치 다운로드 리스트 건수
	 */
	@Value("${analysis.report.listCnt}")
	private int reportListCnt;

	/**
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/form1.do")
	public String form1(HttpServletRequest req, Model model) {
		
		String dbmsId = (String) req.getSession().getAttribute("DDId");
		
		try {
			// 접속 정보 조회
			Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);
			
			if("CSV".equals(connData.get("connType"))) {
				model.addAttribute("connData", connData);
			}else {
				model.addAttribute("schema", connData.get("database"));
			}
		}catch(Exception e) {
			e.getStackTrace();
		}
		
		return "mngr/diagnosis/form1";
	}

	/**
	 * 진단대상 DBMS 스키마 목록 조회
	 * @param params
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/selectSchemaListAjax.do")
	@ResponseBody
	public List<Map<String, Object>> selectSchemaListAjax(@RequestParam Map<String, String> params) {
		return analysisService.selectColumnAnalysis(params);
	}

	/**
	 * 진단대상 DBMS 테이블 목록 조회
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/selectTableListAjax.do")
	@ResponseBody
	public List<String> selectTableListAjax(@RequestParam Map<String, Object> paramMap) throws Exception {
		return diagnosisService.selectTableList(paramMap);
	}

	/**
	 * @param model
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/popup/selectColumn.do")
	public String popupSelectColumn(Model model, @RequestParam Map<String, Object> paramMap) throws Exception {

		model.addAttribute("columnList", diagnosisService.selectColumnList(paramMap));
		model.addAttribute("dataList", diagnosisService.selectDataList(paramMap, 10));

		boolean csvAt = false;
		if (StringUtils.startsWithIgnoreCase(MapUtils.getString(paramMap, "tableName"), "csv")) {
			csvAt = true;
		}
		model.addAttribute("csvAt", csvAt);

		return "mngr/diagnosis/select_column_pop";
	}

	/**
	 * @param session
	 * @param request
	 * @param model
	 * @param schemaName
	 * @param tableName
	 * @param columnName
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/form2.do")
	public String form2(HttpSession session, HttpServletRequest request, Model model,
			String schemaName, String tableName, String[] columnName,
			@RequestParam Map<String, Object> paramMap) throws Exception {

		if (columnName == null || columnName.length == 0) {
			return "redirect:form1.do";
		}

		paramMap.put("schemaNm", schemaName);

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
		paramMap.put("insttCode", insttCode);

		String newDdId = MapUtils.getString(paramMap, "dgnssDbmsId");

		if (StringUtils.isNotEmpty(newDdId) && !StringUtils.equals(dgnssDbmsId, newDdId)) {
			Map<String, String> params = new HashMap<>();
			params.put("dgnssDbmsId", newDdId);
			try {
				basicInfoService.setDbmsUrl(params, request, null);
				dgnssDbmsId = newDdId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		paramMap.put("dgnssDbmsId", dgnssDbmsId);
		
		Map<String, String> data = new HashMap();
		data.put("dgnssDbmsId", dgnssDbmsId);
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);
		

		try {
			model.addAttribute("dbList", analysisService.selectDbList(paramMap));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		model.addAttribute("paramMap", paramMap);

		model.addAttribute("tableList", diagnosisService.selectTableList(paramMap));

		model.addAttribute("columnList", diagnosisService.selectColumnList(schemaName, tableName, columnName));
		model.addAttribute("dataList", diagnosisService.selectDataList(paramMap, columnName, 9));

		paramMap.put("insttCode", insttCode);

		model.addAttribute("analsList", diagnosisService.selectAnalsList(paramMap));
		model.addAttribute("analsGroup", changeListToMap(diagnosisService.selectAnalsGroupList(paramMap), "groupCode"));

		boolean csvAt = false;
		if (StringUtils.startsWithIgnoreCase(MapUtils.getString(paramMap, "tableName"), "csv")) {
			csvAt = true;
		}
		model.addAttribute("csvAt", csvAt);

		return "mngr/diagnosis/form2";
	}

	
	@RequestMapping("/mngr/diagnosis/timecheck.do")
	public void timecheck(@RequestParam Map<String, Object> params, HttpSession session, HttpServletRequest req,
			HttpServletResponse res) {
		try {
			diagnosisService.insertSchedulerTimeConfirm(params, req, res, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param session
	 * @param request
	 * @param schemaName
	 * @param tableName
	 * @param columnNameList
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/insert.do")
	public String insert(@RequestParam Map<String, String> params, HttpSession session, HttpServletRequest request,
			HttpServletResponse response ,String schemaName, String tableName,
			@RequestParam(name = "columnName") String[] columnNameList) throws Exception {

		Map<String, String[]> rMap = request.getParameterMap();
		
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		if (columnNameList == null || columnNameList.length == 0) {
			return "redirect:form1.do";
		}
		
		String dateFmt = null;
		String excBeginTime = null;
		
		//시작시간 스케줄러 예약시간으로 설정
		if(params.get("useAt").equals("1") || params.get("useAt") == "1") {
			dateFmt = params.get("scheduleDe")+"00";
			SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
			Date dt = fm.parse(dateFmt);
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			excBeginTime = dt1.format(dt);
			
			//스케줄러 예약가능시간체크
			String nowDe = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
			Date checkDe = fm.parse(nowDe);
			
			int compare = dt.compareTo(checkDe);
			
			if(compare < 0) {
				String result = "1";
				out.print(result);
	            out.flush();
	            out.close();
	           return "forward:form1.do";
			}
			
			
		} else if(params.get("useAt").equals("0") || params.get("useAt") == "0") {
			dateFmt = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
		}
		Map<String, Object> paramMap = new ConcurrentHashMap<>();

		// 세션값에서 기관정보 , 진단대상데이터베이스정보 조회 처리
		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		String insttCode = SangsUtil.getInsttCode();
		String dgnssSaveNm = params.get("dgnssSaveNm");
		
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		if (dgnssDbmsId == null) {
			dgnssDbmsId = "";
		}
		paramMap.put("insttCode", insttCode);
		paramMap.put("dgnssDbmsId", dgnssDbmsId);
		
		//paramMap.put("insttCode", dbmsMap.get("insttCode"));
		//paramMap.put("dgnssDbmsId", dbmsMap.get("dgnssDbmsId"));
		String dgnssSaveId = dgnssDbmsId + "_" + dateFmt;
		paramMap.put("dgnssInfoId", dgnssDbmsId + "_" + dgnssSaveId);
//		paramMap.put("dgnssNm", dgnssDbmsId + "_" + tableName + "_" + dateFmt);
		paramMap.put("dgnssNm", dgnssSaveNm + "(" + dgnssDbmsId + "_" + tableName + "_" + dateFmt + ")");
		paramMap.put("schemaNm", schemaName);
		paramMap.put("database", schemaName);
		paramMap.put("tableNm", tableName);
		paramMap.put("allCo", diagnosisService.selectRowCount(paramMap));
		paramMap.put("registerId", SangsUtil.getUserName());
		paramMap.put("scheduleDe", params.get("scheduleDe"));
		paramMap.put("excBeginTime", String.valueOf(excBeginTime));
		paramMap.put("useAt", params.get("useAt"));
		
		if(params.get("useAt").equals("1") || params.get("useAt") == "1") {
			paramMap.put("excSttus", "R");
		} else if(params.get("useAt").equals("0") || params.get("useAt") == "0") {
			paramMap.put("excSttus", "S");
		}
		
		
		Map<String, String> data = new HashMap();
		data.put("dgnssDbmsId", dgnssDbmsId);
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);

		diagnosisService.insertDgnssTable(paramMap);
		
		
		/*************/
		paramMap.put("dgnssSaveNm", dgnssSaveNm);
		paramMap.put("dgnssSaveId", dgnssSaveId);

		StringBuilder columnNmDc = new StringBuilder();
		StringBuilder analsIdDc = new StringBuilder();
		String[] analsIdTempList;

		for (String columnName : columnNameList) {

			columnNmDc.append(columnName);
			columnNmDc.append(",");

			analsIdTempList = rMap.get(columnName);

			if (analsIdTempList == null) {
				continue;
			}

			analsIdDc.append("\"" + columnName + "\"");
			analsIdDc.append(":[");
			if("Oracle".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				analsIdDc.append("\"1001\"");
			} else if("MySQL".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				analsIdDc.append("\"3001\"");
			} else if("Tibero".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				analsIdDc.append("\"4001\"");
			} else if("MSSQL".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				analsIdDc.append("\"5001\"");
			} else if("CSV".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				analsIdDc.append("\"2001\"");
			}
			analsIdDc.append(",");
			for (String analsId : analsIdTempList) {
				analsIdDc.append("\"" + analsId + "\"");
				analsIdDc.append(",");
			}
			if (analsIdTempList.length > 0) {
				analsIdDc.setLength(analsIdDc.length() - 1);
			}
			analsIdDc.append("],");
		}
		if (columnNameList.length > 0) {
			columnNmDc.setLength(columnNmDc.length() - 1);
		}
		if (analsIdDc.length() > 0) {
			analsIdDc.setLength(analsIdDc.length() - 1);
		}

		paramMap.put("columnNmDc", columnNmDc.toString());
		paramMap.put("analsIdDc", analsIdDc.toString());

		int dgnssSaveResCnt = diagnosisService.insertDgnssSave(paramMap);
		logger.info("insertDgnssSave : " + dgnssSaveResCnt);
		/**************/
		
		if(params.get("useAt").equals("1") || params.get("useAt") == "1") {
			
			diagnosisService.insertScheduler(paramMap);
		} else if (params.get("useAt").equals("0") || params.get("useAt") == "0") {
			
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

		for (Map<String, Object> columnMap : columnMapList) {

			String columnName = MapUtils.getString(columnMap, "COLUMN_NAME");
			String[] analsIdList = rMap.get(columnName);
			
			List<String> aLsit = new ArrayList<String>();
			
			if("Oracle".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				aLsit.add("1001");
			} else if("MySQL".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				aLsit.add("3001");
			} else if("Tibero".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				aLsit.add("4001");
			} else if("MSSQL".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				aLsit.add("5001");
			} else if("CSV".equalsIgnoreCase(paramMap.get("dbmsKnd").toString())) {
				aLsit.add("2001");
			}
			
			for (int i = 0; i < analsIdList.length; i++) {
				aLsit.add(analsIdList[i]);
			}
			analsIdList = new String[aLsit.size()];
			aLsit.toArray(analsIdList);
			
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

//			for (String analsId : analsIdList) {
//				logger.info("analsId : "+analsId);
//				paramMap.put("analsId", analsId);
//
//				if (!StringUtils.equals(MapUtils.getString(analsIdMap.get(analsId), "analsSe"), "AG000200")) { 
//					// Freq 가  아닌 경우 (DB에 아직 Freq 구분 플래그가 없어서 하드코딩 함)
//					diagnosisService.insertDgnssColumnRes(paramMap);
//				} else {
//					paramMap.put("sn", 0);
//					diagnosisService.insertFrqAnal(paramMap);
//				}
//
//				Map<String, Object> asyncMap = new ConcurrentHashMap<>();
//				asyncMap.putAll(paramMap);
//				// 컬럼별 분석 항목 쓰레드 실행
//				diagnosisService.asyncExecutorAnalysisAnalsId(asyncMap, analsIdMap.get(analsId));
//			}
			
			// 컬럼 단위로 쓰레드 실행 
			Map<String, Object> asyncMap = new ConcurrentHashMap<>();
			asyncMap.putAll(paramMap);
			diagnosisService.asyncExecutorAnalysisColumn(asyncMap, analsIdMap, analsIdList);
		}

		diagnosisService.executeUpdateEndDgnssTable(paramMap);
	}
		return "mngr/diagnosis/form1";
		//return "redirect:result/list.do";
	}

	/**
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/result/list.do")
	public String resultList() {
		return "mngr/diagnosis/result/list";
	}

	/**
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/result/listAjax.do")
	@ResponseBody
	public Map<String, Object> resultListAjax(@RequestParam Map<String, Object> paramMap) throws Exception {
		return diagnosisService.selectDgnssTableList(paramMap);
	}

	/**
	 * @param model
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/result/view.do")
	public String resultView(Model model, @RequestParam Map<String, Object> paramMap) throws Exception {
		int listCntPerPage = viewListCnt;
		if(listCntPerPage <= 0 ) {
			listCntPerPage = 1;
		}
		paramMap.put("insttCode", SangsUtil.getInsttCode());
		Map<String, String> data = new HashMap();
		// 진단대상 DBMS 정보 조회
		data.put("dgnssDbmsId", paramMap.get("dgnssDbmsId").toString());
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);
		// 대상 DBMS 정보 조회
		model.addAttribute("dbms", diagnosisService.selectDgnssDbms(paramMap));
		// 진단 테이블 정보 조회
		SangsMap tableMap = diagnosisService.selectDgnssTable(paramMap);
		model.addAttribute("table", tableMap);
		/*
		 * List<SangsMap> columnList = diagnosisService.selectDgnssColumnList(paramMap);
		 * List<List<SangsMap>> columnResList = new ArrayList<>(columnList.size());
		 * List<List<SangsMap>> frqAnalList = new ArrayList<>(columnList.size());
		 * paramMap.put("rowCount", tableMap.get("allCo"));
		 * 
		 * for (SangsMap columnMap : columnList) { paramMap.put("columnNm",
		 * columnMap.get("columnNm"));
		 * columnResList.add(diagnosisService.selectDgnssColumnResList(paramMap));
		 * frqAnalList.add(diagnosisService.selectFrqAnalList(paramMap)); }
		 */

		// 페이징 처리로 컬럼 단위로 하나씩 끊음
		List<SangsMap> columnList = diagnosisService.selectDgnssColumnList(paramMap);
		List<Object> columnListAll = new ArrayList<>();
		List<Object> frqListAll = new ArrayList<>();
		List<Object> columnCmt = new ArrayList<>();

		Map<String, Object> bodyMap = new HashMap<String, Object>();
		Map<String, Object> frqlList = new HashMap<String, Object>();

		diagnosisService.pagingSetMySql(paramMap);

		int pageIdx = MapUtils.getIntValue(paramMap, "pageIndex", 1);

		if (pageIdx > columnList.size()) {
			pageIdx = 1;
		}

		SangsMap columnMap = null;

		Map<String, Object> codeMap = new HashMap<String, Object>();
		Map<String, Object> freMap = new HashMap<String, Object>();
		List<SangsMap> columnData = new ArrayList<>();
		
		int cnt = 0;
		boolean userAdd = true;
		List<SangsMap> columnResLists = diagnosisService.selectDgnssColumnResList(paramMap);
		
		//사용자 정의 등록후 분석시 버튼 숨김을 위한 체크
		for (int j = 0; j < columnResLists.size(); j++) {
			SangsMap groupCode = columnResLists.get(j); 
			for (int k = 0; k < groupCode.size(); k++) {
				String code = groupCode.get("groupCode").toString();
				String strCode = code.substring(0, 6);
				if(strCode.equalsIgnoreCase("AG0004") || strCode.equalsIgnoreCase("AG0006") ) {
					cnt++;
				}
			}
		}
		
		if(cnt > 0){
			userAdd = false;
		}
		
		boolean csvat = false;
			for (int i = (pageIdx - 1) * listCntPerPage; i < (pageIdx * listCntPerPage); i++) {

				if(columnList.size()> i) {	
					
						List<SangsMap> columnResList = new ArrayList<>();
						List<SangsMap> frqAnalList = new ArrayList<>();
						String cmt;
						columnMap = columnList.get(i);
						
						columnData.add(columnList.get(i));
			
						paramMap.put("rowCount", tableMap.get("allCo"));
						paramMap.put("columnNm", columnMap.get("columnNm"));
						// 컬럼별 진단 결과0111
						columnResList = diagnosisService.selectDgnssColumnResList(paramMap);
						
						// 분석 구분 목록
						frqAnalList = diagnosisService.selectFrqAnalList(paramMap);
						
						columnListAll.add(columnResList);
						frqListAll.add(frqAnalList);
						
			
						if ("CSV".equals(data.get("dbmsKnd").toString())) {
							  paramMap.put("tableName", tableMap.get("tableNm"));
							  paramMap.put("columnName", columnMap.get("columnNm")); // 컬럼별 코멘트 정보
			  				  cmt = diagnosisService.selectColumnComment(paramMap);
							  columnCmt.add(cmt);
						}
				}
			}
			
			bodyMap.put("body", columnListAll);
			frqlList.put("frqAnalList", frqListAll);
			
	
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			System.out.println("bodyMap:::"+bodyMap);
//			System.out.println("columnData:::"+columnData);
//			System.out.println("columnList:::"+columnList);
//			System.out.println("columnMap:::"+tableMap);
//			System.out.println("frqlList:::"+frqlList);
//			System.out.println("columnCmt:::"+columnCmt);
//			System.out.println("listCntPerPage:::"+listCntPerPage);
//			System.out.println("groupList:::"+diagnosisService.selectAnalsGroupList(paramMap));
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			
			model.addAttribute("userAdd", userAdd);
			model.addAttribute("userCnt", cnt);
			model.addAttribute("bodyMap", bodyMap);
			model.addAttribute("columnData", columnData);
			model.addAttribute("columnList", columnList);
			model.addAttribute("column", columnMap);
			model.addAttribute("groupList", diagnosisService.selectAnalsGroupList(paramMap));
			model.addAttribute("frqlList", frqlList);
			model.addAttribute("columnCmt", columnCmt);
			model.addAttribute("listCntPerPage", listCntPerPage);
			boolean csvAt = false; 
			if ("CSV".equals(data.get("dbmsKnd").toString())) {
				csvAt = true;
			}else{
				csvAt = false;
			}
	
			model.addAttribute("csvAt", csvAt);


		return "mngr/diagnosis/result/view";
	}

	/**
	 * @param session
	 * @param request
	 * @param response
	 * @param paramMap
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@RequestMapping("/mngr/diagnosis/result/download/report.do")
	public void downloadReport(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap) throws IOException, InvalidFormatException {
		// PC 버전 배포시 jar 파일 내의 파일을 정상적으로 인식 하지 못하여 파일 경로를 변경함.
		final String fileName = "report";
		File file = new File(reportDir + fileName + ".xlsx");

		if (file.exists() == false) {
			throw new FileNotFoundException(file.getName());
		}
		/*
		final String fileName = "엑셀리포트";
		ClassPathResource resource = new ClassPathResource("/report/" + fileName + ".xlsx");

		if (!resource.exists()) {
			throw new FileNotFoundException(resource.getFilename());
		}
		
		// 테스트용 파라메터 (비로그인용)
		if (SangsUtil.getInsttCode() == null) {
			paramMap.put("insttCode", "0000006");
			paramMap.put("dgnssDbmsId", "1");
			paramMap.put("dgnssInfoId", "1_20191010003255");
		} else {
			paramMap.put("insttCode", SangsUtil.getInsttCode());
		}
		// 테스트용 파라메터 (비로그인용)
*/
		
		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		String insttCode = SangsUtil.getInsttCode();
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		paramMap.put("insttCode", insttCode);
		// 진단대상 DBMS 정보 조회
		Map<String, String> data = new HashMap<>();
		data.put("dgnssDbmsId", MapUtils.getString(paramMap, "dgnssDbmsId"));
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);

		String newDdId = MapUtils.getString(paramMap, "dgnssDbmsId");

		if (!StringUtils.equals(dgnssDbmsId, newDdId)) {
			Map<String, String> params = new HashMap<>();
			params.put("dgnssDbmsId", newDdId);
			try {
				basicInfoService.setDbmsUrl(params, request, response);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}

		// 엑셀파일 작업
		try (FileInputStream fis = new FileInputStream(file);
				XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook(fis)) {

//			SXSSFWorkbook workbook = new SXSSFWorkbook(fileWorkbook);
			XSSFSheet sheet;
			XSSFRow row;
			XSSFCellStyle cellStyle;
			CellCopyPolicy styleCcp = new CellCopyPolicy().createBuilder().cellValue(false).build();
			CellCopyPolicy allCcp = new CellCopyPolicy().createBuilder().build();
			XSSFCellStyle cellStyle0 = workbook.createCellStyle();
			cellStyle0.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
			cellStyle0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle0.setWrapText(true);
			XSSFCellStyle cellStyle1 = workbook.createCellStyle();
			cellStyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
			cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle1.setWrapText(true);
			
			int rowNum;

			SangsMap tableMap = diagnosisService.selectDgnssTable(paramMap);
			paramMap.put("rowCount", tableMap.get("allCo")); // Match% 구하기 위해 총 카운트 입력

			// 1. 표지
			sheet = workbook.getSheetAt(0);

			SangsMap insttMap = diagnosisService.selectInsttForReport(paramMap);

			boolean csvAt = (StringUtils.equals(MapUtils.getString(paramMap, "dbmsKnd"), "CSV"));
			String csvFileName = "";

			sheet.getRow(16).getCell(7).setCellValue("");	// 시스템명
			sheet.getRow(17).getCell(7).setCellValue(MapUtils.getString(insttMap, "insttNm"));	// 기관명
			sheet.getRow(18).getCell(7).setCellValue(MapUtils.getString(insttMap, "bsnsDe"));	// 진단기간

			if (!csvAt) {
				sheet.getRow(19).getCell(7).setCellValue(MapUtils.getString(paramMap, "dbmsKnd"));	// DB 종류
				sheet.getRow(19).getCell(9).setCellValue(MapUtils.getString(paramMap, "database"));	// DATABASE 명
				sheet.getRow(20).getCell(7).setCellValue(MapUtils.getString(insttMap, "dgnssDbmsNm"));	// DB 명
			} else {

				csvFileName = StringUtils.substringBefore(MapUtils.getString(insttMap, "dgnssDbmsNm"), "(");

				sheet.getRow(19).getCell(6).setCellValue("파일명");	// DB 종류 타이틀
				sheet.addMergedRegion(new CellRangeAddress(19, 19, 7, 9));
				sheet.getRow(19).getCell(7).setCellValue(csvFileName);	// 파일명

				cellStyle = workbook.createCellStyle();
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());
				cellStyle.setBorderRight(BorderStyle.THIN);
				cellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());

				row = sheet.getRow(20);
				row.getCell(6).setCellValue("");
				row.getCell(6).setCellStyle(cellStyle);
				row.getCell(7).setCellStyle(cellStyle);
				row.getCell(8).setCellStyle(cellStyle);
				row.getCell(9).setCellStyle(cellStyle);
			}

			// 2. 진단대상정보
			sheet = workbook.getSheetAt(1);

			// 진단 대상 정보 테이블 정보
			paramMap.put("tableNm", tableMap.get("tableNm"));
			List<SangsMap> columnList = diagnosisService.selectDgnssColumnListForReport(paramMap);
			List<String> pkList = null;

			if (!csvAt) {
				try {
					pkList = diagnosisService.selectPKList(paramMap);
				} catch (Exception e) {
					pkList = new ArrayList<>();
				}
			} else {
				pkList = new ArrayList<>();
				sheet.getRow(1).getCell(1).setCellValue("진단대상 파일 정보");
				sheet.getRow(2).getCell(1).setCellValue("파일명");
			}

			rowNum = 3;

			for (SangsMap column : columnList) {

				row = sheet.getRow(rowNum);

				if (row == null) {
					sheet.copyRows(3, 3, rowNum, styleCcp);
					row = sheet.getRow(rowNum);
				}

				if (!csvAt) {
					row.getCell(3).setCellValue(MapUtils.getString(column, "columnNm"));
				} else {
					row.getCell(3).setCellValue(MapUtils.getString(column, "columnComment"));
				}
				row.getCell(4).setCellValue(MapUtils.getString(column, "columnTy") + "(" + MapUtils.getString(column, "columnLt") + ")");

				for (String pk : pkList) {
					if (StringUtils.equals(MapUtils.getString(column, "columnNm"), pk)) {
						row.getCell(5).setCellValue("PK");
					}
				}

				rowNum++;
			}

			if (!csvAt) {
				sheet.getRow(rowNum - columnList.size()).getCell(1).setCellValue(MapUtils.getString(tableMap, "tableNm"));
			} else {
				sheet.getRow(rowNum - columnList.size()).getCell(1).setCellValue(csvFileName);
			}
			sheet.addMergedRegion(new CellRangeAddress(rowNum - columnList.size(), rowNum - 1, 1, 2));

			// 진단대상 컬럼별 진단항목 정보
			rowNum += 2;
			sheet.copyRows(1, 3, rowNum, styleCcp);

			sheet.getRow(rowNum++).getCell(1).setCellValue("진단대상 컬럼별 진단항목 정보");
			sheet.getRow(rowNum).getCell(1).setCellValue("컬럼명");
			sheet.getRow(rowNum).getCell(3).setCellValue("진단항목설정건수");
			sheet.getRow(rowNum).getCell(4).setCellValue("진단대상 데이터 건수");
			sheet.getRow(rowNum++).getCell(5).setCellValue("Match %");

			for (SangsMap column : columnList) {

				row = sheet.getRow(rowNum);

				if (row == null) {
					sheet.copyRows(3, 3, rowNum, styleCcp);
					row = sheet.getRow(rowNum);
				}

				if (!csvAt) {
					row.getCell(1).setCellValue(MapUtils.getString(column, "columnNm"));
				} else {
					row.getCell(1).setCellValue(MapUtils.getString(column, "columnComment"));
				}
				row.getCell(3).setCellValue(MapUtils.getIntValue(column, "trgcnt"));
				row.getCell(4).setCellValue(MapUtils.getIntValue(tableMap, "allCo"));

				row.getCell(5).setCellValue(MapUtils.getFloatValue(column, "perCo") + "%");
				cellStyle = (XSSFCellStyle)row.getCell(4).getCellStyle().clone();
				cellStyle.setAlignment(HorizontalAlignment.RIGHT);
				row.getCell(5).setCellStyle(cellStyle);

				// Row 가 1 일 경우는 셀합치기가 적용되어 복사되므로 추가로 셀합치기를 하면 오류가 남.
				// Row 가 1보다 클경우는 셀합치기가 없어진 후 복사되므로 셀합치기를 다시 한다.
				if (columnList.size() > 1) {
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));
				}

				rowNum++;
			}
			//DBMS별 Data Cnt 추가 파라미터 저장
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

			// 진단 항목별 진단대상 컬럼 정보
			Map<String, SangsMap> analsGroupMap = changeListToMap(diagnosisService.selectAnalsGroupListForReport(paramMap), "groupCode");
			List<SangsMap> analsList = diagnosisService.selectAnalsListForReport(paramMap);
			int groupCount = 0, groupCount2 = 0;

			rowNum += 3;
			sheet.copyRows(1, 3, rowNum, styleCcp);

			sheet.getRow(rowNum++).getCell(1).setCellValue("진단 항목별 진단대상 컬럼 정보");
			this.removeMergedRegion(sheet, rowNum);

			sheet.getRow(rowNum).getCell(1).setCellValue("구분");
			cellStyle = (XSSFCellStyle)sheet.getRow(rowNum).getCell(1).getCellStyle().clone();
			cellStyle.setBorderRight(BorderStyle.THIN);
			sheet.getRow(rowNum).getCell(1).setCellStyle(cellStyle);

			sheet.getRow(rowNum).getCell(2).setCellValue("그룹");
			sheet.getRow(rowNum).getCell(3).setCellValue("진단항목");
			sheet.getRow(rowNum).getCell(4).setCellValue("진단대상 컬럼 건수");
			sheet.getRow(rowNum++).getCell(5).setCellValue("Match %");

			for (SangsMap anals : analsList) {

				row = sheet.getRow(rowNum);

				if (row == null) {
					sheet.copyRows(3, 3, rowNum, styleCcp);
					row = sheet.getRow(rowNum);
				}
				this.removeMergedRegion(sheet, rowNum);

				row.getCell(1).setCellValue(MapUtils.getString(anals, "groupNm"));
				row.getCell(2).setCellValue(MapUtils.getString(anals, "pttrnSeNm"));
				row.getCell(3).setCellValue(MapUtils.getString(anals, "analsNm"));
				row.getCell(4).setCellValue(MapUtils.getIntValue(anals, "colCo"));

				row.getCell(5).setCellValue(MapUtils.getFloatValue(anals, "perCo") + "%");
				cellStyle = (XSSFCellStyle)row.getCell(4).getCellStyle().clone();
				cellStyle.setAlignment(HorizontalAlignment.RIGHT);
				row.getCell(5).setCellStyle(cellStyle);

				rowNum++;
				groupCount++;
				groupCount2++;

				if (groupCount == MapUtils.getIntValue(analsGroupMap.get(MapUtils.getString(anals, "groupCode")), "cnt")) {
					if (groupCount > 1) {
						sheet.addMergedRegion(new CellRangeAddress(rowNum - groupCount, rowNum - 1, 1, 1));
					}
					groupCount = 0;
				}

				if (!StringUtils.equals(row.getCell(2).getStringCellValue(), sheet.getRow(rowNum - 2).getCell(2).getStringCellValue())) {
					if (groupCount2 > 2) {
						sheet.addMergedRegion(new CellRangeAddress(rowNum - groupCount2, rowNum - 2, 2, 2));
					}
					groupCount2 = 1;
				}
			}

			if (groupCount2 > 1 && StringUtils.equals(sheet.getRow(rowNum - 1).getCell(2).getStringCellValue(), sheet.getRow(rowNum - 2).getCell(2).getStringCellValue())) {
				sheet.addMergedRegion(new CellRangeAddress(rowNum - groupCount2, rowNum - 1, 2, 2));
			}

			// 시트 복사
			boolean firstYn = true;

			for (SangsMap anals : analsList) {

				String groupCode = MapUtils.getString(anals, "groupCode");
				String analsNm = MapUtils.getString(anals, "analsNm");
				analsNm = analsNm.replaceAll("/", "");
				analsNm = analsNm.replaceAll("\\/", "");
				analsNm = analsNm.replaceAll("\\|", "");
				analsNm = analsNm.replaceAll("\\[", "");
				analsNm = analsNm.replaceAll("\\]", "");
				analsNm = analsNm.replaceAll("\\:", "");
				//analsNm = analsNm.replaceAll("\\", "");
//				logger.info("analsNm : "+analsNm);
				int sheetIdx = 3;

				if (StringUtils.equals(groupCode, "AG000100")) {
					sheetIdx = 3;
				} else {
					if (firstYn) {
						firstYn = false;
						workbook.cloneSheet(4, "Value Frequency");
					}

					sheetIdx = 5;
				}

				sheet = workbook.cloneSheet(sheetIdx, analsNm);
				sheet.getRow(0).getCell(1).setCellValue(analsNm);
			}

			// 기본패턴을 선택 안했을 경우 4번 시트를 복사 안하게 됨.
			if (firstYn) {
				firstYn = false;
				workbook.cloneSheet(4, "Value Frequency");
			}

			// 시트 템플릿 삭제
			workbook.removeSheetAt(5);
			workbook.removeSheetAt(4);
			workbook.removeSheetAt(3);

			// 3. 종합현황
			sheet = workbook.getSheetAt(2);

			// 분석 개요
			if (!csvAt) {
				sheet.getRow(2).getCell(2).setCellValue(MapUtils.getString(paramMap, "conIp") + ":" + MapUtils.getString(paramMap, "sId"));	// 접속 DBMS
				sheet.getRow(2).getCell(8).setCellValue(MapUtils.getString(tableMap, "dgnssNm"));		// 작업명
				sheet.getRow(3).getCell(2).setCellValue(MapUtils.getString(paramMap, "database"));		// 데이터베이스
				sheet.getRow(3).getCell(8).setCellValue(MapUtils.getString(tableMap, "tableNm"));		// 테이블
				sheet.getRow(4).getCell(2).setCellValue(MapUtils.getString(tableMap, "excBeginTime"));	// 작업 시작 시간
				sheet.getRow(4).getCell(8).setCellValue(MapUtils.getString(tableMap, "workTime"));		// 작업 수행 시간
			} else {
				sheet.getRow(2).getCell(1).setCellValue("파일명");										// 파일명 타이틀
				sheet.getRow(2).getCell(2).setCellValue(csvFileName);									// 파일명
				sheet.getRow(2).getCell(8).setCellValue(MapUtils.getString(tableMap, "dgnssNm"));		// 작업명
				sheet.getRow(3).getCell(1).setCellValue("작업 시작 시간");									// 작업 시작 시간 타이틀
				sheet.getRow(3).getCell(2).setCellValue(MapUtils.getString(tableMap, "excBeginTime"));	// 작업 시작 시간
				sheet.getRow(3).getCell(7).setCellValue("작업 수행 시간");									// 작업 수행 시간 타이틀
				sheet.getRow(3).getCell(8).setCellValue(MapUtils.getString(tableMap, "workTime"));		// 작업 수행 시간
				sheet.removeRow(sheet.getRow(4));
				this.removeMergedRegion(sheet, 4);
			}

			// 분석 결과
			List<SangsMap> columnResList;
			List<SangsMap> frqAnalList;
			String columnNm;
			int baseCount, pttnCount;

			// 진단 컬럼별 시트
			XSSFSheet analsSheet;

			// 차트
			XSSFDrawing drawing;
			XSSFClientAnchor anchor;
			XSSFChart chart;
			XDDFCategoryAxis cateAxis;
			XDDFValueAxis valAxis;
			XDDFCategoryDataSource cateDs;
			XDDFNumericalDataSource<Double> valDs;
			XDDFNumericalDataSource<Double> misValDs;
			XDDFBarChartData chartData;
			Series series;

			rowNum = 7;

			for (SangsMap column : columnList) {

				// 컬럼명
				if (!csvAt) {
					columnNm = MapUtils.getString(column, "columnNm");
				} else {
					columnNm = MapUtils.getString(column, "columnComment");
				}

				if (rowNum > 7) {
					sheet.copyRows(7, 7, rowNum, allCcp);
				}
				sheet.getRow(rowNum).createCell(2).setCellValue(columnNm);

				// 기본 분석
				baseCount = 0;

				paramMap.put("columnNm", MapUtils.getString(column, "columnNm"));
				columnResList = diagnosisService.selectDgnssColumnResList(paramMap);
				//analsList = diagnosisService.selectAnalsListToColumn(paramMap);
				
				rowNum += 2;
				if (rowNum > 9) {
					sheet.copyRows(9, 10, rowNum, allCcp);
				}

				rowNum += 2;

				for (SangsMap columnRes : columnResList) {

					if (!StringUtils.equals(MapUtils.getString(columnRes, "groupCode"), "AG000100")) {
						continue;
					}

					if (rowNum > 11) {
						sheet.copyRows(11, 11, rowNum, styleCcp);
					}
					row = sheet.getRow(rowNum);

					row.getCell(1).setCellValue(MapUtils.getString(columnRes, "analsNm"));
					row.getCell(2).setCellValue(MapUtils.getIntValue(columnRes, "mtchgCo"));
					row.getCell(4).setCellValue(MapUtils.getFloatValue(columnRes, "perCo") + "%");

					// 기본분석시트
					String analsNm = MapUtils.getString(columnRes, "analsNm");
					analsNm = analsNm.replaceAll("/", "");
					analsNm = analsNm.replaceAll("\\/", "");
					analsNm = analsNm.replaceAll("\\|", "");
					analsNm = analsNm.replaceAll("\\[", "");
					analsNm = analsNm.replaceAll("\\]", "");
					analsNm = analsNm.replaceAll("\\:", "");
					//analsNm = analsNm.replaceAll("\\", "");
					analsSheet = workbook.getSheet(analsNm);
					if (analsSheet.getLastRowNum() > 2 || StringUtils.isNotEmpty(analsSheet.getRow(2).getCell(1).getStringCellValue())) {
						analsSheet.copyRows(2, 2, analsSheet.getLastRowNum() + 1, styleCcp);
					}
					row = analsSheet.getRow(analsSheet.getLastRowNum());

					row.getCell(1).setCellValue(columnNm);
					row.getCell(2).setCellValue(MapUtils.getIntValue(columnRes, "mtchgCo"));
					row.getCell(3).setCellValue(MapUtils.getFloatValue(columnRes, "perCo") + "%");

					rowNum++;
					baseCount++;
				}

				// 기본분석 차트
				if (baseCount > 0) {
					drawing = sheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 7, rowNum - baseCount - 1, 9, rowNum);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
					valAxis = chart.createValueAxis(AxisPosition.LEFT);
					valAxis.setCrosses(AxisCrosses.AUTO_ZERO);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(rowNum - baseCount, rowNum - 1, 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(rowNum - baseCount, rowNum - 1, 2, 2));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.COL);

					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
//					for (int i = 1; i < baseCount; i++) {
//						solidFillSeries(chartData, i - 1, PresetColor.CHARTREUSE);
//					}
				} else {
					// 데이터가 없을 경우 타이틀 삭제
					if (rowNum > 11) {
						sheet.removeRow(sheet.getRow(--rowNum));
						this.removeMergedRegion(sheet, rowNum);
						sheet.removeRow(sheet.getRow(--rowNum));
						this.removeMergedRegion(sheet, rowNum);
						--rowNum;
					}
				}

				// Value Frequency
				frqAnalList = diagnosisService.selectFrqAnalList(paramMap);

				rowNum++;
				sheet.copyRows(9, 10, rowNum, allCcp);
				sheet.getRow(rowNum).getCell(1).setCellValue("* Value Frequency");

				rowNum += 2;
				for (SangsMap frqAnal : frqAnalList) {

					sheet.copyRows(11, 11, rowNum, styleCcp);
					row = sheet.getRow(rowNum);

					if (!StringUtils.isEmpty(MapUtils.getString(frqAnal, "dataValue"))) {
						row.getCell(1).setCellValue(MapUtils.getString(frqAnal, "dataValue"));
					} else {
						row.getCell(1).setCellValue("[NULL]");
					}
					row.getCell(2).setCellValue(MapUtils.getIntValue(frqAnal, "dataCo"));
					row.getCell(4).setCellValue(MapUtils.getFloatValue(frqAnal, "perCo") + "%");

					rowNum++;
				}

				// Value Frequency 차트
				if (frqAnalList.size() > 0) {
					drawing = sheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 7, rowNum - frqAnalList.size() - 1, 9, rowNum);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
					valAxis = chart.createValueAxis(AxisPosition.LEFT);
					valAxis.setCrosses(AxisCrosses.AUTO_ZERO);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(rowNum - frqAnalList.size(), rowNum - 1, 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(rowNum - frqAnalList.size(), rowNum - 1, 2, 2));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Match Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.COL);
					
					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
					
				} else {
					// 데이터가 없을 경우 타이틀 삭제
					sheet.removeRow(sheet.getRow(--rowNum));
					this.removeMergedRegion(sheet, rowNum);
					sheet.removeRow(sheet.getRow(--rowNum));
					this.removeMergedRegion(sheet, rowNum);
					--rowNum;
				}

				// Value Frequency 시트
				analsSheet = workbook.getSheet("Value Frequency");

				if (frqAnalList.size() > 0) {
					if (analsSheet.getLastRowNum() > 2) {
						analsSheet.copyRows(0, 1, analsSheet.getLastRowNum() + 2, allCcp);
						analsSheet.getRow(analsSheet.getLastRowNum() - 1).getCell(1).setCellValue(columnNm);
					} else {
						analsSheet.getRow(0).getCell(1).setCellValue(columnNm);
					}
				}

				for (SangsMap frqAnal : frqAnalList) {

					if (analsSheet.getLastRowNum() > 2 || StringUtils.isNotEmpty(analsSheet.getRow(2).getCell(1).getStringCellValue())) {
						analsSheet.copyRows(2, 2, analsSheet.getLastRowNum() + 1, styleCcp);
					}
					row = analsSheet.getRow(analsSheet.getLastRowNum());

					if (!StringUtils.isEmpty(MapUtils.getString(frqAnal, "dataValue"))) {
						row.getCell(1).setCellValue(MapUtils.getString(frqAnal, "dataValue"));
					} else {
						row.getCell(1).setCellValue("[NULL]");
					}
					row.getCell(2).setCellValue(MapUtils.getIntValue(frqAnal, "dataCo"));
					row.getCell(3).setCellValue(MapUtils.getFloatValue(frqAnal, "perCo") + "%");
				}

				// Value Frequency 시트 차트
				if (frqAnalList.size() > 0) {
					drawing = analsSheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 5, analsSheet.getLastRowNum() - frqAnalList.size(), 12, analsSheet.getLastRowNum() + 1);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
					valAxis = chart.createValueAxis(AxisPosition.LEFT);
					valAxis.setCrosses(AxisCrosses.AUTO_ZERO);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(analsSheet, new CellRangeAddress(analsSheet.getLastRowNum() - frqAnalList.size() + 1, analsSheet.getLastRowNum(), 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(analsSheet, new CellRangeAddress(analsSheet.getLastRowNum() - frqAnalList.size() + 1, analsSheet.getLastRowNum(), 2, 2));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Match Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.COL);
					
					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
				}

				// 기본패턴
				pttnCount = 0;

				rowNum++;
				sheet.copyRows(9, 10, rowNum, allCcp);
				row = sheet.getRow(rowNum);
				row.getCell(1).setCellValue("* 기본패턴");
				// 색상별 범주 표시
				row.createCell(2).setCellValue("Match");
				row.createCell(3);
				row.getCell(3).setCellStyle(cellStyle1);
				row.createCell(4).setCellValue("misMatch");
				row.createCell(5);
				row.getCell(5).setCellStyle(cellStyle0);
				
				rowNum++;
				this.removeMergedRegion(sheet, rowNum);
				row = sheet.getRow(rowNum);

				row.getCell(2).setCellValue("Match Count");
				cellStyle = (XSSFCellStyle)row.getCell(2).getCellStyle().clone();
				cellStyle.setBorderRight(BorderStyle.THIN);
				row.getCell(2).setCellStyle(cellStyle);
				row.getCell(3).setCellValue("Match %");
				row.getCell(4).setCellValue("misMatch Count");
				cellStyle = (XSSFCellStyle)row.getCell(4).getCellStyle().clone();
				cellStyle.setBorderRight(BorderStyle.THIN);
				row.getCell(4).setCellStyle(cellStyle);
				row.getCell(5).setCellValue("misMatch %");

				rowNum++;
				// 항목별 진단결과 설정
				for (SangsMap columnRes : columnResList) {
					// 기본패턴, 사용자정의패턴
					if (!StringUtils.equals(MapUtils.getString(columnRes, "groupCode"), "AG000300")
							&& !StringUtils.equals(MapUtils.getString(columnRes, "groupCode"), "AG000401")
							) {
						continue;
					}

					sheet.copyRows(11, 11, rowNum, styleCcp);
					this.removeMergedRegion(sheet, rowNum);
					row = sheet.getRow(rowNum);

					row.getCell(1).setCellValue(MapUtils.getString(columnRes, "analsNm"));
					row.getCell(2).setCellValue(MapUtils.getIntValue(columnRes, "mtchgCo"));
					cellStyle = (XSSFCellStyle)row.getCell(2).getCellStyle().clone();
					cellStyle.setBorderRight(BorderStyle.THIN);
					row.getCell(2).setCellStyle(cellStyle);
					row.getCell(3).setCellValue(MapUtils.getFloatValue(columnRes, "perCo") + "%");
					row.getCell(4).setCellValue(MapUtils.getIntValue(columnRes, "miscnt"));
					cellStyle = (XSSFCellStyle)row.getCell(4).getCellStyle().clone();
					cellStyle.setBorderRight(BorderStyle.THIN);
					row.getCell(4).setCellStyle(cellStyle);
					row.getCell(5).setCellValue(MapUtils.getFloatValue(columnRes, "misperCo") + "%");
					
					
					// 기본패턴시트
					String analsNm = MapUtils.getString(columnRes, "analsNm");
					analsNm = analsNm.replaceAll("/", "");
					analsNm = analsNm.replaceAll("\\/", "");
					analsNm = analsNm.replaceAll("\\|", "");
					analsNm = analsNm.replaceAll("\\[", "");
					analsNm = analsNm.replaceAll("\\]", "");
					analsNm = analsNm.replaceAll("\\:", "");
					//analsNm = analsNm.replaceAll("\\", "");
					analsSheet = workbook.getSheet(analsNm);
					// 색상별 범주 표시
					analsSheet.getRow(0).createCell(11).setCellValue("Match");
					analsSheet.getRow(0).createCell(12);
					analsSheet.getRow(0).getCell(12).setCellStyle(cellStyle1);
					analsSheet.getRow(0).createCell(13).setCellValue("misMatch");
					analsSheet.getRow(0).createCell(14);
					analsSheet.getRow(0).getCell(14).setCellStyle(cellStyle0);
					
					if (analsSheet.getLastRowNum() > 2 || StringUtils.isNotEmpty(analsSheet.getRow(2).getCell(1).getStringCellValue())) {
						analsSheet.copyRows(2, 2, analsSheet.getLastRowNum() + 1, styleCcp);
					}
					row = analsSheet.getRow(analsSheet.getLastRowNum());

					row.getCell(1).setCellValue(columnNm);
					row.getCell(2).setCellValue(MapUtils.getIntValue(columnRes, "mtchgCo"));
					row.getCell(3).setCellValue(MapUtils.getFloatValue(columnRes, "perCo") + "%");
					row.getCell(4).setCellValue(MapUtils.getIntValue(columnRes, "miscnt"));
					row.getCell(5).setCellValue(MapUtils.getFloatValue(columnRes, "misperCo") + "%");

					rowNum++;
					pttnCount++;
				}

				// 기본패턴 차트
				if (pttnCount > 0) {
					drawing = sheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 7, rowNum - pttnCount - 1, 9, rowNum);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.LEFT);
					valAxis = chart.createValueAxis(AxisPosition.BOTTOM);
					valAxis.setCrosses(AxisCrosses.MAX);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(rowNum - pttnCount, rowNum - 1, 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(rowNum - pttnCount, rowNum - 1, 2, 2));
					misValDs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(rowNum - pttnCount, rowNum - 1, 4, 4));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Match Count", null);
					series = chartData.addSeries(cateDs, misValDs);
					series.setTitle("misMatch Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.BAR);
					chartData.getCategoryAxis().setOrientation(AxisOrientation.MAX_MIN);
					
					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
				} else {
					// 데이터가 없을 경우 타이틀 삭제
					sheet.removeRow(sheet.getRow(--rowNum));
					sheet.removeRow(sheet.getRow(--rowNum));
					--rowNum;
				}

				rowNum++;
			}

			// 진단 항목별 시트 차트
			for (SangsMap anals : analsList) {

				String groupCode = MapUtils.getString(anals, "groupCode");
				String analsNm = MapUtils.getString(anals, "analsNm");
				analsNm = analsNm.replaceAll("/", "");
				analsNm = analsNm.replaceAll("\\/", "");
				analsNm = analsNm.replaceAll("\\|", "");
				analsNm = analsNm.replaceAll("\\[", "");
				analsNm = analsNm.replaceAll("\\]", "");
				analsNm = analsNm.replaceAll("\\:", "");
				//analsNm = analsNm.replaceAll("\\", "");

				analsSheet = workbook.getSheet(analsNm);
				//chartData = null;
				if (StringUtils.equals(groupCode, "AG000100")) { // 기본분석

					drawing = analsSheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 5, 1, 13, analsSheet.getLastRowNum() + 1);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.LEFT);
					valAxis = chart.createValueAxis(AxisPosition.BOTTOM);
					valAxis.setCrosses(AxisCrosses.MAX);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 2, 2));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.BAR);
					chartData.getCategoryAxis().setOrientation(AxisOrientation.MAX_MIN);
					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
				} else if (StringUtils.equals(groupCode, "AG000300")) { // 기본패턴

					drawing = analsSheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 7, 1, 15, analsSheet.getLastRowNum() + 1);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.LEFT);
					valAxis = chart.createValueAxis(AxisPosition.BOTTOM);
					valAxis.setCrosses(AxisCrosses.MAX);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 2, 2));
					misValDs = XDDFDataSourcesFactory.fromNumericCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 4, 4));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Match Count", null);
					series = chartData.addSeries(cateDs, misValDs);
					series.setTitle("misMatch Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.BAR);
					chartData.getCategoryAxis().setOrientation(AxisOrientation.MAX_MIN);
					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
				} else { // 사용자 정의 패턴

					drawing = analsSheet.createDrawingPatriarch();
					anchor = drawing.createAnchor(0, 0, 0, 0, 7, 1, 15, analsSheet.getLastRowNum() + 1);
					chart = drawing.createChart(anchor);
					cateAxis = chart.createCategoryAxis(AxisPosition.LEFT);
					valAxis = chart.createValueAxis(AxisPosition.BOTTOM);
					valAxis.setCrosses(AxisCrosses.MAX);
					valAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
					cateDs = XDDFDataSourcesFactory.fromStringCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 1, 1));
					valDs = XDDFDataSourcesFactory.fromNumericCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 2, 2));
					misValDs = XDDFDataSourcesFactory.fromNumericCellRange(analsSheet, new CellRangeAddress(2, analsSheet.getLastRowNum(), 4, 4));
					chartData = (XDDFBarChartData)chart.createData(ChartTypes.BAR, cateAxis, valAxis);
					series = chartData.addSeries(cateDs, valDs);
					series.setTitle("Count", null);
					chart.plot(chartData);
					chartData.setBarDirection(BarDirection.BAR);
					chartData.getCategoryAxis().setOrientation(AxisOrientation.MAX_MIN);
					solidFillSeries(chartData, 0, PresetColor.DEEP_SKY_BLUE);
				}
			}

			// 종합현황의 첫 기본분석 데이터가 없으면 row를 삭제한다. (Template 삭제)
			sheet = workbook.getSheetAt(2);
			if (StringUtils.isEmpty(sheet.getRow(11).getCell(1).getStringCellValue())) {
				sheet.removeRow(sheet.getRow(11));
				this.removeMergedRegion(sheet, 11);
				sheet.removeRow(sheet.getRow(10));
				this.removeMergedRegion(sheet, 10);
				sheet.removeRow(sheet.getRow(9));
			}

			// Value Frequency 시트의 데이터가 없으면 시트를 삭제한다.
			analsSheet = workbook.getSheet("Value Frequency");
			if (analsSheet.getLastRowNum() == 2) {
				workbook.removeSheetAt(workbook.getSheetIndex(analsSheet));
			}

			// 엑셀 작업 완료

			// 파일 전송
			String fileNameDate = fileName + "_" + MapUtils.getString(tableMap, "tableNm") + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + ".xlsx";
			String fileNameEnc = URLEncoder.encode(fileNameDate, "UTF-8").replace("+", "%20");
			String client = request.getHeader("User-Agent");
			response.setContentType("application/vnd.ms-excel");
			if (client.indexOf("MSIE 5.5") != -1) {
				response.setHeader("Content-Type", "doesn/matter; charset=utf-8");
				response.setHeader("Content-Disposition", "filename=\"" + fileNameEnc+ "\";");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
			}
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			workbook.write(response.getOutputStream());
		}
	}

	/**
	 * @param session
	 * @param request
	 * @param response
	 * @param paramMap
	 * @throws IOException
	 */
	@RequestMapping("/mngr/diagnosis/result/download/notMatch.do")
	public void downloadNotMatch(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap)  throws Exception, IOException {

		if (StringUtils.equals(MapUtils.getString(paramMap, "fileType"), "xlsx")) {
			downloadNotMatchXlsx(session, request, response, paramMap);
		} else {
			downloadNotMatchCSV(session, request, response, paramMap);
		}
	}

	/**
	 * @param session
	 * @param request
	 * @param response
	 * @param paramMap
	 * @throws IOException
	 */
	public void downloadNotMatchCSV(HttpSession session, HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramMap)  throws Exception, IOException {
		// 세션값에서 기관정보 , 진단대상데이터베이스정보 조회 처리
		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		String insttCode = SangsUtil.getInsttCode();
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		paramMap.put("limit", reportListCnt);
		paramMap.put("insttCode", insttCode);
		// 진단대상 DBMS 정보 조회
		Map<String, String> data = new HashMap();
		data.put("dgnssDbmsId", MapUtils.getString(paramMap, "dgnssDbmsId"));
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);

		String newDdId = MapUtils.getString(paramMap, "dgnssDbmsId");
		
		if (!StringUtils.equals(dgnssDbmsId, newDdId)) {
			Map<String, String> params = new HashMap<>();
			params.put("dgnssDbmsId", newDdId);
			try {
				// 진단대상 DBMS 설정 >> 다른 방식으로 처리 해야 할 듯 한데...
				basicInfoService.setDbmsUrl(params, request, response);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		
		// CSV 파일 정보인경우 코멘트 정보를 컬럼명으로 처리 하기 위해
		boolean csvAt = false;
		if(paramMap.get("dbmsKnd").equals("CSV")){
			paramMap.put("tableName", paramMap.get("tableNm"));
			paramMap.put("columnName", paramMap.get("columnNm"));
			paramMap.put("columnComment", diagnosisService.selectColumnComment(paramMap));
			csvAt = true;
		}
		// 분석 결과 리스트
		List<SangsMap> analsList = diagnosisService.selectResAnalsList(paramMap);
		// PK 리스트
		List<String> pkList = diagnosisService.selectPKList(paramMap);
		paramMap.put("pkList", pkList);
		// 분석 대상 데이터를 조회
		List<Map<String, Object>> pattenList = diagnosisService.selectDownLoadList(paramMap);
		
		Path dir = Paths.get(dataDir);

		if (Files.notExists(dir)) {
			Files.createDirectories(dir);
		}

		Path file = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;

		int fileCnt = 1;
		int lineCnt = 1;

		try {
			// 파일명 설정
			if(paramMap.get("matchMm").equals("mis")) {
				file = dir.resolve("불일치정보_" + this.getColumnNm(paramMap, csvAt) + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
				
			}else {
				file = dir.resolve("일치정보_" + this.getColumnNm(paramMap, csvAt) + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
				
			}
			bw = Files.newBufferedWriter(file, Charset.forName("EUC-KR"));
			pw = new PrintWriter(bw);
			
			// PK 정보가 있을때
			// csv 와 dbms의 출력 되는 컬럼정보가 달라야하므로 dbmsKnd 체크
			if(paramMap.get("dbmsKnd").equals("CSV")) {
				pw.print("FILE_NAME,COLUMN_NAME,VALUE,");
			}else {
				pw.print("TABLE_NAME,COLUMN_NAME,VALUE,");
			}
			
			if (pkList.size() > 0) {
				for (String pk : pkList) {
					pw.print(pk);
					pw.print(",");
				}
			} else {
				// PK 정보가 없을때
				pw.print("INDEX,");
			}
			pw.println("패턴/지표명");
			
			for (SangsMap analsMap : analsList) {

				String analsTy = MapUtils.getString(analsMap, "analsTy");
				String analsSe = MapUtils.getString(analsMap, "analsSe");
				String analsNm = MapUtils.getString(analsMap, "analsNm");
				List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();

				if (StringUtils.equals(analsTy, "AT000200")) { // 패턴
					
					if(paramMap.get("dbmsKnd").equals("MSSQL")){
						String analsFrmla = (String)analsMap.get("analsFrmla");
						String chkPattern = analsFrmla;
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
					}else {
					
						// 패턴 불일치 리스티
						if(StringUtils.equals(analsNm, "IP(V6)")) {
							// 오라클이고 IP(V6)일때 JAVA 로 처리 필요.....
							String chkPattern = "^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$";
							paramMap.put("analsFrmla", chkPattern);
							valueList = diagnosisService.pattenCheckCnt(valueList, pattenList, paramMap,reportListCnt);
							
						}else if(StringUtils.equals(analsNm, "주소")) {
							// 숫자만 있는 컬럼일 경우 JAVA에서 처리 함.
							int cnt = diagnosisService.selectNumberCount(paramMap);
							if(cnt > 0) {
								//String chkPattern = analsMap.get("analsFrmla").toString();
								paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
								valueList = diagnosisService.pattenCheckCnt(valueList, pattenList, paramMap,reportListCnt);
							}else {
								paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
								valueList = diagnosisService.selectPatternNotMatchList(paramMap);
							}
						}else {
							paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
							valueList = diagnosisService.selectPatternNotMatchList(paramMap);
						}
					}
				} else if (StringUtils.equals(analsTy, "AT000300")) { // 범주
					// 범주 불일치 리스트
					paramMap.put("beginValue", StringUtils.defaultString(MapUtils.getString(analsMap, "beginValue"), ""));
					paramMap.put("endValue", StringUtils.defaultString(MapUtils.getString(analsMap, "endValue"), ""));
					paramMap.put("analsSe", analsSe);
					String matchNm = paramMap.get("matchMm").toString();
					
					if(matchNm.equalsIgnoreCase("m")) {
						valueList = diagnosisService.selectRangeMatchList(paramMap);
					} else {
						valueList = diagnosisService.selectRangeNotMatchList(paramMap);
					}

				} else {
					if(StringUtils.equals(analsSe, "AG000402")) { // SQL
						Map<String, String> params = new HashMap<>();
						String  analsFrmla = (String)analsMap.get("analsFrmla");
						String  tableNm = (String)paramMap.get("tableNm");
						String  columnNm = (String)paramMap.get("columnNm");
						analsFrmla = StringUtils.replace(analsFrmla, "COUNT(1)", columnNm);
						params.put("analsFrmla", analsFrmla);
						paramMap.put("analsFrmla", params.get("analsFrmla"));
						
						valueList = diagnosisService.selectSqlNotMatchList(paramMap);
					}else {
						valueList = new ArrayList<>();
					}
				}
				
				if(valueList.size() > 0 ) {

					for (Map<String, Object> valueMap : valueList) {
	
						if(paramMap.get("dbmsKnd").equals("CSV")){
							pw.print(paramMap.get("csvFileName"));
						}else {
							pw.print(paramMap.get("tableNm"));
						}
						pw.print(",");
						pw.print(this.getColumnNm(paramMap, csvAt));
						pw.print(",");
						if(valueMap != null) {
							if(valueMap.get(paramMap.get("columnNm")) != null && paramMap.get("columnNm") != null) {
								String strVal = ""+valueMap.get(paramMap.get("columnNm"));
								// " 시작시 CSV 파일 엑셀에서 깨짐 방지
								if(strVal != null) {
									if(strVal.indexOf("\"") == 0) {
										strVal = "\"\""+strVal+"\"";
									}
								}
								pw.print(strVal);
								
							}else {
								pw.print("null");
							}
						}else {
							pw.print("null");
						}
						pw.print(",");
						if (pkList.size() > 0) {
							for (String pk : pkList) {
								pw.print(valueMap.get(pk));
								pw.print(",");
							}
						} else {
							pw.print(lineCnt++);
							pw.print(",");
						}
						pw.println(analsMap.get("analsNm"));
	
//						if (lineCnt % 5000 == 0) {
//	
//							IOUtils.closeQuietly(pw);
//							IOUtils.closeQuietly(bw);
//	
//							if(paramMap.get("matchMm").equals("mis")) {
//								file = dir.resolve("불일치정보_" + paramMap.get("columnNm") + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
//								
//							}else {
//								file = dir.resolve("일치정보_" + paramMap.get("columnNm") + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
//								
//							}
//							bw = Files.newBufferedWriter(file, Charset.forName("EUC-KR"));
//							pw = new PrintWriter(bw);
//						}
					}
				}
			}

		} finally {
			IOUtils.closeQuietly(pw);
			IOUtils.closeQuietly(bw);
		}

		String userAgent = request.getHeader("User-Agent");
		String fileNameEnc;

		if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident") || StringUtils.contains(userAgent, "Edge")) {
			// MS IE
			fileNameEnc = URLEncoder.encode(file.getFileName().toString(), "UTF-8").replace("+", "%20");
		} else {
			// Mozilla, Opera
			fileNameEnc = new String(file.getFileName().toString().getBytes("UTF-8"), "8859_1");
		}

		//response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		//response.setContentLength((int)Files.size(file));
		//response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
		//response.setHeader("Content-Transfer-Encoding", "binary");
		
		String client = request.getHeader("User-Agent");
		//response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		//response.setContentType("application/x-msdownload;");
		response.setContentType("application/vnd.ms-excel");
//		if (client.indexOf("MSIE 5.5") != -1) {
//			response.setHeader("Content-Type", "doesn/matter; charset=utf-8");
//			response.setHeader("Content-Disposition", "filename=\"" + fileNameEnc+ "\";");
//		} else {
//			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
//		}
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		OutputStream os = response.getOutputStream();

		try (InputStream is = Files.newInputStream(file)) {
			StreamUtils.copy(is, os);
		}
	}

	/**
	 * @param session
	 * @param request
	 * @param response
	 * @param paramMap
	 * @throws IOException
	 */
	public void downloadNotMatchXlsx(HttpSession session, HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramMap)  throws Exception, IOException {
		// 세션값에서 기관정보 , 진단대상데이터베이스정보 조회 처리
		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		String insttCode = SangsUtil.getInsttCode();
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		paramMap.put("limit", reportListCnt);
		paramMap.put("insttCode", insttCode);
		paramMap.put("matchMm", request.getParameter("matchMm"));
		
		Map<String, String> data = new HashMap();
		data.put("dgnssDbmsId", MapUtils.getString(paramMap, "dgnssDbmsId"));
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);

		String newDdId = MapUtils.getString(paramMap, "dgnssDbmsId");

		if (!StringUtils.equals(dgnssDbmsId, newDdId)) {
			Map<String, String> params = new HashMap<>();
			params.put("dgnssDbmsId", newDdId);
			try {
				basicInfoService.setDbmsUrl(params, request, response);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}


		boolean csvAt = false;
		if(paramMap.get("dbmsKnd").equals("CSV")){
			paramMap.put("tableName", paramMap.get("tableNm"));
			paramMap.put("columnName", paramMap.get("columnNm"));
			paramMap.put("columnComment", diagnosisService.selectColumnComment(paramMap));
			csvAt = true;
		}

		List<SangsMap> analsList = diagnosisService.selectResAnalsList(paramMap);
		List<String> pkList = diagnosisService.selectPKList(paramMap);
		paramMap.put("pkList", pkList);
		
		// 분석 대상 데이터를 조회
		List<Map<String, Object>> pattenList = diagnosisService.selectDownLoadList(paramMap);
				
		Path dir = Paths.get(dataDir);
		logger.info("dir : "+dir);

		if (Files.notExists(dir)) {
			Files.createDirectories(dir);
		}
				
		Path file;
		if(paramMap.get("matchMm").equals("mis")) {
			 file = dir.resolve("불일치정보_" + this.getColumnNm(paramMap, csvAt) + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + ".xlsx");			
		}else {
			 file = dir.resolve("일치정보_" + this.getColumnNm(paramMap, csvAt) + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + ".xlsx");
		}
		logger.info("file : "+file);
		SXSSFWorkbook workbook = null;
		//Workbook workbook = null;
		//HSSFWorkbook workbook = null;
		try {

			workbook = new SXSSFWorkbook(1000);
			workbook.setCompressTempFiles(true);
			//workbook = new XSSFWorkbook();
			//workbook = new HSSFWorkbook();
			Sheet sheet;
			Row row;
			Cell cell;
			CellStyle titleStyle = workbook.createCellStyle();
			CellStyle cellStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			int lineCnt = 1;

			titleFont.setBold(true);

			titleStyle.setBorderTop(BorderStyle.THIN);
			titleStyle.setBorderLeft(BorderStyle.THIN);
			titleStyle.setBorderRight(BorderStyle.THIN);
			titleStyle.setBorderBottom(BorderStyle.THIN);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			titleStyle.setFont(titleFont);

			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			for (SangsMap analsMap : analsList) {

				int r = 0, c = 0;
				//
				String repAnalsNm = MapUtils.getString(analsMap, "analsNm");
				//repAnalsNm = repAnalsNm.replaceAll("/", "");
				repAnalsNm = repAnalsNm.replaceAll("\\/", "");
				repAnalsNm = repAnalsNm.replaceAll("\\|", "");
				repAnalsNm = repAnalsNm.replaceAll("\\[", "");
				repAnalsNm = repAnalsNm.replaceAll("\\]", "");
				repAnalsNm = repAnalsNm.replaceAll("\\:", "");
				//repAnalsNm = repAnalsNm.replaceAll("\", "");
//				logger.info("repAnalsNm : "+repAnalsNm);
				sheet = workbook.createSheet(repAnalsNm);
				row = sheet.createRow(r++);

				cell = row.createCell(c++);
				cell.setCellStyle(titleStyle);
				
				//CSV : DBMS 다운로드 시 엑셀 컬럼 명칭 변경
				if(paramMap.get("dbmsKnd").equals("CSV")) {
					cell.setCellValue("FILE_NAME");
				}else {
					cell.setCellValue("TABLE_NAME");
				}
				

				cell = row.createCell(c++);
				cell.setCellStyle(titleStyle);
				cell.setCellValue("COLUMN_NAME");

				cell = row.createCell(c++);
				cell.setCellStyle(titleStyle);
				cell.setCellValue("VALUE");

				if (pkList.size() > 0) {
					for (String pk : pkList) {
						cell = row.createCell(c++);
						cell.setCellStyle(titleStyle);
						cell.setCellValue(pk);
					}
				} else {
					cell = row.createCell(c++);
					cell.setCellStyle(titleStyle);
					cell.setCellValue("INDEX");
				}
				cell = row.createCell(c++);
				cell.setCellStyle(titleStyle);
				cell.setCellValue("패턴/지표명");

				String analsTy = MapUtils.getString(analsMap, "analsTy");
				String analsSe = MapUtils.getString(analsMap, "analsSe");
				String analsNm = MapUtils.getString(analsMap, "analsNm");
				List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();

				if (StringUtils.equals(analsTy, "AT000200")) { // 패턴
					if(paramMap.get("dbmsKnd").equals("MSSQL")){
						String analsFrmla = (String)analsMap.get("analsFrmla");
						String chkPattern = analsFrmla;
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
								if(key.equals(paramMap.get("columnNm"))  && map.get(key) != null) {
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
							
							// 다운로드 리스트 제한 하기 위해 처리
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
					}else {
						if(StringUtils.equals(analsNm, "IP(V6)")) {
							// 오라클이고 IP(V6)일때 JAVA 로 처리 필요.....
							String chkPattern = "^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$";
							paramMap.put("analsFrmla", chkPattern);
							valueList = diagnosisService.pattenCheckCnt(valueList, pattenList, paramMap,reportListCnt);
						}else if(StringUtils.equals(analsNm, "주소")) {
							// 숫자만 있는 컬럼일 경우 JAVA에서 처리 함.
							int cnt = diagnosisService.selectNumberCount(paramMap);
							if(cnt > 0) {
								//String chkPattern = analsMap.get("analsFrmla").toString();
								paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
								valueList = diagnosisService.pattenCheckCnt(valueList, pattenList, paramMap,reportListCnt);
							}else {
								paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
								valueList = diagnosisService.selectPatternNotMatchList(paramMap);
							}
						}else {
							paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
							valueList = diagnosisService.selectPatternNotMatchList(paramMap);
						}
					}

				} else if (StringUtils.equals(analsTy, "AT000300")) { // 범주
					paramMap.put("beginValue", StringUtils.defaultString(MapUtils.getString(analsMap, "beginValue"), ""));
					paramMap.put("endValue", StringUtils.defaultString(MapUtils.getString(analsMap, "endValue"), ""));
					paramMap.put("analsSe", analsSe);
					String matchNm = paramMap.get("matchMm").toString();
					String dbmsKnd = paramMap.get("dbmsKnd").toString();
					List<Object> columnType = diagnosisService.selectColumnType(paramMap);
					String columnTy = columnType.get(0).toString();
					
					if(analsSe.equalsIgnoreCase("AG000601")) {
						if(columnTy.equalsIgnoreCase("DATE")) {
							paramMap.put("beginValue", "");
							paramMap.put("endValue", "");
						}
					}
					
					if(analsSe.equalsIgnoreCase("AG000603")) {
						if(!columnTy.equalsIgnoreCase("DATE")) {
							paramMap.put("beginValue", "");
							paramMap.put("endValue", "");
							paramMap.put("analsSe", "AG000601");
						}
					}
					
					if(matchNm.equalsIgnoreCase("m")) {
						valueList = diagnosisService.selectRangeMatchList(paramMap);
					} else {
						valueList = diagnosisService.selectRangeNotMatchList(paramMap);
					}
					
				} else {
					if(StringUtils.equals(analsSe, "AG000402")) { // SQL
						Map<String, String> params = new HashMap<>();
						String  analsFrmla = (String)analsMap.get("analsFrmla");
						String  tableNm = (String)paramMap.get("tableNm");
						String  columnNm = (String)paramMap.get("columnNm");
						analsFrmla = StringUtils.replace(analsFrmla, "COUNT(1)", columnNm);
						params.put("analsFrmla", analsFrmla);
						paramMap.put("analsFrmla", params.get("analsFrmla"));
						
						valueList = diagnosisService.selectSqlNotMatchList(paramMap);
					}else {
						valueList = new ArrayList<>();
					}
				}

				for (Map<String, Object> valueMap : valueList) {

					row = sheet.createRow(r++);
					c = 0;

					cell = row.createCell(c++);
					cell.setCellStyle(cellStyle);
					
					// csv 분석일 시 파일명으로 
					// dbms 분석일 시 테이블명으로
					if(paramMap.get("dbmsKnd").equals("CSV")){
						cell.setCellValue(MapUtils.getString(paramMap, "csvFileName"));
					}else {
						cell.setCellValue(MapUtils.getString(paramMap, "tableNm"));
					}

					cell = row.createCell(c++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(this.getColumnNm(paramMap, csvAt));

					cell = row.createCell(c++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(MapUtils.getString(valueMap, MapUtils.getString(paramMap, "columnNm")));
					
					if (pkList.size() > 0) {
						for (String pk : pkList) {
							cell = row.createCell(c++);
							cell.setCellStyle(cellStyle);
							cell.setCellValue(MapUtils.getString(valueMap, pk));
						}
					} else {
						cell = row.createCell(c++);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(lineCnt++);
					}
					cell = row.createCell(c++);
					cell.setCellStyle(cellStyle);
//					String repAnalsNm = MapUtils.getString(analsMap, "analsNm");
//					repAnalsNm = repAnalsNm.replaceAll("/", "");
//					repAnalsNm = repAnalsNm.replaceAll("\\|", "");
//					repAnalsNm = repAnalsNm.replaceAll("\\[", "");
//					repAnalsNm = repAnalsNm.replaceAll("\\]", "");
//					repAnalsNm = repAnalsNm.replaceAll("\\:", "");
					cell.setCellValue(repAnalsNm);
				}

				for (int i = 0; i < row.getLastCellNum(); i++) {
//					sheet.autoSizeColumn(i); // 에러남
					sheet.setColumnWidth(i, 3500);
				}
			}

			String userAgent = request.getHeader("User-Agent");
			String fileNameEnc;
			if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident") || StringUtils.contains(userAgent, "Edge")) {
				// MS IE
				fileNameEnc = URLEncoder.encode(file.getFileName().toString(), "UTF-8").replace("+", "%20");
			} else {
				// Mozilla, Opera
				fileNameEnc = new String(file.getFileName().toString().getBytes("UTF-8"), "8859_1");
			}
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			workbook.dispose();
            workbook.close();
            os.flush();          
            os.close();
            
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.close();
				IOUtils.closeQuietly(workbook);
			}
		}

	}
	
	@RequestMapping("/mngr/diagnosis/result/download/fileDownload.do")
	public void fileDownload( HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		logger.info("========================= fileDownload =================");
	    String path = paramMap.get("filePath").toString(); //full경로
	    String fileName = paramMap.get("fileName").toString(); //파일명
		//String path = "D:/upload/테스트.txt";
		//String fileName = "테스트.txt";
	    File file = new File(path);
	 
	    FileInputStream fileInputStream = null;
	    ServletOutputStream servletOutputStream = null;
	    try{
	        String downName = null;
	        String browser = request.getHeader("User-Agent");
	        //파일 인코딩
	        if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Edge") ){//브라우저 확인 파일명 encode  
	            downName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
	        }else{
	            downName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	        }
	         
	        response.setHeader("Content-Disposition","attachment;filename=\"" + downName+"\"");             
	        response.setContentType("application/octer-stream");
	        response.setHeader("Content-Transfer-Encoding", "binary;");
	 
	        fileInputStream = new FileInputStream(file);
	        servletOutputStream = response.getOutputStream();
	 
	        byte b [] = new byte[1024];
	        int data = 0;
	 
	        while((data=(fileInputStream.read(b, 0, b.length))) != -1){
	             
	            servletOutputStream.write(b, 0, data);
	             
	        }
	 
	        servletOutputStream.flush();//출력
	        logger.info("========================= fileDownload end ================="); 
	    }catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(servletOutputStream!=null){
	            try{
	                servletOutputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	        if(fileInputStream!=null){
	            try{
	                fileInputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	    }
	}

	/**
	 * @param model
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/saveForm/list.do")
	public String saveFormList(Model model, @RequestParam Map<String, Object> paramMap) {
		return "mngr/diagnosis/saveForm/list";
	}

	/**
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/saveForm/listAjax.do")
	@ResponseBody
	public Map<String, Object> saveFormListAjax(@RequestParam Map<String, Object> paramMap) {
		paramMap.put("insttCode", SangsUtil.getInsttCode());
		return diagnosisService.selectDgnssSaveList(paramMap);
	}

	/**
	 * @param params
	 * @param res
	 * @param session
	 * @param request
	 * @param columnNameList
	 * @throws Exception
	 */
	@RequestMapping("/mngr/diagnosis/saveForm/insert.do")
	public void saveFormInsert(@RequestParam Map<String, String> params, HttpServletResponse res, HttpSession session, HttpServletRequest request
			                   , @RequestParam(name = "columnName") String[] columnNameList) throws Exception { 
		
		String dgnssSaveNm = null;
		String schemaName = null;
		String tableName = null;
		
		dgnssSaveNm = params.get("dgnssSaveNm");
		schemaName = params.get("schemaName");
		tableName = params.get("tableName");
		
		try {
			
		
			Map<String, String[]> rMap = request.getParameterMap();

			if (columnNameList == null || columnNameList.length == 0) {
				return;
			}

			String dateFmt = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
			Map<String, Object> paramMap = new ConcurrentHashMap<>();

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

			paramMap.put("insttCode", insttCode);
			paramMap.put("dgnssSaveNm", dgnssSaveNm);
			paramMap.put("dgnssDbmsId", dgnssDbmsId);
			paramMap.put("dgnssSaveId", dateFmt);
			paramMap.put("schemaNm", schemaName);
			paramMap.put("tableNm", tableName);

			Map<String, String> data = new HashMap();
			
			
			data.put("dgnssDbmsId", dgnssDbmsId);
			data = ruleMngService.selectDgnssDbmsInfo(data);
			paramMap.putAll(data);

			StringBuilder columnNmDc = new StringBuilder();
			StringBuilder analsIdDc = new StringBuilder();
			String[] analsIdList;

			for (String columnName : columnNameList) {

				columnNmDc.append(columnName);
				columnNmDc.append(",");

				analsIdList = rMap.get(columnName);

				if (analsIdList == null) {
					continue;
				}

				analsIdDc.append("\"" + columnName + "\"");
				analsIdDc.append(":[");
				for (String analsId : analsIdList) {
					analsIdDc.append("\"" + analsId + "\"");
					analsIdDc.append(",");
				}
				if (analsIdList.length > 0) {
					analsIdDc.setLength(analsIdDc.length() - 1);
				}
				analsIdDc.append("],");
			}
			if (columnNameList.length > 0) {
				columnNmDc.setLength(columnNmDc.length() - 1);
			}
			if (analsIdDc.length() > 0) {
				analsIdDc.setLength(analsIdDc.length() - 1);
			}

			paramMap.put("columnNmDc", columnNmDc.toString());
			paramMap.put("analsIdDc", analsIdDc.toString());

			diagnosisService.insertDgnssSave(paramMap);
									  
								
							 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Map<String, Object> paramMap = new HashMap();
			diagnosisService.insertDgnssError(paramMap,e);
		}					 

	}

	/**
	 * @param session
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/saveForm/delete.do")
	public String saveFormDelete(HttpSession session, @RequestParam Map<String, Object> paramMap) {

		paramMap.put("insttCode", SangsUtil.getInsttCode());
		diagnosisService.deleteDgnssSave(paramMap);

		return "redirect:list.do";
	}

	/**
	 * @param model
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/error/list.do")
	public String errorFormList(Model model, @RequestParam Map<String, Object> paramMap) {
		return "mngr/diagnosis/error/list_pop";
	}

	/**
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/diagnosis/error/listAjax.do")
	@ResponseBody
	public Map<String, Object> errorFormListAjax(@RequestParam Map<String, Object> paramMap) {
		paramMap.put("insttCode", SangsUtil.getInsttCode());
		return diagnosisService.selectDgnssErrorList(paramMap);
	}

	/**
	 * @param list
	 * @param key
	 * @return
	 */
	private Map<String, SangsMap> changeListToMap(List<SangsMap> list, String key) {

		Map<String, SangsMap> resultMap = new HashMap<>();

		for (SangsMap map : list) {
			resultMap.put(MapUtils.getString(map, key), map);
		}

		return resultMap;
	}

	/**
	 * @param paramMap
	 * @param csvAt
	 * @return
	 */
	private String getColumnNm(Map<String, Object> paramMap, boolean csvAt) {

		if (csvAt) {
			return MapUtils.getString(paramMap, "columnComment");
		} else {
			return MapUtils.getString(paramMap, "columnNm");
		}
	}

	/**
	 * @param sheet
	 * @param rowNum
	 */
	private void removeMergedRegion(XSSFSheet sheet, int rowNum) {

		for (int i = sheet.getMergedRegions().size() - 1; i >= 0; i--) {
			if (sheet.getMergedRegion(i).getFirstRow() == rowNum) {
				sheet.removeMergedRegion(i);
			}
		}
	}
	
	
	
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/mngr/diagnosis/selectColumnAjax")
	@ResponseBody
	public Map<String, Object> selectColumnAjax(@RequestParam Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		return  diagnosisService.selectColumnAjax(params, req, res);
		
	}
	
	
	/**
	 * @param schemaName
	 * @param tableName
	 * @param columnName
	 * @param rowCount
	 * @param req
	 * @param res
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping("/mngr/diagnosis/selectAnalysisAjax")
	@ResponseBody
	public Map<String, Object> selectAnalysisAjax(
			@RequestParam String schemaName,
			@RequestParam String tableName,
			@RequestParam String[] columnName,
			@RequestParam String rowCount,
			HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
		
		return diagnosisService.selectAnalysisAjax(schemaName, tableName, columnName, rowCount, req, res, session);
		
	}
	
	/**
	 * @param session
	 * @param request
	 * @param response
	 * @param paramMap
	 * @throws IOException
	 */
	@RequestMapping("/mngr/diagnosis/export.do")
	public void exportCSV(HttpSession session, HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramMap)  throws Exception, IOException {
		// 세션값에서 기관정보 , 진단대상데이터베이스정보 조회 처리
		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		String insttCode = SangsUtil.getInsttCode();
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		paramMap.put("limit", reportListCnt);
		paramMap.put("insttCode", insttCode);
		// 진단대상 DBMS 정보 조회
		Map<String, String> data = new HashMap();
		data.put("dgnssDbmsId", MapUtils.getString(paramMap, "dgnssDbmsId"));
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);

		String newDdId = MapUtils.getString(paramMap, "dgnssDbmsId");
		
		if (!StringUtils.equals(dgnssDbmsId, newDdId)) {
			Map<String, String> params = new HashMap<>();
			params.put("dgnssDbmsId", newDdId);
			try {
				// 진단대상 DBMS 설정 >> 다른 방식으로 처리 해야 할 듯 한데...
				basicInfoService.setDbmsUrl(params, request, response);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		
		// CSV 파일 정보인경우 코멘트 정보를 컬럼명으로 처리 하기 위해
		boolean csvAt = false;
		if(paramMap.get("dbmsKnd").equals("CSV")){
			paramMap.put("tableName", paramMap.get("tableNm"));
			paramMap.put("columnName", paramMap.get("columnNm"));
			paramMap.put("columnComment", diagnosisService.selectColumnComment(paramMap));
			csvAt = true;
		}
		// 분석 결과 리스트
		List<SangsMap> analsList = diagnosisService.selectResAnalsList(paramMap);
		// PK 리스트
		List<String> pkList = diagnosisService.selectPKList(paramMap);
		paramMap.put("pkList", pkList);
		// 분석 대상 데이터를 조회
		List<Map<String, Object>> pattenList = diagnosisService.selectDownLoadList(paramMap);
		
		Path dir = Paths.get(dataDir);

		if (Files.notExists(dir)) {
			Files.createDirectories(dir);
		}

		Path file = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;

		int fileCnt = 1;
		int lineCnt = 1;

		try {
			// 파일명 설정
			if(paramMap.get("matchMm").equals("mis")) {
				file = dir.resolve("불일치정보_" + this.getColumnNm(paramMap, csvAt) + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
				
			}else {
				file = dir.resolve("일치정보_" + this.getColumnNm(paramMap, csvAt) + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
				
			}
			bw = Files.newBufferedWriter(file, Charset.forName("EUC-KR"));
			pw = new PrintWriter(bw);
			
			// PK 정보가 있을때
			// csv 와 dbms의 출력 되는 컬럼정보가 달라야하므로 dbmsKnd 체크
			if(paramMap.get("dbmsKnd").equals("CSV")) {
				pw.print("FILE_NAME,COLUMN_NAME,VALUE,");
			}else {
				pw.print("TABLE_NAME,COLUMN_NAME,VALUE,");
			}
			
			if (pkList.size() > 0) {
				for (String pk : pkList) {
					pw.print(pk);
					pw.print(",");
				}
			} else {
				// PK 정보가 없을때
				pw.print("INDEX,");
			}
			pw.println("ANALS_NAME");
			
			for (SangsMap analsMap : analsList) {

				String analsTy = MapUtils.getString(analsMap, "analsTy");
				String analsSe = MapUtils.getString(analsMap, "analsSe");
				String analsNm = MapUtils.getString(analsMap, "analsNm");
				List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();

				if (StringUtils.equals(analsTy, "AT000200")) { // 패턴
					
					if(paramMap.get("dbmsKnd").equals("MSSQL")){
						String analsFrmla = (String)analsMap.get("analsFrmla");
						String chkPattern = analsFrmla;
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
					}else {
					
						// 패턴 불일치 리스티
						if(StringUtils.equals(analsNm, "IP(V6)")) {
							// 오라클이고 IP(V6)일때 JAVA 로 처리 필요.....
							String chkPattern = "^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$";
							paramMap.put("analsFrmla", chkPattern);
							valueList = diagnosisService.pattenCheckCnt(valueList, pattenList, paramMap,reportListCnt);
						}else if(StringUtils.equals(analsNm, "주소")) {
							// 숫자만 있는 컬럼일 경우 JAVA에서 처리 함.
							int cnt = diagnosisService.selectNumberCount(paramMap);
							if(cnt > 0) {
								//String chkPattern = analsMap.get("analsFrmla").toString();
								paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
								valueList = diagnosisService.pattenCheckCnt(valueList, pattenList, paramMap,reportListCnt);
							}else {
								paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
								valueList = diagnosisService.selectPatternNotMatchList(paramMap);
							}
						}else {
							paramMap.put("analsFrmla", analsMap.get("analsFrmla"));
							valueList = diagnosisService.selectPatternNotMatchList(paramMap);
						}
					}
				} else if (StringUtils.equals(analsTy, "AT000300")) { // 범주
					// 범주 불일치 리스트
					paramMap.put("beginValue", StringUtils.defaultString(MapUtils.getString(analsMap, "beginValue"), ""));
					paramMap.put("endValue", StringUtils.defaultString(MapUtils.getString(analsMap, "endValue"), ""));
					paramMap.put("analsSe", analsSe);
					String matchNm = paramMap.get("matchMm").toString();
					
					if(matchNm.equalsIgnoreCase("m")) {
						valueList = diagnosisService.selectRangeMatchList(paramMap);
					} else {
						valueList = diagnosisService.selectRangeNotMatchList(paramMap);
					}

				} else {
					if(StringUtils.equals(analsSe, "AG000402")) { // SQL
						Map<String, String> params = new HashMap<>();
						String  analsFrmla = (String)analsMap.get("analsFrmla");
						String  tableNm = (String)paramMap.get("tableNm");
						String  columnNm = (String)paramMap.get("columnNm");
						analsFrmla = StringUtils.replace(analsFrmla, "COUNT(1)", columnNm);
						params.put("analsFrmla", analsFrmla);
						paramMap.put("analsFrmla", params.get("analsFrmla"));
						
						valueList = diagnosisService.selectSqlNotMatchList(paramMap);
					}else {
						valueList = new ArrayList<>();
					}
				}
				
				if(valueList.size() > 0 ) {

					for (Map<String, Object> valueMap : valueList) {
	
						if(paramMap.get("dbmsKnd").equals("CSV")){
							pw.print(paramMap.get("csvFileName"));
						}else {
							pw.print(paramMap.get("tableNm"));
						}
						pw.print(",");
						pw.print(this.getColumnNm(paramMap, csvAt));
						pw.print(",");
						if(valueMap.get(paramMap.get("columnNm")) != null) {
							String strVal = MapUtils.getString(paramMap, "columnNm");
							// " 시작시 CSV 파일 엑셀에서 깨짐 방지
							if(strVal != null) {
								if(strVal.indexOf("\"") == 0) {
									strVal = "\"\""+strVal+"\"";
								}
							}
							pw.print(strVal);
							
						}else {
							pw.print("null");
						}
						pw.print(",");
						if (pkList.size() > 0) {
							for (String pk : pkList) {
								pw.print(valueMap.get(pk));
								pw.print(",");
							}
						} else {
							pw.print(lineCnt++);
							pw.print(",");
						}
						pw.println(analsMap.get("analsNm"));
	
//						if (lineCnt % 5000 == 0) {
//	
//							IOUtils.closeQuietly(pw);
//							IOUtils.closeQuietly(bw);
//	
//							if(paramMap.get("matchMm").equals("mis")) {
//								file = dir.resolve("불일치정보_" + paramMap.get("columnNm") + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
//								
//							}else {
//								file = dir.resolve("일치정보_" + paramMap.get("columnNm") + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + "_" + fileCnt++ + ".csv");
//								
//							}
//							bw = Files.newBufferedWriter(file, Charset.forName("EUC-KR"));
//							pw = new PrintWriter(bw);
//						}
					}
				}
			}

		} finally {
			IOUtils.closeQuietly(pw);
			IOUtils.closeQuietly(bw);
		}

		String userAgent = request.getHeader("User-Agent");
		String fileNameEnc;
		if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident") || StringUtils.contains(userAgent, "Edge")) {
			// MS IE
			fileNameEnc = URLEncoder.encode(file.getFileName().toString(), "UTF-8").replace("+", "%20");
		} else {
			// Mozilla, Opera
			fileNameEnc = new String(file.getFileName().toString().getBytes("UTF-8"), "8859_1");
		}

		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setContentLength((int)Files.size(file));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream os = response.getOutputStream();

		try (InputStream is = Files.newInputStream(file)) {
			StreamUtils.copy(is, os);
		}
//		try {
//			
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@RequestMapping("/mngr/diagnosis/result/exportData.do")
	public void exportData(HttpSession session, HttpServletRequest request, HttpServletResponse response, Map<String, Object> paramMap) throws IOException {
		// 세션값에서 기관정보 , 진단대상데이터베이스정보 조회 처리
		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		String insttCode = SangsUtil.getInsttCode();
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		String zipFileFolder = insttCode+"_"+DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddhhmmss");
		// 분석 대상 데이터를 조회
		//List<Map<String, Object>> pattenList = diagnosisService.selectDownLoadList(paramMap);
		
		Path dir = Paths.get(dataDir+"/"+zipFileFolder);

		if (Files.notExists(dir)) {
			Files.createDirectories(dir);
		}

		Path file = null;
		Path zipfile = null;
		
		BufferedWriter bw = null;
		PrintWriter pw = null;
		
		try {
			// 다운로드 대상 Table 설정
			String[] tables = {"instt","instt_dbms","dgnss_dbms","dgnss_save","dgnss_tables","frq_anals","dgnss_columns","dgnss_columns_res","anals","dgnss_error"};
			String database = "dq_database";
			// 테이블 데이터 조회
			for(int i=0;i<tables.length;i++) {
				int lineCnt = 0;
				bw = null;
				pw = null;
				String tableNm = tables[i];
				paramMap.put("dataBase", database);
				paramMap.put("tableName", tableNm);
				// 데이블별 select
				List<?> dataList = diagnosisService.selectTableDataList(paramMap);
				List<?> columnsList = diagnosisService.selectTableColumnsList(paramMap);
//				if("anals".equals(tableNm)) {
//					dataList = diagnosisService.selectAnalsTableDataList(paramMap);
//				}
				
				file = dir.resolve(tableNm + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + ".csv");
				bw = Files.newBufferedWriter(file, Charset.forName("EUC-KR"));
				//pw = new PrintWriter(bw);
				File tempfiil =  new File(dir + "/" + file);
				Object key = null;
				int columnlen = 0;
				int columnsize = columnsList.size();
				int datasize = dataList.size();
				// 컬럼 명 설정
				for(int ci=0;ci<columnsList.size();ci++) {
					HashMap map = (HashMap)columnsList.get(ci);
					Iterator it = map.keySet().iterator();
					while (it.hasNext()) {
						key = it.next();
						String columnNm = map.get(key).toString();
						bw.write(columnNm);
						if(ci < columnsize-1) {
							bw.write(",");
						}
						
//						pw.print(columnNm);
//						if(ci < columnsize-1) {
//						pw.print(",");
//						}
					}
				}
				
				
				// 데이터 설정
				if(datasize > 0 ) {
					key = null;
					String json = null;
					ObjectMapper mapper = new ObjectMapper(); // parser
					int line = 0;
					
					for (int di=0;di<datasize;di++) {
						HashMap valueMap = (HashMap)dataList.get(di);
						String tempstr = makeCsvFormat(valueMap,columnsList);
						bw.newLine();
						bw.write(tempstr);
						if("anals".equals(tableNm)) {
							logger.info(line +" : "+tempstr);
						}
						//File file = new File("c:\\temp\\test.txt");
						//FileUtils.writeStringToFile(tempfiil, tempstr, "UTF-8");
//						FileUtils.writeStringToFile(tempfiil, tempstr);
//						pw.println();
//						pw.print(tempstr);
						line++;
					}
				}
				//pw.flush();
				bw.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(pw);
			IOUtils.closeQuietly(bw);
		}
		
		// 생성된 CSV 파일을 하나의 ZIP 파일로 압축
		String zipFilePaht = dataDir+"/"+zipFileFolder;
		logger.info("============================zipFilePaht :  "+zipFilePaht);
		logger.info("============================dataDir :  "+dataDir);
		logger.info("============================zipFileFolder :  "+zipFileFolder);
	    createZipFile(zipFilePaht, dataDir, zipFileFolder+".zip");
		
		Path zipdir = Paths.get(dataDir);
		zipfile = zipdir.resolve(zipFileFolder + ".zip");
		logger.info("============================zipfile :  "+zipfile);
		String userAgent = request.getHeader("User-Agent");
		String fileNameEnc;

		if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident") || StringUtils.contains(userAgent, "Edge")) {
			// MS IE
			fileNameEnc = URLEncoder.encode(zipfile.getFileName().toString(), "UTF-8").replace("+", "%20");
		} else {
			// Mozilla, Opera
			fileNameEnc = new String(zipfile.getFileName().toString().getBytes("UTF-8"), "8859_1");
		}

		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setContentLength((int)Files.size(zipfile));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameEnc + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream os = response.getOutputStream();

		try (InputStream is = Files.newInputStream(zipfile)) {
			StreamUtils.copy(is, os);
		}
	} 
	
	
	public static String makeCsvFormat(HashMap valueMap,List<?> columnsList ) {
		String resStr = "";
		StringBuffer strBuf = new StringBuffer("");
		//String json = null;
		//ObjectMapper mapper = new ObjectMapper(); // parser
		
		try {
			Object key = null;
			//json = mapper.writeValueAsString(valueMap);
			//System.out.println(json);
			for(int i=0; i< columnsList.size();i++) {
				HashMap colmap = (HashMap)columnsList.get(i);
				Iterator it = colmap.keySet().iterator();
				String columnNm = "";
				while (it.hasNext()) {
					key = it.next();
					columnNm = colmap.get(key).toString();
				}
				if(valueMap.get(columnNm) != null) {
					String value = valueMap.get(columnNm).toString();
					if("true".equals(valueMap.get(columnNm).toString())) {
						value = "1";
					}
					value = value.replaceAll("\n", "");
					if("ANALS_FRMLA".equals(columnNm) || "RM".equals(columnNm)) {
						value = value.replaceAll("\"", "");
						value = "\""+value+"\"";
					}
					if("COLUMN_NM_DC".equals(columnNm) || "ANALS_ID_DC".equals(columnNm)) {
						strBuf.append(value.replaceAll(",", "&#806"));
					}else {
						strBuf.append(value);
					}
				}else {
					//strBuf.append(valueMap.get(columnNm));
				}
				if("USE_AT".equals(columnNm)) {
					System.out.println("USE_AT : "+valueMap.get(columnNm).toString());
					//System.out.println("==="+columnsList.get(i).get("USE_AT").toString());
				}
				if(i<columnsList.size()-1) {
				strBuf.append(",");
				}
			}
			resStr = strBuf.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resStr;
	}
	

	public static void zipDirectory(String dir, String zipName) {
		
		//디렉토리 존재 유무 체크 및 해당 파일 리스트를 가져오기 위하여 객체 생성
		File d = new File(dir);
		
		//디렉토리 존재 유무 체크
		if (!d.isDirectory())
		throw new IllegalArgumentException("Not a directory:  " + dir);
		
		//해당 경로의 파일을 배열로 가져옴
		String[] entries = d.list();
		
		// 파일을 읽기위한 버퍼
		byte[] buffer = new byte[4096];
		int bytesRead;
		
		try {
			// 압축파일명
			String zipfile = dir+"/"+zipName+".zip";
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			
			// 파일 압축
			for (int i=0; i<entries.length; i++) {
				System.out.println("압축 대상 파일 : " + entries[i]);
				File f = new File(d,entries[i]);
				if (f.isDirectory()) continue;// Ignore directory
				
				//스트림으로 파일을 읽음
				FileInputStream in = new FileInputStream(f);
				
				//zip파일을 만들기 위하여 out객체에 write하여 zip파일 생성
				ZipEntry entry = new ZipEntry(f.getPath()); // Make a ZipEntry
				
				System.out.println("압축 대상 파일 : " + entry);
				
				// 압축 항목추가
				out.putNextEntry(entry);
				//out.putNextEntry(entry);
				
				// 바이트 전송
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				in.close();
			}
				
			out.closeEntry();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 디렉토리 및 파일을 압축한다.
     * @param path 압축할 디렉토리 및 파일
     * @param toPath 압축파일을 생성할 경로
     * @param fileName 압축파일의 이름
     */
    public static void createZipFile(String path, String toPath, String fileName) {
 
        File dir = new File(path);
        String[] list = dir.list();
        String _path;
 
        if (!dir.canRead() || !dir.canWrite())
            return;
 
        int len = list.length;
 
        if (path.charAt(path.length() - 1) != '/')
            _path = path + "/";
        else
            _path = path;
 
        try {
            ZipOutputStream zip_out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(toPath+"/"+fileName), 2048));
 
            for (int i = 0; i < len; i++)
                zip_folder("",new File(_path + list[i]), zip_out);
 
            zip_out.close();
 
        } catch (FileNotFoundException e) {
            //Log.e("File not found", e.getMessage());
 
        } catch (IOException e) {
            //Log.e("IOException", e.getMessage());
        } finally {
 
 
        }
    }
 
    /**
     * ZipOutputStream를 넘겨 받아서 하나의 압축파일로 만든다.
     * @param parent 상위폴더명
     * @param file 압축할 파일
     * @param zout 압축전체스트림
     * @throws IOException
     */
    private static void zip_folder(String parent, File file, ZipOutputStream zout) throws IOException {
        byte[] data = new byte[2048];
        int read;
        String zipPath = "";
        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(parent + file.getName());
            zout.putNextEntry(entry);
            BufferedInputStream instream = new BufferedInputStream(new FileInputStream(file));
 
            while ((read = instream.read(data, 0, 2048)) != -1)
                zout.write(data, 0, read);
 
            zout.flush();
            zout.closeEntry();
            instream.close();
 
        } else if (file.isDirectory()) {
            String parentString = file.getPath().replace(zipPath,"");
            parentString = parentString.substring(0,parentString.length() - file.getName().length());
            ZipEntry entry = new ZipEntry(parentString+file.getName()+"/");
            zout.putNextEntry(entry);
 
            String[] list = file.list();
            if (list != null) {
                int len = list.length;
                for (int i = 0; i < len; i++) {
                    zip_folder(entry.getName(),new File(file.getPath() + "/" + list[i]), zout);
                }
            }
        }
    }
 
    /**
     * 압축을 해제 한다
     *
     * @param zip_file
     * @param directory
     */
    public static boolean extractZipFiles(String zip_file, String directory) {
        boolean result = false;
 
        byte[] data = new byte[2048];
        ZipEntry entry = null;
        ZipInputStream zipstream = null;
        FileOutputStream out = null;
 
        if (!(directory.charAt(directory.length() - 1) == '/'))
            directory += "/";
 
        File destDir = new File(directory);
        boolean isDirExists = destDir.exists();
        boolean isDirMake = destDir.mkdirs();
 
        try {
            zipstream = new ZipInputStream(new FileInputStream(zip_file));
 
            while ((entry = zipstream.getNextEntry()) != null) {
 
                int read = 0;
                File entryFile;
 
                //디렉토리의 경우 폴더를 생성한다.
                if (entry.isDirectory()) {
                    File folder = new File(directory+entry.getName());
                    if(!folder.exists()){
                        folder.mkdirs();
                    }
                    continue;
                }else {
                    entryFile = new File(directory + entry.getName());
                }
 
                if (!entryFile.exists()) {
                    boolean isFileMake = entryFile.createNewFile();
                }
 
                out = new FileOutputStream(entryFile);
                while ((read = zipstream.read(data, 0, 2048)) != -1)
                    out.write(data, 0, read);
 
                zipstream.closeEntry();
 
            }
 
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
 
            if (zipstream != null) {
                try {
                    zipstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return result;
    }
	
    
    
    public void solidFillSeries(XDDFChartData data, int index, PresetColor color) {

		XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
		XDDFChartData.Series series = data.getSeries().get(index);
		XDDFShapeProperties properties = series.getShapeProperties();

		if (properties == null) {
			properties = new XDDFShapeProperties();
			//System.out.println(XDDFColor.from(color));
		} else {
			XDDFSolidFillProperties fillProp = (XDDFSolidFillProperties)properties.getFillProperties();
			//System.out.println(fillProp.getColor());
		}
		properties.setFillProperties(fill);
		series.setShapeProperties(properties);
	}
    
    private void fileDownload2(HttpServletRequest request, HttpServletResponse response, InputStream is, String filename, long filesize) throws ServletException, IOException {
		logger.info("==================== fileDownload2 =============");
		byte[] buffer = new byte[4096];
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.indexOf("MSIE 5.5") != -1) {
			response.setHeader("Content-Type", "doesn/matter; charset=euc-kr");
			response.setHeader("Content-Disposition", "filename="
					+ new String(filename));
		}else if (userAgent.indexOf("Opera") != -1) {
			int extIndex = filename.lastIndexOf(".");
			String extFileName = filename.substring(extIndex+1);
			
			if("hwp".equals(extFileName)){
				response.setContentType("application/x-hwp;");
			}else{
				response.setContentType("application/octet-stream;");
			}
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(filename));
		}else if(userAgent.indexOf("Firefox") > -1){
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + "\"" + filename + "\"");
		}else if(userAgent.indexOf("Chrome") > -1 ) {
			response.setHeader("Content-Disposition", "attachment; filename=" + "\"" + filename + "\"");
		}else {
			response.setHeader("Content-Disposition", "attachment; filename="
			 		+ new String(filename));
		}

		if (filesize > 0) {
			response.setHeader("Content-Length", "" + filesize);
		}

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(is);
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;
			
			while ((read = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}
		} finally {
			try {
				outs.close();
			} catch (Exception ex1) {}
			try {
				fin.close();
			} catch (Exception ex2) {}
		} // end of try/catch
	}
}

package com.webapp.dqsys.mngr.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.dqsys.mngr.domain.BaseFile;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.service.DataStandardService;
import com.webapp.dqsys.mngr.service.DiagnosisService;
import com.webapp.dqsys.mngr.service.RuleMngService;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.util.FileUploadUtil;
import com.webapp.dqsys.util.SangsUtil;

/**
 * @author user
 *
 */
@Controller
public class DataStandardController {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DataStandardService dataStandardService;

	@Value("${file.csv.dataDir}")
	private String csvDir;
	
	@Resource
	private RuleMngService ruleMngService;

	@Resource
	private DiagnosisService diagnosisService;

	/**
	 * 분석결과 보기 페이지당 리스트 건수
	 */
	@Value("${analysis.view.listCnt}")
	private int viewListCnt;

	/**
	 * 표준화 항목 관리 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/standardItemManage.do") //첫번째 페이지
	public String dataStandardManage(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		
		String dbmsId = (String) req.getSession().getAttribute("DDId");
		Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);
		
		if(connData != null) {
			model.addAttribute("schema", connData.get("database"));
			model.addAttribute("dbmsId", connData.get("dbmsId"));
		}
		return "mngr/dataStandard/standard_item_manage";
	}

	/**
	 * 표준화 항목 관리 목록 조회
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/selectStandardItemManageList")
	public void selectDataStandardManageList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		dataStandardService.selectDataStandardManageList(params, res);
	}

	/**
	 * 도메인 insert 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/saveDomainInfo")
	public void saveDomainInfo(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		dataStandardService.saveDomainInfo(params, req, res);
	}

	/**
	 * 도메인 CSV insert 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/saveDomainFile")
	public void saveDomainCsv(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		
		String attFileOutputPath = csvDir + "/";
		List<BaseFile> fileList = null;
		
		try {
			if (ServletFileUpload.isMultipartContent(req)) {
				fileList = FileUploadUtil.uploadFiles(req, attFileOutputPath);
			}
	
			// 임시저장에 등록된 파일 읽어들여 도메인 정보 저장
			if (fileList != null && fileList.size() > 0) {
				BaseFile vo = fileList.get(0);
				String attFileOutputFullPath = attFileOutputPath + vo.getPhysicalName();
				// 업로드된 파일 읽어오기
	
				String extension = FilenameUtils.getExtension(attFileOutputFullPath);
		
				//파일 확장자 확인
				if (StringUtils.equalsIgnoreCase(extension, "xls") || StringUtils.equalsIgnoreCase(extension, "xlsx") ) {
					//엑셀파일 업로드
					dataStandardService.saveDomainExcel(params,req,res);
				} else if(StringUtils.equalsIgnoreCase(extension, "csv")) {
					//엑셀파일 업로드
					dataStandardService.saveDomainCsv(params, req, res);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	/**
	 * 표준 용어 insert 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/saveWordInfo")
	public void saveWordInfo(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		dataStandardService.saveWordInfo(params, req, res);
	}

	/**
	 * 표준 용어 CSV insert 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/saveWordFile")
	public void saveWordCsv(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
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

		String extension = FilenameUtils.getExtension(attFileOutputFullPath);

		//파일 확장자 확인
		if (StringUtils.equalsIgnoreCase(extension, "xls") || StringUtils.equalsIgnoreCase(extension, "xlsx") ) {
			//엑셀파일 업로드
			dataStandardService.saveWordExcel(params,req,res);
		} else if(StringUtils.equalsIgnoreCase(extension, "csv")) {
			//엑셀파일 업로드
			dataStandardService.saveWordCsv(params, req, res);
		}
		
		}
	}

	/**
	 * 표준화 분석 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/standardAnalysis.do")
	public String dataStandardAnalysis(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		String dbmsId = (String) req.getSession().getAttribute("DDId");

		try {
			// DB접속 정보 조회
			Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);

			if ("CSV".equals(connData.get("connType"))) {
				model.addAttribute("connData", connData);
			} else {
				model.addAttribute("schema", connData.get("database"));
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		return "mngr/dataStandard/standard_analysis";
	}

	/**표준화 분석 실행
	 * @param params
	 * @param session
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/excuteStandardAnalysis")
	public String excuteStandardAnalysis(@RequestParam Map<String, String> params, HttpSession session,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		Map<String, String[]> rMap = req.getParameterMap();
		

		Member member = (Member)session.getAttribute("member");
		String dgnssDbmsId = (String)session.getAttribute("DDId");
		params.put("ddId", dgnssDbmsId);
		String insttCode = SangsUtil.getInsttCode();
		if (member != null) {
			insttCode = member.getInsttCode();
		}
		if (dgnssDbmsId == null) {
			dgnssDbmsId = "";
		}
		
		//분석 테이블 정보조회
		List<?> tableInfoList = dataStandardService.selectTableAnalysis(params, req, res);
		
		Map<String, Object> paramMap = new ConcurrentHashMap<>();
		
		
		String dateFmt = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
		String schemaName = params.get("tableSchema");
		String allCo = params.get("tableTotalCnt");
		String dgnssSaveId = dgnssDbmsId + "_" + dateFmt;
		String analysisSaveName = params.get("analysisSaveName"); 
		
		paramMap.put("insttCode", insttCode);
		paramMap.put("dgnssDbmsId", dgnssDbmsId);
		paramMap.put("dgnssInfoId", dgnssDbmsId + "_" + dgnssSaveId);
		paramMap.put("dgnssNm", analysisSaveName + "(" + dgnssDbmsId + "_" + schemaName + "_" + dateFmt + ")");
		paramMap.put("allCo", allCo);
		paramMap.put("schemaName", schemaName);
		
		//분석 대상 스키마 정보 입력
		dataStandardService.insertStandardSchema(paramMap);
		
		String[] analsisItemList;
		for (Object tableItem : tableInfoList) {
			Map<String, Object> tableInfoMap = (Map<String, Object>) tableItem;
			
			analsisItemList = rMap.get(tableInfoMap.get("tableName"));

			String tableName = SangsUtil.isEmpty(tableInfoMap.get("tableName")) ? ""
					: tableInfoMap.get("tableName").toString();
			
			//스키마별 테이블 정보 입력
			dataStandardService.insertStandardTable(paramMap, tableInfoMap, analsisItemList);
			
			paramMap.put("tableName", tableName);
			
			//테이블별 컬럼 정보 조회
			List<Map<String, Object>> columnMapList = diagnosisService.selectColumnList(paramMap);
			
			

			for (Map<String, Object> columnMap : columnMapList) {
				String columnName = MapUtils.getString(columnMap, "COLUMN_NAME");
				String columnCM = MapUtils.getString(columnMap, "COLUMN_COMMENT");

				paramMap.put("columnNm", columnName);
				//paramMap.put("columnCM", columnCM);
				paramMap.put("columnTy", columnMap.get("DATA_TYPE"));
				if (columnMap.get("DATA_LENGTH") != null) {
					if(!"text".equals(columnMap.get("DATA_TYPE")) && !"clob".equals(columnMap.get("DATA_TYPE")) && !"mediumtext".equals(columnMap.get("DATA_TYPE"))
							&& !"longblob".equals(columnMap.get("DATA_TYPE"))&& !"blob".equals(columnMap.get("DATA_TYPE"))	) {
						paramMap.put("columnLt", columnMap.get("DATA_LENGTH"));
					}else {
						paramMap.put("columnLt", "");
					}
				}else {
					paramMap.put("columnLt", "");
				}
				//테이블별 컬럼 상세정보 입력
				dataStandardService.insertStandardColumn(paramMap);
				
				
				// 컬럼 단위로 쓰레드 실행 
				Map<String, Object> asyncMap = new ConcurrentHashMap<>();
				asyncMap.putAll(paramMap);
				dataStandardService.asyncExecutorDataStandardColumn(asyncMap);
				
			}
			//분석결과 update
			dataStandardService.executeUpdateEndStandardSchema(paramMap);
			
		}
		return "mngr/dataStandard/standard_analysis_result";
	}

	/**
	 * 표준화 분석 결과 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/standardAnalysisResult.do")
	public String dataStandardAnalysisResult(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		return "mngr/dataStandard/standard_analysis_result";
	}
	
	
	
	/**
	 * 표준화 분석 결과 리스트 ajax호출
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/result/listAjax.do")
	@ResponseBody
	public Map<String, Object> resultListAjax(@RequestParam Map<String, Object> paramMap) throws Exception {
		return dataStandardService.selectStandardSchemaList(paramMap);
	}
	
	
	
	/**
	 * 분석결과 상세화면 
	 * @param model
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/standardAnalysisResultView.do")
	public String dataStandardAnalysisResultView(Model model, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		int listCntPerPage = viewListCnt;
		if (listCntPerPage <= 0) {
			listCntPerPage = 1;
		}
		paramMap.put("insttCode", SangsUtil.getInsttCode());
		Map<String, String> data = new HashMap<String, String>();
		// 진단대상 DBMS 정보 조회
		data.put("dgnssDbmsId", paramMap.get("dgnssDbmsId").toString());
		data = ruleMngService.selectDgnssDbmsInfo(data);
		paramMap.putAll(data);
		// 대상 DBMS 정보 조회
		model.addAttribute("dbms", diagnosisService.selectDgnssDbms(paramMap));
		// 진단  스키마 정보 조회
		SangsMap schemaMap = dataStandardService.selectStandardSchema(paramMap);
		model.addAttribute("schema", schemaMap);
		
		// 페이징 처리로 컬럼 단위로 하나씩 끊음
		List<SangsMap> tableList = dataStandardService.selectStandardTableList(paramMap);
		List<SangsMap> wordObsryList = dataStandardService.selectWordObsryList(paramMap);
		List<SangsMap> domainObsryList = dataStandardService.selectDomainObsryList(paramMap);
		List<Object> columnListAll = new ArrayList<>();
		List<Object> columnCmt = new ArrayList<>();

		Map<String, Object> bodyMap = new HashMap<String, Object>();
		Map<String, Object> frqlList = new HashMap<String, Object>();
		Map<String, Object> summery = new HashMap<String, Object>();

		diagnosisService.pagingSetMySql(paramMap);

		int pageIdx = MapUtils.getIntValue(paramMap, "pageIndex", 1);

		if (pageIdx > tableList.size()) {
			pageIdx = 1;
		}

		SangsMap tableMap = null;

		List<SangsMap> columnData = new ArrayList<>();
		List<Map<String,Object>> summeryList = new ArrayList<>();
		
		//분석요약정보 조회
		summery = dataStandardService.selectStandardSummery(paramMap);
		
		Map<String,Object> summeryMap = new HashMap<>();
		
		//요약 정보를 위한 하드코딩
		summeryMap.put("object", "테이블");
	    summeryMap.put("objectCnt", summery.get("tablecnt"));
		summeryList.add(summeryMap);
		
		summeryMap = new HashMap<>();
		
		summeryMap.put("object", "대상 건수");
	    summeryMap.put("objectCnt", summery.get("columncnt"));
		summeryList.add(summeryMap);
		
		summeryMap = new HashMap<>();
		
		
		summeryMap.put("object", "표준용어 미준수 컬럼");
	    summeryMap.put("objectCnt", summery.get("wordObsry"));
		summeryList.add(summeryMap);
		
		
		summeryMap = new HashMap<>();
		
		summeryMap.put("object", "도메인 미준수 컬럼");
	    summeryMap.put("objectCnt", summery.get("domainObsry"));
		summeryList.add(summeryMap);
		
		
		for (int i = (pageIdx - 1) * listCntPerPage; i < (pageIdx * listCntPerPage); i++) {

			if (tableList.size() > i) {
				
				List<SangsMap> columnResList = new ArrayList<>();
				List<SangsMap> frqAnalList = new ArrayList<>();
				String cmt;
				tableMap = tableList.get(i);
				
				columnData.add(tableList.get(i));

				paramMap.put("rowCount", schemaMap.get("allCo"));
				paramMap.put("tableNm", tableMap.get("tableNm"));
				
				
				// 컬럼별 진단 결과0111
				columnResList = dataStandardService.selectStandardColumnResList(paramMap);
				
				// 분석 구분 목록

				columnListAll.add(columnResList);

				if ("CSV".equals(data.get("dbmsKnd").toString())) {
					paramMap.put("schemaName", schemaMap.get("schemaNm"));
					paramMap.put("tableName", tableMap.get("tableNm")); // 컬럼별 코멘트 정보
					cmt = diagnosisService.selectColumnComment(paramMap);
					columnCmt.add(cmt);
				}
			}
		}

		bodyMap.put("body", columnListAll);
		
		
		model.addAttribute("bodyMap", bodyMap);
		model.addAttribute("wordObsryList", wordObsryList);
		model.addAttribute("domainObsryList", domainObsryList);
		model.addAttribute("summery", summeryList);
		model.addAttribute("columnData", columnData);
		model.addAttribute("tableList", tableList);
		model.addAttribute("column", tableMap);
		model.addAttribute("groupList", columnData);
		model.addAttribute("columnCmt", columnCmt);
		model.addAttribute("listCntPerPage", listCntPerPage);
		boolean csvAt = false;
		if ("CSV".equals(data.get("dbmsKnd").toString())) {
			csvAt = true;
		} else {
			csvAt = false;
		}

		model.addAttribute("csvAt", csvAt);

		return "mngr/dataStandard/standard_analysis_result_view";
	}
	
	/**
	 * 도메인 update 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/updateDomainInfo")
	public void updateDomainInfo(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		dataStandardService.updateDomainInfo(params, req, res);
	}
	
	/**
	 * 표준 용어 update 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/updateWordInfo")
	public void updateWordInfo(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		dataStandardService.updateWordInfo(params, req, res);
	}
	
	/**
	 * 도메인 delete 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/deleteDomainInfo")
	public void deleteDomainInfo(@RequestParam(value="jsonData", required=false)String params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		
		String[] arrparams = params.split(",");
				
		dataStandardService.deleteDomainInfo(arrparams, req, res);
	}
	
	/**
	 * 표준 용어 delete 호출
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/dataStandard/deleteWordInfo")
	public void deleteWordInfo(@RequestParam(value="jsonData", required=false)String params, Model model, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String[] arrparams = params.split(",");
		dataStandardService.deleteWordInfo(arrparams, req, res);
	}
	
	
	 /**
     * 컬럼별 도메인 설정
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS
     */
    // TODO: 컬럼 구조 분석 페이지 이동
	@RequestMapping("/mngr/dataStandard/standardDomainManage")
	public String moveAnalysisColumn(HttpServletRequest req, Model model) {
		try {
			String dbmsId = (String) req.getSession().getAttribute("DDId");
			// 	접속 정보 조회
			Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);
			
			if("CSV".equals(connData.get("connType"))) {
				model.addAttribute("connData", connData);
			}else {
				model.addAttribute("schema", connData.get("database"));
			}
		}catch (Exception e) {
			e.getStackTrace();
		}
		return "mngr/dataStandard/standard_domain_manage";
	}

	
	
	/**
	 * 컬럼별 도메인 매칭
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/standardDomainMatch")
	public void standardDomainMatch(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		
		String dbmsId = (String) req.getSession().getAttribute("DDId");
		Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, String> data = new HashMap();
		List<Map<String, String>> columnData = new ArrayList<>();
		int columnCnt = Integer.parseInt(params.get("columnCnt"));
		
		String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
		// dgnss_dbms 테이블에서 대상 DB 정보를 가지고 온다.
		params.put("dgnssDbmsId", dgnssDbmsId);
		
			data = ruleMngService.selectDgnssDbmsInfo(params);
			params.putAll(data);
		
		for(int i = 0; i < columnCnt; i++) {
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("columnNm",	params.get("columnNm_"+i));
			map.put("domainCl",	params.get("domainCl_"+i));
			map.put("domainNm",	params.get("domainNm_"+i));
			map.put("domainTy",	params.get("domainTy_"+i));
			map.put("domainLt",	params.get("domainLt_"+i));
			map.put("domainId",	params.get("domainId_"+i));
			map.put("updateYn",	params.get("updateYn_"+i));
			map.put("insChk",	params.get("insChk_"+i));
			map.put("schema",	params.get("schema"));
			map.put("tableNm",	params.get("tableNm"));
			
			
			columnData.add(map);
		}
		
		paramMap.put("columnCnt", columnCnt);
		paramMap.put("dbmsId", params.get("dbmsId"));
		dataStandardService.insertDomainMatch(columnData,paramMap, req ,res);
	}
	
	/**
	 * 컬럼별 도메인 매칭 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/selectMatchData")
	public void selectMatchData(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
	
		dataStandardService.selectMatchData(params, req ,res);
	}
	
	
	/**
	 * 컬럼별 도메인 매칭 삭제
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/deleteMatch")
	public void deleteMatch(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		dataStandardService.deleteMatch(params, req ,res);
	}
	
	
	/**
	 * 도메인 분류코드조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/dataStandard/domainGroupList")
	@ResponseBody
	public List<?> selectDomainGroupList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		
		return dataStandardService.selectDomainGroupList(params, req ,res);
	}
	
}
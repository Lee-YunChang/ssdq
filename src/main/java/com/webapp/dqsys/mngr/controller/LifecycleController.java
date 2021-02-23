package com.webapp.dqsys.mngr.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.webapp.dqsys.mngr.domain.BaseFile;
import com.webapp.dqsys.mngr.domain.SangsMap;
import com.webapp.dqsys.mngr.service.AnalysisService;
import com.webapp.dqsys.mngr.service.DiagnosisService;
import com.webapp.dqsys.mngr.service.LifecycleService;
import com.webapp.dqsys.mngr.service.RuleMngService;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.util.FileUploadUtil;
import com.webapp.dqsys.util.SangsUtil;

@Controller
public class LifecycleController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LifecycleService lifecycleService; 
	
	@Value("${file.csv.dataDir}")
	private String csvDir;
	
	@Resource
	private RuleMngService ruleMngService;
	
	@Resource
	private DiagnosisService diagnosisService;
	
	@Autowired
	AnalysisService analysisService;
	
	/**
	 * Lifecycle 항목 관리 페이지 이동
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/lifecycleManageList.do")
	public String LifecycleListMove() throws Exception{
		return "mngr/lifecycle/lifecycle_manage_list";
		
	}
	
	
	/**
	 * Lifecycle 항목 조회
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/ajaxSelLifecycleManageList")
	public void AjaxSelLifecycleList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		lifecycleService.AjaxSelLifecycleList(params, res);
		
	}
	
	/**
	 * Lifecycle 항목 등록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/ajaxInsLifecycleManageList")
	public void AjaxInsLifecycleList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		lifecycleService.AjaxInsLifecycleList(params, res);
		
	}
	
	/**
	 * Lifecycle 항목 수정
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/ajaxUdtLifecycleManageList")
	public void AjaxUdtLifecycleList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		lifecycleService.AjaxUdtLifecycleList(params, req, res);
		
	}
	
	/**
	 * Lifecycle 항목 삭제
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/ajaxDelLifecycleManageList")
	public void AjaxDelLifecycleList(@RequestParam(value="jsonData", required=false)String params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] arrparams = params.split(",");
		lifecycleService.AjaxDelLifecycleList(arrparams, req, res);
		
	}
	
	/**
	 * Lifecycle 파일 등록
	 * 
	 * @param params
	 * @param model
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/lifecycle/ajaxInsLifecycleFile")
	public void AjaxInsLifecycleFile(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,HttpServletResponse res) throws Exception {
		
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
					lifecycleService.ajaxInsLifecycleExcelFile(params,req,res);
				} else if(StringUtils.equalsIgnoreCase(extension, "csv")) {
					//CSV파일 업로드
					lifecycleService.ajaxInsLifecycleCsvFile(params, req, res);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lifecycle 분석 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/lifecycleAnalysis.do")
	public String LifecycleAnalsMove(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		
		String dbmsId = (String) req.getSession().getAttribute("DDId");
		
		try {
			// 접속 정보 조회
			Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);
			
			if ("CSV".equals(connData.get("connType"))) {
				model.addAttribute("connData", connData);
			} else {
				model.addAttribute("schema", connData.get("database"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "mngr/lifecycle/lifecycle_analysis";
		
	}
	
	/**
	 * Lifecycle 분석 항목명 조회
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/ajaxSelLifecycleFiledNm")
	public void AjaxSelLifecycleFiledNm(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		lifecycleService.AjaxSelLifecycleFiledNm(params, res);
		
	}
	
	/**
	 * Lifecycle 기준컬럼 조회
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/selectColumnAnalysis")
	public void selectColumnAnalysis(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{
		try {
			lifecycleService.selectColumnAnalysis(params, req, res);
		} catch (Exception e) {

		}
	}
	
	/**
	 * Lifecycle 분석 실행
	 * 
	 * @param params
	 * @param session
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/excuteLifecycleAnalysis")
	public void excuteLifecycleAnalysis(@RequestParam Map<String, String> params, HttpSession session, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
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
		
		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("analsData"));
			HashMap<String, String> param = new HashMap<String, String>();
			
			Map<String, Object> paramMap = new ConcurrentHashMap<>();
			
			String dateFmt = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
//			Calendar cal = Calendar.getInstance();
//	        cal.setTime(new Date());
//	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
			String tableSchema = params.get("tableSchema");
			String allCo = params.get("tableCnt");
			String dgnssSaveId = dgnssDbmsId + "_" + dateFmt;
			String analysisSaveNm = params.get("analysisSaveNm"); 
			int chk = 0;
			
			paramMap.put("insttCode", insttCode);
			paramMap.put("dgnssDbmsId", dgnssDbmsId);
			paramMap.put("dgnssInfoId", dgnssDbmsId + "_" + dgnssSaveId);
			paramMap.put("dgnssNm", analysisSaveNm + "(" + dgnssDbmsId + "_" + tableSchema + "_" + dateFmt + ")");
			paramMap.put("allCo", allCo);
			paramMap.put("tableSchema", tableSchema);
			
			//분석 스키마 정보 입력
			lifecycleService.insertLifecycleSchema(paramMap);
			
			for(Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				
				Calendar cal = Calendar.getInstance();
		        cal.setTime(new Date());
		        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				
				param.put("tableNm", item.get("colTableNm").toString());
				param.put("schemaName", tableSchema);
				param.put("analsId", item.get("filedNm").toString());
				param.put("colNm", item.get("colNm").toString());
				param.put("colType", item.get("colType").toString());
				param.put("allCo", allCo);
				
				//항목 기간, 기간구분 조회
				List<Map<String,String>> periodList = lifecycleService.selectAnalysisPeriod(param);
				
				param.put("analsTy", periodList.get(0).get("analsTy"));
				param.put("period", String.valueOf(periodList.get(0).get("period")));
				param.put("periodCl", periodList.get(0).get("periodCl"));
				
				paramMap.put("analsId", param.get("analsId"));
				paramMap.put("tableName", param.get("tableNm"));
				paramMap.put("columnName", param.get("colNm"));
				paramMap.put("columnType", param.get("colType"));
				paramMap.put("analsTy", param.get("analsTy"));
				paramMap.put("period", param.get("period"));
				paramMap.put("periodCl", param.get("periodCl"));
				
				int period = -Integer.valueOf(param.get("period")); //기간 계산을 위한 음수값
				String periodCl = param.get("periodCl");
				
				if("M".equalsIgnoreCase(periodCl)) {
					cal.add(Calendar.MONTH, period);
				} else if("Y".equalsIgnoreCase(periodCl)) {
					cal.add(Calendar.YEAR, period);
				}
				
				paramMap.put("chkDate", df.format(cal.getTime()));
				
				// 컬럼 단위로 쓰레드 실행 
				Map<String, Object> asyncMap = new ConcurrentHashMap<>();
				asyncMap.putAll(paramMap);
				lifecycleService.asyncExecutorDataLifecycleAnalysis(asyncMap, res, req);
				
				chk++;
			}
			
			if(arrItems.size() == chk) {
				//분석성공시 스키마 정보 업데이트
				paramMap.put("excSttus", "E");
				lifecycleService.updateLifecycleSchema(paramMap);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lifecycle 분석 결과 페이지 이동
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/lifecycleResultList.do")
	public String LifecycleResultListMove() throws Exception{
		return "mngr/lifecycle/lifecycle_analysis_result";
		
	}
	
	/**
	 * Lifecycle 분석  결과 리스트 ajax호출
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("/mngr/lifecycle/result/listAjax.do")
	@ResponseBody
	public Map<String, Object> resultListAjax(@RequestParam Map<String, Object> paramMap) throws Exception {
		return lifecycleService.selectLifecycleSchemaList(paramMap);
	}
	
	/**
	 * Lifecycle 분석  결과 상세 페이지 이동 및 데이터 조회
	 * 
	 * @param model
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/lifecycle/lifecycleResultView.do")
	public String lifecycleResultView(Model model, @RequestParam Map<String, Object> paramMap) throws Exception {
		
		// 진단  스키마 정보 조회
		SangsMap schemaMap = lifecycleService.selectLifecycleSchema(paramMap);
		model.addAttribute("schema", schemaMap);
		
		Map<String, Object> summery = new HashMap<String, Object>();
		//분석항목 정보 조회
		List<Map<String,Object>> summeryList = new ArrayList<>();
		Map<String,Object> summeryMap = new HashMap<>();
		List<Map<String,Object>> summeryRateList = new ArrayList<>();
		Map<String,Object> summeryRateMap = new HashMap<>();
		summery = lifecycleService.selectLifecycleSummery(paramMap);

		//요약 정보를 위한 하드코딩
		summeryMap.put("object", "테이블");
	    summeryMap.put("objectCnt", summery.get("tableCnt"));
		summeryList.add(summeryMap);
		
		summeryMap = new HashMap<>();
		
		summeryMap.put("object", "데이터 건수");
	    summeryMap.put("objectCnt", summery.get("totalCo"));
		summeryList.add(summeryMap);
		
		summeryMap = new HashMap<>();
		
		summeryMap.put("object", "정상 데이터");
		summeryMap.put("objectCnt", summery.get("mtchgCo"));
		summeryList.add(summeryMap);

		summeryMap = new HashMap<>();
		
		summeryMap.put("object", "초과 데이터");
	    summeryMap.put("objectCnt", summery.get("missCo"));
		summeryList.add(summeryMap);
		
		summeryMap = new HashMap<>();
		
		summeryMap.put("object", "에러 데이터");
	    summeryMap.put("objectCnt", summery.get("errCo"));
		summeryList.add(summeryMap);
		
		model.addAttribute("summery", summeryList);
		
		summeryRateMap.put("mtchgPer", summery.get("mtchgPer"));
	    summeryRateMap.put("missPer", summery.get("missPer"));
	    summeryRateMap.put("errPer", summery.get("errPer"));
	    summeryRateList.add(summeryRateMap);
	    
	    model.addAttribute("summeryRate", summeryRateList);
	    
		//테이블 진단 결과
	    List<SangsMap> tableRes = lifecycleService.selectLifecycleTableRes(paramMap);
		
		List<Map<String,Object>> tableResList = new ArrayList<>();
		Map<String,Object> tableResMap = new HashMap<>();

		for (int i = 0; i < tableRes.size(); i++) {
			tableResMap = new HashMap<>();
			
			tableResMap.put("object", "데이터 건수");
			tableResMap.put("objectCnt", tableRes.get(i).get("totalCo"));
			tableResMap.put("tableNm", tableRes.get(i).get("tableNm"));
			tableResList.add(tableResMap);
			
			tableResMap = new HashMap<>();
			
			tableResMap.put("object", "정상 건수");
			tableResMap.put("objectCnt", tableRes.get(i).get("mtchgCo"));
			tableResMap.put("tableNm", tableRes.get(i).get("tableNm"));
			tableResList.add(tableResMap);
			
			tableResMap = new HashMap<>();
			
			tableResMap.put("object", "초과 건수");
			tableResMap.put("objectCnt", tableRes.get(i).get("missCo"));
			tableResMap.put("tableNm", tableRes.get(i).get("tableNm"));
			tableResList.add(tableResMap);
			
			tableResMap = new HashMap<>();
			
			tableResMap.put("object", "에러 건수");
			tableResMap.put("objectCnt", tableRes.get(i).get("errCo"));
			tableResMap.put("tableNm", tableRes.get(i).get("tableNm"));
			tableResList.add(tableResMap);
		}
		
		model.addAttribute("tableResList", tableResList);
		model.addAttribute("tableRes", tableRes);
		model.addAttribute("param", paramMap);
		
		return "mngr/lifecycle/lifecycle_analysis_result_view";
	}

	/**
	 * 항목명 중복체크
	 * 
	 * @param params
	 * @param model
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/lifecycle/ajaxSelChkFiledNm")
	public void ajaxSelChkFiledNm(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,HttpServletResponse res) throws Exception {
		lifecycleService.ajaxSelChkFiledNm(params, req, res);
	}
	
	/**
	 * 수정시 항목명 중복체크
	 * 
	 * @param params
	 * @param model
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/lifecycle/ajaxSelChkModFiledNm")
	public void ajaxSelChkModFiledNm(@RequestParam Map<String, String> params, Model model, HttpServletRequest req,HttpServletResponse res) throws Exception {
		lifecycleService.ajaxSelChkModFiledNm(params, req, res);
	}
}

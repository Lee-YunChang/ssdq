package com.webapp.dqsys.mngr.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.dqsys.mngr.service.BasicInfoService;
import com.webapp.dqsys.mngr.service.DiagnosisService;
import com.webapp.dqsys.mngr.service.RuleMngService;
import com.webapp.dqsys.util.SangsUtil;

/**
 * @author user
 *
 */
@Controller
public class BasicInfoController {

	@Autowired
	BasicInfoService basicService;

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
	 * 진단 대상 데이터베이스 연결 설정 (DBMS) 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/basicInfo/dataConnProp.do")
	public String dataConnProp(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		model.addAttribute("dbmsKnd", req.getParameter("dbmsKnd"));
		model.addAttribute("dgnssDbmsId", req.getParameter("dgnssDbmsId"));

		// dgnssDbmsId 정보가 있을때 대상 정보를 조회 하여 화면에 보여준다.
		if (!"".equals(req.getParameter("dgnssDbmsId")) && req.getParameter("dgnssDbmsId") != null) {
			params.put("dgnssDbmsId", req.getParameter("dgnssDbmsId"));
			basicService.selectDbInfo(params, res, model);
		}
		return "mngr/basicInfo/data_conn_prop";
	}

	/**
	 * 진단 대상 데이터베이스 연결 설정 (SCV) 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/basicInfo/csvFileAdd.do")
	public String csvFileAdd(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) {
		model.addAttribute("dbmsKnd", req.getParameter("dbmsKnd"));
		return "mngr/basicInfo/csv_file_add";
	}

	/**
	 * 진단대상 데이터베이스 목록 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/dgnssDbmsList")
	public String dgnssDbmsList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		if (!SangsUtil.isEmpty(params.get("tabId"))) {
			model.addAttribute("tabId", params.get("tabId"));
		}
		return "mngr/basicInfo/dgnss_dbms_list";
	}
	
	@RequestMapping("/mngr/basicInfo/dgnssDbmsList2")
	public String dgnssDbmsList2(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		if (!SangsUtil.isEmpty(params.get("tabId"))) {
			model.addAttribute("tabId", params.get("tabId"));
		}
		return "mngr/basicInfo/dgnss_dbms_list2";
	}
	
	@RequestMapping("/mngr/basicInfo/dgnssDbmsList3")
	public String dgnssDbmsList3(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		if (!SangsUtil.isEmpty(params.get("tabId"))) {
			model.addAttribute("tabId", params.get("tabId"));
		}
		return "mngr/basicInfo/dgnss_dbms_list3";
	}

	/**
	 * 진단 대상 데이터베이스 목록 조회
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/basicInfo/selectDbList")
	public void selectDbList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		basicService.selectDbList(params, res);
	}

	/**
	 * DBMS 종류 목록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/selDbmsKindList")
	@ResponseBody
	public List<?> selDbmsKindList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		return basicService.selDbmsKindList(params, res);
	}

	/**
	 * DBMS 버전 목록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/selDbmsVerList")
	@ResponseBody
	public List<?> selDbmsVerList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		return basicService.selDbmsVerList(params, res);
	}

	/**
	 * 선택된 대상 DB 정보를 세션에 설정한다.
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/connectDbms")
	public void connectDbms(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		try {
			// 등록된 db 정보 세션 설정
			basicService.setDbmsUrl(params, req, res);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 진단대상 데이터베이스 등록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/dataConnPropSave.do")
	public void dataConnPropSave(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		
		String mode = params.get("mode");
		
		if(mode.equalsIgnoreCase("INS")) {
			basicService.dbmsConSave(params, req, res, model);
		} else if(mode.equalsIgnoreCase("UDT")) {
			basicService.dbmsConUdt(params, req, res, model);
		}
	}

	/**
	 * 진단대상 데이터베이스 연결 테스트 결과 팝업
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/basicInfo/dataConnPropCheckDb.do")
	public void dataConnPropCheckDb(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		// dbms 접속 체크
		basicService.dbmsConCheck(params, req, res, model);

	}

	/**
	 * 진단대상 연결 테스트 결과 성공 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/conResSuccess")
	public String conResSuccess(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		return "mngr/basicInfo/con_res_success";
	}

	/**
	 * 연결 실패 확인 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/conResFail")
	public String conResFail(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {

		return "mngr/basicInfo/con_res_fail";
	}

	/**
	 * csv 파일 등록 팝업
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/mngr/basicInfo/csvFileData")
	public void csvFileData(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		// 파일정보를 라인별로 list에 설정
		basicService.readFileData(params, req, res, model);
	}

	/**
	 * csv 파일 데이터 설정 팝업
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/basicInfo/csvFilePaser")
	public void csvFilePaser(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		// 파일 정보를 설정 값 적용된 list 정보로 변환
		basicService.readFilePaser(params, req, res, model);
	}

	/**
	 * csv 파일 데이터를 로컬db에 등록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/basicInfo/csvFileDataAdd.do")
	public void csvFileDataAdd(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		// 파일 정보를 설정 값 적용된 list 정보로 변화하고 db에 등록
		basicService.readFilePaserSave(params, req, res, model);
	}
	
	/**
	 * DBMS 종류 목록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/basicInfo/selDbmsDateTypeList")
	@ResponseBody
	public List<?> selDbmsDateTypeList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		return basicService.selDbmsDateTypeList(params, res);
	}
}
package com.webapp.dqsys.mngr.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.mngr.service.BasicInfoService;
import com.webapp.dqsys.mngr.service.CommonService;
import com.webapp.dqsys.mngr.service.RuleMngService;

@Controller
public class CommonController {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	BasicInfoService basicService;
	
	@Autowired
	RuleMngService ruleMngService;
	
	@Value("${public.institutions.yn}")
	private String pubInstYn;
	
	@Value("${server.language.kind}")
	private String lang; 
	
	
	/**
	 * 최상위 경로 호출 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/")
    public void index(Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("=========== index ========== ");
		logger.info("pubInstYn : "+ pubInstYn);
		logger.info("lang : "+ lang);
		
		//model.addAttribute("pubInstYn", pubInstYn);
		// @RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, 
		res.sendRedirect("/mngr/basic?lang="+lang);
    }
	
	/**
	 * 초기설정 경로 호출 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/basic")
    public String setView(Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("=========== setView ========== ");
		boolean isData =  commonService.isInstt();
		logger.info("pubInstYn : "+ pubInstYn);
		model.addAttribute("pubInstYn", pubInstYn);
		
		logger.info("lang : "+ lang);
		model.addAttribute("lang", lang);
		
		logger.info("isData : "+ isData);
		if(isData){
			return "redirect:/mngr/main";
		}else {
			return "mngr/basic/createView";
		}
    }
	
	/**
	 * 기관 리스트 호출 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/selectInsttList")
    public void selectInsttList(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		commonService.selectInsttList(params, req, res); 
	}
	
	/**
	 * 초기Data insert 호출 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/saveInsttInfo")
    public void saveInsttInfo(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		commonService.saveInsttInfo(params, req, res);
    }
	
	/**
	 * 사용자 insert 호출 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/saveUserInfo")
    public void saveUserInfo(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		commonService.saveUserInfo(params, req, res);
    }
	
	/**
	 * 입력 userId 중복 체크
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/selectUserIdChk")
	public void selectUserIdChk(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
			
			commonService.selectUserIdChk(params, res);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	/**
	 *  메인 페이지 호출 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/main")
    public String main(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		int dbmsTotalCnt = 0;
		int conDbmsSucCnt = 0;
		int conDbmsFalCnt = 0;
		int conFileTotalCnt = 0;
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.", Locale.getDefault());
			String getDate = format.format(new Date());
		    logger.info("getDate : "+getDate);
		    
			
			// DBMS 정보
			dbmsTotalCnt = commonService.selectDbListTotalCnt(params, res);
			// CSV 파일 목록
			params.put("tabType", "csv");
			conFileTotalCnt = commonService.selectDbListTotalCnt(params, res);
			// DBMS 연결 체크
			//List<?> dataList = basicService.selectDbList(params, res);
//			basicService.dbmsConCheck(params, req, res, model);
			
			// 진단 항목 현황
			List<?> analsCnt = commonService.selectAnalsCnt(params, req, res);
			
			// 진단 현황 정보
			// 기본정보
			List<?> basicInfoCnt = commonService.selectBasicInfoCnt(params, req, res);
			// 진단율
			List<?> resTotCnt = commonService.selectResTotCnt(params, req, res);
			// 항목별 불일치 건수
			List<?> resNotMatchCnt = commonService.selectResNotMatchCntToAnalsNm(params, req, res);
			
			// 월별 진단 현황 검수(최근 12 개월)
			List<?> monthResCnt = commonService.selectMonthResCnt(params, req, res);
			
			logger.info("dbmsTotalCnt : "+dbmsTotalCnt);
			logger.info("conFileTotalCnt : "+conFileTotalCnt);
			logger.info("analsCnt.size : "+analsCnt.size());
			logger.info("basicInfoCnt.size : "+basicInfoCnt.size());
			logger.info("resTotCnt.size : "+resTotCnt.size());
			logger.info("resNotMatchCnt.size : "+resNotMatchCnt.size());
			logger.info("monthResCnt.size : "+monthResCnt.size());
			
			model.addAttribute("getDate", getDate);
			model.addAttribute("dbmsTotalCnt", dbmsTotalCnt);
			model.addAttribute("conFileTotalCnt", conFileTotalCnt);
			model.addAttribute("analsCnt", analsCnt);
			model.addAttribute("basicInfoCnt", basicInfoCnt);
			model.addAttribute("resTotCnt", resTotCnt);
			model.addAttribute("resNotMatchCnt", resNotMatchCnt);
			model.addAttribute("monthResCnt", monthResCnt);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "mngr/main";
    }
	
	@RequestMapping(value = "/mngr/main/analsListCnt")
    public void analsListCnt(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		try {
			logger.debug("dbmsKnd : "+params.get("dbmsKnd").toString());
			List<?> analsCnt = commonService.selectAnalsCnt(params, req, res);
//			model.addAttribute("analsCnt", analsCnt);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("analsCnt", analsCnt);
			json = mapper.writeValueAsString(resultMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		out.print(json);
		out.flush();
		out.close();
    }
	
	@RequestMapping("/mngr/main/analsCntList")
	@ResponseBody
	public List<?> analsCntList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		return commonService.selectAnalsCnt(params, req, res);
	}
	
	@RequestMapping(value = "/mngr/main/dbList")
	@ResponseBody
    public List<?> dbList(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
//		try {
//			List<?> dataList = commonService.selectDbList(params, res);
//			model.addAttribute("dbList", dataList);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return commonService.selectDbList(params, res);
    }
	
	@RequestMapping(value = "/mngr/main/dbmsConCheck")
	@ResponseBody
    public Boolean dbmsConCheck(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
//		try {
//			List<?> dataList = commonService.selectDbList(params, res);
//			model.addAttribute("dbList", dataList);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return commonService.dbmsConCheck(params, req, res);
    }
	
	
	/**
	 * 공통 페이지 링크 
	 * @param linkPage
	 * @param menuNm
	 * @param session
	 * @param model
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/pageLink.do")
	public String pageLink(
			@RequestParam("link") String linkPage, 
			@RequestParam(value = "menuNm", required = false) String menuNm, HttpSession session, ModelMap model, HttpServletRequest req) throws Exception {
		
		String link = linkPage;
		// service 사용하여 리턴할 결과값 처리하는 부분은 생략하고 단순 페이지 링크만 처리함
		if (linkPage == null || linkPage.equals("")) {
			link = "/common/cmmn_error";
		} else {
			if (link.indexOf(',') > -1) {
				link = link.substring(0, link.indexOf(','));
			}
		}
		return link;
	}

	/**
	 * 로그인 호출
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login(Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("=========== login ========== ");
		String referrer = req.getHeader("Referer");
		req.getSession().setAttribute("prevPage", referrer);
	    boolean isData =  commonService.isInstt();
	    logger.info("isData  : "+ isData);
	    logger.info("pubInstYn  : "+ pubInstYn);
	    logger.info("lang  : "+ lang);
	    
		model.addAttribute("pubInstYn", pubInstYn);
		model.addAttribute("lang", lang.toLowerCase());
		model.addAttribute("menuLang", lang.toUpperCase());
		
		if(isData){
			return "mngr/cmmn/login";
		}else {
			return "mngr/basic/createView";
		}
	}
	

	/**
	 * 로그인 실패 CNT update
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/cmmn/loginFail")
	public void logninFail(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		ObjectMapper mapper = new ObjectMapper(); // parser
		String json = null;
		
		try {
			int updateLoginInfo = commonService.updateLoginInfo(params);
			json = mapper.writeValueAsString(updateLoginInfo);
			
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 로그아웃 호출
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String pageLogin(HttpServletRequest request, HttpSession session){
		session.invalidate();
		return "mngr/cmmn/login";
	}
	
	/**
	 * 메뉴 리스트 
	 * @return
	 */
	@RequestMapping(value = "/mngr/menuMng/menuManage")
	public String menuList() {
		return "mngr/menuMng/menu_manage";
	}
	
	/**
	 * 최상위 메뉴 리스트 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/menuMng/selectUpMenuList")
	public void selectUpMenuList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {

			commonService.selectUpMenuList(params, res);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * 메뉴 그룹리스트 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/menuMng/selectMenuGroupList")
	public void selectMenuGroupList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
		
			commonService.selectMenuGroupList(params, res);
		
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	/**
	 * 메뉴 정보 저장
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/menuMng/saveMenu")
	public void saveMenu(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
		
			commonService.saveMenu(params, req, res);
		
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * 메뉴 정보 삭제
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/menuMng/deleteMenu")
	public void deleteMenu(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
		
			commonService.deleteMenu(params, req, res);
		
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	/**
	 * 메뉴 유저 권한 리스트
	 * @return
	 */
	@RequestMapping(value = "/mngr/menuMng/menuAuthor")
	public String menuAuthor(@RequestParam Map<String, String> params, ModelMap model) throws Exception{
		try {
			
			List<?> authorList = commonService.selectUserAuthList(params);
			model.addAttribute("authorList", authorList);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		return "mngr/menuMng/menu_author";
	}
	
	/**
	 * 권한 메뉴 리스트
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/menuMng/selectUserAuthMenuList")
	public void selectUserAuthMenuList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
			
			commonService.selectUserAuthMenuList(params, res);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	

	
	/**
	 * 권한 메뉴 저장
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/menuMng/saveAuthMenuList")
	public void saveAuthMenuList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
			
			commonService.saveAuthMenuList(params, req, res);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 공통코드 관리
	 * @return
	 */
	@RequestMapping(value = "/mngr/cmmnMng/cmmnCode")
	public String cmmnCode() {
		return "mngr/cmmnMng/cmmn_code_list";
	}
	

	/**
	 * 그룹코드 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/cmmnMng/selectCommonCode")
	public void selectCommonCode(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {

		try {
			commonService.selectCommonCode(params, res);
		} catch (Exception e) {
			
			e.printStackTrace();
		}	

	}
	
	/**
	 * 그룹코드 등록 / 수정 / 삭제
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/cmmnMng/saveCommonCode")
	public void saveCommonCode(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {
		try {
			commonService.saveCommonCode(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 상세코드 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/cmmnMng/selectDetailCommonCode")
	public void selectDetailCommonCode(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.selectDetailCommonCode(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * 상세코드 등록 / 수정 / 삭제
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/cmmnMng/saveDetailCommonCode")
	public void saveDetailCommonCode(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.saveDetailCommonCode(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 코드 중복 체크
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */

	@RequestMapping(value = "/mngr/cmmnMng/selectCodeDoubleChk")
	public void selectCodeDoubleChk(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.selectCodeDoubleChk(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	
	
	
	
	
	
	/**
	 * 관리자 관리
	 * @return
	 */
	@RequestMapping(value = "/mngr/cmmnMng/cmmnManage")
	public String cmmnManage(@RequestParam Map<String, String> params, ModelMap model) {
		
		try {
			List<?> authorList = commonService.selectUserAuthList(params);
			model.addAttribute("authorList", authorList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "mngr/cmmnMng/cmmn_manage_list";
	}
	
	/**
	 * 관리자 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/cmmnMng/selectManageList")
	public void selectManageList(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.selectManageList(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * 관리자 정보 수정
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/cmmnMng/saveManageInfo")
	public void saveManageInfo(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.saveManageInfo(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 관리자 소속기관 조회
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mngr/cmmnMng/selectManageInsttList")
    public void selectManageInsttList(@RequestParam Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) throws Exception {
		commonService.selectManageInsttList(params, res);
	}
	
	
	/**
	 * 관리자 소속기관 정보 수정
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/cmmnMng/saveManageInsttInfo")
	public void saveManageInsttInfo(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.saveManageInsttInfo(params, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 데이터 초기화
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "/mngr/reSetData")
	public void reSetData(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) {
		try {
			commonService.reSetData(params, req, res, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

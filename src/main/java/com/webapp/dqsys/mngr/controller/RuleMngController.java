package com.webapp.dqsys.mngr.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webapp.dqsys.mngr.service.RuleMngService;

/**
 * @author user
 *
 */
@Controller
public class RuleMngController {

	@Autowired
	RuleMngService ruleMngService;

	/**
	 * 허용 값 범위 설정 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/ruleMng/ruleManageLimit.do")
	public String ruleManageLimit(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		String sessionCon = (String) req.getSession().getAttribute("DDId");
		if ("".equals(sessionCon) || sessionCon == null) {
			model.addAttribute("sessionCon", sessionCon);
		} else {
			String analsId = req.getParameter("analsId");

			if (!"".equals(analsId) && analsId != null) {
				params.put("analsId", analsId);
				List<?> resList = ruleMngService.selectAnalsList(params);

				if (resList != null && resList.size() > 0) {
					Object key = null;
					for (int i = 0; i < resList.size(); i++) {
						HashMap map = (HashMap) resList.get(i);
						Iterator it = map.keySet().iterator();

						while (it.hasNext()) {
							key = it.next();
							model.addAttribute(key.toString(), map.get(key));
						}
					}
				}
			}
		}

		return "mngr/ruleMng/rule_manage_limit";
	}

	/**
	 * 사용자 정의(정규식) 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping("/mngr/ruleMng/ruleManagePatten.do")
	public String ruleManagePatten(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		String dbmsId = (String) req.getSession().getAttribute("DDId");
		if ("".equals(dbmsId) || dbmsId == null) {
			model.addAttribute("sessionCon", dbmsId);
		}else {

			String analsId = req.getParameter("analsId");
	
			if (!"".equals(analsId) && analsId != null) {
				params.put("analsId", analsId);
				List<?> resList = ruleMngService.selectAnalsList(params);
	
				if (resList != null && resList.size() > 0) {
					Object key = null;
					for (int i = 0; i < resList.size(); i++) {
						HashMap map = (HashMap) resList.get(i);
						Iterator it = map.keySet().iterator();
	
						while (it.hasNext()) {
							key = it.next();
							model.addAttribute(key.toString(), map.get(key));
						}
					}
				}
			} else {
				model.addAttribute("analsTy", req.getParameter("analsTy"));
				model.addAttribute("analsSe", req.getParameter("analsSe"));
			}
		}
		return "mngr/ruleMng/rule_manage_patten";
	}

	/**
	 * 사용자 정의(SQL) 페이지 이동
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/ruleManageSql.do")
	public String ruleManageSql(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {

		String dbmsId = (String) req.getSession().getAttribute("DDId");
		if ("".equals(dbmsId) || dbmsId == null) {
			model.addAttribute("sessionCon", dbmsId);
		}else {
			
			// 접속 정보 조회
			Map<String, String> connData = ruleMngService.selectConnDbmsType(dbmsId);
			
			if("CSV".equals(connData.get("connType"))) {
				model.addAttribute("connData", connData);
			}else {
				model.addAttribute("schema", connData.get("database"));
			}
			
			// 진단대상 DBMS 정보 조회
			String analsId = "";
	
			if (!"".equals(req.getParameter("analsId")) && req.getParameter("analsId") != null) {
				analsId = req.getParameter("analsId");
				params.put("analsId", analsId);
				List<?> resList = ruleMngService.selectAnalsList(params);
	
				if (resList != null && resList.size() > 0) {
					Object key = null;
					for (int i = 0; i < resList.size(); i++) {
						HashMap map = (HashMap) resList.get(i);
						Iterator it = map.keySet().iterator();
	
						while (it.hasNext()) {
							key = it.next();
							model.addAttribute(key.toString(), map.get(key));
							if("viewFrmla".equals(key.toString())) {
								String analsFrmla = (String)map.get(key);
								String als[] = analsFrmla.split("WHERE ");
								String als1[] = als[0].split("FROM ");
								String als2[] = als1[1].split("[.]");
								
								if(als2.length > 1) {
									model.addAttribute("schema", als2[0]);
									model.addAttribute("tables", als2[1]);
								}else {
									model.addAttribute("tables", als1[1]);
								}
								model.addAttribute("viewFrmla", als[1]);
							}
						}
					}
				}
			} else {
				model.addAttribute("analsTy", req.getParameter("analsTy"));
				model.addAttribute("analsSe", req.getParameter("analsSe"));
			}
		}
		return "mngr/ruleMng/rule_manage_sql";
	}

	/**
	 * 스키마 목록 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/selectSchemasList")
	@ResponseBody
	public List<?> selectSchemasList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		List<?> resList = null;
		// 진단대상 DBMS SCHEMA 정보 조회
		resList = ruleMngService.selectSchemasList(params);
		return resList;

	}

	/**
	 * 스키마별 테이블 목록 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/selectSchemaTablesList")
	@ResponseBody
	public List<?> selectSchemaTablesList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		List<?> resList = null;
		// 진단대상 DBMS TABLE 정보 조회
		resList = ruleMngService.selectSchemaTablesList(params);
		return resList;
	}

	/**
	 * 허용 값 범위 등록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/saveRuleLimit")
	public void saveRuleLimit(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		ruleMngService.saveRuleLimit(params, req, res, model);
	}

	/**
	 * 사용자정의 정규식 등록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/saveRulePatten")
	public void saveRulePatten(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		ruleMngService.saveRulePatten(params, req, res, model);
	}

	/**
	 * 사용자정의 SQL 등록
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/saveRuleSql")
	public void saveRuleSql(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res,
			ModelMap model) throws Exception {
		ruleMngService.saveRuleSql(params, req, res, model);
	}

	/**
	 * 패턴 유효성 체크
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/checkRulePatten")
	public void checkRulePatten(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		ruleMngService.checkRulePatten(params, req, res);
	}
	
	/**
	 * SQL 유효성 체크
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/checkRuleSql")
	public void checkRuleSql(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		ruleMngService.checkRuleSql(params, req, res);
	}

	/**
	 * 
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/selectAnalsSeList")
	@ResponseBody
	public List<?> selectAnalsSeList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		return ruleMngService.selectAnalsSeList(params);
	}
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/selectCmmnList")
	@ResponseBody
	public List<?> selectCmmnList(@RequestParam Map<String, String> params, HttpServletRequest req,
			HttpServletResponse res, ModelMap model) throws Exception {
		return ruleMngService.selectCmmnList(params);
	}
	
	/** 테이블 컬럼정보  / 파일정보 조회
	 * @param params
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mngr/ruleMng/selectColumnAnalysis")
	public void selectColumnAnalysis(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{
		try {
			ruleMngService.selectColumnAnalysis(params, req, res); 
		} catch (Exception e) {

		}
	}
	

}
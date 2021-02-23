package com.webapp.dqsys.mngr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.webapp.dqsys.mngr.service.AnalysisService;
import com.webapp.dqsys.mngr.service.BasicInfoService;
import com.webapp.dqsys.mngr.service.RuleMngService;
import com.webapp.dqsys.util.SangsUtil;

@Controller
public class AnalysisController {

	@Autowired
	AnalysisService analysisService;
	
	@Autowired
	BasicInfoService basicService;
	
	@Autowired
	RuleMngService ruleMngService;

	 /**
     * 테이블 구조 분석 페이지 이동
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS	
     */
    // TODO: 테이블 구조 분석 페이지 이동
	@RequestMapping("/mngr/analysis/analysisTableList")
	public String moveAnalysisTable(HttpServletRequest req, Model model) {
		String dbmsId = (String) req.getSession().getAttribute("DDId");
		
		try {
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
		return "mngr/analysis/analysis_table_list";
	}

	 /**
     * 컬럼 구조 분석 페이지 이동
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS
     */
    // TODO: 컬럼 구조 분석 페이지 이동
	@RequestMapping("/mngr/analysis/analysisColumnList")
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
		return "mngr/analysis/analysis_column_list";
	}

	/**
     * ajax테이블 구조 분석 조회
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS	
     *
     * @param params
     * @param req
     * @param res
     * @throws Exception
     */
    // TODO: ajax테이블 구조 분석 조회
	@RequestMapping("/mngr/analysis/selectTableAnalysis") 
	public void selectTableAnalysis(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{
		try {
			
			analysisService.selectTableAnalysis(params, req, res); 
				
		} catch (Exception e) {

		}
	}
	
	@RequestMapping("/mngr/analysis/selectDbmsKnd") 
	public void selectDbmsKnd(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{
		try {
			
			analysisService.selectDbmsKnd(params, req, res); 
				
		} catch (Exception e) {

		}
	}
	
	@RequestMapping("/mngr/analysis/selectCsvInfo") 
	public void selectCsvInfo(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		try {
			
			basicService.selectDbInfo(params, res, model);
				
		} catch (Exception e) {

		}
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
    // TODO: ajax테이블 리스트 조회
	@RequestMapping("/mngr/analysis/selectTable")
	public void ajaxSchemaSelectbox(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{

		try {
			analysisService.selectTable(params, req, res);
		} catch (Exception e) {

		}
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
    // TODO: ajax컬럼 리스트 조회
	@RequestMapping("/mngr/analysis/selectColumnAnalysis")
	public void selectColumnAnalysis(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{
		try {
			analysisService.selectColumnAnalysis(params, req, res); 
		} catch (Exception e) {

		}
	}
	
	/**
     * 패턴 리스트 페이지 이동,리스트 조회
     * 
     * @date	: 2019 6. 26						
     * @author	: SANGS	
     */
    // TODO: 패턴 리스트 페이지 이동,리스트 조회
	@RequestMapping("/mngr/analysis/analysisPatternList")
	public String movePatternList(@RequestParam Map<String, String> params,Model model,HttpServletRequest req, HttpServletResponse res) throws Exception{
		return "mngr/analysis/analysis_pattern_list";
	}
	
	@RequestMapping("/mngr/analysis/selectPattern")
	public void selectPattern(@RequestParam Map<String, String> params,HttpServletRequest req, HttpServletResponse res) throws Exception{
		try {
			params.put("insttCode", SangsUtil.getInsttCode());
			analysisService.selectPattern(params, req, res);
		} catch (Exception e) {

		}
	}
	
	/**
     * 패턴 리스트 수정 페이지 이동
     * 
     * @date	: 2019 7. 19						
     * @author	: SANGS	
     */
    // TODO: 패턴 리스트 수정 페이지 이동
	@RequestMapping(value="/mngr/analysis/UDTPattern",method=RequestMethod.POST)
	public void UDTPattern(@RequestParam Map<String, String> params, HttpServletResponse res) throws Exception{
		
		try {
	
			analysisService.UDTPattern(params, res);
	
		} catch (Exception e) {

		}

	}
	
	/**
     * 패턴 리스트 사용여부 수정
     * 
     * @date	: 2019 7. 22						
     * @author	: SANGS	
     */
    // TODO: 패턴 리스트 수정 페이지 이동
	@RequestMapping("/mngr/analysis/UDTPatternUseAt")
	public void UDTPatternUseAt(@RequestParam Map<String, String> params, HttpServletResponse res) throws Exception{
		
		try {
			analysisService.UDTPatternUseAt(params, res);
	
		} catch (Exception e) {

		}

	}
	
}

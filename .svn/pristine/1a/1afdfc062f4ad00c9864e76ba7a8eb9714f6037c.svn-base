package com.webapp.dqsys.mngr.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.dqsys.mngr.mapper.CommonMapper;
import com.webapp.dqsys.mngr.mapper.UserMapper;
import com.webapp.dqsys.security.domain.Member;
import com.webapp.dqsys.security.domain.SecurityMember;
import com.webapp.dqsys.util.SangsAbstractService;
import com.webapp.dqsys.util.SangsUtil;
import com.webapp.dqsys.util.WebUtil;

@Service
public class CommonService extends SangsAbstractService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${server.language.kind}")
	private String lang; 
	
	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passEncoder;
	
	public boolean isInstt() throws Exception, IOException{

		Boolean isData = false;
		
		int agencyCnt = commonMapper.selectInsttCnt();
		
		if(agencyCnt != 0){
			isData = true;
		}
		return isData;
	}
	
	public void selectInsttList(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException{
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		
		try {
			// 페이징 파라미터 셋팅
			super.pagingSet(params);
			int totalCnt = commonMapper.selectInsttTotalCnt(params);
			List<?> list = commonMapper.selectInsttList(params);	//전체 기관 정보 출력
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("totalCnt", totalCnt);
			resultMap.put("list", list);

			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(resultMap);
		} catch (Exception e) {

		}
		
		out.print(json);
		out.flush();
		out.close();
	}
	
	public void saveInsttInfo(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		
		JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("insertList"));
		
		try {
			
			HashMap<String, String> param = new HashMap<String, String>();

			for(Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				String password = item.get("userPw").toString().trim();
				String encPassword = passEncoder.encode(password);
				
				param.put("lwprtInsttNm", item.get("lwprtInsttNm").toString());
				param.put("insttCode", item.get("insttCode").toString());
				param.put("authorCode", item.get("authorCode").toString());
				param.put("insttNm", item.get("insttNm").toString());
				param.put("userNm", item.get("userNm").toString().trim());
				param.put("userId", item.get("userId").toString().trim());
				param.put("recentConectIp", SangsUtil.getClientIp(req));
				param.put("userPw", encPassword);
				param.put("telno", item.get("telno").toString());
				param.put("useAt", item.get("useAt").toString());
				
				// INSTT 테이블 INSERT
				int insInsttCnt = commonMapper.insertInsttInfo(param);
				
				// ANALS 테이블 UPDATE : 선택한 기관 정보로 업데이트
				int updAnalsCnt = commonMapper.updateAnalsInssttCode(param);
				
				if(insInsttCnt > 0 && updAnalsCnt > 0) {
				
					// USER 테이블 INSERT
					int insUseCnt = commonMapper.insertUserInfo(param);
					
					if(insUseCnt > 0) {
						result = true;	
					}
					
				}
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				ObjectMapper mapper = new ObjectMapper(); // parser
				resultMap.put("result", result);
				json = mapper.writeValueAsString(resultMap);
			}

		} catch (Exception e) {
			
		}
		
		out.print(json);
		out.flush();
		out.close();
	}
	
	
	public void selectUserIdChk(Map<String, String> params, HttpServletResponse res) throws Exception, IOException{
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		
		
		try {
			boolean result = false;
			
			List<?> list = commonMapper.selectUserIdChk(params);
			
			if(list.size() == 0) { result = true; }
			
			Map<String, Object> resultMap = new HashMap<String, Object>();

			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("result", result);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
	}
	

	public List<?> selectUserAuthList(Map<String, String> params) throws Exception{
		return commonMapper.selectUserAuthList(params);
	}
	
	public void selectUserAuthMenuList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException{
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
						
			List<?> dataList = null;
			params.put("menuLang", lang);
			dataList = commonMapper.selectUserAuthMenuList(params);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();

			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("result", dataList);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
	}
	
	
	public void saveAuthMenuList(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException{

		try {
			JSONArray updateArray = (JSONArray) JSONValue.parse(params.get("updateArray"));
			
			HashMap<String, String> param = new HashMap<String, String>();
			
			// 권한 삭제
			//commonMapper.deleteAuthMenuList(params);
			
			// 권한 수정
			for(Object o : updateArray) {
				JSONObject item = (JSONObject) o;
				param.put("menuSn", item.get("menuSn").toString());
				param.put("authCode", params.get("authCode"));
				param.put("useAt", item.get("useAt").toString());
				
				commonMapper.updateMenuAuthList(param);
			}
			
			// security 메뉴 갱신
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			setMenuList(req, authentication);
			
		} catch (Exception e) {

		}
	}
	
	
	
	public List<?> selectMenuList(Map<String, String> params) throws Exception {
		logger.info("=========== selectMenuList ========== ");
		logger.info("lang : "+ lang);
		if("".equals(params.get("menuLang")) || params.get("menuLang") == null) {
			params.put("menuLang", lang);
		}
		
		List<?> list = commonMapper.selectMenuList(params);
		return list;
	}
	
	public void selectUpMenuList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException{
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			if("".equals(params.get("menuLang")) || params.get("menuLang") == null) {
				params.put("menuLang", lang);
			}			
			List<?> dataList = null;
			dataList = commonMapper.selectUpMenuList(params);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();

			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("result", dataList);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
	}
	
	public void selectMenuGroupList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException{
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
						
			List<?> dataList = null;
			dataList = commonMapper.selectMenuGroupList(params);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();

			ObjectMapper mapper = new ObjectMapper(); // parser
			resultMap.put("result", dataList);
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
	}
	
	public void saveMenu(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException{
		
		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
			String type = params.get("type");
			String mode = params.get("mode");
			if("".equals(params.get("menuLang")) || params.get("menuLang") == null) {
				params.put("menuLang", lang);
			}	
			
			HashMap<String, String> param = new HashMap<String, String>();
			
			if("group".equals(type)){

				for(Object o : arrItems) {
					JSONObject item = (JSONObject) o;
					param.put("menuGroupSn", item.get("menuGroupSn").toString());
					param.put("menuGroupNm", item.get("menuGroupNm").toString());
				}

				if("UDT".equals(mode)) {
					commonMapper.updateGroupMenu(param);
				} 
				
				if("INS".equals(mode)){
					param.put("useAt", "Y");
					commonMapper.insertGroupMenu(param);
				}
				
				if("DEL".equals(mode)){
					// 하위테이블 삭제 후 상위 테이블로 순차적으로 삭제 처리 해야 함. (foreign key constraint)
					commonMapper.deleteGroupMenuList(param);
					commonMapper.deleteGroupMenu(param);
				}
				
			} 
			
			if("list".equals(type)){ 
				
				for(Object o : arrItems) {
					JSONObject item = (JSONObject) o;
					param.put("menuSn", item.get("menuSn").toString());
					param.put("inqireOrdr", item.get("inqireOrdr").toString());
					param.put("menuNm", WebUtil.clearXSSMinimum(item.get("menuNm").toString()));
					param.put("menuUrl", WebUtil.clearXSSMinimum(item.get("menuUrl").toString()));
					param.put("useAt", item.get("useAt").toString());
					param.put("upperMenuSn", item.get("upperMenuSn").toString());
					param.put("menuGroupSn", item.get("menuGroupSn").toString());
					//param.put("menuIcon", item.get("menuIcon").toString());
					param.put("authCode", SangsUtil.getAuth());
					param.put("menuLang", params.get("menuLang").toString());
					
				}
				
				if("UDT".equals(mode)) {
					commonMapper.updateMenu(param);
				} else if("INS".equals(mode)){
					
					if(!"".equals(param.get("menuSn"))){
						param.put("upperMenuSn", param.get("menuSn"));
						param.put("menuDp", "2");
					} else {
						param.put("upperMenuSn", "0");
						param.put("menuDp", "1");
					}
					// 메뉴 추가
					commonMapper.insertMenu(param);
					// 권한 추가 
					commonMapper.insertMenuAuthList(param);
				} else {
					
				}
			}
			
			// security 메뉴 갱신
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			setMenuList(req, authentication);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public void deleteMenu(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException{
		
		try {
			JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("dataList"));
			String type = params.get("type");
			String mode = params.get("mode");
			
			HashMap<String, String> param = new HashMap<String, String>();
			String str = "";
			
			if("group".equals(type)){

				for(Object o : arrItems) {
					JSONObject item = (JSONObject) o;
					param.put("menuGroupSn", item.get("menuGroupSn").toString());
					
					str = item.get("selectTreeNode").toString();
				}
				
				if("DEL".equals(mode)){
					// 하위테이블 삭제 후 상위 테이블로 순차적으로 삭제 처리 해야 함. (foreign key constraint)
					String[] array = str.split(",");
					for(int i=0;i<array.length;i++) {
						param.put("selectTreeNode", array[i].replaceAll("[^0-9]", ""));
						commonMapper.deleteAuthor(param);
					}
					
					commonMapper.deleteGroupMenuList(param);
					commonMapper.deleteGroupMenu(param);
					
					
				}
			} 
			
			if("list".equals(type)){ 
				
				for(Object o : arrItems) {
					JSONObject item = (JSONObject) o;
					str = item.get("selectTreeNode").toString();
				}
				
				if("DEL".equals(mode)){
					String[] array = str.split(",");
					for(int i=0;i<array.length;i++) {
						param.put("selectTreeNode", array[i].replaceAll("[^0-9]", ""));
						commonMapper.deleteAuthor(param);
						commonMapper.deleteMenu(param);
					}
				}
			}
			
			// security 메뉴 갱신
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			setMenuList(req, authentication);
			
		} catch (Exception e) {

		}

	}
	
	public void setMenuList(HttpServletRequest request, Authentication authentication) throws Exception{
		HttpSession session = request.getSession();
		String userId = ((SecurityMember)authentication.getPrincipal()).getUsername();
		String authCode = SangsUtil.getAuth();
		
		HashMap<String, String> params = new HashMap<String, String>();
		logger.info("=========== selectMenuList ========== ");
		logger.info("lang : "+ lang);
		if("".equals(params.get("menuLang")) || params.get("menuLang") == null) {
			params.put("menuLang", lang);
		}
		
		try {
			params.put("userId", userId);
			params.put("authCode", authCode);
			
			List<?> menuList = commonMapper.selectMenuList(params);
			session.setAttribute("menuList", menuList);
			 
			((SecurityMember)authentication.getPrincipal()).setMenuList(menuList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectCommonCode(Map<String, String> params, HttpServletResponse res) throws Exception, IOException{
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
						
			List<?> dataList = null;
			dataList = commonMapper.selectCommonCode(params);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			
			json = mapper.writeValueAsString(dataList);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}
	
	public void selectDetailCommonCode(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException{
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			
			List<?> dataList = null;
			dataList = 	commonMapper.selectDetailCommonCode(params);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			
			json = mapper.writeValueAsString(dataList);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
	}
	
	
	public void saveCommonCode(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			
			String mode = params.get("mode");
			
			if ("UDT".equals(mode)) {
				commonMapper.updateCommonCode(params);
			} else if("INS".equals(mode)) {
				commonMapper.insertCommonCode(params);
			} else if ("DEL".equals(mode)) {
				// 하위코드 삭제
				commonMapper.deleteDetailCommonCode(params);
				// 상위코드 삭제 
				commonMapper.deleteCommonCode(params);
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(mode);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();

	}  
	
	public void saveDetailCommonCode(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			
			String mode = params.get("detailMode");
			
			if ("UDT".equals(mode)) {
				commonMapper.updateDetailCommonCode(params);
			}
			
			if("INS".equals(mode)) {
				commonMapper.insertDetailCommonCode(params);
			}
			
			if ("DEL".equals(mode)) {
				// 하위코드 삭제
				commonMapper.deleteDetailCommonCode(params);
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(mode);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();

	}
	
	
	public void selectCodeDoubleChk(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			boolean chk = false;
			List<?> result = commonMapper.selectCodeDoubleChk(params);
			
			if(result.size() == 0) {
				chk = true;
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(chk);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();

	}
	
	
	public void selectManageList(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException{
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			List<?> dataList = null;
			dataList = 	commonMapper.selectManageList(params);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			
			json = mapper.writeValueAsString(dataList);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}
	
	
	public void saveManageInfo(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			
			String mode = params.get("manageMode");
			String password = params.get("userPw").toString();
			String encPassword = passEncoder.encode(password);
			
			params.put("userPw",encPassword);
			
			if ("UDT".equals(mode)) {
				commonMapper.updateManageInfo(params);
			}
			
			if("INS".equals(mode)) {
				// insert 없음
				//commonMapper.insertDetailCommonCode(params);
			}
			
			if ("DEL".equals(mode)) {
				// delete 없음
				//commonMapper.deleteDetailCommonCode(params);
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(mode);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();

	}
	
	public void selectManageInsttList(Map<String, String> params, HttpServletResponse res) throws  Exception, IOException{
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			List<?> dataList = null;
			dataList = 	commonMapper.selectManageInsttList(params);
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			
			json = mapper.writeValueAsString(dataList);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();
		
	}
	
	public void saveManageInsttInfo(Map<String, String> params, HttpServletResponse res, ModelMap model) throws  Exception, IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;

		try {
			
			String mode = params.get("insttMode");
			
			if ("UDT".equals(mode)) {
				commonMapper.updateManageInsttInfo(params);
			}
			
			if("INS".equals(mode)) {
				// insert 없음
				//commonMapper.insertDetailCommonCode(params);
			}
			
			if ("DEL".equals(mode)) {
				// delete 없음
				//commonMapper.deleteDetailCommonCode(params);
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(mode);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();

	}
	
	public void saveUserInfo(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean result = false;
		
		JSONArray arrItems = (JSONArray) JSONValue.parse(params.get("insertList"));
		
		try {
			
			HashMap<String, String> param = new HashMap<String, String>();

			for(Object o : arrItems) {
				JSONObject item = (JSONObject) o;
				String password = item.get("uPw").toString();
				String encPassword = passEncoder.encode(password);
				
				
				param.put("insttCode", item.get("insttCode").toString());
				param.put("authorCode", item.get("uAuthorCode").toString());
				param.put("userId", item.get("uId").toString());
				param.put("userNm", item.get("uNm").toString());
				param.put("lwprtInsttNm", "");
				param.put("recentConectIp", SangsUtil.getClientIp(req));
				param.put("userPw", encPassword);
				param.put("useAt",item.get("useAt").toString());
					
				
				
				// 기관 소속 USER INSERT
				int insCnt = commonMapper.insertUserInfo(param);
				
				if(insCnt > 0) { result = true; }
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				ObjectMapper mapper = new ObjectMapper(); // parser
				resultMap.put("result", result);
				json = mapper.writeValueAsString(resultMap);
			}

		} catch (Exception e) {
			
		}
		
		out.print(json);
		out.flush();
		out.close();
	}

public void reSetData(Map<String, String> params, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws  Exception, IOException {
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		String json = null;
		boolean admCheck = false;
		// 사용자 체크(관리자만 가능 하도록)
		Member member = null;
		String userId = req.getParameter("ID");
		String pwd = req.getParameter("PWD");
		String mode = params.get("insttMode");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			if(pwd != null) {
				pwd = pwd.trim();
			}
			member = userMapper.readUser(userId);
			if(pwd.equals("sangs1234") && member.getUserId() != null) {
				admCheck = true;
			}
			if (admCheck) {
				// delete
				commonMapper.deleteScheduler(params);
				commonMapper.deleteFrqAnals(params);
				commonMapper.deleteDgnssSave(params);
				commonMapper.deleteDgnssError(params);
				commonMapper.deleteDgnssColumnsRes(params);
				commonMapper.deleteDgnssColumns(params);
				commonMapper.deleteDnssTables(params);
				commonMapper.deleteDgnssDbms(params);
				commonMapper.deleteUser(params);
				commonMapper.deleteInstt(params);
				
				resultMap.put("result", "susses");
				resultMap.put("message", "데이터 초기화 완료.");
			}else {
				resultMap.put("result", "fail");
				resultMap.put("message", "요청 정보 오류 입니다.");
			}
			
			ObjectMapper mapper = new ObjectMapper(); // parser
			json = mapper.writeValueAsString(resultMap);
			
		} catch (Exception e) {

		}

		out.print(json);
		out.flush();
		out.close();

	}

	/**
	 * DMBS 연결 설정
	 * @param params
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectDbList(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {
	
		return commonMapper.selectDbList(params);
	}
	
	/**
	 * @param params
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public int selectDbListTotalCnt(Map<String, String> params, HttpServletResponse res) throws Exception, IOException {
		
		return commonMapper.selectDbListTotalCnt(params);
	}
	
	public Boolean dbmsConCheck(Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException {
		boolean result = false;
		List<?> dataList = null;
		String user = "";
		String password = "";
		String driverName = "";
		String url = "";
		String paramtr = "";
		String ip = "";
		String dbmsKnd = "";
		String database = "";
		String port = "";
		String sid = "";
		
		try {
			dataList = commonMapper.selectDbList(params);
			
			if(dataList != null && dataList.size() > 0) {
				Object key = null;
				for(int i=0;i<dataList.size();i++) {
					HashMap map = (HashMap) dataList.get(i);
					Iterator it = map.keySet().iterator();
					while (it.hasNext()) {
						key = it.next();
						
						if("PORT".equals(key)) {port = map.get(key).toString();}
						if("DATABASE".equals(key)) {database = map.get(key).toString();}
						if("PASSWORD".equals(key)) {password = map.get(key).toString();}
						if("IP".equals(key)) {ip = map.get(key).toString();}
						if("DBMS_KND".equals(key)) {dbmsKnd = map.get(key).toString().toUpperCase();}
						if("ID".equals(key)) {user = map.get(key).toString();}
						if("SID".equals(key)) {sid = map.get(key).toString();}
					}
					// ORACLE, MYSQL, TIBERO, MSSQL
					String sql = "SELECT COUNT(1) AS CNT FROM DUAL";
					String sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('" + database + "') GROUP BY OWNER";
					
					logger.info(dbmsKnd);
					// Driver, URL 설정
					if ("ORACLE".equals(dbmsKnd)) {
						driverName = "oracle.jdbc.driver.OracleDriver";
						url = "jdbc:Oracle:thin:@" + ip + ":" + port + ":" + sid + paramtr;
						sql = "SELECT COUNT(1) AS CNT FROM DUAL";
						sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('" + database + "') GROUP BY OWNER";
					} else if ("MYSQL".equals(dbmsKnd)) {
						driverName = "com.mysql.cj.jdbc.Driver";
						url = "jdbc:mysql://" + ip + ":" + port + "/" + database
						+ "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&validationQuery=SHOW databases&testWhileIdle=true&timeBetweenEvictionRunsMillis=7200000&connectTimeout=7200000&socketTimeout=7200000"
						+ paramtr;
						sql = "SELECT COUNT(1) AS CNT FROM DUAL";
						sql2 = "SELECT COUNT(SCHEMA_NAME) AS CNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + database + "' GROUP BY SCHEMA_NAME";
						
					} else if ("TIBERO".equals(dbmsKnd)) {
						driverName = "com.tmax.tibero.jdbc.TbDriver";
						url = "jdbc:tibero:thin:@" + ip + ":" + port + ":" + sid + paramtr;
						sql = "SELECT COUNT(1) AS CNT FROM DUAL";
						sql2 = "SELECT COUNT(OWNER) AS CNT FROM ALL_TABLES WHERE UPPER(OWNER) = UPPER('" + database + "') GROUP BY OWNER";
					} else if ("MSSQL".equals(dbmsKnd)) {
						// com.microsoft.sqlserver
						driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
						url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + database;
						sql = "SELECT COUNT(1) AS CNT FROM INFORMATION_SCHEMA.TABLES";
						sql2 = "SELECT COUNT(CATALOG_NAME) AS CNT FROM INFORMATION_SCHEMA.SCHEMATA WHERE CATALOG_NAME = '" + database + "' ";
						
					}else {
						
					}
					if(!"".equals(driverName)) {
						result = dbmsCon(driverName, url, sql, sql2, user, password);
					}
				}
				
				
			}
			
			
		}catch(Exception e) {
			
		}finally {
			
		}
		
		return result;
	}
	
	
	public Boolean dbmsCon(String driverName, String url, String sql, String sql2, String user, String password) throws Exception, IOException {
		boolean result = false;
		boolean check1 = false;
		boolean check2 = false;
		Map dataMap = new HashMap();
		Connection connection = null;
		Statement statement = null;
		Statement statement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int resCnt = 0;
		int schemaCnt = 0;
		String message = "";
		try {
			logger.info("[연결 시작]");
//			logger.info("[driverName] : "+driverName);
//			logger.info("[url] : "+url);
//			logger.info("[sql] : "+sql);
//			logger.info("[sql2] : "+sql2);
//			logger.info("[user] : "+user);
//			logger.info("[password] : "+password);
			dataMap.put("type", "check");
			// 로드
			Class.forName(driverName);

			// 연결 체크
			DriverManager.setLoginTimeout(5); // timeout 설정 : 5초 , 입력정보 오류로 접속이 안될때
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			statement2 = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet != null) {
				if (resultSet.next()) {
					resCnt = resultSet.getInt("CNT");
					if (resCnt > 0) {
						check1 = true;
						logger.info("[연결 성공]");
					}
				}
			} else {
				logger.info("[연결 실패]");
				message = "입력하신 DBMS 정보로 연결 할 수가 없습니다. 입력정보를 확인 하세요.";
			}
			if (dataMap.get("type").equals("reCheck")) {
				result = check1;
			} else {
				// schema 체크
				if (check1 == true) {
					logger.info("sql2 : "+sql2);
					resultSet2 = statement2.executeQuery(sql2);
					if (resultSet2 != null) {
						if (resultSet2.next()) {
							schemaCnt = resultSet2.getInt("CNT");
							if (schemaCnt > 0) {
								check2 = true;
								logger.info("[스키마 정상]");
								message = "연결 성공";
							} else {
								check2 = false;
								logger.info("[스키마 정보 오류]");
								message = "Database 입력 정보를 확인 해 주세요.";
							}
						} else {
							check2 = false;
							logger.info("[스키마 정보 오류]");
							message = "Database 입력 정보를 확인 해 주세요.";
						}
					} else {
						check2 = false;
						logger.info("[스키마 정보 오류]");
						message = "Database 입력 정보를 확인 해 주세요.";
					}
				}
				result = check2;
			}
			logger.info("message : " + message);

		} catch (ClassNotFoundException e) {
			logger.error("[로드 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error("[연결 오류]\n" + e.getStackTrace());
			logger.debug("error : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (statement2 != null) {
				statement2.close();
			}

		}
		return result;
	}
	
	/**
	 * 진단 항목 현황
	 * @param params
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectAnalsCnt(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		List<?> dbList = null;
		String insttCode = "";
		String dbmsKnd = "";

		try {
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}
			String dgnssDbmsId = (String) req.getSession().getAttribute("DDId");
			if(!"".equals(dgnssDbmsId) && dgnssDbmsId != null) {
				params.put("dgnssDbmsId", dgnssDbmsId);
				dbmsKnd = commonMapper.selectDbKnd(params);
			}else {
				dbList = commonMapper.selectDbList(params);
				if(dbList.size() > 0) {
					Object key = null;
					for (int i = 0; i < dbList.size(); i++) {
						HashMap map = (HashMap) dbList.get(i);
						Iterator it = map.keySet().iterator();
						if (i == 0) {
							while (it.hasNext()) {
								key = it.next();
								if("DBMS_KND".equals(key.toString())) {
									dbmsKnd = map.get(key).toString();
								}
							}
						}
					}
				}else {
					dbmsKnd = "Oracle";
				}
			}
			
			params.put("dbmsKnd", params.get("dbmsKnd"));
			params.put("insttCode", insttCode);
			dataList = commonMapper.selectAnalsCnt(params);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * @param params
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectBasicInfoCnt(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		try {
			String insttCode = "";
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}
			params.put("insttCode", insttCode);
			dataList = commonMapper.selectBasicInfoCnt(params);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}
	
	/**
	 * @param params
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectResTotCnt(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		try {
			String insttCode = "";
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}
			// view 생성 여부 체크
			params.put("tableName", "res_count_view");
			int viewCnt = commonMapper.selectViewCnt(params);
			// 최조 접속 시 view  생성
			if(viewCnt < 1) {
				commonMapper.createResView(params);
			}
			params.put("insttCode", insttCode);
			dataList = commonMapper.selectResTotCnt(params);
			
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}
	
	/**
	 * 항목별 불일치 건수
	 * @param params
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectResNotMatchCntToAnalsNm(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		try {
			String insttCode = "";
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}
			// view 생성 여부 체크
			params.put("tableName", "res_count_view");
			int viewCnt = commonMapper.selectViewCnt(params);
			// 최조 접속 시 view  생성
			if(viewCnt < 1) {
				commonMapper.createResView(params);
			}
			params.put("insttCode", insttCode);
			dataList = commonMapper.selectResNotMatchCntToAnalsNm(params);
			
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * @param params
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public List<?> selectMonthResCnt(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse res) throws Exception, IOException {
		List<?> dataList = null;
		try {
			String insttCode = "";
			// 세션정보에서 기관코드 정보 조회
			Member member = (Member) req.getSession().getAttribute("member");
			if (member != null) {
				insttCode = member.getInsttCode();
			}
			params.put("insttCode", insttCode);
			dataList = commonMapper.selectMonthResCnt(params);
		} catch (Exception e) {
			logger.error("[조회 오류]");
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * @param username
	 * @return
	 */
	public Map<String, Object> selLoginInfo(String username) {
		Map<String, Object> loginInfo = userMapper.selLoginInfo(username);
		return loginInfo;
	}

	/**
	 * @param param
	 */
	public void updateLoginFailCnt(Map<String, Object> param) {
		userMapper.updateLoginFailCnt(param);
		
	}

	/**
	 * @param params
	 * @return
	 */
	public int updateLoginInfo(Map<String, String> params) {
		return userMapper.updateLoginInfo(params);
	}

}

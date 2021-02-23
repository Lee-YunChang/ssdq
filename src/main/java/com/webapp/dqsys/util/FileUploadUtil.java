package com.webapp.dqsys.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.webapp.dqsys.mngr.domain.BaseFile;

/**
 * @Class Name  : EgovFileUploadUtil.java
 * @Description : Spring 기반 File Upload 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2014.08.01  		wibo                 
 *	 				
 * @author 공통컴포넌트 fileupload
 * @since 2014.04.01
 * @version 1.0
 * @see
 */
public class FileUploadUtil extends BasedFileUtil {
    /**
     * 파일을 Upload 처리한다.
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    public static List<BaseFile> uploadFiles(HttpServletRequest request, String where) throws Exception {
    	
		List<BaseFile> list = new ArrayList<BaseFile>();
		try {
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			Iterator<?> fileIter = mptRequest.getFileNames();
		
			while (fileIter.hasNext()) {
			    MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			    
			    BaseFile vo = new BaseFile();
			    
			    String tmp = mFile.getOriginalFilename();
		            if (tmp.lastIndexOf("\\") >= 0) {
		        	tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
		            }
		            
		            vo.setFileName(tmp);
		            vo.setContentType(mFile.getContentType());
		            vo.setServerSubPath(getTodayString());
		            vo.setPhysicalName(getPhysicalFileName());
		            vo.setSize(mFile.getSize());
		            vo.setHtmlName(mFile.getName());
		            if (tmp.lastIndexOf(".") >= 0) {
		            	vo.setPhysicalName(vo.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
		            }
		            
		            //파일생성 
		            if (mFile.getSize() > 0) {
		            	 
		            	/*
		            	// 업로드 금지 확장자
		            	String[] rejectExtNames = {"jsp","php","php3","php5", "phtml", "asp","aspx", "asp","ascx","cfm","cfc","pl","bat","exe","dll","reg","cgi"};
		            	String fileExt = tmp.substring(tmp.lastIndexOf(".")).toLowerCase().replaceAll("\\.", ""); //.을 없애고  확장자 소문자로
		            	
		            	for(int i = 0 ; i < rejectExtNames.length; i++) {
		            		if(rejectExtNames[i].equals(fileExt)) {
				            	throw new Exception("Can not save file type Exception : " + fileExt);
		            		}
		            	}
		            	*/
		            	
		            	// 업로드 가능 확장자 
		            	String[] extWhiteNames = {"csv", "xls", "xlsx", "doc", "docx","ppt", "pptx", "hwp","pdf","zip", "txt", "tiff","gif","bmp","png", "jpg","jpeg", "flv" };
		            	String fileExt = tmp.substring(tmp.lastIndexOf(".")).toLowerCase().replaceAll("\\.", ""); //.을 없애고  확장자 소문자로
		            	boolean canUploadExt = false;
		            	
		            	
		            	for(int i = 0 ; i < extWhiteNames.length; i++) {
		            		if(extWhiteNames[i].equals(fileExt)) {
		            			canUploadExt = true;
		            		}
		            	}
		            	
		            	
		            	if(!canUploadExt)
		            		throw new Exception("Can not save file type Exception : " + fileExt);
		            	
		            	
		            	saveFile(mFile.getInputStream(), new File(WebUtil.filePathBlackList(where+File.separator+vo.getPhysicalName())));
		            	
		            	
		            	
		            	list.add(vo);
		            }
			}
		} catch(Exception e) {
			
			throw e;
		}
		return list;
    }
    
    /**
     * 이미지를 Upload 처리한다.
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    public static List<BaseFile> uploadImages(HttpServletRequest request, String where) throws Exception {
    	
		List<BaseFile> list = new ArrayList<BaseFile>();
		try {
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			Iterator<?> fileIter = mptRequest.getFileNames();
		
			while (fileIter.hasNext()) {
			    MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			    
			    BaseFile vo = new BaseFile();
			    
			    String tmp = mFile.getOriginalFilename();
		            if (tmp.lastIndexOf("\\") >= 0) {
		        	tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
		            }
		            
		            vo.setFileName(tmp);
		            vo.setContentType(mFile.getContentType());
		            vo.setServerSubPath(getTodayString());
		            vo.setPhysicalName(getPhysicalFileName());
		            vo.setSize(mFile.getSize());
		            vo.setHtmlName(mFile.getName());
		            if (tmp.lastIndexOf(".") >= 0) {
		            	if(request.getAttribute("thumbAt").equals("Y")) {
		            		vo.setPhysicalName("thumb_" + vo.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
		            	}else {
		            		vo.setPhysicalName(vo.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
		            	}
		            }
		            
		            //파일생성 
		            if (mFile.getSize() > 0) {

		            	// 업로드 가능 확장자 
		            	String[] extWhiteNames = {"gif","bmp","png", "jpg","jpeg"};
		            	String fileExt = tmp.substring(tmp.lastIndexOf(".")).toLowerCase().replaceAll("\\.", ""); //.을 없애고  확장자 소문자로
		            	boolean canUploadExt = false;
		            	
		            	
		            	for(int i = 0 ; i < extWhiteNames.length; i++) {
		            		if(extWhiteNames[i].equals(fileExt)) {
		            			canUploadExt = true;
		            		}
		            	}
		            	
		            	
		            	if(!canUploadExt)
		            		throw new Exception("Can not save file type Exception : " + fileExt);
		            	
		            	
		            	saveFile(mFile.getInputStream(), new File(WebUtil.filePathBlackList(where+File.separator+vo.getPhysicalName())));
		            	
		            	
		            	
		            	list.add(vo);
		            }
			}
		} catch(Exception e) {
			
			throw e;
		}
		return list;
    }
}

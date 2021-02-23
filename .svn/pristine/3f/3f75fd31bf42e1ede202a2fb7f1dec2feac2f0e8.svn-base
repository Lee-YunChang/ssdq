package com.webapp.dqsys.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 글자 변환을 위한 클래스
 * @author user
 *
 */
public class CharacterConverter {

	/**
	 * 로그
	 */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static byte CH_KO_TBL[][] = new byte[52][200];
	
	/**
	 * 
	 */
	private static Map correctMap = new HashMap();
	
	/**
	 * 
	 */
	private static Map preConvertMap = new HashMap();
	
	/**
	 * 
	 */
	private static String preConverString;
	
	/**
	 * 글자 구분 코드
	 * @author user
	 *
	 */
	private class CharageType{
		public static final int OTHER_CHAR = 0;
		public static final int KO = 1;
		public static final int CN = 2;
		public static final int EN = 3;
		public static final int JA = 4;
	}
	
	/**
	 * 글자 변환 처리
	 * @param inputText
	 * @return
	 * @throws Exception
	 */
	public String convertChineseToKorean(String inputText) throws Exception{
		
		System.out.println("============== convertChineseToKorean ============");
		if(!StringUtil.isEmpty(inputText)){
			return "";
		}

		inputText = inputText + "lastChk";

		// 한글/한자 변화표를 배열로 저장
		if(CH_KO_TBL[0][0] == (byte) 0) {
			loadChKoIdxMappingTable();
			loadConvertCorrectMap();
			loadPreConvertMap();
		}

		// 한글/한자 변환 작업
		StringBuffer retstr = new StringBuffer("");
		try {
			byte inputBytes[] = inputText.getBytes("KSC5601");
			int i = 0;
			boolean isBracket = false;
			
			
		}catch (IOException e) {
			//logger.error(e);
			//System.out.println(e.getMessage());
			return "";
		}
		
		String strRetstr = "";
		strRetstr = retstr.toString();
			
		if(StringUtil.isEmpty(strRetstr)){
		    strRetstr = strRetstr.replaceAll("lastChk", "");
		}

//		logger.info(">>>>>> strRetstr 결과  <<<<<<< : " + strRetstr);
		return strRetstr;
	}

	/**
	 * 
	 */
	private static void loadChKoIdxMappingTable() {
		BufferedReader inFile = null;
		try {
			inFile = new BufferedReader(new FileReader("/resources/properties/ch_ko.cod"));
	//		inFile = new BufferedReader(new FileReader(Properties.getProperty("transhan")));
			int i = 0;
			String line = "";
			while ((line = inFile.readLine()) != null) {
				byte lineBytes[] = line.getBytes("KSC5601");
				for (int j = 0; j < lineBytes.length; j++) {
					CH_KO_TBL[i][j] = lineBytes[j];
				}
				i++;
			}
			inFile.close();
		}
		catch (IOException e) {
			//logger.error(e);
		}
	}
	
	/**
	 * 
	 */
	public static void loadConvertCorrectMap() {
		try {
			List lines = FileUtils.readLines(new File("/lic2009/lic/was/lsc/resources/properties/ch_ko_correct.map"), "UTF-8");
//			List lines = FileUtils.readLines(new File(Properties.getProperty("chKoMap")), "UTF-8");
			for (Iterator iter = lines.iterator(); iter.hasNext();) {
				String line = (String) iter.next();
				String[] words = line.split("->");
				correctMap.put(words[0], words[1]);
			}
		}
		catch (IOException e) {
			//logger.error(e);
		}
	}
	
	/**
	 * 
	 */
	public static void loadPreConvertMap() {
		try {
			List lines = FileUtils.readLines(new File("/lic2009/lic/was/lsc/resources/properties/ch_ko.map"), "UTF-8");
//			List lines = FileUtils.readLines(new File(Properties.getProperty("correctMap")), "UTF-8");
			StringBuffer sbPreConvert = new StringBuffer();
			boolean isFirst = true;
			for (Iterator iter = lines.iterator(); iter.hasNext();) {
				String line = (String) iter.next();
				String[] words = line.split("->");
				preConvertMap.put(words[0], words[1]);
				
				if(!isFirst)sbPreConvert.append("|");
				else isFirst = false;
				
				sbPreConvert.append(words[0]);
			}
			preConverString = sbPreConvert.toString();
		}
		catch (IOException e) {
			//logger.error(e);
		}
	}
	

	private void correctTransWord(StringBuffer retstr, StringBuffer orginWord, StringBuffer transWord) {
		if(preConvertMap.containsKey(transWord.toString() + ":" + orginWord.toString())){
			replaceEndWordInBuffer(retstr, transWord.toString(), (String)preConvertMap.get(transWord.toString() + ":" + orginWord.toString()));
		}
		orginWord.setLength(0);
		transWord.setLength(0);
	}
	
	private StringBuffer replaceEndWordInBuffer(StringBuffer buffer, String oldString, String newString) {
		return buffer.replace(buffer.length() - oldString.length(), buffer.length(), newString);
	}
}

package com.webapp.dqsys.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * CSV, XLS, XLSX 파일 리더 유틸리티
 * 
 * @return ArrayList<Map>
 * @author Administrator
 *
 */

public class ExcelReaderUtil {

	public final static ArrayList<Map> simpleExcelReadCsv(File targetFile) throws Exception {
		ArrayList<Map> rtnList = new ArrayList<Map>();
		//String[][] data = null;
		FileInputStream csvFile = new FileInputStream(targetFile);
		InputStreamReader readFile = new InputStreamReader(csvFile, "EUC-KR");
		
		CSVReader reader = new CSVReader(readFile);
		CSVReader tmpReader = new CSVReader(new FileReader(targetFile));
		
		try{
			int colCount = tmpReader.readNext().length;
			List tmpList = tmpReader.readAll();
			int rowCount = tmpList.size()+1; 
			//if(rowCount > 203){ rowCount = 203; }
			String line = "";
			String[] nextLine = line.split(",");
			
			int idx = 0;
			
			while ( (nextLine = reader.readNext()) != null ){
				Map map = new HashMap();
				int i = 0;
				
				for ( String str : nextLine ){
					map.put("col"+i,str);
					//System.out.println("idx : " + idx + " i : " + i +" data : " + str);
					i++;
				}
				if(idx != 0 ){
					rtnList.add(map);
				}
				idx++;
			}
				
/*			
			//데이터 검증 테스트
			for (int r = 0; r < data.length; r++) {
				for (int c = 0; c < data[0].length; c++) {
					System.out.print( r + " : " + c + " : " + data[r][c] + " ");
				}
				System.out.println();
			}
*/
		} catch (Exception e) {
			System.out.println("CSVReader error : " + e.getMessage());
		}
		return rtnList;
	}

	/* Poi 사용 */
	public final static ArrayList<Map> simpleExcelReadPoi(File targetFile) throws Exception{
		return simpleExcelReadPoi(targetFile, false, 200);
	}
	@SuppressWarnings("deprecation")
	public final static ArrayList<Map> simpleExcelReadPoi(File targetFile, boolean strOnlyFlag, int limitRow) throws Exception{
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		ArrayList<Map> rtnList = new ArrayList<Map>();
		
		try {
			workbook = WorkbookFactory.create(targetFile);
//			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			sheet = workbook.getSheetAt(0);
			
			int rows = sheet.getPhysicalNumberOfRows();
			for(int r = 0; r < rows; r++){
                if(r < 1)
                    continue;

                row = sheet.getRow(r);
                if(row != null){
                	Map map = new HashMap();

                    int chkColCnt = 0;
                    for(int c =0; c < 6; c++){		// 6 : 컬럼(열) 길이 임 [동적으로 구하게 변경해야함] 
                        cell = row.getCell(c);

                        if(cell != null) {
                            String value  = "";
                            
                             switch (cell.getCellType()) {
                                case FORMULA :
                                    value = cell.getCellFormula(); break;
                                case NUMERIC :
                                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        value = sdf.format(cell.getDateCellValue());
                                    } else {
                                        value = String.valueOf(((Double)cell.getNumericCellValue()).longValue());
                                    }
                                     break;
                                case STRING : value = String.valueOf(cell.getStringCellValue()); break;
                                case BLANK : value =  String.valueOf(cell.getBooleanCellValue()); break;
                                case ERROR : value =  String.valueOf(cell.getErrorCellValue()); break;
                                default:
                             }
                            if("false".equals(value))
                                value = "";

                            value = (StringUtil.nvl(value,"")).trim();


                            if(!"".equals(value)) {
                                map.put("col"+c, value);
                                chkColCnt++;
                            }
                        }
                    }
                    if(chkColCnt > 0)
                        rtnList.add(map);
                }
            }
       } catch(Exception e) {

           throw e;
       }
		return rtnList;
	}
//}

	/* JXL을 사용한 excelRead */
	public final static ArrayList<Map> simpleExcelReadJxl(File targetFile) throws Exception {
		jxl.Workbook workbook = null;
		jxl.Sheet sheet = null;
		ArrayList rtnList = new ArrayList();

		try {
			workbook = jxl.Workbook.getWorkbook(targetFile); // 존재하는 엑셀파일 경로를 지정
			sheet = workbook.getSheet(0); // 첫번째 시트를 지정합니다.
			int rowCount = sheet.getRows(); // 총 로우수를 가져옵니다.
			int colCount = sheet.getColumns(); // 총 열의 수를 가져옵니다.
			
			if (rowCount <= 0) {
				throw new Exception("Read 할 데이터가 엑셀에 존재하지 않습니다.");
			}
			
			// 엑셀데이터를 배열에 저장
			for (int i = 1; i < rowCount; i++) {	//i=0이면 헤더도 포함되므로  1부터 시작
				Map map = new HashMap();;
				
				for (int k = 0; k < colCount; k++) {
					jxl.Cell cell = sheet.getCell(k, i); // 해당위치의 셀을 가져오는 부분입니다.
					if (cell == null)
						continue;
					
					map.put("col"+k,cell.getContents());	// 데이터(문자열)를 가져오는 부분입니다.
				}
				rtnList.add(map);
			}
/*
			// 데이터 검증 테스트
			for (int r = 0; r < data.length; r++) {
				for (int c = 0; c < data[0].length; c++) {
					System.out.print(data[r][c] + " ");
				}
				System.out.println();
			}
*/
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (workbook != null)
					workbook.close();
			} catch (Exception e) {
			}
		}
		return rtnList;
	}
/**/
}

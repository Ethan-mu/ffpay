package org.xxpay.common.util.excel.write.util;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class WriteExcelUtil {
	public static void generateSelect(Sheet sheet, Object object, int rowNum, int cn) {
		if(object != null && object instanceof String[]){
			String[] textlist = (String[]) object;
			// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
			CellRangeAddressList regions = new CellRangeAddressList(rowNum,  
					rowNum, cn, cn);  
			if(sheet instanceof HSSFSheet){
				DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist); 
				// 数据有效性对象  
				HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);  
				sheet.addValidationData(data_validation_list);  
			}else if(sheet instanceof XSSFSheet){
				 XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
				 XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
				            .createExplicitListConstraint(textlist);
				 XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
				                dvConstraint, regions);
				 sheet.addValidationData(validation);
			}else{
				return;
			}
		}
		
	}
	
}

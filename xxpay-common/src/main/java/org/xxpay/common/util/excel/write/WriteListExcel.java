package org.xxpay.common.util.excel.write;

import org.xxpay.common.util.excel.annotation.Excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xxpay.common.util.excel.util.ExcelUtil;
import org.xxpay.common.util.excel.write.style.CommonCellStyle;
import org.xxpay.common.util.excel.write.style.MyCellStyle;
import org.xxpay.common.util.excel.write.util.WriteListExcelHelp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WriteListExcel implements WriteExcel{
	private MyCellStyle cellStyle;
	private boolean isNeedSequence;
	private Workbook workbook;
	private Sheet sheet;
	private int beginNum = 1;
	private int sheetSize;
	private String outFilePath;
	private String sheetName = "sheet";
	@Override
	public boolean write(Map<String,Object> param,List list) throws IOException, IllegalArgumentException, IllegalAccessException {
		initParam(param,list.get(0).getClass());
		generateExcel(list);
		ExcelUtil.workbookToFile(workbook,outFilePath);
		return true;
	}

	public void generateExcel(List list) throws IllegalAccessException {
		if(workbook instanceof HSSFWorkbook && sheetSize == 0 && list.size() > 65535)
			sheetSize = 65535;
		if(workbook instanceof SXSSFWorkbook && sheetSize == 0 && list.size() > 1048575)
			sheetSize = 1048575;
		WriteListExcelHelp help = new WriteListExcelHelp(cellStyle);
		if(sheetSize!=0 && list.size()>sheetSize){
			double sn = (double)list.size()/sheetSize;
			sn = Math.ceil(sn);
			int initBeginNum = beginNum;
			int k = 0;
			for(int i=1;i<=sn;i++){
				beginNum = initBeginNum;
				sheet = workbook.createSheet(sheetName+"-"+i);
				Row row = help.generateHeader(sheet, beginNum, list.get(0).getClass());
				if(row == null)
					beginNum = beginNum - 1;
				help.setSequence(1);
				for(int j=0;j<sheetSize;j++){
					if( k == list.size())
						break;
					help.generateBody(sheet,++beginNum,list.get(k));
					k++;
				}
			}
		}else{
			if(sheet == null)
				sheet = workbook.createSheet(sheetName);
			Row row = help.generateHeader(sheet,beginNum,list.get(0).getClass());
			if(row == null)
				beginNum = beginNum - 1;
			for(Object t : list){
				help.generateBody(sheet, ++beginNum, t);
			}
		}
	}

	public void initParam(Map<String, Object> param, Class clazz) {
		Excel excel = (Excel) clazz.getAnnotation(Excel.class);
		org.xxpay.common.util.excel.annotation.Sheet s = (org.xxpay.common.util.excel.annotation.Sheet) clazz.getAnnotation(org.xxpay.common.util.excel.annotation.Sheet.class);
		if(excel != null){
			isNeedSequence = excel.isNeedSequence();
			outFilePath = excel.outFilePath();
			beginNum = excel.beginRow();
		}
		if(s != null){
			sheetName = s.sheetName();
			sheetSize = s.sheetSize();
		}
		if(param == null){
			workbook = new HSSFWorkbook();
			cellStyle = new CommonCellStyle(workbook);
		}else{
			if(param.get("workbook") == null){
                workbook = new HSSFWorkbook();
            }else{
                workbook = (Workbook) param.get("workbook");
                if(param.get("isConvertWorkbook") == null){
                	if(workbook instanceof  XSSFWorkbook)
                		workbook = new SXSSFWorkbook();
                }
            }
			if(param.get("myCellStyle") == null)
				cellStyle = new CommonCellStyle(workbook);
			else
				cellStyle = (MyCellStyle)param.get("myCellStyle");
			if(param.get("outFilePath") != null)
				outFilePath = param.get("outFilePath").toString();
			if( param.get("beginRow") != null)
				beginNum = Integer.parseInt(param.get("beginRow").toString());
			if(param.get("sheet") != null)
				sheet = (Sheet) param.get("sheet");
		}
	}
	
}

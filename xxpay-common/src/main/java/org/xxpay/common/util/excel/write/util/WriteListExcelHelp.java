package org.xxpay.common.util.excel.write.util;


import org.apache.poi.ss.usermodel.*;
import org.xxpay.common.util.excel.annotation.Excel;
import org.xxpay.common.util.excel.annotation.Select;
import org.xxpay.common.util.excel.util.ExcelUtil;
import org.xxpay.common.util.excel.util.MyStringUtils;
import org.xxpay.common.util.excel.write.style.MyCellStyle;

import java.lang.reflect.Field;

public class WriteListExcelHelp {
	private int sequence = 1;
	private CellStyle headCellStyle;
	private CellStyle commonCellStyle;
	private CellStyle textCellStyle;
	private CellStyle numberCellStyle;

	public WriteListExcelHelp(MyCellStyle cellStyle){
		headCellStyle = cellStyle.createCommonHeaderCellStyler();
		commonCellStyle = cellStyle.createCommonCellStyle();
		textCellStyle = cellStyle.createCommonTextCellStyle();
		numberCellStyle = cellStyle.createCommonNumberCellStyle();
	}

	public void setSequence(int sequence){
		this.sequence = sequence;
	}
	public Row generateHeader(Sheet sheet,int beginNum,Class clazz) {
        Excel excel = (Excel) clazz.getAnnotation(Excel.class);
        Row row = null;
		if(excel != null){
			String dataHeader = excel.dataHeader();
			if(MyStringUtils.isNotBlank(dataHeader)){
				if(excel.createRowWay().equals("add")){
					row = sheet.createRow(beginNum-1);
				}else{
					 sheet.shiftRows(beginNum-1, sheet.getLastRowNum(), 1, true, false);
			            row = sheet.createRow(beginNum-1);
				}
				if(excel.autoHeight())
					row.setHeight((short) 400);
				boolean isNeedSequence = excel.isNeedSequence();
				if(isNeedSequence){
					Cell cell = row.createCell(0);
					sheet.setColumnWidth(0, 1680);
					cell.setCellValue("序号");
					cell.setCellStyle(headCellStyle);
				}
				int cdn = 1;
				for(String cellData : dataHeader.split(",")){
					String[] cds = cellData.split(":");
					if(MyStringUtils.isNotBlank(cds[0])){
						Cell cell = null;
						if(cds.length==1){
							if(!isNeedSequence)
								cdn = cdn - 1;
							cell = row.createCell(cdn);
						}else{
							int cn = Integer.parseInt(cds[1]);
							if(!isNeedSequence)
								cn = cn - 1;
							cell = row.createCell(cn);
							if(cds.length==3)
								sheet.setColumnWidth(Integer.parseInt(cds[1]),Integer.parseInt(cds[2]));
						}
						cell.setCellValue(cds[0]);
						cell.setCellStyle(headCellStyle);
					}
					cdn ++;
				}
			}
		}
		if(excel != null && excel.autoHeight())
			ExcelUtil.calcAndSetRowHeigt(row, 1.3);
		return row;
	}
	 
	public Row generateBody(Sheet sheet,int rowNum,Object t) throws IllegalArgumentException, IllegalAccessException{
        Class<? extends Object> clazz = t.getClass();
        Excel excel = clazz.getAnnotation(Excel.class);
        Row row = null;
        if(excel==null || excel.createRowWay().equals("add")){
            row = sheet.createRow(rowNum-1);
        }else{
            sheet.shiftRows(rowNum-1, sheet.getLastRowNum(), 1, true, false);
            row = sheet.createRow(rowNum-1);
        }
        if(excel != null && excel.autoHeight())
			row.setHeight((short) 400);
		if(excel == null || excel.isNeedSequence()){
			Cell cell = row.createCell(0);
			cell.setCellValue(sequence);
			cell.setCellStyle(commonCellStyle);
		}
		for(Field field : clazz.getDeclaredFields()){
			field.setAccessible(true);
			org.xxpay.common.util.excel.annotation.Cell ca = field.getAnnotation(org.xxpay.common.util.excel.annotation.Cell.class);
			if(ca==null)
				continue;
			int cn = ExcelUtil.cellTNumByCa(ca);
			if(cn==0)
				continue;
			if(excel !=null && !excel.isNeedSequence())
				cn = cn - 1 ;
			Select select = field.getAnnotation(Select.class);
			if(select != null){
				WriteExcelUtil.generateSelect(sheet,field.get(t),row.getRowNum(),cn);
			}else{
				Cell cell = row.createCell(cn);
				Object obj = field.get(t);
				if(obj!=null){
					if(obj instanceof String){
						String value = obj+"";
						setCellValueAndCellStyle(cell, value, textCellStyle);
					}else if(obj instanceof Double || obj instanceof Float){
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue((double)obj);
						cell.setCellStyle(numberCellStyle);
					}else if(obj instanceof Integer){
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue((int)obj);
						cell.setCellStyle(commonCellStyle);
					}else{
						setCellValueAndCellStyle(cell, obj+"", commonCellStyle);
					}
				}else{
					setCellValueAndCellStyle(cell, "", commonCellStyle);
				}
			}
		}
		sequence ++;
		if(excel != null && excel.autoHeight())
			ExcelUtil.calcAndSetRowHeigt(row, 1.3);
		return row;
	}
	private void setCellValueAndCellStyle(Cell cell, String value,CellStyle cellStyle) {
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
	}

}

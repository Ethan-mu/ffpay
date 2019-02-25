package org.xxpay.common.util.excel.write.style;

import org.apache.poi.ss.usermodel.*;

public class CommonCellStyle extends MyCellStyle{
	public CommonCellStyle(Workbook workbook) {
		super(workbook);
	}
	//生成一般样式
	public CellStyle createCommonCellStyle() {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle .setWrapText(true);
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		cellStyle.setFont(font);
		return cellStyle;
	}
	//生成一般数字样式
	public CellStyle createCommonNumberCellStyle() {
		CellStyle cellStyle = createCommonCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		return cellStyle;
	}
	//生成一般文字样式
	public CellStyle createCommonTextCellStyle() {
		CellStyle cellStyle = createCommonCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		return cellStyle;
	}
	//生成一般列头样式
	public CellStyle createCommonHeaderCellStyler() {
		CellStyle cellStyle = createCommonCellStyle();
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	
}	

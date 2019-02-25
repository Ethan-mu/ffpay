package org.xxpay.common.util.excel.write.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class MyCellStyle{
	protected Workbook workbook;
	public MyCellStyle(Workbook workbook){
		this.workbook = workbook;
	}
	//生成一般样式
	public abstract CellStyle createCommonCellStyle();
	//生成一般数字样式
	public abstract CellStyle createCommonNumberCellStyle();
	//生成一般文字样式
	public abstract CellStyle createCommonTextCellStyle();
	//生成一般列头样式
	public abstract CellStyle createCommonHeaderCellStyler();
}

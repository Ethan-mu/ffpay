package org.xxpay.common.util.excel.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {
	
	/**
     * Excel column index begin 1
     * @param colStr
     * @param length
     * @return
     */
    private static int excelColStrToNum(String colStr, int length) {
        int num = 0;
        int result = 0;
        for(int i = 0; i < length; i++) {
            char ch = colStr.charAt(length - i - 1);
            num = (int)(ch - 'A' + 1) ;
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }
    
    public static int cellTNumByCa(org.xxpay.common.util.excel.annotation.Cell cell){
    	if(MyStringUtils.isNotBlank(cell.columnNum())){
    		if(MyStringUtils.isNumeric(cell.columnNum())){
    			return Integer.parseInt(cell.columnNum());
    		}else{
    			String cl = cell.columnNum().toUpperCase();
    			return excelColStrToNum(cl, cl.length());
    		}
    	}else{
    		return 0;
    	}
    }
  //根据行内内容自动设置行高
  	public static void calcAndSetRowHeigt(Row sourceRow,double multiple) {
  		for (int cellIndex = sourceRow.getFirstCellNum(); cellIndex <= sourceRow
  				.getPhysicalNumberOfCells(); cellIndex++) {
  			// 行高
  			double maxHeight = sourceRow.getHeight();
  			Cell sourceCell = sourceRow.getCell(cellIndex);
  			// 单元格的内容
  			String cellContent = getCellStringValue(sourceCell);
  			if (null == cellContent || "".equals(cellContent)) {
  				continue;
  			}
  			// 单元格的宽高及单元格信息
  			Map<String, Object> cellInfoMap = getCellInfo(sourceCell);
  			Integer cellWidth = (Integer) cellInfoMap.get("width");
  			double cellHeight = (double) cellInfoMap.get("height");
  			if(cellHeight>maxHeight)
  				maxHeight = cellHeight;
  			CellStyle cellStyle = sourceCell.getCellStyle();
  			Font font = sourceRow.getSheet().getWorkbook().getFontAt(cellStyle.getFontIndex());
  			// 字体的高度
  			short fontHeight = font.getFontHeight();
  			// cell内容字符串总宽度
  			double cellContentWidth=0;
  			if(cellContent.contains(",")){
  				String[] ss = cellContent.split(",");
  				cellContent = "";
  				for(String s : ss){
  					cellContent += s;
  				}
  			}
  			if(MyStringUtils.isDouble(cellContent)){
				int length = cellContent.getBytes().length;
				double len = Math.floor((length-1)/3);
				cellContentWidth = (length+len+1)  * 256;
  			}else if(MyStringUtils.isENum(cellContent)){
				double s = Double.parseDouble(cellContent);
				cellContent = new DecimalFormat().format(s);
				int length = cellContent.getBytes().length;
				double len = Math.floor((length-1)/3);
				cellContentWidth = (length+3+len)  * 256;
			}else{
  				if(cellContent.contains("\r\n")){
  					int i = amountIndexof(cellContent,"\r\n");
  					cellContentWidth = (cellContent.getBytes().length+i*33) * 256 * multiple;
  				}
  				else{
  					cellContentWidth = (cellContent.getBytes().length) * multiple * 256;
  				}
  			}
  			// 字符串需要的行数 不做四舍五入之类的操作
  			double stringNeedsRows = (double) cellContentWidth / cellWidth;
  			stringNeedsRows = Math.ceil(stringNeedsRows);
  			double stringNeedsHeight = (fontHeight+60) * stringNeedsRows;
  			if (stringNeedsHeight > maxHeight) 
  				maxHeight = stringNeedsHeight;
  			// 最后取天花板防止高度不够
  			maxHeight = Math.ceil(maxHeight);
  			Boolean isPartOfRowsRegion = (Boolean)cellInfoMap.get("isPartOfRowsRegion");  
              if(isPartOfRowsRegion){  
                  Integer firstRow = (Integer)cellInfoMap.get("firstRow");  
                  Integer lastRow = (Integer)cellInfoMap.get("lastRow");  
                  //平均每行需要增加的行高  
                  double addHeight = (maxHeight - cellHeight)/(lastRow - firstRow + 1);  
                  for (int i = firstRow; i <= lastRow; i++) {  
                      double rowsRegionHeight =sourceRow.getSheet().getRow(i).getHeight() + addHeight;  
                      sourceRow.getSheet().getRow(i).setHeight((short)rowsRegionHeight);  
                  }  
  			}else{
  				sourceRow.setHeight((short) maxHeight);
  			}
  		}
  	}
  	
  	private static int amountIndexof(String cellContent,String patten) {
		int i = cellContent.indexOf(patten);
		int times = 0;
		while(i>-1){
			times ++;
			cellContent = cellContent.substring(i+patten.length()+1);
			i = cellContent.indexOf(patten);
		}
		return times;
	}
  	
  	/**
	 * 获取单元格值
	 * @param cell
	 * @return
	 */
	public static String getCellStringValue(Cell cell) {
		if (cell == null)
			return "";
		String cellValue = "";
        switch (cell.getCellTypeEnum().toString()) {
		case "STRING":// 字符串类型
			cellValue = cell.getStringCellValue();
			if (cellValue.trim().equals("") || cellValue.trim().length() <= 0)
				cellValue = null;
			break;
		case "NUMERIC": // 数值类型
			short i = cell.getCellStyle().getDataFormat();
			 if (DateUtil.isCellDateFormatted(cell) || i == 56) {// 处理日期格式、时间格式  
	                SimpleDateFormat sdf = null;  
	                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
	                        .getBuiltinFormat("h:mm")) {  
	                    sdf = new SimpleDateFormat("HH:mm");  
	                } else {// 日期  
	                    sdf = new SimpleDateFormat("yyyy/MM/dd");  
	                }  
	                Date date = cell.getDateCellValue();  
	                cellValue = sdf.format(date);  
	            }else {  
	                double value = cell.getNumericCellValue();  
	                cellValue = String.valueOf(value);  
	            }  
			break;
		case "FORMULA": // 公式
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case "BLANK":
			cellValue = "";
			break;
		case "BOOLEAN":
			break;
		case "ERROR":
			break;
		default:
			break;
		}
		return cellValue;
	}
	
	/**
	 * 获取单元格及合并单元格的宽度
	 * 
	 * @param cell
	 * @return
	 */
	public static Map<String, Object> getCellInfo(Cell cell) {
	    Sheet sheet = cell.getSheet();  
        int rowIndex = cell.getRowIndex();  
        int columnIndex = cell.getColumnIndex();  
          
        boolean isPartOfRegion = false;  
        int firstColumn = 0;  
        int lastColumn = 0;  
        int firstRow = 0;  
        int lastRow = 0;  
        int sheetMergeCount = sheet.getNumMergedRegions();  
        for (int i = 0; i < sheetMergeCount; i++) {  
            CellRangeAddress ca = sheet.getMergedRegion(i);  
            firstColumn = ca.getFirstColumn();
            lastColumn = ca.getLastColumn();  
            firstRow = ca.getFirstRow();  
            lastRow = ca.getLastRow();  
            if (rowIndex >= firstRow && rowIndex <= lastRow) {  
                if (columnIndex >= firstColumn && columnIndex <= lastColumn) {  
                    isPartOfRegion = true;  
                    break;  
                }  
            }  
        }  
        Map<String, Object> map = new HashMap<String, Object>();  
        Integer width = 0;  
        double height = 0;  
        boolean isPartOfRowsRegion = false;  
        if(isPartOfRegion){  
            for (int i = firstColumn; i <= lastColumn; i++) {  
                width += sheet.getColumnWidth(i);  
            }  
            for (int i = firstRow; i <= lastRow; i++) {  
                height += sheet.getRow(i).getHeight();  
            }  
            if(lastRow > firstRow){  
                isPartOfRowsRegion = true;  
            }  
        }else{  
            width = sheet.getColumnWidth(columnIndex);  
            height += cell.getRow().getHeight();  
        }  
        map.put("isPartOfRowsRegion", isPartOfRowsRegion);  
        map.put("firstRow", firstRow);  
        map.put("lastRow", lastRow);  
        map.put("width", width);  
        map.put("height", height);  
        return map;  
	}
	
	public static Workbook getWorkbookTypeByFile(String filepath) throws IOException{
		File file = new File(filepath);
		Workbook workbook = null;
		BufferedInputStream in = null;
		if(file.exists())
			in = new BufferedInputStream(new FileInputStream(file));
		else
			throw new FileNotFoundException("文件找不到！");
		if(filepath.endsWith("xls"))
			workbook = new HSSFWorkbook(in);
		else if(filepath.endsWith("xlsx"))
			workbook = new XSSFWorkbook(in);
		return workbook;
	}
	
	public static void workbookToFile(Workbook workbook,String outFilePath) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(outFilePath);
		workbook.write(outputStream);
		outputStream.close();
	}	 
	
}

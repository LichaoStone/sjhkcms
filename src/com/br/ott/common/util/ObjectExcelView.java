package com.br.ott.common.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.br.ott.common.net.Encoder;
/**
* 导入到EXCEL
* 类名称：ObjectExcelView.java
* 
* @version 1.0
 */
public class ObjectExcelView extends AbstractExcelView{

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String filename = (String) model.get("filename");
		if(StringUtil.isEmpty(filename)){
			filename = DateUtil.getCurrentmin();
		}
		HSSFSheet sheet;
		HSSFCell cell;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+Encoder.encode(filename)+".xls");
		sheet = workbook.createSheet("sheet1");
		
		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont headerFont = workbook.createFont();	//标题字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		short width = 20,height=25*20;
		sheet.setDefaultColumnWidth(width);
		for(int i=0; i<len; i++){ //设置标题
			String title = titles.get(i);
			cell = getCell(sheet, 1, i);
			cell.setCellStyle(headerStyle);
			setText(cell,title);
		}
		sheet.getRow(1).setHeight(height);
		
		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		List<Map<String, Object>> varList = (List<Map<String, Object>>) model.get("varList");
		int varCount = varList.size();
		
		HSSFCellStyle firstRowStyle = workbook.createCellStyle(); //内容样式
		firstRowStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
		sheet.addMergedRegion(new Region((short)0, (short)0, (short)0, (short)varCount));
		cell = getCell(sheet, 0, 0);
		cell.setCellStyle(firstRowStyle);
		String firstDescript = (String) model.get("firstDescript");
		setText(cell,firstDescript);
		
		for(int i=0; i<varCount; i++){
			Map<String, Object> vpd = varList.get(i);
			for(int j=0;j<len;j++){
				Object obj = vpd.get("var"+(j+1));
				String varstr = "";
				if(obj!=null) {
					if(obj instanceof String) {
						varstr = (String)obj;
					} else {
						varstr = String.valueOf(obj);
					}
				}
				cell = getCell(sheet, i+2, j);
				cell.setCellStyle(contentStyle);
				setText(cell,varstr);
			}
			
		}
		
	}

}

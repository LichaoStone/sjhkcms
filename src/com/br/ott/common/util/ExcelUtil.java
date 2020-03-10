package com.br.ott.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/* 
 *  Excel操作工具类
 *  文件名：ExcelUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-18 - 上午10:54:05
 */
public class ExcelUtil {
	private static Logger logger = Logger.getLogger(ExcelUtil.class);
	private Sheet sheet;

	/**
	 * 
	 * 构造方法
	 * 
	 * @param is
	 * @param indexShett
	 *            第几张工作表
	 */
	public ExcelUtil(InputStream is, int indexShett) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(is);
			sheet = wb.getSheetAt(indexShett);
			logger.debug("读取Excel文件,工作Sheet:" + sheet.getSheetName());
		} catch (InvalidFormatException e) {
			logger.equals(e);
		} catch (IOException e) {
			logger.equals(e);
		}
	}

	/**
	 * 按文件方式读文件内容 构造方法
	 * 
	 * @param file
	 * @param indexShett
	 */
	public ExcelUtil(File file, int indexShett) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(file);
			sheet = wb.getSheetAt(indexShett);
			logger.debug("读取Excel文件,工作Sheet:" + sheet.getSheetName());
		} catch (InvalidFormatException e) {
			logger.equals(e);
		} catch (IOException e) {
			logger.equals(e);
		}
	}

	private Row getRow(int indexRow) {
		if (sheet == null) {
			return null;
		}
		return sheet.getRow(indexRow);
	}

	private Cell getCell(Row row, int indexCell) {
		if (row == null) {
			return null;
		}
		return row.getCell(indexCell);
	}

	/**
	 * 获取单元格的文本 创建人：陈登民 创建时间：2012-12-18 - 上午11:43:50
	 * 
	 * @param indexRow
	 *            行下标
	 * @param indexCell
	 *            　列下标
	 * @return 返回类型：String
	 * @exception throws
	 */
	public String getCellStr(int indexRow, int indexCell) {
		Row row = getRow(indexRow);
		if (row == null)
			return null;

		return getCellStr(row, indexCell);
	}

	private String getCellStr(Row row, int indexCell) {
		Cell cell = getCell(row, indexCell);
		if (cell == null) {
			return null;
		}
		return convertCellStr(cell);
	}

	/**
	 * 获取工作表的有效行数 创建人：陈登民 创建时间：2012-12-18 - 上午11:44:25
	 * 
	 * @return 返回类型：int
	 * @exception throws
	 */
	public int getRowCount() {
		if (sheet == null) {
			return 0;
		}
		int first = sheet.getFirstRowNum();
		int last = sheet.getLastRowNum();
		return (last - first) + 1;
	}

	/**
	 * 获取该行的有效列数 创建人：陈登民 创建时间：2012-12-18 - 上午11:44:56
	 * 
	 * @param indexRow
	 *            行下标
	 * @return 返回类型：int
	 * @exception throws
	 */
	public int getCellCount(int indexRow) {
		Row row = getRow(indexRow);
		if (row == null) {
			return 0;
		}
		int first = row.getFirstCellNum();
		int last = row.getLastCellNum();
		return (last - first) + 1;
	}

	/**
	 * 把单元格内的类型转换至String类型
	 */
	private String convertCellStr(Cell cell) {
		String cellStr = "";
		if (null == cell) {
			return cellStr;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			// 读取String
			cellStr = cell.getStringCellValue().toString();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 得到Boolean对象的方法
			cellStr = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// 先看是否是日期格式
			if (DateUtil.isCellDateFormatted(cell)) {
				// 读取日期格式
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				cellStr = sdf.format(cell.getDateCellValue());
			} else {
				// 读取数字
				// cellStr = String.valueOf(cell.getNumericCellValue());

				double number = cell.getNumericCellValue();
				int num = (int) cell.getNumericCellValue();
				if (number == num) {
					cellStr = String.valueOf(num);
				} else {
					cellStr = String.valueOf(number);
				}
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 读取公式
			cellStr = cell.getCellFormula().toString();
			break;
		}
		return cellStr;
	}

	public static String ConvertCellStr(Cell cell, String cellStr) {
		if (null == cell) {
			return "";
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			// 读取String
			cellStr = cell.getStringCellValue().toString().trim();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 得到Boolean对象的方法
			cellStr = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// 先看是否是日期格式
			if (DateUtil.isCellDateFormatted(cell)) {
				// 读取日期格式
				cellStr = cell.getDateCellValue().toString();
			} else {
				// 读取数字
				double number = cell.getNumericCellValue();
				int num = (int) cell.getNumericCellValue();
				if (number == num) {
					cellStr = String.valueOf(num);
				} else {
					cellStr = String.valueOf(number);
				}
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 读取公式
			cellStr = cell.getCellFormula().toString();
			break;
		}
		return cellStr;
	}

	public static String ConvertCellStr(Cell cell) {
		if (null == cell) {
			return "";
		}
		String cellStr = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			// 读取String
			cellStr = cell.getStringCellValue().toString().trim();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 得到Boolean对象的方法
			cellStr = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// 先看是否是日期格式
			if (DateUtil.isCellDateFormatted(cell)) {
				// 读取日期格式
				cellStr = cell.getDateCellValue().toString();
			} else {
				// 读取数字
				double number = cell.getNumericCellValue();
				int num = (int) cell.getNumericCellValue();
				if (number == num) {
					cellStr = String.valueOf(num);
				} else {
					cellStr = String.valueOf(number);
				}
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 读取公式
			cellStr = cell.getCellFormula().toString();
			break;
		}
		return cellStr;
	}

	public static SXSSFWorkbook generateExcel(List<Map<String, String>> list,
			String title) {
		SXSSFWorkbook book = new SXSSFWorkbook(300);
		try {
			Sheet sheet = book.createSheet("Sheet1");
			sheet.autoSizeColumn(1, true);// 自适应列宽度
			// 样式设置
			CellStyle style = book.createCellStyle();
			style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			Font font = book.createFont();
			font.setColor(HSSFColor.VIOLET.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			style.setFont(font);

			CellStyle style2 = book.createCellStyle();
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			// 设置上下左右边框
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 填充表头标题
			int colSize = list.get(0).entrySet().size();
			// 合并单元格供标题使用(表名)
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colSize - 1));
			Row firstRow = sheet.createRow(0);// 第几行（从0开始）
			Cell firstCell = firstRow.createCell(0);
			firstCell.setCellValue(title);
			firstCell.setCellStyle(style);

			// 填充表头header
			Row row = sheet.createRow(1);
			Set<Entry<String, String>> set = list.get(0).entrySet();
			List<Entry<String, String>> l = new ArrayList<Map.Entry<String, String>>(
					set);
			for (int i = 0; i < l.size(); i++) {
				String key = l.get(i).getKey();
				Cell cell = row.createCell(i);
				cell.setCellValue(key);
				cell.setCellStyle(style2);
			}

			// 填充表格内容
			for (int i = 0; i < list.size(); i++) {
				Row row2 = sheet.createRow(i + 2);// index：第几行
				Map<String, String> map = list.get(i);
				Set<Entry<String, String>> set2 = map.entrySet();
				List<Entry<String, String>> ll = new ArrayList<Entry<String, String>>(set2);
				for (int j = 0; j < ll.size(); j++) {
					String val = ll.get(j).getValue();
					Cell cell = row2.createCell(j);// 第几列：从0开始
					cell.setCellValue(val);
					cell.setCellStyle(style2);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return book;
	}
}

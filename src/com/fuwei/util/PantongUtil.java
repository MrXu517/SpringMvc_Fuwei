package com.fuwei.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.fuwei.entity.PantongColor;

public class PantongUtil {

	public static Map<String, PantongColor> getPantongColorMapByEXCEL(
			InputStream file) throws IOException, BiffException {
		return readXls(file);
	}

	/**
	 * 通过文件路径获取key为潘通色号,值为潘通色号对象的一个Map
	 * 
	 * @param excelPath
	 *            excel文件路径
	 * @return Map<String,PantongColor>
	 * @throws IOException
	 * @throws BiffException
	 */
//	private static Map<String, PantongColor> readXls(String excelPath)
//			throws IOException, BiffException {
//		InputStream is = new FileInputStream(excelPath);
//
//		Map<String, PantongColor> pantongMap = new HashMap<String, PantongColor>();
//
//		Workbook rb = Workbook.getWorkbook(is);
//		Sheet[] sheet = rb.getSheets();// 获取每个Sheet表
//		for (int i = 0; i < sheet.length; i++) {
//			Sheet rs = rb.getSheet(i);
//			int rows = rs.getRows();// 获取每行
//			for (int j = 0; j < rows; j++) {// 从第一行开始
//				Cell[] cells = rs.getRow(j);
//				for (int k = 0; k < cells.length; k++) {// 获取每个单元格
//					String pantongName = cells[k].getContents().trim();
//					pantongMap.put(pantongName, new PantongColor(pantongName,
//							i + 1, j + 1, k + 1));
//				}
//
//			}
//		}
//
//		return pantongMap;
//	}

	private static Map<String, PantongColor> readXls(InputStream is)
			throws IOException, BiffException {

		Map<String, PantongColor> pantongMap = new HashMap<String, PantongColor>();

		Workbook rb = Workbook.getWorkbook(is);
		Sheet[] sheet = rb.getSheets();// 获取每个Sheet表
		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();// 获取每行
			for (int j = 0; j < rows; j++) {// 从第一行开始
				Cell[] cells = rs.getRow(j);
				for (int k = 0; k < cells.length; k++) {// 获取每个单元格
					String pantongName = cells[k].getContents().trim();
					pantongMap.put(pantongName, new PantongColor(pantongName,
							i + 1, j + 1, k + 1));
				}

			}
		}

		return pantongMap;
	}
}

//package com.fuwei.util;
//
//import java.io.File;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import com.fuwei.commons.SystemCache;
//import com.fuwei.commons.SystemContextUtils;
//import com.fuwei.constant.Constants;
//import com.fuwei.entity.ProductionNotification;
//import com.fuwei.entity.QuoteOrder;
//import com.fuwei.entity.QuoteOrderDetail;
//import com.fuwei.entity.QuotePrice;
//import com.fuwei.entity.Sample;
//
//import jxl.Workbook;
//import jxl.format.Alignment;
//import jxl.format.Border;
//import jxl.format.BorderLineStyle;
//import jxl.format.PageOrientation;
//import jxl.format.PaperSize;
//import jxl.format.VerticalAlignment;
//import jxl.write.Label;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableFont;
//import jxl.write.WritableImage;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONSerializer;
//
///***
// * @author lsf
// */
//public class ExportExcel {
//	/***************************************************************************
//	 * @param fileName
//	 *            EXCEL文件名称
//	 * @param listTitle
//	 *            EXCEL文件第一行列标题集合
//	 * @param listContent
//	 *            EXCEL文件正文数据集合
//	 * @return
//	 */
//
//	private static final String shifadidian = "桐庐";
//	private static final String factoryName = "桐庐富伟针织厂";
//	private static final String factoryAddress = "浙江省杭州市桐庐县横村镇孙家村";
//	private static final String factoryPhone = "0571-64677060";
//
//	// 生成报价单excel
//	public final static void exportExcel(String fileName, String filePath,
//			QuoteOrder quoteorder, String appPath) throws Exception {
//		java.io.File File = new java.io.File(filePath + fileName);
//		java.io.File pathFile = new java.io.File(File.getParent());
//		if (!pathFile.exists()) {
//			pathFile.mkdirs();
//		}
//		fileName = filePath + fileName;
//		// 以下开始输出到EXCEL
//		try {
//			// 定义输出流，以便打开保存对话框______________________begin
//			// 定义输出流，以便打开保存对话框_______________________end
//			/** **********创建工作簿************ */
//			WritableWorkbook workbook = Workbook.createWorkbook(new File(
//					fileName));
//
//			/** **********创建工作表************ */
//
//			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
//
//			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
//			jxl.SheetSettings sheetset = sheet.getSettings();
//			sheetset.setProtected(false);
//			sheetset.setLeftMargin(0.3);
//			sheetset.setRightMargin(0.3);
//			sheetset.setTopMargin(0.3);
//			sheetset.setBottomMargin(0.3);
//			/** ************设置单元格字体************** */
//			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
//			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
//					WritableFont.BOLD);
//
//			/** ************以下设置三种单元格样式，灵活备用************ */
//			// 用于标题居中
//			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
//			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
//			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			wcf_center.setWrap(false); // 文字是否换行
//
//			// 用于正文居左
//			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
//			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
//			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
//			wcf_left.setWrap(false); // 文字是否换行
//
//			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
//			WritableFont title_BoldFont = new WritableFont(WritableFont.ARIAL,
//					20, WritableFont.BOLD);
//
//			WritableCellFormat title_wcf_center = new WritableCellFormat(
//					title_BoldFont);
//			title_wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			title_wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			title_wcf_center.setWrap(false); // 文字是否换行
//			sheet.mergeCells(0, 0, 6, 0);
//			sheet.addCell(new Label(0, 0, "桐庐富伟针织厂报价单", title_wcf_center));
//			/** ***************以下是EXCEL第2行报价公司及业务员及第3行报价时间********************* */
//			WritableFont company_BoldFont = new WritableFont(
//					WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
//
//			WritableCellFormat company_wcf_center = new WritableCellFormat(
//					company_BoldFont);
//			company_wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			company_wcf_center.setAlignment(Alignment.LEFT); // 文字水平对齐
//			company_wcf_center.setWrap(false); // 文字是否换行
//			sheet.mergeCells(0, 2, 6, 2);
//			sheet.addCell(new Label(0, 2, "  No:" + " "
//					+ quoteorder.getQuotationNumber(), company_wcf_center));
//			sheet.mergeCells(0, 3, 6, 3);
//			sheet.addCell(new Label(0, 3, "  TO:"
//					+ SystemCache.getCompanyName(quoteorder.getCompanyId())
//					+ "   "
//					+ SystemCache.getSalesmanName(quoteorder.getSalesmanId()),
//					company_wcf_center));
//
//			sheet.mergeCells(0, 4, 6, 4);
//			sheet.addCell(new Label(0, 4, "  时间:"
//					+ DateTool.formateDate(quoteorder.getCreated_at()),
//					company_wcf_center));
//			/** ***************以下是EXCEL第4行列标题列********************* */
//
//			// sheet.addCell(new Label(i+1, 0, Title[i], wcf_center));
//			String[] Title = { "序号", "图片", "款号", "材料", "尺寸", "重量", "价格" };
//			for (int i = 0; i < Title.length; i++) {
//				sheet.addCell(new Label(i, 5, Title[i], wcf_center));
//			}
//
//			sheet.setColumnView(0, 8);
//			sheet.setColumnView(1, 15);
//			sheet.setColumnView(2, 16);
//			sheet.setColumnView(3, 20);
//			sheet.setColumnView(4, 18);
//			sheet.setColumnView(5, 12);
//			sheet.setColumnView(6, 10);
//
//			/** ***************以下是EXCEL正文数据********************* */
//			WritableFont content_BoldFont = new WritableFont(
//					WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
//			WritableCellFormat content_center = new WritableCellFormat(
//					content_BoldFont);
//			content_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
//			content_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			content_center.setWrap(true); // 文字是否换行
//			// SampleDAO sampleDAO = new SampleDAO();
//			List<QuoteOrderDetail> detaillist = quoteorder.getDetaillist();
//			for (int i = 0; i < detaillist.size(); i++) {
//				sheet.setRowView(6 + i, 1500);
//				QuoteOrderDetail detail = detaillist.get(i);
//				// Sample sample = sampleDAO.getSample(companyPriceList.get(i)
//				// .getSampleId());
//				sheet.addCell(new Label(0, 6 + i, i + 1 + "", content_center));
//				File pictureFile = new File(appPath + detail.getImg_ss());// imgPath
//				if (pictureFile.exists()) {
//					WritableImage a = new WritableImage(1, 6 + i, 1, 1,
//							pictureFile);
//					sheet.addImage(a);
//				} else {
//					sheet.addCell(new Label(1, 6 + i, "图片缺失", content_center));
//
//				}
//				sheet.addCell(new Label(2, 6 + i, detail.getName(),
//						content_center));
//				sheet.addCell(new Label(3, 6 + i, detail.getMaterial(),
//						content_center));
//				sheet.addCell(new Label(4, 6 + i, detail.getSize(),
//						content_center));
//				sheet.addCell(new Label(5, 6 + i, detail.getWeight() + " g",
//						content_center));
//				sheet.addCell(new Label(6, 6 + i, detail.getPrice() + "",
//						content_center));
//			}
//
//			/** **********将以上缓存中的内容写到EXCEL文件中******** */
//			workbook.write();
//			/** *********关闭文件************* */
//			workbook.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	// 删除报价单excel
//	public final static Boolean deleteExcel(String filename, String filePath) {
//		File file = new File(filePath + filename);
//		if (file.exists()) {
//			return file.delete();
//		}
//		return true;
//	}
//
//	// 生成样品标签
//	public final static void exportSampleSignExcel(List<Sample> samplelist,
//			String fileName, String filePath) throws Exception {
//		java.io.File File = new java.io.File(filePath + fileName);
//		java.io.File pathFile = new java.io.File(File.getParent());
//		if (!pathFile.exists()) {
//			pathFile.mkdirs();
//		}
//		// 以下开始输出到EXCEL
//		try {
//			// 定义输出流，以便打开保存对话框______________________begin
//			// 定义输出流，以便打开保存对话框_______________________end
//			/** **********创建工作簿************ */
//			WritableWorkbook workbook = Workbook.createWorkbook(new File(
//					filePath + fileName));
//
//			/** **********创建工作表************ */
//
//			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
//
//			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
//			jxl.SheetSettings sheetset = sheet.getSettings();
//			sheetset.setProtected(false);
//			sheetset.setLeftMargin(0);
//			sheetset.setRightMargin(0);
//			sheetset.setTopMargin(0);
//			sheetset.setBottomMargin(0);
//			/** ************设置单元格字体************** */
//			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
//			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
//					WritableFont.BOLD);
//
//			/** ************以下设置三种单元格样式，灵活备用************ */
//			// 用于标题居中
//			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
//			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
//			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_center.setAlignment(Alignment.LEFT); // 文字水平对齐
//			wcf_center.setWrap(false); // 文字是否换行
//
//			// 用于正文居左
//			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
//			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
//			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
//			wcf_left.setWrap(false); // 文字是否换行
//
//			// 设置列宽
//			sheet.setColumnView(0, 6);
//			sheet.setColumnView(1, 17);
//
//			/** ***************以下是EXCEL正文数据********************* */
//			WritableFont content_BoldFont = new WritableFont(
//					WritableFont.ARIAL, 11, WritableFont.NO_BOLD);
//			WritableCellFormat content_center = new WritableCellFormat(
//					content_BoldFont);
//			content_center.setBorder(Border.BOTTOM, BorderLineStyle.THIN); // 线条
//			content_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			content_center.setWrap(false); // 文字是否换行
//
//			WritableFont content_BoldFont2 = new WritableFont(
//					WritableFont.ARIAL, 11, WritableFont.NO_BOLD);
//			WritableCellFormat content_center2 = new WritableCellFormat(
//					content_BoldFont2);
//
//			content_center2.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			content_center2.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			content_center2.setWrap(false); // 文字是否换行
//			int index = 0;
//			int number = 0;
//			for (Sample sample : samplelist) {
//				if (sample.getMemo().length() > 1) {
//					switch (Integer.valueOf(sample.getMaterial().length()) / 9) {
//					case 0: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//						switch (Integer.valueOf(sample.getMemo().length()) / 9) {
//						case 0: {
//							sheet.setRowView(0, 340);
//							sheet.setRowView(2, 200);
//							sheet.setRowView(4, 200);
//							sheet.setRowView(6, 200);
//							sheet.setRowView(8, 200);
//							sheet.setRowView(10, 200);
//							sheet.setRowView(12, 200);
//							sheet.setRowView(14, 200);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial(), content_center));
//
//							sheet.addCell(new Label(0, index + 9 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 9 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 11 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 11 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 13 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 13 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 15 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 15 + number,
//									sample.getMemo(), content_center));
//						}
//							break;
//						case 1: {
//							sheet.setRowView(0, 340);
//							sheet.setRowView(2, 200);
//							sheet.setRowView(4, 200);
//							sheet.setRowView(6, 200);
//							sheet.setRowView(8, 200);
//							sheet.setRowView(10, 200);
//							sheet.setRowView(12, 200);
//							sheet.setRowView(14, 200);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial(), content_center));
//
//							sheet.addCell(new Label(0, index + 9 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 9 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 11 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 11 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 13 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 13 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 15 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 15 + number,
//									sample.getMemo().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 16 + number,
//									sample.getMemo().substring(8,
//											sample.getMemo().length()),
//									content_center));
//						}
//
//							break;
//						case 2: {
//							sheet.setRowView(0, 200);
//							sheet.setRowView(2, 200);
//							sheet.setRowView(4, 200);
//							sheet.setRowView(6, 200);
//							sheet.setRowView(8, 200);
//							sheet.setRowView(10, 200);
//							sheet.setRowView(12, 200);
//							sheet.setRowView(14, 200);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial(), content_center));
//
//							sheet.addCell(new Label(0, index + 9 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 9 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 11 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 11 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 13 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 13 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 15 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 15 + number,
//									sample.getMemo().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 16 + number,
//									sample.getMemo().substring(8, 16),
//									content_center));
//							sheet.addCell(new Label(1, index + 17 + number,
//									sample.getMemo().substring(16,
//											sample.getMemo().length()),
//									content_center));
//						}
//
//							break;
//
//						default:
//							break;
//						}
//
//					}
//						break;
//
//					case 1: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//
//						switch (Integer.valueOf(sample.getMemo().length()) / 9) {
//						case 0: {
//							sheet.setRowView(0, 200);
//							sheet.setRowView(2, 200);
//							sheet.setRowView(4, 200);
//							sheet.setRowView(6, 200);
//							sheet.setRowView(9, 200);
//							sheet.setRowView(11, 200);
//							sheet.setRowView(13, 200);
//							sheet.setRowView(15, 200);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 8 + number,
//									sample.getMaterial().substring(8,
//											sample.getMaterial().length()),
//									content_center));
//
//							sheet.addCell(new Label(0, index + 10 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 10 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 12 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 12 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 14 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 14 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 16 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 16 + number,
//									sample.getMemo(), content_center));
//						}
//
//							break;
//
//						case 1: {
//							sheet.setRowView(0, 200);
//							sheet.setRowView(2, 200);
//							sheet.setRowView(4, 200);
//							sheet.setRowView(6, 200);
//							sheet.setRowView(9, 200);
//							sheet.setRowView(11, 200);
//							sheet.setRowView(13, 200);
//							sheet.setRowView(15, 200);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 8 + number,
//									sample.getMaterial().substring(8,
//											sample.getMaterial().length()),
//									content_center));
//
//							sheet.addCell(new Label(0, index + 10 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 10 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 12 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 12 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 14 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 14 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 16 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 16 + number,
//									sample.getMemo().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 17 + number,
//									sample.getMemo().substring(8,
//											sample.getMemo().length()),
//									content_center));
//						}
//
//							break;
//						case 2: {
//							sheet.setRowView(0, 150);
//							sheet.setRowView(2, 150);
//							sheet.setRowView(4, 150);
//							sheet.setRowView(6, 150);
//							sheet.setRowView(9, 150);
//							sheet.setRowView(11, 150);
//							sheet.setRowView(13, 150);
//							sheet.setRowView(15, 150);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 8 + number,
//									sample.getMaterial().substring(8,
//											sample.getMaterial().length()),
//									content_center));
//
//							sheet.addCell(new Label(0, index + 10 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 10 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 12 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 12 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 14 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 14 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 16 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 16 + number,
//									sample.getMemo().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 17 + number,
//									sample.getMemo().substring(8, 16),
//									content_center));
//							sheet.addCell(new Label(1, index + 18 + number,
//									sample.getMemo().substring(16,
//											sample.getMemo().length()),
//									content_center));
//
//						}
//
//							break;
//						default:
//							break;
//						}
//
//					}
//
//						break;
//					case 2: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//						switch (Integer.valueOf(sample.getMemo().length()) / 9) {
//						case 0: {
//							sheet.setRowView(0, 200);
//							sheet.setRowView(2, 170);
//							sheet.setRowView(4, 170);
//							sheet.setRowView(6, 170);
//							sheet.setRowView(10, 170);
//							sheet.setRowView(12, 170);
//							sheet.setRowView(14, 170);
//							sheet.setRowView(16, 170);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 8 + number,
//									sample.getMaterial().substring(8, 16),
//									content_center));
//							sheet.addCell(new Label(1, index + 9 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()).substring(16,
//											SystemCache.getFactoryName(sample.getFactoryId()).length()),
//									content_center));
//
//							sheet.addCell(new Label(0, index + 11 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 11 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 13 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 13 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 15 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 15 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 17 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 17 + number,
//									sample.getMemo(), content_center));
//						}
//							break;
//
//						case 1: {
//							sheet.setRowView(0, 170);
//							sheet.setRowView(2, 170);
//							sheet.setRowView(4, 170);
//							sheet.setRowView(6, 170);
//							sheet.setRowView(10, 170);
//							sheet.setRowView(12, 170);
//							sheet.setRowView(14, 170);
//							sheet.setRowView(16, 170);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 8 + number,
//									sample.getMaterial().substring(8, 16),
//									content_center));
//							sheet.addCell(new Label(1, index + 9 + number,
//									sample.getMaterial().substring(16,
//											sample.getMaterial().length()),
//									content_center));
//
//							sheet.addCell(new Label(0, index + 11 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 11 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 13 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 13 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 15 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 15 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 17 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 17 + number,
//									sample.getMemo().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 18 + number,
//									sample.getMemo().substring(8,
//											sample.getMemo().length()),
//									content_center));
//						}
//							break;
//						case 2: {
//							sheet.setRowView(0, 130);
//							sheet.setRowView(2, 130);
//							sheet.setRowView(4, 130);
//							sheet.setRowView(6, 130);
//							sheet.setRowView(10, 130);
//							sheet.setRowView(12, 130);
//							sheet.setRowView(14, 130);
//							sheet.setRowView(16, 130);
//							sheet.addCell(new Label(0, index + 1 + number,
//									"款号:", content_center2));
//							sheet.addCell(new Label(1, index + 1 + number,
//									sample.getProductNumber(), content_center));
//
//							sheet.addCell(new Label(0, index + 3 + number,
//									"打样:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 3 + number,
//											SystemCache.getUserName(sample
//													.getCharge_user()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 5 + number,
//									"尺寸:", content_center2));
//							sheet.addCell(new Label(1, index + 5 + number,
//									sample.getSize(), content_center));
//
//							sheet.addCell(new Label(0, index + 7 + number,
//									"材料:", content_center2));
//							sheet.addCell(new Label(1, index + 7 + number,
//									sample.getMaterial().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 8 + number,
//									sample.getMaterial().substring(8, 16),
//									content_center));
//							sheet.addCell(new Label(1, index + 9 + number,
//									sample.getMaterial().substring(16,
//											sample.getMaterial().length()),
//									content_center));
//
//							sheet.addCell(new Label(0, index + 11 + number,
//									"克重:", content_center2));
//							sheet.addCell(new Label(1, index + 11 + number,
//									sample.getWeight() + " 克", content_center));
//
//							sheet.addCell(new Label(0, index + 13 + number,
//									"机织:", content_center2));
//							sheet.addCell(new Label(1, index + 13 + number,
//									SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//							sheet.addCell(new Label(0, index + 15 + number,
//									"时间:", content_center2));
//							sheet
//									.addCell(new Label(1, index + 15 + number,
//											DateTool.formateDate(sample
//													.getCreated_at()),
//											content_center));
//
//							sheet.addCell(new Label(0, index + 17 + number,
//									"备注:", content_center2));
//							sheet.addCell(new Label(1, index + 17 + number,
//									sample.getMemo().substring(0, 8),
//									content_center));
//							sheet.addCell(new Label(1, index + 18 + number,
//									sample.getMemo().substring(8, 16),
//									content_center));
//							sheet.addCell(new Label(1, index + 19 + number,
//									sample.getMemo().substring(16,
//											sample.getMemo().length()),
//									content_center));
//
//						}
//
//							break;
//
//						default:
//							break;
//						}
//
//					}
//						break;
//					default: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//						sheet.addCell(new Label(0, index + 1 + number, "款号:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 1 + number, sample
//								.getProductNumber(), content_center));
//
//						sheet.addCell(new Label(0, index + 3 + number, "打样:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 3 + number,
//								SystemCache
//										.getUserName(sample.getCharge_user()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 5 + number, "尺寸:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 5 + number, sample
//								.getSize(), content_center));
//
//						sheet.addCell(new Label(0, index + 7 + number, "材料:",
//								content_center2));
//
//						sheet.addCell(new Label(0, index + 10 + number, "克重:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 10 + number, sample
//								.getWeight()
//								+ " 克", content_center));
//
//						sheet.addCell(new Label(0, index + 12 + number, "机织:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 12 + number, SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//						sheet.addCell(new Label(0, index + 14 + number, "时间:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 14 + number,
//								DateTool.formateDate(sample.getCreated_at()),
//								content_center));
//
//					}
//						break;
//					}
//				} else {
//					switch (Integer.valueOf(sample.getMaterial().length()) / 9) {
//					case 0: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//
//						sheet.addCell(new Label(0, index + 1 + number, "款号:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 1 + number, sample
//								.getProductNumber(), content_center));
//
//						sheet.addCell(new Label(0, index + 3 + number, "打样:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 3 + number,
//								SystemCache
//										.getUserName(sample.getCharge_user()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 5 + number, "尺寸:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 5 + number, sample
//								.getSize(), content_center));
//
//						sheet.addCell(new Label(0, index + 7 + number, "材料:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 7 + number, sample
//								.getMaterial(), content_center));
//
//						sheet.addCell(new Label(0, index + 9 + number, "克重:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 9 + number, sample
//								.getWeight()
//								+ " 克", content_center));
//
//						sheet.addCell(new Label(0, index + 11 + number, "机织:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 11 + number, SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//						sheet.addCell(new Label(0, index + 13 + number, "时间:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 13 + number,
//								DateTool.formateDate(sample.getCreated_at()),
//								content_center));
//					}
//						break;
//
//					case 1: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//						sheet.addCell(new Label(0, index + 1 + number, "款号:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 1 + number, sample
//								.getProductNumber(), content_center));
//
//						sheet.addCell(new Label(0, index + 3 + number, "打样:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 3 + number,
//								SystemCache
//										.getUserName(sample.getCharge_user()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 5 + number, "尺寸:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 5 + number, sample
//								.getSize(), content_center));
//
//						sheet.addCell(new Label(0, index + 7 + number, "材料:",
//								content_center2));
//						sheet
//								.addCell(new Label(1, index + 7 + number,
//										sample.getMaterial().substring(0, 8),
//										content_center));
//						sheet.addCell(new Label(1, index + 8 + number, sample
//								.getMaterial().substring(8,
//										sample.getMaterial().length()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 10 + number, "克重:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 10 + number, sample
//								.getWeight()
//								+ " 克", content_center));
//
//						sheet.addCell(new Label(0, index + 12 + number, "机织:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 12 + number, SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//						sheet.addCell(new Label(0, index + 14 + number, "时间:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 14 + number,
//								DateTool.formateDate(sample.getCreated_at()),
//								content_center));
//
//					}
//
//						break;
//					case 2: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//						sheet.addCell(new Label(0, index + 1 + number, "款号:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 1 + number, sample
//								.getProductNumber(), content_center));
//
//						sheet.addCell(new Label(0, index + 3 + number, "打样:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 3 + number,
//								SystemCache
//										.getUserName(sample.getCharge_user()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 5 + number, "尺寸:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 5 + number, sample
//								.getSize(), content_center));
//
//						sheet.addCell(new Label(0, index + 7 + number, "材料:",
//								content_center2));
//						sheet
//								.addCell(new Label(1, index + 7 + number,
//										sample.getMaterial().substring(0, 8),
//										content_center));
//						sheet
//								.addCell(new Label(1, index + 8 + number,
//										sample.getMaterial().substring(8, 16),
//										content_center));
//						sheet.addCell(new Label(1, index + 9 + number, sample
//								.getMaterial().substring(16,
//										sample.getMaterial().length()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 10 + number, "克重:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 10 + number, sample
//								.getWeight()
//								+ " 克", content_center));
//
//						sheet.addCell(new Label(0, index + 12 + number, "机织:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 12 + number, SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//						sheet.addCell(new Label(0, index + 14 + number, "时间:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 14 + number,
//								DateTool.formateDate(sample.getCreated_at()),
//								content_center));
//
//					}
//						break;
//					default: {
//						for (int i = index; i < 16 + index; i++) {
//							sheet.setRowView(i, 290);
//						}
//						sheet.addCell(new Label(0, index + 1 + number, "款号:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 1 + number, sample
//								.getProductNumber(), content_center));
//
//						sheet.addCell(new Label(0, index + 3 + number, "打样:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 3 + number,
//								SystemCache
//										.getUserName(sample.getCharge_user()),
//								content_center));
//
//						sheet.addCell(new Label(0, index + 5 + number, "尺寸:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 5 + number, sample
//								.getSize(), content_center));
//
//						sheet.addCell(new Label(0, index + 7 + number, "材料:",
//								content_center2));
//
//						sheet.addCell(new Label(0, index + 10 + number, "克重:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 10 + number, sample
//								.getWeight()
//								+ " 克", content_center));
//
//						sheet.addCell(new Label(0, index + 12 + number, "机织:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 12 + number, SystemCache.getFactoryName(sample.getFactoryId()), content_center));
//
//						sheet.addCell(new Label(0, index + 14 + number, "时间:",
//								content_center2));
//						sheet.addCell(new Label(1, index + 14 + number,
//								DateTool.formateDate(sample.getCreated_at()),
//								content_center));
//
//					}
//						break;
//					}
//				}
//				number -= 2;
//				index = index + 18;
//			}
//
//			/** **********将以上缓存中的内容写到EXCEL文件中******** */
//			workbook.write();
//			/** *********关闭文件************* */
//			workbook.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	// 生成样品详情
//	public final static void exportSampleDetailExcel(Sample sample,
//			QuotePrice quotePrice, String filePath, String fileName,
//			String appPath) throws Exception {
//
//		java.io.File File = new java.io.File(filePath + fileName);
//		java.io.File pathFile = new java.io.File(File.getParent());
//		if (!pathFile.exists()) {
//			pathFile.mkdirs();
//		}
//		// 以下开始输出到EXCEL
//		try {
//			// 定义输出流，以便打开保存对话框______________________begin
//			// 定义输出流，以便打开保存对话框_______________________end
//			/** **********创建工作簿************ */
//			WritableWorkbook workbook = Workbook.createWorkbook(new File(
//					filePath + fileName));
//
//			/** **********创建工作表************ */
//
//			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
//
//			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
//			jxl.SheetSettings sheetset = sheet.getSettings();
//			sheetset.setProtected(false);
//			sheetset.setLeftMargin(0);
//			sheetset.setRightMargin(0);
//			sheetset.setTopMargin(0);
//			sheetset.setBottomMargin(0);
//			/** ************设置单元格字体************** */
//			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
//			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
//					WritableFont.BOLD);
//
//			/** ************以下设置三种单元格样式，灵活备用************ */
//			// 用于标题居中
//			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
//			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
//			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_center.setAlignment(Alignment.LEFT); // 文字水平对齐
//			wcf_center.setWrap(false); // 文字是否换行
//
//			// 用于正文居左
//			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
//			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
//			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
//			wcf_left.setWrap(false); // 文字是否换行
//
//			/** ***************以下是EXCEL正文数据********************* */
//
//			WritableFont content_BoldFont2 = new WritableFont(
//					WritableFont.ARIAL, 14, WritableFont.NO_BOLD);
//			WritableFont content_BoldFont = new WritableFont(
//					WritableFont.ARIAL, 14, WritableFont.NO_BOLD);
//			WritableCellFormat content_center2 = new WritableCellFormat(
//					content_BoldFont2);
//			WritableCellFormat content_center = new WritableCellFormat(
//					content_BoldFont);
//			content_center.setBorder(Border.BOTTOM, BorderLineStyle.THIN); // 线条
//			content_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			content_center.setWrap(false); // 文字是否换行
//
//			content_center2.setVerticalAlignment(VerticalAlignment.TOP); // 文字垂直对齐
//			content_center2.setAlignment(Alignment.LEFT); // 文字水平对齐
//			content_center2.setWrap(true); // 文字是否换行
//
//			sheet.mergeCells(0, 12, 4, 21);
//			File pictureFile = new File(appPath + sample.getImg_s());// imgPath
//			if (pictureFile.exists()) {
//				WritableImage a = new WritableImage(0, 12, 5, 10, pictureFile);
//				sheet.addImage(a);
//			} else {
//				sheet.addCell(new Label(0, 12, "图片缺失", content_center));
//
//			}
//			sheet.mergeCells(6, 12, 9, 30);
//			sheet
//					.addCell(new Label(6, 12, sample.getDetail(),
//							content_center2));
//
//			for (int i = 1; i < 31; i++) {
//				sheet.setRowView(i, 450);
//			}
//			for (int i = 0; i < 5; i++) {
//				sheet.setColumnView(i, 10);
//			}
//
//			sheet.setColumnView(9, 20);
//			sheet.setColumnView(5, 2);
//			sheet.setColumnView(6, 12);
//			sheet.mergeCells(0, 1, 4, 1);
//			sheet.mergeCells(1, 2, 4, 2);
//			sheet.mergeCells(1, 3, 4, 3);
//			sheet.mergeCells(1, 4, 4, 4);
//			sheet.mergeCells(1, 5, 4, 5);
//			sheet.mergeCells(1, 6, 4, 6);
//			sheet.mergeCells(1, 7, 4, 7);
//			sheet.mergeCells(1, 8, 4, 8);
//			sheet.mergeCells(1, 9, 4, 9);
//			sheet.mergeCells(1, 10, 4, 10);
//
//			sheet.mergeCells(6, 1, 9, 1);
//			sheet.mergeCells(7, 2, 9, 2);
//			sheet.mergeCells(7, 3, 9, 3);
//			sheet.mergeCells(7, 4, 9, 4);
//			sheet.mergeCells(7, 5, 9, 5);
//			sheet.mergeCells(7, 6, 9, 6);
//			sheet.mergeCells(7, 7, 9, 7);
//
//			WritableFont sample_content_BoldFont = new WritableFont(
//					WritableFont.ARIAL, 17, WritableFont.NO_BOLD);
//			WritableCellFormat sample_content_center = new WritableCellFormat(
//					sample_content_BoldFont);
//			sample_content_center
//					.setBorder(Border.BOTTOM, BorderLineStyle.THIN); // 线条
//			sample_content_center.setBorder(Border.TOP, BorderLineStyle.THIN); // 线条
//			sample_content_center.setBorder(Border.LEFT, BorderLineStyle.THIN); // 线条
//			sample_content_center.setBorder(Border.RIGHT, BorderLineStyle.THIN); // 线条
//			sample_content_center
//					.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			sample_content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			sample_content_center.setWrap(false); // 文字是否换行
//
//			WritableFont sample_detail_content_BoldFont = new WritableFont(
//					WritableFont.ARIAL, 16, WritableFont.NO_BOLD);
//			WritableCellFormat sample_detail_content_center = new WritableCellFormat(
//					sample_detail_content_BoldFont);
//			sample_detail_content_center.setBorder(Border.BOTTOM,
//					BorderLineStyle.THIN); // 线条
//			sample_detail_content_center.setBorder(Border.TOP,
//					BorderLineStyle.THIN); // 线条
//			sample_detail_content_center.setBorder(Border.LEFT,
//					BorderLineStyle.THIN); // 线条
//			sample_detail_content_center.setBorder(Border.RIGHT,
//					BorderLineStyle.THIN); // 线条
//			sample_detail_content_center
//					.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			sample_detail_content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			sample_detail_content_center.setWrap(false); // 文字是否换行
//
//			sheet.setRowView(2, 450);
//			sheet.setRowView(3, 450);
//			sheet.setRowView(4, 450);
//			sheet.setRowView(5, 450);
//			sheet.setRowView(6, 450);
//			sheet.setRowView(7, 450);
//			sheet.setRowView(8, 450);
//			sheet.setRowView(9, 450);
//			sheet.setRowView(10, 450);
//
//			sheet.addCell(new Label(0, 1, "样品基础信息", sample_content_center));
//			sheet
//					.addCell(new Label(0, 2, "款  号",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 3, "打  样",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 4, "尺  寸",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 5, "克  重",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 6, "材  料",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 7, "机  织",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 8, "时  间",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 9, "价  格",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(0, 10, "备  注",
//							sample_detail_content_center));
//
//			sheet.addCell(new Label(1, 2, sample.getProductNumber(),
//					sample_detail_content_center));
//			sheet.addCell(new Label(1, 3, SystemCache.getUserName(sample
//					.getCreated_user()), sample_detail_content_center));
//			sheet.addCell(new Label(1, 4, sample.getSize(),
//					sample_detail_content_center));
//			sheet.addCell(new Label(1, 5, sample.getWeight() + "  克",
//					sample_detail_content_center));
//			sheet.addCell(new Label(1, 6, sample.getMaterial(),
//					sample_detail_content_center));
//			sheet.addCell(new Label(1, 7, SystemCache.getFactoryName(sample.getFactoryId()),
//					sample_detail_content_center));
//			sheet.addCell(new Label(1, 8, DateTool.formateDate(sample
//					.getCreated_at()), sample_detail_content_center));
//			sheet.addCell(new Label(1, 9, sample.getCost() + "  元",
//					sample_detail_content_center));
//			sheet.addCell(new Label(1, 10, sample.getMemo(),
//					sample_detail_content_center));
//
//			sheet.addCell(new Label(6, 1, "公司信息", sample_content_center));
//			sheet
//					.addCell(new Label(6, 2, "公司名称",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(6, 3, "公司款号",
//							sample_detail_content_center));
//			sheet.addCell(new Label(6, 4, "业务员", sample_detail_content_center));
//			sheet
//					.addCell(new Label(6, 5, "所报价格",
//							sample_detail_content_center));
//			sheet
//					.addCell(new Label(6, 6, "报价时间",
//							sample_detail_content_center));
//			sheet.addCell(new Label(6, 7, "备注", sample_detail_content_center));
//
//			sheet.addCell(new Label(7, 2, SystemCache.getCompanyName(quotePrice
//					.getCompanyId()), sample_detail_content_center));
//			sheet.addCell(new Label(7, 3, quotePrice.getCproductN(),
//					sample_detail_content_center));
//			sheet.addCell(new Label(7, 4, SystemCache
//					.getSalesmanName(quotePrice.getSalesmanId()),
//					sample_detail_content_center));
//			sheet.addCell(new Label(7, 5, quotePrice.getPrice() + " 元",
//					sample_detail_content_center));
//			sheet.addCell(new Label(7, 6, DateTool.formateDate(quotePrice
//					.getCreated_at()), sample_detail_content_center));
//			sheet.addCell(new Label(7, 7, quotePrice.getMemo(),
//					sample_detail_content_center));
//			/** **********将以上缓存中的内容写到EXCEL文件中******** */
//			workbook.write();
//			/** *********关闭文件************* */
//			workbook.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	// public final static String exportKuaiDiDan(String filePath,
//	// String fileName, String developerName, String companyName,
//	// String salesName, String destination, String address,
//	// String companyPhone) {
//	//
//	// String result = "系统提示：Excel文件导出成功！";
//	// // 以下开始输出到EXCEL
//	// try {
//	// // 定义输出流，以便打开保存对话框______________________begin
//	// // 定义输出流，以便打开保存对话框_______________________end
//	// /** **********创建工作簿************ */
//	// WritableWorkbook workbook = Workbook.createWorkbook(new File(
//	// filePath + fileName));
//	// /** **********创建工作表************ */
//	// WritableSheet sheet = workbook.createSheet("Sheet1", 0);
//	// /** **********设置纵横打印（默认为纵打）、打印纸***************** */
//	// jxl.SheetSettings sheetset = sheet.getSettings();
//	// sheetset.setProtected(false);
//	// sheetset.setLeftMargin(1);
//	// sheetset.setRightMargin(0);
//	// sheetset.setTopMargin(0.75);
//	// sheetset.setBottomMargin(0.75);
//	// sheetset.setPaperSize(PaperSize.C);
//	//			
//	// // 设置列宽
//	// sheet.setRowView(0, 220);
//	// sheet.setRowView(1, 370);
//	// sheet.setRowView(3, 402);
//	// sheet.setRowView(5, 300);
//	//
//	// /** ***************以下是EXCEL正文数据********************* */
//	// WritableFont content_BoldFont = new WritableFont(
//	// WritableFont.ARIAL, 11, WritableFont.NO_BOLD);
//	// WritableCellFormat content_center = new WritableCellFormat(
//	// content_BoldFont);
//	// content_center.setBorder(Border.BOTTOM, BorderLineStyle.THIN); // 线条
//	// content_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//	// content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//	// content_center.setWrap(false); // 文字是否换行
//	//
//	// sheet.mergeCells(0, 1, 9, 1);
//	// sheet.mergeCells(0, 3, 9, 3);
//	// sheet.mergeCells(0, 6, 9, 6);
//	// sheet.mergeCells(0, 8, 9, 8);
//	// Label label = new Label(0, 1, "           " + developerName
//	// + get25LengthString(developerName) + shifadidian
//	// + get45LengthString(shifadidian) + salesName
//	// + get25LengthString(salesName) + "  " + destination);
//	// Label label3 = new Label(0, 3, "           " + factoryName
//	// + get72LengthString(factoryName) + companyName);
//	// Label label4 = null;
//	// Label label5 = null;
//	// if (address.length() < 19) {
//	// label4 = new Label(0, 6, factoryAddress
//	// + "                             " + address);
//	// } else {
//	// label4 = new Label(0, 6, factoryAddress
//	// + "                          "
//	// + address.substring(0, 18));
//	// sheet.mergeCells(0, 7, 9, 7);
//	// label5 = new Label(
//	// 0,
//	// 7,
//	// "                                                                                  "
//	// + address.substring(18, address.length()));
//	// }
//	// Label label6 = new Label(
//	// 0,
//	// 8,
//	// "             "
//	// + factoryPhone
//	// +
//	// "                                                                       "
//	// + companyPhone);
//	// WritableCellFormat cellFormat = new WritableCellFormat(
//	// content_BoldFont);
//	// cellFormat.setAlignment(jxl.format.Alignment.LEFT);
//	// cellFormat
//	// .setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//	// cellFormat.setWrap(true);
//	// label.setCellFormat(cellFormat);
//	// label3.setCellFormat(cellFormat);
//	// label4.setCellFormat(cellFormat);
//	// label6.setCellFormat(cellFormat);
//	// sheet.addCell(label);
//	// sheet.addCell(label3);
//	// sheet.addCell(label4);
//	// if (label5 != null) {
//	// label5.setCellFormat(cellFormat);
//	// sheet.addCell(label5);
//	// }
//	// sheet.addCell(label6);
//	// /** **********将以上缓存中的内容写到EXCEL文件中******** */
//	// workbook.write();
//	// /** *********关闭文件************* */
//	// workbook.close();
//	//
//	// } catch (Exception e) {
//	// result = "系统提示：Excel文件导出失败，原因：" + e.toString();
//	// System.out.println(result);
//	// e.printStackTrace();
//	// }
//	// return result;
//	// }
//	//	
//
//	// public final static String exportProductionNotification(String filePath,
//	// String fileName,ProductionNotification productionNotification,String
//	// picturePath) {
////	public final static String exportProductionNotification(
////			ProductionNotification productionNotification, String filePath,
////			String fileName, String appPath) throws Exception {
////
////		java.io.File File = new java.io.File(filePath + fileName);
////		java.io.File pathFile = new java.io.File(File.getParent());
////		if (!pathFile.exists()) {
////			pathFile.mkdirs();
////		}
////		// 以下开始输出到EXCEL
////		// 以下开始输出到EXCEL
////		try {
////			// 定义输出流，以便打开保存对话框______________________begin
////			// 定义输出流，以便打开保存对话框_______________________end
////			/** **********创建工作簿************ */
////			WritableWorkbook workbook = Workbook.createWorkbook(new File(
////					filePath + fileName));
////			/** **********创建工作表************ */
////			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
////			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
////			jxl.SheetSettings sheetset = sheet.getSettings();
////			sheetset.setProtected(false);
////			sheetset.setLeftMargin(0);
////			sheetset.setRightMargin(0);
////			sheetset.setTopMargin(0);
////			sheetset.setBottomMargin(0);
////			sheetset.setPaperSize(PaperSize.A4);
////			sheetset.setOrientation(PageOrientation.LANDSCAPE);
////
////			// 设置列宽
////			sheet.setColumnView(0, 17);
////			sheet.setColumnView(1, 15);
////			sheet.setColumnView(2, 1);
////			sheet.setColumnView(3, 18);
////			sheet.setColumnView(4, 17);
////			sheet.setColumnView(5, 13);
////			sheet.setColumnView(6, 15);
////			sheet.setColumnView(7, 16);
////			sheet.setColumnView(8, 21);
////			sheet.setColumnView(9, 13);
////
////			sheet.mergeCells(1, 4, 2, 4);
////			sheet.mergeCells(1, 5, 2, 5);
////			sheet.mergeCells(1, 8, 2, 8);
////			sheet.mergeCells(1, 9, 2, 9);
////			sheet.mergeCells(1, 10, 2, 10);
////			sheet.mergeCells(1, 11, 2, 11);
////			sheet.mergeCells(1, 12, 2, 12);
////			/** ***************以下是EXCEL正文数据********************* */
////			WritableFont content_BoldFont = new WritableFont(
////					WritableFont.ARIAL, 22, WritableFont.NO_BOLD);
////			WritableCellFormat content_center = new WritableCellFormat(
////					content_BoldFont);
////			content_center.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
////			content_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
////			content_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
////			content_center.setWrap(false); // 文字是否换行
////
////			sheet.mergeCells(0, 0, 9, 0);
////			sheet.setRowView(0, 680);
////			sheet.addCell(new Label(0, 0, "桐庐富伟针织厂生产通知单", content_center));
////
////			WritableFont content_BoldFont2 = new WritableFont(
////					WritableFont.ARIAL, 14, WritableFont.NO_BOLD);
////			WritableCellFormat content_center2 = new WritableCellFormat(
////					content_BoldFont2);
////			content_center2.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
////			content_center2.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
////			content_center2.setAlignment(Alignment.CENTRE); // 文字水平对齐
////			content_center2.setWrap(false); // 文字是否换行
////
////			WritableCellFormat content_center3 = new WritableCellFormat(
////					content_BoldFont2);
////			content_center3.setBorder(Border.BOTTOM, BorderLineStyle.THIN); // 线条
////			content_center3.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
////			content_center3.setAlignment(Alignment.CENTRE); // 文字水平对齐
////			content_center3.setWrap(false); // 文字是否换行
////
////			WritableCellFormat content_center4 = new WritableCellFormat(
////					content_BoldFont2);
////			content_center4.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
////			content_center4.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
////			content_center4.setAlignment(Alignment.CENTRE); // 文字水平对齐
////			content_center4.setWrap(true); // 文字是否换行
////
////			sheet.setRowView(1, 170);
////			sheet.setRowView(2, 370);
////
////			sheet.addCell(new Label(0, 2, "加工单位：", content_center2));
////			sheet.mergeCells(1, 2, 3, 2);
////			sheet.addCell(new Label(1, 2, productionNotification
////					.getProcessfactory(), content_center3));
////
////			WritableCellFormat content_center2_1 = new WritableCellFormat(
////					content_BoldFont2);
////			content_center2_1.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
////			content_center2_1.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
////			content_center2_1.setAlignment(Alignment.RIGHT); // 文字水平对齐
////			content_center2_1.setWrap(false); // 文字是否换行
////
////			sheet.mergeCells(8, 2, 9, 2);
////			sheet.addCell(new Label(8, 2, "№："
////					+ productionNotification.getNotificationNumber(),
////					content_center2_1));
////
////			sheet.addCell(new Label(0, 4, "公司名称", content_center4));
////			sheet.addCell(new Label(1, 4, "订单号", content_center4));
////			sheet.addCell(new Label(3, 4, "货号", content_center4));
////
////			sheet.mergeCells(4, 4, 5, 4);
////			sheet.addCell(new Label(4, 4, "产品名称", content_center4));
////
////			sheet.addCell(new Label(6, 4, "生产计划数", content_center4));
////			sheet.addCell(new Label(7, 4, "产品克重", content_center4));
////			sheet.addCell(new Label(8, 4, "交货日期", content_center4));
////			sheet.addCell(new Label(9, 4, "机器针型", content_center4));
////
////			// 第一行数据
////			sheet.addCell(new Label(0, 5, FuweiSystemData.getCompanyById(
////					productionNotification.getCompanyId()).getName(),
////					content_center4));
////			sheet.addCell(new Label(1, 5, productionNotification
////					.getFworderNumber(), content_center4));
////			sheet.addCell(new Label(3, 5, productionNotification
////					.getStyleNumber(), content_center4));
////
////			sheet.mergeCells(4, 5, 5, 5);
////			sheet.addCell(new Label(4, 5, productionNotification
////					.getProductName(), content_center4));
////
////			sheet.addCell(new Label(6, 5, productionNotification
////					.getExceptProductQuantity()
////					+ "", content_center4));
////			sheet.addCell(new Label(7, 5, productionNotification.getKezhong()
////					+ "", content_center4));
////			sheet.addCell(new Label(8, 5, DateFormateUtil
////					.formateDate(productionNotification.getDeadlineTime()),
////					content_center4));
////			sheet.addCell(new Label(9, 5, productionNotification
////					.getMachineZhenXing(), content_center4));
////
////			sheet.mergeCells(0, 6, 0, 7);
////			sheet.mergeCells(1, 6, 2, 7);
////			sheet.mergeCells(3, 6, 3, 7);
////			sheet.mergeCells(4, 6, 4, 7);
////
////			sheet.addCell(new Label(0, 6, "色号", content_center4));
////
////			sheet.addCell(new Label(1, 6, "色别", content_center4));
////			sheet.addCell(new Label(3, 6, "尺寸", content_center4));
////			sheet.addCell(new Label(4, 6, "生产数量", content_center4));
////
////			sheet.mergeCells(5, 6, 9, 6);
////			sheet.addCell(new Label(5, 6, "原材料信息", content_center4));
////			sheet.mergeCells(5, 7, 6, 7);
////			sheet.addCell(new Label(5, 7, "材料名称", content_center4));
////			sheet.addCell(new Label(7, 7, "材料数量kg", content_center4));
////			sheet.addCell(new Label(8, 7, "损耗", content_center4));
////			sheet.addCell(new Label(9, 7, "总材料kg", content_center4));
////			sheet.mergeCells(5, 8, 6, 8);
////			sheet.mergeCells(5, 9, 6, 9);
////			sheet.mergeCells(5, 10, 6, 10);
////			sheet.mergeCells(5, 11, 6, 11);
////			sheet.mergeCells(5, 12, 6, 12);
////
////			JSONArray jsonArray = (JSONArray) JSONSerializer
////					.toJSON(productionNotification.getContentJSONString());
////			sheet.addCell(new Label(0, 8, "", content_center4));
////			sheet.addCell(new Label(1, 8, "", content_center4));
////			sheet.addCell(new Label(2, 8, "", content_center4));
////			sheet.addCell(new Label(3, 8, "", content_center4));
////			sheet.addCell(new Label(4, 8, "", content_center4));
////			sheet.addCell(new Label(5, 8, "", content_center4));
////			sheet.addCell(new Label(6, 8, "", content_center4));
////			sheet.addCell(new Label(7, 8, "", content_center4));
////			sheet.addCell(new Label(8, 8, "", content_center4));
////			sheet.addCell(new Label(9, 8, "", content_center4));
////
////			sheet.addCell(new Label(0, 9, "", content_center4));
////			sheet.addCell(new Label(1, 9, "", content_center4));
////			sheet.addCell(new Label(2, 9, "", content_center4));
////			sheet.addCell(new Label(3, 9, "", content_center4));
////			sheet.addCell(new Label(4, 9, "", content_center4));
////			sheet.addCell(new Label(5, 9, "", content_center4));
////			sheet.addCell(new Label(6, 9, "", content_center4));
////			sheet.addCell(new Label(7, 9, "", content_center4));
////			sheet.addCell(new Label(8, 9, "", content_center4));
////			sheet.addCell(new Label(9, 9, "", content_center4));
////
////			sheet.addCell(new Label(0, 10, "", content_center4));
////			sheet.addCell(new Label(1, 10, "", content_center4));
////			sheet.addCell(new Label(2, 10, "", content_center4));
////			sheet.addCell(new Label(3, 10, "", content_center4));
////			sheet.addCell(new Label(4, 10, "", content_center4));
////			sheet.addCell(new Label(5, 10, "", content_center4));
////			sheet.addCell(new Label(6, 10, "", content_center4));
////			sheet.addCell(new Label(7, 10, "", content_center4));
////			sheet.addCell(new Label(8, 10, "", content_center4));
////			sheet.addCell(new Label(9, 10, "", content_center4));
////
////			sheet.addCell(new Label(0, 11, "", content_center4));
////			sheet.addCell(new Label(1, 11, "", content_center4));
////			sheet.addCell(new Label(2, 11, "", content_center4));
////			sheet.addCell(new Label(3, 11, "", content_center4));
////			sheet.addCell(new Label(4, 11, "", content_center4));
////			sheet.addCell(new Label(5, 11, "", content_center4));
////			sheet.addCell(new Label(6, 11, "", content_center4));
////			sheet.addCell(new Label(7, 11, "", content_center4));
////			sheet.addCell(new Label(8, 11, "", content_center4));
////			sheet.addCell(new Label(9, 11, "", content_center4));
////
////			sheet.addCell(new Label(0, 12, "", content_center4));
////			sheet.addCell(new Label(1, 12, "", content_center4));
////			sheet.addCell(new Label(2, 12, "", content_center4));
////			sheet.addCell(new Label(3, 12, "", content_center4));
////			sheet.addCell(new Label(4, 12, "", content_center4));
////			sheet.addCell(new Label(5, 12, "", content_center4));
////			sheet.addCell(new Label(6, 12, "", content_center4));
////			sheet.addCell(new Label(7, 12, "", content_center4));
////			sheet.addCell(new Label(8, 12, "", content_center4));
////			sheet.addCell(new Label(9, 12, "", content_center4));
////
////			if (jsonArray.size() > -1 && jsonArray.size() < 6) {
////				for (int i = 0; i < jsonArray.size(); i++) {
////					sheet.addCell(new Label(0, 8 + i, jsonArray
////							.getJSONObject(i).getString("sehao"),
////							content_center4));
////					sheet.addCell(new Label(1, 8 + i, jsonArray
////							.getJSONObject(i).getString("sebie"),
////							content_center4));
////					sheet.addCell(new Label(3, 8 + i, jsonArray
////							.getJSONObject(i).getString("size"),
////							content_center4));
////					sheet.addCell(new Label(4, 8 + i, jsonArray
////							.getJSONObject(i).getString("quantity"),
////							content_center4));
////					sheet.addCell(new Label(5, 8 + i, jsonArray
////							.getJSONObject(i).getString("materialName"),
////							content_center4));
////					sheet.addCell(new Label(7, 8 + i, jsonArray
////							.getJSONObject(i).getString("materialQuantity"),
////							content_center4));
////					sheet.addCell(new Label(8, 8 + i, jsonArray
////							.getJSONObject(i).getString("sunhao"),
////							content_center4));
////					sheet.addCell(new Label(9, 8 + i, jsonArray
////							.getJSONObject(i).getString("totalMaterial"),
////							content_center4));
////				}
////			}
////
////			sheet.mergeCells(3, 14, 6, 14);
////			sheet.addCell(new Label(3, 14, "加工说明", content_center4));
////
////			sheet.mergeCells(7, 14, 9, 14);
////			sheet.addCell(new Label(7, 14, "备注", content_center4));
////
////			sheet.mergeCells(0, 14, 2, 14);
////			sheet.addCell(new Label(0, 14, "图片", content_center4));
////
////			sheet.mergeCells(0, 15, 2, 21);
////			sheet.mergeCells(3, 15, 6, 21);
////			sheet.mergeCells(7, 15, 9, 21);
////
////			sheet.addCell(new Label(0, 15, "", content_center4));
////			sheet.addCell(new Label(3, 15, "", content_center4));
////			sheet.addCell(new Label(6, 15, "", content_center4));
////
////			sheet.setRowView(21, 40);
////			sheet.setRowView(22, 180);
////
////			sheet.addCell(new Label(3, 23, "负责人：", content_center2));
////			sheet.addCell(new Label(5, 23, "加工单位：", content_center2));
////			sheet.mergeCells(7, 23, 8, 23);
////			sheet.addCell(new Label(7, 23, "通知时间："
////					+ DateTool.formateDate(productionNotification
////							.getCreated_at()), content_center2));
////
////			WritableCellFormat content_center5 = new WritableCellFormat(
////					content_BoldFont2);
////			content_center5.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
////			content_center5.setVerticalAlignment(VerticalAlignment.TOP); // 文字垂直对齐
////			content_center5.setAlignment(Alignment.LEFT); // 文字水平对齐
////			content_center5.setWrap(true); // 文字是否换行
////			sheet
////					.addCell(new Label(
////							3,
////							15,
////							"一：加工方必须先到打样间封样，确认后再进行大货生产， 否则后果由加工方承担\n二：加工方务必在交货期限内保质保量的完成大货生产任务",
////							content_center5));
////
////			sheet.addCell(new Label(7, 15, productionNotification.getMemo(),
////					content_center4));
////
////			sheet.addCell(new Label(0, 15, "", content_center4));
////			File pictureFile = new File(appPath + sample.getImg_s());// imgPath
////			if (pictureFile.exists()) {
////				WritableImage a = new WritableImage(0, 12, 5, 10, pictureFile);
////				sheet.addImage(a);
////			} else {
////				sheet.addCell(new Label(0, 12, "图片缺失", content_center));
////
////			}
////			
////			File pictureFile = new File(picturePath
////					+ productionNotification.getPictureName());
////			if (pictureFile.exists()) {
////				WritableImage a = new WritableImage(0, 15, 2, 6, pictureFile);
////				sheet.addImage(a);
////			} else {
////				sheet.addCell(new Label(0, 15, "图片缺失", content_center4));
////
////			}
////
////			sheet.setRowView(4, 500);
////			sheet.setRowView(5, 500);
////			sheet.setRowView(6, 550);
////			sheet.setRowView(7, 550);
////			sheet.setRowView(8, 800);
////			sheet.setRowView(9, 800);
////			sheet.setRowView(10, 800);
////			sheet.setRowView(11, 800);
////			sheet.setRowView(12, 800);
////			sheet.setRowView(15, 1600);
////			/** **********将以上缓存中的内容写到EXCEL文件中******** */
////			workbook.write();
////			/** *********关闭文件************* */
////			workbook.close();
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw e;
////		}
////	}
//
//	private static String get25LengthString(String str) {
//		if (str == null) {
//			System.out.println("str==null");
//		} else {
//			System.out.println("长度：" + str.length());
//		}
//		if (str.length() > 27) {
//			return null;
//		}
//		StringBuffer stringBuffer = new StringBuffer();
//		for (int i = 0; i < 27 - str.length(); i++) {
//			stringBuffer.append(" ");
//		}
//		return stringBuffer.toString();
//	}
//
//	private static String get45LengthString(String str) {
//		if (str.length() > 47) {
//			return null;
//		}
//		StringBuffer stringBuffer = new StringBuffer();
//		for (int i = 0; i < 47 - str.length(); i++) {
//			stringBuffer.append(" ");
//		}
//		return stringBuffer.toString();
//	}
//
//	private static String get72LengthString(String str) {
//		if (str.length() > 74) {
//			return null;
//		}
//		StringBuffer stringBuffer = new StringBuffer();
//		for (int i = 0; i < 74 - str.length(); i++) {
//			stringBuffer.append(" ");
//		}
//		return stringBuffer.toString();
//	}
//
//}

package com.fuwei.controller.ordergrid;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.constant.Constants;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.entity.Factory;
import com.fuwei.entity.Message;
import com.fuwei.entity.Order;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Bank;
import com.fuwei.entity.financial.Invoice;
import com.fuwei.entity.ordergrid.GongxuProducingOrder;
import com.fuwei.entity.ordergrid.GongxuProducingOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrder;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.MessageService;
import com.fuwei.service.OrderService;
import com.fuwei.service.SampleService;
import com.fuwei.service.ordergrid.PlanOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/planorder")
@Controller
public class PlanOrderController extends BaseController {
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	MessageService messageService;
	@Autowired
	PlanOrderService planOrderService;
	
	/* -- 导出*/
	@RequestMapping(value = "/export/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> export(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (id == null) {
			throw new Exception("缺少计划单ID");
		}
		String lcode = "planorder/export";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有导出计划单的权限",
					null);
		}
		PlanOrder planOrder = planOrderService.get(id);
		if(planOrder == null){
			throw new Exception("找不到ID为" + id + "的计划单");
		}
		Order order = orderService.get(planOrder.getOrderId());
		if(order == null){
			throw new Exception("找不到计划单对应的订单");
		}	
		//获取订单对应的生产单的工厂IDS
		List<Integer> factoryIdlist = orderService.getProducingOrderFactoryIds(planOrder.getOrderId());
		String productfactoryStr = "";
		String seq = "";
		if(factoryIdlist!=null){
			for(Integer factoryId : factoryIdlist){
				productfactoryStr += seq + SystemCache.getFactoryName(factoryId);
				seq = " | ";
			}
		}
		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createPlanOrderFile(os, planOrder,order,productfactoryStr);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName =  "计划单_" + order.getOrderNumber()+order.getName();

		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
		return this.returnSuccess();
	}
	public void createPlanOrderFile(OutputStream os,PlanOrder planOrder,Order order,String productfactoryStr) throws Exception {

		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称
		wsheet.getSettings().setLeftMargin(0.2);//设置打印边距
		wsheet.getSettings().setRightMargin(0.2);
		wsheet.getSettings().setTopMargin(0.2);
		wsheet.getSettings().setBottomMargin(0.2);
		wsheet.getSettings().setFooterMargin(0);
		wsheet.getSettings().setHeaderMargin(0);
		//设置列宽
		wsheet.setColumnView(0, 15);//颜色
		wsheet.setColumnView(1, 15);//尺寸
		wsheet.setColumnView(2, 15);//颜色
		wsheet.setColumnView(3, 18);//纱线
		wsheet.setColumnView(4, 18);//尺寸
		wsheet.setColumnView(5, 18);//数量
		wsheet.setRowView(2, 360);//  数值/20=像素值
		wsheet.setRowView(3, 360);//
		wsheet.setRowView(4, 360);//
		wsheet.setRowView(5, 360);//
		wsheet.setRowView(6, 360);//
		wsheet.setRowView(7, 360);//
		wsheet.setRowView(8, 360);//

		// 写入表头
		WritableFont companyfont = new WritableFont(WritableFont
				.createFont("宋体"), 18, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont);
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		
		String line0_text = "桐庐富伟针织厂计划单";
		Label excelCompany = new Label(0, 0, line0_text, companyFormat);
		wsheet.addCell(excelCompany);
		wsheet.mergeCells(0, 0, 5, 0);
		wsheet.setRowView(0, 800);

		//通用字体格式
		
		WritableFont commonfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat commonFormat = new WritableCellFormat(commonfont);
		//有边框的字体（用于表格）
		WritableFont commonBorderfont = new WritableFont(WritableFont.createFont("宋体"),
				11, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat commonBorderFormat = new WritableCellFormat(commonBorderfont);
		commonBorderFormat.setAlignment(jxl.format.Alignment.CENTRE);
		commonBorderFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		
		
		WritableFont commonBorder_leftfont = new WritableFont(WritableFont.createFont("宋体"),
				11, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat commonBorder_leftFormat = new WritableCellFormat(commonBorder_leftfont);
		commonBorder_leftFormat.setAlignment(jxl.format.Alignment.LEFT);
		commonBorder_leftFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		
		//加粗
		WritableFont commonBoldfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat commonBoldFormat = new WritableCellFormat(commonBoldfont);
		//右对齐
		WritableFont common_rightfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat common_rightFormat = new WritableCellFormat(common_rightfont);
		common_rightFormat.setAlignment(jxl.format.Alignment.RIGHT);
		//左对齐
		WritableFont common_leftfont = new WritableFont(WritableFont.createFont("宋体"),
				12, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat common_leftFormat = new WritableCellFormat(common_leftfont);
		common_leftFormat.setAlignment(jxl.format.Alignment.LEFT);
		
		//写入订单号
		Label orderNumber = new Label(0, 1, "№："+order.getOrderNumber(), common_rightFormat);
		wsheet.addCell(orderNumber);
		wsheet.mergeCells(0, 1, 5, 1);
		
		
		//写入图片
		//图片的绝对路径
		String picPath = Constants.UPLOADSite + order.getImg_s();
		File pic = new File(picPath);
		//生成一个图片对象
	    // 把图片插入到sheet  
		addPictureToExcel(wsheet,pic);
		wsheet.mergeCells(0, 2, 2, 8);//先合成一个单元格
		
		//写入生产单位、公司、客户、货号、款名、跟单、发货时间	
		Label factorylabel = new Label(3, 2, "生产单位", commonBorder_leftFormat);
		wsheet.addCell(factorylabel);
		Label factory = new Label(4, 2,productfactoryStr , commonBorder_leftFormat);
		wsheet.addCell(factory);
		wsheet.mergeCells(4, 2, 5, 2);

		Label companylabel = new Label(3, 3, "公司", commonBorder_leftFormat);
		wsheet.addCell(companylabel);
		Label company = new Label(4, 3, SystemCache.getCompanyShortName(order.getCompanyId()) , commonBorder_leftFormat);
		wsheet.addCell(company);
		wsheet.mergeCells(4, 3, 5, 3);

		Label costomerlabel = new Label(3, 4, "客户", commonBorder_leftFormat);
		wsheet.addCell(costomerlabel);
		Label costomer = new Label(4, 4,SystemCache.getCustomerName(order.getCustomerId()) , commonBorder_leftFormat);
		wsheet.addCell(costomer);
		wsheet.mergeCells(4, 4, 5, 4);

		Label company_productNumberlabel = new Label(3, 5, "货号", commonBorder_leftFormat);
		wsheet.addCell(company_productNumberlabel);
		Label company_productNumber = new Label(4, 5,order.getCompany_productNumber() , commonBorder_leftFormat);
		wsheet.addCell(company_productNumber);
		wsheet.mergeCells(4, 5, 5, 5);

		Label namelabel = new Label(3, 6, "款名", commonBorder_leftFormat);
		wsheet.addCell(namelabel);
		Label name = new Label(4, 6,order.getName() , commonBorder_leftFormat);
		wsheet.addCell(name);
		wsheet.mergeCells(4, 6, 5, 6);

		Label charge_employeelabel = new Label(3, 7, "跟单", commonBorder_leftFormat);
		wsheet.addCell(charge_employeelabel);
		Label charge_employee = new Label(4, 7 ,SystemCache.getEmployeeName(order.getCharge_employee())  , commonBorder_leftFormat);
		wsheet.addCell(charge_employee);
		wsheet.mergeCells(4, 7, 5, 7);

		Label deliverytimelabel = new Label(3, 8, "发货时间", commonBorder_leftFormat);
		wsheet.addCell(deliverytimelabel);
		Label deliverytime = new Label(4, 8,DateTool.formatDateYMD(order.getDelivery_at()) , commonBorder_leftFormat);
		wsheet.addCell(deliverytime);
		wsheet.mergeCells(4, 8, 5, 8);
		
		//写入颜色及数量
		Label tabletitlelabel = new Label(0, 9, "颜色及数量", commonBoldFormat);
		wsheet.addCell(tabletitlelabel);
		wsheet.mergeCells(0, 9, 5, 9);
		
		// 写入颜色及数量表格明细
		String[] title = { "颜色","克重(g)", "机织克重(g)","纱线种类","尺寸", "生产数量"};

		// 设置Excel表头
		int columnBestWidth[] = new int[title.length]; // 保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) {
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i, 10, title[i], commonBoldFormat);
			wsheet.addCell(excelTitle);
		}
		wsheet.setRowView(10, 400);

		int c = 11; // 用于循环时Excel的行号
		List<PlanOrderDetail> detaillist = planOrder.getDetaillist();
		int count = 1;
		for (PlanOrderDetail item : detaillist) {
			wsheet.setRowView(c, 400);
			String material = SystemCache.getMaterialName(item.getYarn());
			Label content1 = new Label(0, c, item.getColor(), commonBorderFormat);
			Label content2 = new Label(1, c, item.getWeight()+"", commonBorderFormat);
			Label content3 = new Label(2, c, item.getProduce_weight()+"", commonBorderFormat);
			Label content4 = new Label(3, c, material, commonBorderFormat);
			Label content5 = new Label(4, c, item.getSize(), commonBorderFormat);
			Label content6 = new Label(5, c, item.getQuantity()+"", commonBorderFormat);

			
			wsheet.addCell(content1);
			wsheet.addCell(content2);
			wsheet.addCell(content3);
			wsheet.addCell(content4);
			wsheet.addCell(content5);
			wsheet.addCell(content6);
			++count;
			c++;

		}
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}
	
	/** 
	 * 插入图片到EXCEL 
	 *  
	 * @param picSheet sheet 
	 * @param pictureFile 图片file对象 
	 * @param cellRow 行数 
	 * @param cellCol 列数 
	 * @throws Exception 例外 
	 */  
	private void addPictureToExcel(WritableSheet picSheet, File pictureFile)  
	    throws Exception {  
	    // 开始位置  
	    double picBeginCol = 0;//cellCol - 1; //0 
	    double picBeginRow = 2;//cellRow - 1;  //2
	    // 图片时间的高度，宽度  
	    double picCellWidth = 0.0;  
	    double picCellHeight = 0.0;  
	    // 读入图片  
	    BufferedImage picImage = ImageIO.read(pictureFile);  
	    // 取得图片的像素高度，宽度   ,宽度高度不得超过150
	    int ori_picWidth = picImage.getWidth();  
	    int ori_picHeight = picImage.getHeight();   
	    int picWidth = picImage.getWidth();  
	    int picHeight = picImage.getHeight();  
	    if(picWidth>150){
	    	picWidth = 150;
	    	picHeight = picImage.getHeight()*150/ori_picWidth;
	    	ori_picHeight = picHeight;
	    }
	    if(picHeight>150){
	    	picHeight = 150;
	    	picWidth = picWidth * 150/ori_picHeight;
	    }
	      
	    // 计算图片的实际宽度  
	    int picWidth_t = picWidth * 32;  //具体的实验值，原理不清楚。  
	    for (int x = 0; x < 1234; x++) {  
	        int bc = (int) Math.floor(picBeginCol + x);  
	        // 得到单元格的宽度  
	        int v = picSheet.getColumnView(bc).getSize();  
	        double offset0_t = 0.0;  
	        if (0 == x)  
	            offset0_t = (picBeginCol - bc) * v;  
	        if (0.0 + offset0_t + picWidth_t > v) {  
	            // 剩余宽度超过一个单元格的宽度  
	            double ratio_t = 1.0;  
	            if (0 == x) {  
	                ratio_t = (0.0 + v - offset0_t) / v;  
	            }  
	            picCellWidth += ratio_t;  
	            picWidth_t -= (int) (0.0 + v - offset0_t);  
	        } else { //剩余宽度不足一个单元格的宽度  
	            double ratio_r = 0.0;  
	            if (v != 0)  
	                ratio_r = (0.0 + picWidth_t) / v;  
	            picCellWidth += ratio_r;  
	            break;  
	        }  
	    }          
	    // 计算图片的实际高度  
	    int picHeight_t = picHeight * 15;  
	    for (int x = 0; x < 1234; x++) {  
	        int bc = (int) Math.floor(picBeginRow + x);  
	        // 得到单元格的高度  
	        int v = picSheet.getRowView(bc).getSize();  
	        double offset0_r = 0.0;  
	        if (0 == x)  
	            offset0_r = (picBeginRow - bc) * v;  
	        if (0.0 + offset0_r + picHeight_t > v) {  
	            // 剩余高度超过一个单元格的高度  
	            double ratio_q = 1.0;  
	            if (0 == x)  
	                ratio_q = (0.0 + v - offset0_r) / v;  
	            picCellHeight += ratio_q;  
	            picHeight_t -= (int) (0.0 + v - offset0_r);  
	        } else {//剩余高度不足一个单元格的高度  
	            double ratio_m = 0.0;  
	            if (v != 0)  
	                ratio_m = (0.0 + picHeight_t) / v;  
	            picCellHeight += ratio_m;  
	            break;  
	        }  
	    }  
	    //生成一个图片对象。  
	    WritableImage image = new WritableImage(picBeginCol, picBeginRow,  
	            picCellWidth, picCellHeight, pictureFile);  
	    // 把图片插入到sheet  
	    picSheet.addImage(image);  
	}  
}

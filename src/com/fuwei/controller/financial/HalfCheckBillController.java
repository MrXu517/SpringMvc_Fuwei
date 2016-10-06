package com.fuwei.controller.financial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.Pager;
import com.fuwei.commons.Sort;
import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.commons.SystemSettings;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.DataCorrectRecord;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.Salary;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.ProduceBill;
import com.fuwei.entity.financial.ProduceBillDetail;
import com.fuwei.entity.financial.ProduceBillDetail_Detail;
import com.fuwei.entity.ordergrid.GongxuProducingOrder;
import com.fuwei.entity.ordergrid.GongxuProducingOrderDetail;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrder;
import com.fuwei.entity.ordergrid.HalfCheckRecordOrderDetail;
import com.fuwei.entity.ordergrid.PlanOrderDetail;
import com.fuwei.entity.ordergrid.ProducingOrder;
import com.fuwei.entity.ordergrid.ProducingOrderDetail;
import com.fuwei.entity.producesystem.FuliaoIn;
import com.fuwei.entity.producesystem.HalfStoreInOut;
import com.fuwei.entity.producesystem.HalfStoreInOutDetail;
import com.fuwei.entity.producesystem.HalfStoreReturn;
import com.fuwei.entity.producesystem.HalfStoreReturnDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.ProduceBillService;
import com.fuwei.service.ordergrid.GongxuProducingOrderService;
import com.fuwei.service.ordergrid.HalfCheckRecordOrderService;
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.producesystem.HalfCurrentStockService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/halfcheckbill")
@Controller
/* 半检对账单 */
public class HalfCheckBillController extends BaseController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	HalfCheckRecordOrderService halfCheckRecordOrderService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,
			Integer companyId, Integer charge_employee, String orderNumber,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		String lcode = "halfcheckbill/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看半检记录单列表的权限", null);
		}

		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		Pager pager = new Pager();
		if (page != null && page > 0) {
			pager.setPageNo(page);
		}

		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		
		pager = halfCheckRecordOrderService.getList(pager, start_time_d, end_time_d,
				companyId,charge_employee,orderNumber, sortList);
		
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("charge_employeeId", charge_employee);
		List<Employee> employeelist = new ArrayList<Employee>();
		for(Employee temp : SystemCache.employeelist){
			if(temp.getIs_charge_employee()){
				employeelist.add(temp);
			}		
		}
		request.setAttribute("employeelist", employeelist);
		request.setAttribute("companyId", companyId);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("pager", pager);
		return new ModelAndView("financial/halfcheckbill/index");
	}
	
	// 导出
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> export(String start_time, String end_time,
			Integer companyId, Integer charge_employeeId, String orderNumber,
			String sortJSON,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "halfcheckbill/export";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有导出半检记录单列表的权限", null);
		}
		
		Date start_time_d = DateTool.parse(start_time);
		Date end_time_d = DateTool.parse(end_time);
		List<Sort> sortList = null;
		if (sortJSON != null) {
			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
		}
		if (sortList == null) {
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		List<HalfCheckRecordOrder> list = halfCheckRecordOrderService.getList(start_time_d, end_time_d, companyId, charge_employeeId, orderNumber, sortList);

		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createExportFile(os, start_time,  end_time,
				 companyId,  charge_employeeId,  orderNumber, list);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName= "富伟半检记录列表";
		if(companyId!=null){
			fileName += "_" + SystemCache.getCompanyShortName(companyId);
		}
		if(charge_employeeId!=null){
			fileName += "_" + SystemCache.getEmployeeName(charge_employeeId);
		}
		if(start_time_d!=null){
			fileName += "_从" + DateTool.formatDateYMD(start_time_d);
		}
		if(end_time_d!=null){
			fileName += "_至" + DateTool.formatDateYMD(end_time_d);
		}
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

	public void createExportFile(OutputStream os, String start_time, String end_time,
			Integer companyId, Integer charge_employeeId, String orderNumber,List<HalfCheckRecordOrder> list) throws Exception {

		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件

		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称
		wsheet.setPageSetup(PageOrientation.PORTRAIT);// 设置打印横向
		wsheet.getSettings().setLeftMargin(0.4);// 设置打印边距
		wsheet.getSettings().setRightMargin(0.4);
		wsheet.getSettings().setTopMargin(0.4);
		wsheet.getSettings().setBottomMargin(0.4);
		wsheet.getSettings().setFooterMargin(0);
		wsheet.getSettings().setHeaderMargin(0);
		// 设置公司名
		WritableFont companyfont = new WritableFont(WritableFont
				.createFont("宋体"), 18, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont);
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		String line0_text = "桐庐富伟针织厂半检记录单列表";
		Label excelCompany = new Label(0, 0, line0_text, companyFormat);
		wsheet.addCell(excelCompany);
		wsheet.mergeCells(0, 0, 8, 0);
		wsheet.setRowView(0, 800);

		// 设置Excel字体
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		int tempRow = 1;
		// 添加 公司，跟单人，日期
		WritableCellFormat headFormat = new WritableCellFormat(
				new WritableFont(WritableFont.createFont("宋体"), 12,
						WritableFont.BOLD, false,
						jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK));
		int temp_count=0;
		if(companyId!=null){
			Label excelFactory1 = new Label(temp_count, tempRow, "公司：", headFormat);
			++temp_count;
			Label excelFactory2 = new Label(temp_count, tempRow, SystemCache.getCompanyShortName(companyId), headFormat);
			++temp_count;
			wsheet.addCell(excelFactory1);
			wsheet.addCell(excelFactory2);
			tempRow=1;
		}
		
		
		//跟单人
		if(charge_employeeId!=null){
			Label excelEmployee1 = new Label(temp_count, tempRow, "跟单人：", headFormat);
			++temp_count;
			Label excelEmployee2 = new Label(temp_count, tempRow, SystemCache.getEmployeeName(charge_employeeId), headFormat);
			++temp_count;
			wsheet.addCell(excelEmployee1);
			wsheet.addCell(excelEmployee2);
			tempRow=1;
		}
		
		//单号
		if(orderNumber!=null && !orderNumber.equals("")){
			Label excelOrderNumber1 = new Label(temp_count, tempRow, "订单号：", headFormat);
			++temp_count;
			Label excelOrderNumber2 = new Label(temp_count, tempRow, orderNumber, headFormat);
			++temp_count;
			wsheet.addCell(excelOrderNumber1);
			wsheet.addCell(excelOrderNumber2);
			tempRow=1;
		}
		//时间
		if((start_time!=null && !start_time.equals("")) || (end_time!=null && !end_time.equals(""))){
			Label excelTime1 = new Label(0, tempRow, "时间：", headFormat);
			Label excelTimeStart1 = new Label(1, tempRow, "从：" + start_time, headFormat);
			Label excelTimeEnd2 = new Label(3, tempRow, "到：" + end_time, headFormat);
			wsheet.addCell(excelTime1);
			wsheet.addCell(excelTimeStart1);
			wsheet.addCell(excelTimeEnd2);
			wsheet.mergeCells(1, tempRow, 2, tempRow);
			wsheet.mergeCells(3, tempRow, 4, tempRow);
			++tempRow;
		}
		
		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2);
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat2.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框

		String[] title = { "序号",  "日期", "订单号", "公司", "跟单人",  "款名",
				"颜色及数量","","" };
		String[] title2 = { "", "", "", "", "", "", "颜色", "尺寸", "生产数量"};
		// 设置Excel表头
		int columnBestWidth[] = new int[title.length]; // 保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) {
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i, tempRow, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		for (int i = 0; i < title2.length; i++) {
			int tempLength = title2[i].getBytes().length;
			if (tempLength > columnBestWidth[i]) {
				columnBestWidth[i] = tempLength;
			}
			Label excelTitle = new Label(i, tempRow+1, title2[i], titleFormat);
			wsheet.addCell(excelTitle);
		}

		wsheet.setRowView(1, 400);
		wsheet.mergeCells(0, tempRow, 0, tempRow+1);
		wsheet.mergeCells(1, tempRow, 1, tempRow+1);
		wsheet.mergeCells(2, tempRow, 2, tempRow+1);
		wsheet.mergeCells(3, tempRow, 3, tempRow+1);
		wsheet.mergeCells(4, tempRow, 4, tempRow+1);
		wsheet.mergeCells(5, tempRow, 5, tempRow+1);
		wsheet.mergeCells(6, tempRow, 8, tempRow);

		int c = tempRow+2; // 用于循环时Excel的行号

		int count = 1;
		
		for (HalfCheckRecordOrder item : list) {
			wsheet.setRowView(c, 400);
			
			
			Label content1 = new Label(0, c, count + "", titleFormat2);
			++count;
			Label content2 = new Label(1, c,
					DateTool.formatDateYMD(item.getCreated_at()) , titleFormat2);
			Label content3 = new Label(2, c,item.getOrderNumber(),
					titleFormat2);
			Label content4 = new Label(3, c,SystemCache.getCompanyShortName(item.getCompanyId()),
					titleFormat2);
			Label content5 = new Label(4, c,SystemCache.getEmployeeName(item.getCharge_employee()),
					titleFormat2);
			Label content6 = new Label(5, c, item.getName(),
					titleFormat2);
			
			List<PlanOrderDetail> detail_detaillist = item.getDetaillist();
			Label content7 = new Label(6, c, detail_detaillist.get(0).getColor(), titleFormat2);
			Label content8 = new Label(7, c, detail_detaillist.get(0).getSize(), titleFormat2);
			Label content9 = new Label(8, c, detail_detaillist.get(0).getQuantity()+"", titleFormat2);
			
			wsheet.addCell(content1);
			wsheet.addCell(content2);
			wsheet.addCell(content3);
			wsheet.addCell(content4);
			wsheet.addCell(content5);
			wsheet.addCell(content6);
			wsheet.addCell(content7);
			wsheet.addCell(content8);
			wsheet.addCell(content9);
			
			c++;
			
			detail_detaillist.remove(0);
			for(PlanOrderDetail detail_detail : detail_detaillist){
				Label tempcontent7 = new Label(6, c, detail_detail.getColor(), titleFormat2);
				Label tempcontent8 = new Label(7, c, detail_detail.getSize(), titleFormat2);
				Label tempcontent9 = new Label(8, c, detail_detail.getQuantity()+"", titleFormat2);
				wsheet.addCell(new Label(0, c, "", titleFormat2));
				wsheet.addCell(new Label(1, c, "", titleFormat2));
				wsheet.addCell(new Label(2, c, "", titleFormat2));
				wsheet.addCell(new Label(3, c, "", titleFormat2));
				wsheet.addCell(new Label(4, c, "", titleFormat2));
				wsheet.addCell(new Label(5, c, "", titleFormat2));
				wsheet.addCell(tempcontent7);
				wsheet.addCell(tempcontent8);
				wsheet.addCell(tempcontent9);
				c++;
			}
			
			int width1 = content1.getContents().getBytes().length;
			int width2 = content2.getContents().getBytes().length;
			int width3 = content3.getContents().getBytes().length;
			int width4 = content4.getContents().getBytes().length;
			int width5 = content5.getContents().getBytes().length;
			int width6 = content6.getContents().getBytes().length;
			int width7 = content7.getContents().getBytes().length;
			int width8 = content8.getContents().getBytes().length;
			int width9 = content9.getContents().getBytes().length;
			
			if (columnBestWidth[0] < width1) {
				columnBestWidth[0] = width1;
			}
			if (columnBestWidth[1] < width2) {
				columnBestWidth[1] = width2;
			}
			if (columnBestWidth[2] < width3) {
				columnBestWidth[2] = width3;
			}
			if (columnBestWidth[3] < width4) {
				columnBestWidth[3] = width4;
			}
			if (columnBestWidth[4] < width5) {
				columnBestWidth[4] = width5;
			}
			if (columnBestWidth[5] < width6) {
				columnBestWidth[5] = width6;
			}
			if (columnBestWidth[6] < width7) {
				columnBestWidth[6] = width7;
			}
			if (columnBestWidth[7] < width8) {
				columnBestWidth[7] = width8;
			}
			if (columnBestWidth[8] < width9) {
				columnBestWidth[8] = width9;
			}
		}
		
		for (int p = 0; p < columnBestWidth.length; ++p) {
			wsheet.setColumnView(p, columnBestWidth[p]);
		}
		
		
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}
}

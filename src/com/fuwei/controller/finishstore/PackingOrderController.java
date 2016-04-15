package com.fuwei.controller.finishstore;

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
import com.fuwei.controller.BaseController;
import com.fuwei.entity.Employee;
import com.fuwei.entity.Order;
import com.fuwei.entity.Salary;
import com.fuwei.entity.User;
import com.fuwei.entity.finishstore.PackingOrder;
import com.fuwei.entity.finishstore.PackingOrderDetail;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.OrderService;
import com.fuwei.service.finishstore.PackingOrderService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/packing_order")
@Controller
public class PackingOrderController extends BaseController {
	@Autowired
	OrderService orderService;
	@Autowired
	PackingOrderService packingOrderService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/list/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorder(@PathVariable Integer orderId ,HttpSession session, HttpServletRequest request)
			throws Exception {
		if(orderId == null){
			throw new Exception("订单号不能为空");
		}
		request.setAttribute("orderId", orderId);
		List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
		request.setAttribute("packingOrderList", packingOrderList);
		return new ModelAndView("packing_order/order");
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView listbyorderNumber(String orderNumber ,HttpSession session, HttpServletRequest request)
			throws Exception {
		if(orderNumber == null){
			throw new Exception("订单号不能为空");
		}
		request.setAttribute("orderNumber", orderNumber);
		Order order = orderService.get(orderNumber);
		if(order == null){
			throw new Exception("找不到订单号为"+orderNumber +"的订单");
		}
		int orderId = order.getId();
		request.setAttribute("orderId", orderId);
		List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
		request.setAttribute("packingOrderList", packingOrderList);
		return new ModelAndView("packing_order/order");
		
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, String orderNumber,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "packing_order/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看装箱单列表的权限", null);
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
		pager = packingOrderService.getList(pager, start_time_d, end_time_d,companyId,orderNumber, sortList);

		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("companyId", companyId);
		request.setAttribute("orderNumber", orderNumber);//订单号
		request.setAttribute("pager", pager);
		return new ModelAndView("packing_order/index");
	}
	
	
	
	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView add(@PathVariable Integer orderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
		}
		try {
			if(orderId!=null){
				Order order = orderService.get(orderId);
				request.setAttribute("order", order);
				return new ModelAndView("packing_order/add");
			}
			throw new Exception("缺少订单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/scan", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView scan(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("packing_order/scan");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> add(PackingOrder packingOrder,String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
		}
		try {	
			if (packingOrder.getOrderId() == null
					|| packingOrder.getOrderId() == 0) {
				throw new PermissionDeniedDataAccessException(
						"装箱单必须属于一张订单", null);
			}
			Order order = orderService.get(packingOrder.getOrderId());
			
			if(order == null){
				throw new PermissionDeniedDataAccessException(
						"订单不存在", null);
			}
			packingOrder.setCreated_at(DateTool.now());// 设置创建时间
			packingOrder.setUpdated_at(DateTool.now());// 设置更新时间
			packingOrder.setCreated_user(user.getId());// 设置创建人
			
			packingOrder.setCharge_employee(order.getCharge_employee());
			packingOrder.setCompany_productNumber(order.getCompany_productNumber());
			packingOrder.setCompanyId(order.getCompanyId());
			packingOrder.setName(order.getName());
			packingOrder.setOrderNumber(order.getOrderNumber());
			
			//判断是否有数量为0的明细项 、 设置总数量、总箱数、总立方
			int total_quantity = 0;
			int total_cartons = 0 ;
			double total_capacity = 0.0;
			List<PackingOrderDetail> detaillist = SerializeTool
			.deserializeList(details, PackingOrderDetail.class);
			Iterator<PackingOrderDetail> iter = detaillist.iterator();  
			while(iter.hasNext()){  
				PackingOrderDetail detail = iter.next();  
			    int quantity = detail.getQuantity();
			    int per_carton_quantity = detail.getPer_carton_quantity();
			    if(quantity == 0){  
			        iter.remove();  
			    } 
			    if(per_carton_quantity ==0){
			    	iter.remove(); 
			    }
			    int cartons = quantity%per_carton_quantity==0?quantity/per_carton_quantity:quantity/per_carton_quantity+1;;  
				double L = detail.getBox_L();
				double W = detail.getBox_W();
				double H = detail.getBox_H();
				double capacity = L/100 * W/100 * H/100 * cartons;
				capacity = NumberUtil.formateDouble(capacity, 2);
				detail.setCapacity(capacity);
			    detail.setCartons(cartons);
			    total_quantity += quantity;
				total_cartons += cartons;
				total_capacity += capacity;
				detail.setOrderId(packingOrder.getOrderId());
			    	
			}  
			if(detaillist.size() <=0){
				throw new Exception("本次装箱数量均为0，无法创建装箱单");
			}
			//判断是否有数量为0的明细项			
			packingOrder.setDetaillist(detaillist);
    		// 设置总数量、总箱数、总立方
			total_capacity = NumberUtil.formateDouble(total_capacity, 2);
			packingOrder.setCapacity(total_capacity);
			packingOrder.setCartons(total_cartons);
			packingOrder.setQuantity(total_quantity);
			
			
			
			Integer tableOrderId = packingOrderService.add(packingOrder);
			return this.returnSuccess("id", tableOrderId);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有删除装箱单的权限", null);
		}
		int success = packingOrderService.remove(id);	
		return this.returnSuccess();
		
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public PackingOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String lcode = "packing_order/get";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
		}
		PackingOrder packingOrder = packingOrderService.get(id);
		return packingOrder;
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		
		if (id == null) {
			throw new Exception("缺少装箱单ID");
		}		
		String lcode = "packing_order/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
		}
		
		PackingOrder packingOrder = packingOrderService.getAndDetail(id);
		Order order = orderService.get(packingOrder.getOrderId());
		request.setAttribute("packingOrder", packingOrder);
		request.setAttribute("order", order);
		return new ModelAndView("packing_order/detail");
	}
	
	@RequestMapping(value = "/put/{tableOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView update(@PathVariable Integer tableOrderId,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
		}
		try {
			if(tableOrderId!=null){
				PackingOrder packingOrder = packingOrderService.getAndDetail(tableOrderId);
				request.setAttribute("packingOrder", packingOrder);
				return new ModelAndView("packing_order/edit");			
			}
			throw new Exception("缺少装箱单ID");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(PackingOrder packingOrder, String details,HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "packing_order/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有编辑装箱单的权限", null);
		}
		packingOrder.setUpdated_at(DateTool.now());
		//判断是否有数量为0的明细项 、 设置总数量、总箱数、总立方
		int total_quantity = 0;
		int total_cartons = 0 ;
		double total_capacity = 0.0;
		List<PackingOrderDetail> detaillist = SerializeTool
		.deserializeList(details, PackingOrderDetail.class);
		Iterator<PackingOrderDetail> iter = detaillist.iterator();  
		while(iter.hasNext()){  
			PackingOrderDetail detail = iter.next();  
		    int quantity = detail.getQuantity();
		    int per_carton_quantity = detail.getPer_carton_quantity();
		    if(quantity == 0){  
		        iter.remove();  
		    } 
		    if(per_carton_quantity ==0){
		    	iter.remove(); 
		    }
		    int cartons = quantity%per_carton_quantity==0?quantity/per_carton_quantity:quantity/per_carton_quantity+1;;  
			double L = detail.getBox_L();
			double W = detail.getBox_W();
			double H = detail.getBox_H();
			double capacity = L/100 * W/100 * H/100 * cartons;
			capacity = NumberUtil.formateDouble(capacity, 2);
			detail.setCapacity(capacity);
		    detail.setCartons(cartons);
		    total_quantity += quantity;
			total_cartons += cartons;
			total_capacity += capacity;
		}   
		if(detaillist.size() <=0){
			throw new Exception("本次装箱数量均为0，无法创建装箱单");
		}
		//判断是否有数量为0的明细项			
		packingOrder.setDetaillist(detaillist);
		// 设置总数量、总箱数、总立方
		total_capacity = NumberUtil.formateDouble(total_capacity, 2);
		packingOrder.setCapacity(total_capacity);
		packingOrder.setCartons(total_cartons);
		packingOrder.setQuantity(total_quantity);
		Integer tableOrderId = packingOrderService.update(packingOrder);
		return this.returnSuccess("id", tableOrderId);
		
	}

	//2015-5-10导出花名册
	@RequestMapping(value = "/export/{packingOrderId}", method = RequestMethod.GET)
	@ResponseBody
	public void export(@PathVariable Integer packingOrderId, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		if(packingOrderId == null){
			throw new Exception("装箱单ID不能为空");
		}
		PackingOrder packingOrder = packingOrderService.getAndDetail(packingOrderId);
		if(packingOrder == null){
			throw new Exception("找不到ID为"+packingOrderId+"的装箱单");
		}
	    //填充数据	       
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	        	createPackingOrderFile(packingOrder,os);
	        } catch (IOException e) {
	            throw e;
	        }
	        String fileName= packingOrder.getCompany_productNumber() + packingOrder.getName() + "装箱单";
	        byte[] content = os.toByteArray();
	        InputStream is = new ByteArrayInputStream(content);
	        // 设置response参数，可以打开下载页面
	        response.reset();
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
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
	}
	
	public void createPackingOrderFile(PackingOrder packingOrder, OutputStream os)throws Exception {	
		WritableWorkbook wbook = Workbook.createWorkbook(os); //建立excel文件 
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); //工作表名称 
		wsheet.setPageSetup(PageOrientation.LANDSCAPE);//设置打印横向
		wsheet.getSettings().setLeftMargin(0.4);//设置打印边距
		wsheet.getSettings().setRightMargin(0.4);
		wsheet.getSettings().setTopMargin(1);
		wsheet.getSettings().setBottomMargin(0.4);
		wsheet.getSettings().setTopMargin(0.4);
		wsheet.getSettings().setFooterMargin(0);
		wsheet.getSettings().setHeaderMargin(0);
		//设置公司名
		WritableFont companyfont = new WritableFont(WritableFont.createFont("宋体"), 22, 
				WritableFont.NO_BOLD, false, 
				jxl.format.UnderlineStyle.NO_UNDERLINE, 
				jxl.format.Colour.BLACK); 
		WritableCellFormat companyFormat = new WritableCellFormat(companyfont); 
		companyFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		companyFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		String line0_text = "桐庐富伟针织厂" + DateTool.getYear(packingOrder.getCreated_at()) + "年  " + packingOrder.getCompany_productNumber()+ packingOrder.getName();
		Label excelCompany = new Label(0, 0,line0_text , companyFormat); 
		wsheet.addCell(excelCompany); 
		wsheet.setRowView(0, 800);
		
		
		
		//设置Excel字体 
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), 12, 
		WritableFont.NO_BOLD, false, 
		jxl.format.UnderlineStyle.NO_UNDERLINE, 
		jxl.format.Colour.BLACK); 
		WritableCellFormat titleFormat = new WritableCellFormat(wfont); 
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);   
		titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		//数字字体格式
		jxl.write.NumberFormat nf_int = new jxl.write.NumberFormat("0");    //设置数字格式：整数
		jxl.write.NumberFormat nf_double = new jxl.write.NumberFormat("0.00");    //设置数字格式：小数保留两位小数
		jxl.write.WritableCellFormat wcfN_int = new jxl.write.WritableCellFormat(nf_int); //设置表单格式    
		jxl.write.WritableCellFormat wcfN_double = new jxl.write.WritableCellFormat(nf_double); //设置表单格式    
		wcfN_int.setAlignment(jxl.format.Alignment.CENTRE);  
		wcfN_int.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		wcfN_int.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfN_double.setAlignment(jxl.format.Alignment.CENTRE);  
		wcfN_double.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //BorderLineStyle边框
		wcfN_double.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfN_double.setFont(wfont);
		wcfN_int.setFont(wfont);
		
		List<String> title = new ArrayList<String>();
		int col = 0;
		if(packingOrder.getCol1_id()!=null){
			title.add(SystemCache.getPackPropertyName(packingOrder.getCol1_id()));
			++col;
		}
		if(packingOrder.getCol2_id()!=null){
			title.add(SystemCache.getPackPropertyName(packingOrder.getCol2_id()));
			++col;
		}
		if(packingOrder.getCol3_id()!=null){
			title.add(SystemCache.getPackPropertyName(packingOrder.getCol3_id()));
			++col;
		}
		if(packingOrder.getCol4_id()!=null){
			title.add(SystemCache.getPackPropertyName(packingOrder.getCol4_id()));
			++col;
		}
		title.add("颜色");title.add("数量");title.add("每箱数量");
		title.add("外箱尺寸");title.add("");title.add("");
		title.add("毛净重");title.add("");title.add("箱数");title.add("箱号");title.add("每包几件");
		
		String []title2 = {"","","","L","W","H","毛重","净重","","",""};
		//设置Excel表头 
		int columnBestWidth[]= new int[title.size()];    //保存最佳列宽数据的数组
		for (int i = 0; i < title.size(); i++) { 	
			columnBestWidth[i] = title.get(i).getBytes().length;
			Label excelTitle = new Label(i,1, title.get(i), titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		for(int i = 0 ; i < col ; ++i){
			Label excelTitle = new Label(i,2, "", titleFormat); 
			wsheet.addCell(excelTitle); 
		}
		for (int i = 0; i < title2.length; i++) { 	
			int tempLength = title2[i].getBytes().length;
			int index = col + i;
			if(tempLength>columnBestWidth[index]){
				columnBestWidth[index] = tempLength;
			}
			Label excelTitle = new Label(index,2, title2[i], titleFormat); 
			wsheet.addCell(excelTitle); 
		} 
		
		wsheet.setRowView(1,400);
		for(int i = 0 ; i < col ; ++i){
			wsheet.mergeCells(i,1,i,2);//列号、行号、列号、行号
		}
		wsheet.mergeCells(col,1,col,2);//列号、行号、列号、行号
		wsheet.mergeCells(col+1,1,col+1,2);
		wsheet.mergeCells(col+2,1,col+2,2);
		wsheet.mergeCells(col+3,1,col+5,1);
		wsheet.mergeCells(col+6,1,col+7,1);
		wsheet.mergeCells(col+8,1,col+8,2);
		wsheet.mergeCells(col+9,1,col+9,2);
		wsheet.mergeCells(col+10,1,col+10,2);
		
		wsheet.mergeCells(0,0,col+10,0);//合并标题行
		
		int c = 3; //用于循环时Excel的行号 			
		
		int count = 1 ;
		for(PackingOrderDetail detail : packingOrder.getDetaillist()){
			wsheet.setRowView(c,400);
			++count;
			int tempCol = 0;
			if(packingOrder.getCol1_id()!=null){
				Label content1 = new Label(tempCol, c, detail.getCol1_value(),titleFormat); 
				wsheet.addCell(content1); 
				int width1 = content1.getContents().getBytes().length;
				if(columnBestWidth[tempCol] < width1){
					columnBestWidth[tempCol] = width1;
				}
				++tempCol;
			}
			if(packingOrder.getCol2_id()!=null){
				Label content2 = new Label(tempCol, c, detail.getCol2_value(),titleFormat); 
				wsheet.addCell(content2); 
				int width2 = content2.getContents().getBytes().length;
				if(columnBestWidth[tempCol] < width2){
					columnBestWidth[tempCol] = width2;
				}
				++tempCol;
			}
			if(packingOrder.getCol3_id()!=null){
				Label content3 = new Label(tempCol, c, detail.getCol3_value(),titleFormat); 
				wsheet.addCell(content3); 
				int width3 = content3.getContents().getBytes().length;
				if(columnBestWidth[tempCol] < width3){
					columnBestWidth[tempCol] = width3;
				}
				++tempCol;
			}
			if(packingOrder.getCol4_id()!=null){
				Label content4 = new Label(tempCol, c, detail.getCol4_value(),titleFormat); 
				wsheet.addCell(content4); 
				int width4 = content4.getContents().getBytes().length;
				if(columnBestWidth[tempCol] < width4){
					columnBestWidth[tempCol] = width4;
				}
				++tempCol;
			}
			Label content5 = new Label(tempCol, c, detail.getColor().trim(),titleFormat); 
			jxl.write.Number content6 = new jxl.write.Number(tempCol+1, c, detail.getQuantity(),wcfN_int); 
			jxl.write.Number content7 = new jxl.write.Number(tempCol+2, c, detail.getPer_carton_quantity(),wcfN_int); 
			jxl.write.Number content8 = new jxl.write.Number(tempCol+3, c, detail.getBox_L(),wcfN_int); 
			jxl.write.Number content9 = new jxl.write.Number(tempCol+4, c, detail.getBox_W(),wcfN_int); 
			jxl.write.Number content10 = new jxl.write.Number(tempCol+5, c, detail.getBox_H(),wcfN_int); 
			jxl.write.Number content11 = new jxl.write.Number(tempCol+6, c, detail.getGross_weight(),wcfN_double); 
			jxl.write.Number content12 = new jxl.write.Number(tempCol+7, c, detail.getNet_weight(),wcfN_double); 
			jxl.write.Number content13 = new jxl.write.Number(tempCol+8, c, detail.getCartons(),wcfN_int); 
			Label content14 = new Label(tempCol+9, c, detail.getBox_number_start() + "-" + detail.getBox_number_end(),titleFormat); 
			jxl.write.Number content15 = new jxl.write.Number(tempCol+10, c, detail.getPer_pack_quantity(),wcfN_int); 			
			
			wsheet.addCell(content5); 
			wsheet.addCell(content6); 
			wsheet.addCell(content7); 
			wsheet.addCell(content8); 
			wsheet.addCell(content9); 
			wsheet.addCell(content10); 
			wsheet.addCell(content11); 
			wsheet.addCell(content12); 
			wsheet.addCell(content13);
			wsheet.addCell(content14);
			wsheet.addCell(content15);
			
			
			int width5 = content5.getContents().getBytes().length;
			int width6 = content6.getContents().getBytes().length;
			int width7 = content7.getContents().getBytes().length;
			int width8 = content8.getContents().getBytes().length;
			int width9 = content9.getContents().getBytes().length;
			int width10 = content10.getContents().getBytes().length;
			int width11 = content11.getContents().getBytes().length;
			int width12 = content12.getContents().getBytes().length;
			int width13 = content13.getContents().getBytes().length;
			int width14 = content14.getContents().getBytes().length;
			int width15 = content15.getContents().getBytes().length;
			if(columnBestWidth[tempCol] < width5){
				columnBestWidth[tempCol] = width5;
			}if(columnBestWidth[tempCol+1] < width6){
				columnBestWidth[tempCol+1] = width6;
			}if(columnBestWidth[tempCol+2] < width7){
				columnBestWidth[tempCol+2] = width7;
			}if(columnBestWidth[tempCol+3] < width8){
				columnBestWidth[tempCol+3] = width8;
			}if(columnBestWidth[tempCol+4] < width9){
				columnBestWidth[tempCol+4] = width9;
			}if(columnBestWidth[tempCol+5] < width10){
				columnBestWidth[tempCol+5] = width10;
			}if(columnBestWidth[tempCol+6] < width11){
				columnBestWidth[tempCol+6] = width11;
			}if(columnBestWidth[tempCol+7] < width12){
				columnBestWidth[tempCol+7] = width12;
			}if(columnBestWidth[tempCol+8] < width13){
				columnBestWidth[tempCol+8] = width13;
			}if(columnBestWidth[tempCol+9] < width14){
				columnBestWidth[tempCol+9] = width14;
			}if(columnBestWidth[tempCol+10] < width15){
				columnBestWidth[tempCol+10] = width15;
			}
			c++; 
		} 
		for(int p = 0 ; p < columnBestWidth.length ; ++p){
			wsheet.setColumnView(p, columnBestWidth[p]+3);
		}
		wbook.write(); //写入文件 
		wbook.close(); 
		os.close(); 
	}
}



//package com.fuwei.controller.finishstore;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import net.sf.json.JSONObject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.PermissionDeniedDataAccessException;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.fuwei.commons.Pager;
//import com.fuwei.commons.Sort;
//import com.fuwei.commons.SystemCache;
//import com.fuwei.commons.SystemContextUtils;
//import com.fuwei.constant.Constants;
//import com.fuwei.controller.BaseController;
//import com.fuwei.entity.Order;
//import com.fuwei.entity.User;
//import com.fuwei.entity.finishstore.PackingOrder;
//import com.fuwei.service.AuthorityService;
//import com.fuwei.service.OrderService;
//import com.fuwei.service.finishstore.PackingOrderService;
//import com.fuwei.util.Any2PDFUtil;
//import com.fuwei.util.DateTool;
//import com.fuwei.util.SerializeTool;
//
//@RequestMapping("/packing_order")
//@Controller
//public class PackingOrderController extends BaseController {
//	@Autowired
//	OrderService orderService;
//	@Autowired
//	PackingOrderService packingOrderService;
//	@Autowired
//	AuthorityService authorityService;
//	
//	@RequestMapping(value = "/list/{orderId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView listbyorder(@PathVariable Integer orderId ,HttpSession session, HttpServletRequest request)
//			throws Exception {
//		if(orderId == null){
//			throw new Exception("订单号不能为空");
//		}
//		request.setAttribute("orderId", orderId);
//		List<PackingOrder> packingOrderList = packingOrderService.getListByOrder(orderId);
//		request.setAttribute("packingOrderList", packingOrderList);
//		return new ModelAndView("packing_order/order");
//		
//	}
//	@RequestMapping(value = "/index", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId, String orderNumber,
//			String sortJSON, HttpSession session, HttpServletRequest request)
//			throws Exception {
//
//		String lcode = "packing_order/index";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看装箱单列表的权限", null);
//		}
//
//		Date start_time_d = DateTool.parse(start_time);
//		Date end_time_d = DateTool.parse(end_time);
//		Pager pager = new Pager();
//		if (page != null && page > 0) {
//			pager.setPageNo(page);
//		}
//
//		List<Sort> sortList = null;
//		if (sortJSON != null) {
//			sortList = SerializeTool.deserializeList(sortJSON, Sort.class);
//		}
//		if (sortList == null) {
//			sortList = new ArrayList<Sort>();
//		}
//		Sort sort = new Sort();
//		sort.setDirection("desc");
//		sort.setProperty("created_at");
//		sortList.add(sort);
//		pager = packingOrderService.getList(pager, start_time_d, end_time_d,companyId,orderNumber, sortList);
//		if (pager != null & pager.getResult() != null) {
//			List<PackingOrder> orderlist = (List<PackingOrder>) pager.getResult();
//		}
//
//		request.setAttribute("start_time", start_time_d);
//		request.setAttribute("end_time", end_time_d);
//		request.setAttribute("companyId", companyId);
//		request.setAttribute("orderNumber", orderNumber);//订单号
//		request.setAttribute("pager", pager);
//		return new ModelAndView("packing_order/index");
//	}
//	
//	
//	
//	@RequestMapping(value = "/add/{orderId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView add(@PathVariable Integer orderId,
//			HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/add";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
//		}
//		try {
//			if(orderId!=null){
//				Order order = orderService.get(orderId);
//				request.setAttribute("order", order);
//				return new ModelAndView("packing_order/add");
//			}
//			throw new Exception("缺少订单ID");
//			
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	@RequestMapping(value = "/add", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> add(PackingOrder packingOrder,@RequestParam("file") CommonsMultipartFile file,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/add";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
//		}
//		try {	
//			if (packingOrder.getOrderId() == null
//					|| packingOrder.getOrderId() == 0) {
//				throw new PermissionDeniedDataAccessException(
//						"装箱单必须属于一张订单", null);
//			}
//			Order order = orderService.get(packingOrder.getOrderId());
//			
//			if(order == null){
//				throw new PermissionDeniedDataAccessException(
//						"订单不存在", null);
//			}
//			packingOrder.setCreated_at(DateTool.now());// 设置创建时间
//			packingOrder.setUpdated_at(DateTool.now());// 设置更新时间
//			packingOrder.setCreated_user(user.getId());// 设置创建人
//			
//			//上传图片
//			JSONObject jObject = fileUpload(order.getId(), request, file);;
//			packingOrder.setFilepath((String)jObject.get("excel"));
//			packingOrder.setPdfpath((String)jObject.get("pdf"));
//    		
//			Integer tableOrderId = packingOrderService.add(packingOrder);
//			return this.returnSuccess("id", tableOrderId);
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
//	
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> delete(@PathVariable int id,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/delete";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有删除装箱单的权限", null);
//		}
//		
//		//删除原先的EXCEL和PDF文件
//		PackingOrder packingOrder = packingOrderService.get(id);
//		String filepath = Constants.UPLOADSite + packingOrder.getFilepath();
//		File file = new File(filepath);
//		if(file.exists()){
//		    file.delete();
//		}
//		
//		String pdfpath = Constants.UPLOADSite + packingOrder.getPdfpath();
//		File pdffile = new File(pdfpath);
//		if(pdffile.exists()){
//		    pdffile.delete();
//		}
//		
//		//再删除数据库信息
//		int success = packingOrderService.remove(id);
//		
//		
//		
//		return this.returnSuccess();
//		
//	}
//	
//	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public PackingOrder get(@PathVariable int id, HttpSession session,HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		String lcode = "packing_order/get";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
//		}
//		PackingOrder packingOrder = packingOrderService.get(id);
//		return packingOrder;
//	}
//	
//	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
//			HttpServletRequest request) throws Exception {
//		
//		if (id == null) {
//			throw new Exception("缺少装箱单ID");
//		}		
//		String lcode = "packing_order/detail";
//		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有查看装箱单详情的权限", null);
//		}
//		
//		PackingOrder packingOrder = packingOrderService.get(id);
//		Order order = orderService.get(packingOrder.getOrderId());
//		request.setAttribute("packingOrder", packingOrder);
//		request.setAttribute("order", order);
//		return new ModelAndView("packing_order/detail");
//	}
//	
//	@RequestMapping(value = "/put/{tableOrderId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView update(@PathVariable Integer tableOrderId,
//			HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if (!hasAuthority) {
//			throw new PermissionDeniedDataAccessException("没有添加装箱单的权限", null);
//		}
//		try {
//			if(tableOrderId!=null){
//				PackingOrder packingOrder = packingOrderService.get(tableOrderId);
//				request.setAttribute("packingOrder", packingOrder);
//				return new ModelAndView("packing_order/edit");			
//			}
//			throw new Exception("缺少装箱单ID");
//			
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	@RequestMapping(value = "/put", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> update(PackingOrder packingOrder, String details,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
//		String lcode = "packing_order/edit";
//		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
//		if(!hasAuthority){
//			throw new PermissionDeniedDataAccessException("没有编辑装箱单的权限", null);
//		}
//		if(packingOrder.getOrderId() == null){
//			throw new PermissionDeniedDataAccessException(
//					"订单不存在", null);
//		}
//		packingOrder.setUpdated_at(DateTool.now());
//		//修改filepath 与 pdfpath
//		//上传图片
//		// 转型为MultipartHttpRequest  
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
//        // 获得上传的文件（根据前台的name名称得到上传的文件）  
//        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();  
//        List<MultipartFile> file = multiValueMap.get("file"); 
//        if(file!=null && !file.isEmpty()){  
//        	JSONObject jObject = fileUpload(packingOrder.getOrderId(), request, (CommonsMultipartFile)file.get(0));;
//    		packingOrder.setFilepath((String)jObject.get("excel"));
//    		packingOrder.setPdfpath((String)jObject.get("pdf"));
//        }
//		
//		
//		Integer tableOrderId = packingOrderService.update(packingOrder);
//		return this.returnSuccess("id", tableOrderId);
//		
//	}
//	
//	//预览EXCEL文件转化为PDF
//	@RequestMapping(value = "/preview", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> preview(@RequestParam("file") CommonsMultipartFile file,HttpSession session, HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		try {	
//			
//			//上传文件
//			JSONObject jObject = fileUpload(0, request, file);;
//			String excelpath = Constants.UPLOADSite + (String)jObject.get("excel");
//			String pdfpath = Constants.UPLOADSite + (String)jObject.get("pdf");
//			
//			
//			
//			// 设置response参数，可以打开下载页面
//	        response.reset();
//	        response.setContentType("application/pdf;charset=utf-8");
////	        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
//	        BufferedInputStream bis = null;
//	        BufferedOutputStream bos = null;
//	        try {
//	            bis = new BufferedInputStream(new FileInputStream(new File(pdfpath)));
//	            bos = new BufferedOutputStream(response.getOutputStream());
//	            byte[] buff = new byte[2048];
//	            int bytesRead;
//	            // Simple read/write loop.
//	            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//	                bos.write(buff, 0, bytesRead);
//	            }
//	           
//	        } catch (final IOException e) {
//	            throw e;
//	        } finally {
//	            if (bis != null)
//	                bis.close();
//	            if (bos != null)
//	                bos.close();
//	            //删除文件
//				File excelfile = new File(excelpath);
//				if(excelfile.exists()){
//					excelfile.delete();
//				}
//				File pdffile = new File(pdfpath);
//				if(pdffile.exists()){
//				    pdffile.delete();
//				}
//	        }
//	        
//	      
//	        return this.returnSuccess();
//	        
//		} catch (Exception e) {
//			throw e;
//		}
//		
//	}
//	
//	public JSONObject fileUpload(int orderId , HttpServletRequest request , CommonsMultipartFile file) throws Exception {
//    	String nameString = file.getOriginalFilename();
//    	if(nameString.lastIndexOf(".") == -1 || nameString.lastIndexOf(".") == 0){
//    		throw new Exception("请上传有效的EXCEL文件，包括 以.xls,.xlsx为扩展名的文件");
//    	}
//    	else{
//    		String extString = nameString.substring(nameString.lastIndexOf(".")+1,nameString.length());
//    		extString = extString.toLowerCase();
//    		if(!extString.equals("xls")  && !extString.equals("xlsx")){
//    			throw new Exception("请上传有效的EXCEL文件，包括 以.xls,.xlsx为扩展名的文件");
//    		}
//    	}
//        long  startTime=System.currentTimeMillis();
//        String fileName = "orderId=" + orderId + "_" + DateTool.formatDateYMD(DateTool.now(),"-") +"_" + new Date().getTime();
//        String path = Constants.UPLOADEXCEL_Packing + fileName + ".xls";
//        
//       
//        java.io.File pathFile=new java.io.File(Constants.UPLOADSite + Constants.UPLOADEXCEL_Packing);
//        if(!pathFile.exists()){
//        	pathFile.mkdirs();
//        }
//       
//        java.io.File newFile=new java.io.File(Constants.UPLOADSite + path);
//        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
//        file.transferTo(newFile);
//        
//        long  endTime=System.currentTimeMillis();
//        
//        //上传excel后，上传pdf
//        java.io.File upload_pdf_pathFile=new java.io.File(Constants.UPLOADSite + Constants.UPLOADPDF_Packing);
//        if(!upload_pdf_pathFile.exists()){
//        	upload_pdf_pathFile.mkdirs();
//        }
//        String upload_pdf_full_path = Constants.UPLOADSite + Constants.UPLOADPDF_Packing + fileName + ".pdf";
//        Any2PDFUtil.convert2PDF(Constants.UPLOADSite + path, upload_pdf_full_path);
//      
//		
//		JSONObject jObject = new JSONObject();
//		jObject.put("excel",Constants.UPLOADEXCEL_Packing + fileName +".xls");
//		jObject.put("pdf", Constants.UPLOADPDF_Packing + fileName + ".pdf");
//		
//        return jObject;  
//    }
//	
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	@ResponseBody
//	public void test(HttpServletRequest request,
//			HttpServletResponse response) throws Exception{
//		//读取excel文件
//		try{
//		File file =new File("C:\\Users\\Administrator\\Desktop\\张明霞正唛1.xls");
//		
//		Any2PDFUtil.convert2PDF("C:\\Users\\Administrator\\Desktop\\张明霞正唛1.xls", "C:\\Users\\Administrator\\Desktop\\text2.pdf");
//		}catch(Exception e){
//			throw e;
////			e.printStackTrace();
//		}
//		//		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
//		
////		// 设置response参数，可以打开下载页面
////        response.reset();
////        response.setContentType("application/pdf;charset=utf-8");
////        response.setHeader("Content-Disposition", "attachment;filename="+ new String(("test.pdf").getBytes(), "iso-8859-1"));
////		
////        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
////		byte[] buff = new byte[2048];
////        int bytesRead;
////        // Simple read/write loop.
////        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
////            bos.write(buff, 0, bytesRead);
////        }
////        bis.close();
////        bos.close();
//	}
//    
//}

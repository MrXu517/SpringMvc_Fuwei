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
import com.fuwei.service.ordergrid.ProducingOrderService;
import com.fuwei.service.producesystem.HalfCurrentStockService;
import com.fuwei.util.DateTool;
import com.fuwei.util.NumberUtil;
import com.fuwei.util.SerializeTool;

@RequestMapping("/producebill")
@Controller
/* 辅料入库单 */
public class ProduceBillController extends BaseController {
	@Autowired
	ProduceBillService produceBillService;
	@Autowired
	AuthorityService authorityService;
	@Autowired
	ProducingOrderService producingOrderService;
	@Autowired
	GongxuProducingOrderService gongxuProducingOrderService;
	@Autowired
	HalfCurrentStockService halfCurrentStockService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, Integer year, Integer factoryId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {

		String lcode = "producebill/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看生产对账单列表的权限",
					null);
		}
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
		Sort sort2 = new Sort();
		sort2.setDirection("desc");
		sort2.setProperty("id");
		sortList.add(sort2);

		pager = produceBillService.getList(pager, year, factoryId, sortList);
		request.setAttribute("factoryId", factoryId);
		request.setAttribute("year", year);
		request.setAttribute("pager", pager);
		return new ModelAndView("financial/producebill/index");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少生产对账单ID");
		}
		String lcode = "producebill/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看生产对账单详情的权限",
					null);
		}
		ProduceBill bill = produceBillService.get(id);
		if (bill == null) {
			throw new Exception("找不到ID为" + id + "的生产对账单");
		}
		request.setAttribute("bill", bill);
		return new ModelAndView("financial/producebill/detail");
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addbyorder(Integer factoryId, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (factoryId == null) {
			return new ModelAndView("financial/producebill/add");
		}
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producebill/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑生产对账单的权限",
					null);
		}
		try {
			List<ProducingOrder> producingOrderlist = producingOrderService
					.getByFactoryNoBill(factoryId);
			// 去掉生产单为数量为0的行
			for (ProducingOrder temp : producingOrderlist) {
				Iterator iterator = temp.getDetaillist().iterator();
				while (iterator.hasNext()) {
					ProducingOrderDetail item = (ProducingOrderDetail) iterator
							.next();
					if (item.getQuantity() == 0) {
						iterator.remove();
					}
				}
			}
			List<GongxuProducingOrder> gongxuProducingOrderlist = gongxuProducingOrderService
					.getByFactoryNoBill(factoryId);
			// 去掉工序加工单为数量为0的行
			for (GongxuProducingOrder temp : gongxuProducingOrderlist) {
				Iterator iterator = temp.getDetaillist().iterator();
				while (iterator.hasNext()) {
					GongxuProducingOrderDetail item = (GongxuProducingOrderDetail) iterator
							.next();
					if (item.getQuantity() == 0) {
						iterator.remove();
					}
				}
			}
			// 1.获取某工厂各个订单的实际生产数量
			// Map<订单ID_工序ID_planOrderDetailId，实际入库数量>
			Map<String, Integer> actual_inMap = halfCurrentStockService
					.factory_actual_in(factoryId);

			// 2.
			List<ProduceBillDetail> resultlist = new ArrayList<ProduceBillDetail>();
			for (ProducingOrder temp : producingOrderlist) {
				ProduceBillDetail item = new ProduceBillDetail();
				item.setCharge_employee(temp.getCharge_employee());
				item.setCompany_productNumber(temp.getCompany_productNumber());
				item.setCompanyId(temp.getCompanyId());
				item.setGongxuId(SystemCache.producing_GONGXU.getId());
				item.setName(temp.getName());
				item.setOrderId(temp.getOrderId());
				item.setOrderNumber(temp.getOrderNumber());
				item.setProducingOrder_created_at(temp.getCreated_at());
				item.setProducingOrderId(temp.getId());
				item.setProducingOrderNumber(temp.getNumber());
				item.setSampleId(temp.getSampleId());
				List<ProduceBillDetail_Detail> itemdetaillist = new ArrayList<ProduceBillDetail_Detail>();
				double total_amount = 0;
				String key = temp.getOrderId() + "_"
						+ SystemCache.producing_GONGXU.getId() + "_";
				for (ProducingOrderDetail tempDetail : temp.getDetaillist()) {
					ProduceBillDetail_Detail itemdetail = new ProduceBillDetail_Detail();
					itemdetail.setColor(tempDetail.getColor());
					itemdetail.setPlanOrderDetailId(tempDetail
							.getPlanOrderDetailId());
					itemdetail
							.setProduce_weight(tempDetail.getProduce_weight());
					itemdetail.setSize(tempDetail.getSize());
					itemdetail.setWeight(tempDetail.getWeight());
					itemdetail.setYarn(tempDetail.getYarn());

					double price = tempDetail.getPrice();
					itemdetail.setPrice(price);
					itemdetail.setPlan_quantity(tempDetail.getQuantity());
					int actual_in_quantity = 0;
					if (actual_inMap.containsKey(key
							+ itemdetail.getPlanOrderDetailId())) {
						actual_in_quantity = actual_inMap.get(key
								+ itemdetail.getPlanOrderDetailId());
					}
					// 设置明细-明细的金额
					double amount = price * actual_in_quantity;
					itemdetail.setAmount(amount);
					
					total_amount += amount;
					itemdetail.setQuantity(actual_in_quantity);
					itemdetaillist.add(itemdetail);
				}
				item.setAmount(total_amount);
				item.setDeduct(0);
				item.setPayable_amount(total_amount);
				item.setDetaillist(itemdetaillist);
				resultlist.add(item);
			}

			for (GongxuProducingOrder temp : gongxuProducingOrderlist) {
				ProduceBillDetail item = new ProduceBillDetail();
				item.setCharge_employee(temp.getCharge_employee());
				item.setCompany_productNumber(temp.getCompany_productNumber());
				item.setCompanyId(temp.getCompanyId());
				item.setGongxuId(temp.getGongxuId());
				item.setName(temp.getName());
				item.setOrderId(temp.getOrderId());
				item.setOrderNumber(temp.getOrderNumber());
				item.setProducingOrder_created_at(temp.getCreated_at());
				item.setProducingOrderId(temp.getId());
				item.setProducingOrderNumber(temp.getNumber());
				item.setSampleId(temp.getSampleId());
				List<ProduceBillDetail_Detail> itemdetaillist = new ArrayList<ProduceBillDetail_Detail>();
				double total_amount = 0;
				String key = temp.getOrderId() + "_" + temp.getGongxuId() + "_";
				for (GongxuProducingOrderDetail tempDetail : temp
						.getDetaillist()) {
					ProduceBillDetail_Detail itemdetail = new ProduceBillDetail_Detail();
					itemdetail.setColor(tempDetail.getColor());
					itemdetail.setPlanOrderDetailId(tempDetail
							.getPlanOrderDetailId());
					itemdetail
							.setProduce_weight(tempDetail.getProduce_weight());
					itemdetail.setSize(tempDetail.getSize());
					itemdetail.setWeight(tempDetail.getWeight());
					itemdetail.setYarn(tempDetail.getYarn());

					double price = tempDetail.getPrice();
					itemdetail.setPrice(price);
					itemdetail.setPlan_quantity(tempDetail.getQuantity());
					int actual_in_quantity = 0;
					if (actual_inMap.containsKey(key
							+ itemdetail.getPlanOrderDetailId())) {
						actual_in_quantity = actual_inMap.get(key
								+ itemdetail.getPlanOrderDetailId());
					}
					// 设置明细-明细的金额
					double amount = price * actual_in_quantity;
					//如果是工序加工单，由于单价是以打为单位，则应除以12
					amount = NumberUtil.formateDouble(amount/12, 2);
					
					
					total_amount += amount;
					itemdetail.setQuantity(actual_in_quantity);
					itemdetail.setAmount(amount);
					itemdetaillist.add(itemdetail);
				}
				item.setAmount(total_amount);
				item.setDeduct(0);
				item.setPayable_amount(total_amount);
				item.setDetaillist(itemdetaillist);
				resultlist.add(item);
			}
			request.setAttribute("resultlist", resultlist);
			request.setAttribute("factoryId", factoryId);
			return new ModelAndView("financial/producebill/add");
		} catch (Exception e) {
			throw e;
		}
	}

	// 添加或保存
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addbyorder(ProduceBill produceBill,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producebill/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有创建或编辑生产对账单的权限",
					null);
		}
		try {
			/* 判断有些数据不能为空 */
			Integer factoryId = produceBill.getFactoryId();
			if (factoryId == null) {
				throw new Exception("工厂ID不能为空");
			}
			/* 判断有些数据不能为空 */

			if (produceBill.getId() == 0) {// 添加对账单
				Map<String, Object> data = new HashMap<String, Object>();
				produceBill.setCreated_at(DateTool.now());// 设置创建时间
				produceBill.setCreated_user(user.getId());// 设置创建人
				produceBill.setUpdated_at(DateTool.now());

				List<ProduceBillDetail> detaillist = SerializeTool
						.deserializeList(details, ProduceBillDetail.class);
				if(detaillist.size() <= 0){
					throw new Exception("对账单至少得有一条明细记录");
				}
				int total_quantity = 0;
				double total_amount = 0;
				double total_deduct = 0;
				// 设置明细的金额与数量
				for (ProduceBillDetail detail : detaillist) {
					int detail_quantity = 0;
					double detail_amount = 0;
					for (ProduceBillDetail_Detail detail_detail : detail
							.getDetaillist()) {
						// 设置明细-明细的金额
						double amount = detail_detail.getQuantity() * detail_detail.getPrice();
						//如果是工序加工单，由于单价是以打为单位，则应除以12
						if(detail.getGongxuId() != SystemCache.producing_GONGXU.getId()){
							amount = NumberUtil.formateDouble(amount/12, 2);
						}
						detail_detail.setAmount(amount);
						detail_quantity += detail_detail.getQuantity();
						detail_amount += detail_detail.getAmount();
					}
					// 设置生产单的总数量
					detail.setQuantity(detail_quantity);
					// 设置生产单的总金额（未减去扣款）
					detail.setAmount(detail_amount);
					// 设置生产单的应付金额
					detail
							.setPayable_amount(detail_amount
									- detail.getDeduct());

					total_amount += detail_amount;
					total_deduct += detail.getDeduct();
					total_quantity += detail_quantity;
				}

				produceBill.setQuantity(total_quantity);// 总数量
				produceBill.setAmount(total_amount);// 总金额，未扣款
				produceBill.setDeduct(total_deduct);// 总扣款
				double amount_actual = total_amount - total_deduct;
				double rate_deduct = NumberUtil.formateDouble(amount_actual * SystemSettings.local_tax_rate, 2) ;
				produceBill.setRate_deduct(rate_deduct);// 地税扣款
				produceBill.setPayable_amount(NumberUtil.formateDouble(amount_actual - rate_deduct,2));// 最终应付金额
				produceBill.setDetaillist(detaillist);
				int produceBillId = produceBillService.add(produceBill);
				data.put("id", produceBillId);
				return this.returnSuccess(data);
			} else {
				throw new Exception("id错误，创建时id只能为空");
			}

		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail2(Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		return detail(id, session, request);
	}

	// 删除
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producebill/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除生产对账单的权限", null);
		}
		// 删除
		int success = produceBillService.remove(id);
		return this.returnSuccess();

	}

	//编辑
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView put(@PathVariable Integer id, HttpSession session,
			HttpServletRequest request) throws Exception {
		if (id == null) {
			throw new Exception("缺少生产对账单ID");
		}
		String lcode = "producebill/edit";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑生产对账单详情的权限",
					null);
		}
		ProduceBill bill = produceBillService.get(id);
		if (bill == null) {
			throw new Exception("找不到ID为" + id + "的生产对账单");
		}
		request.setAttribute("bill", bill);
		return new ModelAndView("financial/producebill/edit");
	}
	
	// 添加或保存
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> put(ProduceBill produceBill,
			String details, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producebill/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑生产对账单的权限",
					null);
		}
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			produceBill.setUpdated_at(DateTool.now());

			List<ProduceBillDetail> detaillist = SerializeTool
					.deserializeList(details, ProduceBillDetail.class);
			if(detaillist.size() <= 0){
				throw new Exception("对账单至少得有一条明细记录");
			}
			int total_quantity = 0;
			double total_amount = 0;
			double total_deduct = 0;
			// 设置明细的金额与数量
			for (ProduceBillDetail detail : detaillist) {
				int detail_quantity = 0;
				double detail_amount = 0;
				for (ProduceBillDetail_Detail detail_detail : detail.getDetaillist()) {
					// 设置明细-明细的金额
					double amount = detail_detail.getQuantity() * detail_detail.getPrice();
					//如果是工序加工单，由于单价是以打为单位，则应除以12
					if(detail.getGongxuId() != SystemCache.producing_GONGXU.getId()){
						amount = NumberUtil.formateDouble(amount/12, 2);
					}
					detail_detail.setAmount(amount);
					detail_quantity += detail_detail.getQuantity();
					detail_amount += detail_detail.getAmount();
				}
				// 设置生产单的总数量
				detail.setQuantity(detail_quantity);
				// 设置生产单的总金额（未减去扣款）
				detail.setAmount(detail_amount);
				// 设置生产单的应付金额
				detail.setPayable_amount(detail_amount- detail.getDeduct());
				total_amount += detail_amount;
				total_deduct += detail.getDeduct();
				total_quantity += detail_quantity;
			}
			produceBill.setQuantity(total_quantity);// 总数量
			produceBill.setAmount(total_amount);// 总金额，未扣款
			produceBill.setDeduct(total_deduct);// 总扣款
			double amount_actual = total_amount - total_deduct;
			double rate_deduct = NumberUtil.formateDouble(amount_actual * SystemSettings.local_tax_rate, 2) ;
			produceBill.setRate_deduct(rate_deduct);// 地税扣款
			produceBill.setPayable_amount(NumberUtil.formateDouble(amount_actual - rate_deduct,2));// 最终应付金额
			produceBill.setDetaillist(detaillist);
			int produceBillId = produceBill.getId();
			produceBillService.update(produceBill);
			data.put("id", produceBillId);
			return this.returnSuccess(data);
		} catch (Exception e) {
			throw e;
		}

	}
	
	// 导出
	@RequestMapping(value = "/export/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> export(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "producebill/export";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有导出生产对账单的权限", null);
		}
		ProduceBill bill = produceBillService.get(id);

		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createExportFile(os, bill);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName = SystemCache.getFactoryName(bill.getFactoryId()) + bill.getYear() + "年生产对账单";

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

	public void createExportFile(OutputStream os, ProduceBill bill) throws Exception {

		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件

		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称
		wsheet.setPageSetup(PageOrientation.LANDSCAPE);// 设置打印横向
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
		String line0_text = "桐庐富伟针织厂生产对账单";
		Label excelCompany = new Label(0, 0, line0_text, companyFormat);
		wsheet.addCell(excelCompany);
		wsheet.mergeCells(0, 0, 16, 0);
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

		// 添加 加工方，年份
		WritableCellFormat headFormat = new WritableCellFormat(
				new WritableFont(WritableFont.createFont("宋体"), 12,
						WritableFont.BOLD, false,
						jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK));
		Label excelFactory1 = new Label(0, 1, "加工方：", headFormat);
		Label excelFactory2 = new Label(2, 1, SystemCache.getFactoryName(bill.getFactoryId()), headFormat);
		wsheet.addCell(excelFactory1);
		wsheet.addCell(excelFactory2);
		wsheet.mergeCells(0, 1, 1, 1);

		Label excelYear1 = new Label(4, 1, "账单年份：", headFormat);
		Label excelYear2 = new Label(6, 1, bill.getYear()+"", headFormat);
		wsheet.addCell(excelYear1);
		wsheet.addCell(excelYear2);
		wsheet.mergeCells(4, 1, 5, 1);

		WritableFont wfont2 = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.NO_BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat2 = new WritableCellFormat(wfont2);
		titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat2.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框

		String[] title = { "序号", "加工单号", "工序", "开单日期", "订单号", "公司", "跟单人", "货号", "款名",
				"加工明细","","","","","", "扣款", "备注" };
		String[] title2 = { "", "", "", "", "", "", "", "", "", "颜色", "尺寸", "计划数量", "结账数量",
				"单价", "金额", "", ""};
		// 设置Excel表头
		int columnBestWidth[] = new int[title.length]; // 保存最佳列宽数据的数组
		for (int i = 0; i < title.length; i++) {
			columnBestWidth[i] = title[i].getBytes().length;
			Label excelTitle = new Label(i, 2, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		for (int i = 0; i < title2.length; i++) {
			int tempLength = title2[i].getBytes().length;
			if (tempLength > columnBestWidth[i]) {
				columnBestWidth[i] = tempLength;
			}
			Label excelTitle = new Label(i, 3, title2[i], titleFormat);
			wsheet.addCell(excelTitle);
		}

		wsheet.setRowView(1, 400);
		wsheet.mergeCells(0, 2, 0, 3);
		wsheet.mergeCells(1, 2, 1, 3);
		wsheet.mergeCells(2, 2, 2, 3);
		wsheet.mergeCells(3, 2, 3, 3);
		wsheet.mergeCells(4, 2, 4, 3);
		wsheet.mergeCells(5, 2, 5, 3);
		wsheet.mergeCells(6, 2, 6, 3);
		wsheet.mergeCells(7, 2, 7, 3);
		wsheet.mergeCells(8, 2, 8, 3);
		wsheet.mergeCells(15, 2, 15, 3);
		wsheet.mergeCells(16, 2, 16, 3);
		wsheet.mergeCells(9, 2, 14, 2);

		int c = 4; // 用于循环时Excel的行号

		int count = 1;
		List<ProduceBillDetail> detaillist = bill.getDetaillist();
		for (ProduceBillDetail detail : detaillist) {
			wsheet.setRowView(c, 400);
			String price_fx = "";//单价后缀
			if(detail.getGongxuId() != SystemCache.producing_GONGXU.getId()){//若是工序加工单，则设置单价
				price_fx = "/打";
			}
			Label content1 = new Label(0, c, count + "", titleFormat2);
			++count;
			Label content2 = new Label(1, c, detail.getProducingOrderNumber(), titleFormat2);
			Label content3 = new Label(2, c, SystemCache
					.getGongxuName(detail.getGongxuId()), titleFormat2);
			Label content4 = new Label(3, c,
					DateTool.formatDateYMD(detail.getProducingOrder_created_at()) , titleFormat2);
			Label content5 = new Label(4, c,detail.getOrderNumber(),
					titleFormat2);
			Label content6 = new Label(5, c,SystemCache.getCompanyShortName(detail.getCompanyId()),
					titleFormat2);
			Label content7 = new Label(6, c,SystemCache.getEmployeeName(detail.getCharge_employee()),
					titleFormat2);
			Label content8 = new Label(7, c, detail.getCompany_productNumber(),
					titleFormat2);
			Label content9 = new Label(8, c,
					detail.getName(), titleFormat2);
			List<ProduceBillDetail_Detail> detail_detaillist = detail.getDetaillist();
			Label content10 = new Label(9, c, detail_detaillist.get(0).getColor(), titleFormat2);
			Label content11 = new Label(10, c, detail_detaillist.get(0).getSize(), titleFormat2);
			Label content12 = new Label(11, c, detail_detaillist.get(0).getPlan_quantity()+"", titleFormat2);
			Label content13 = new Label(12, c, detail_detaillist.get(0).getQuantity()+"", titleFormat2);
			
			Label content14 = new Label(13, c, detail_detaillist.get(0).getPrice()+price_fx, titleFormat2);
			Label content15 = new Label(14, c, detail_detaillist.get(0).getAmount()+"", titleFormat2);
			Label content16 = new Label(15, c,detail.getDeduct()+"", titleFormat2);
			Label content17 = new Label(16, c, detail.getMemo(), titleFormat2);
			

			wsheet.addCell(content1);
			wsheet.addCell(content2);
			wsheet.addCell(content3);
			wsheet.addCell(content4);
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
			wsheet.addCell(content16);
			wsheet.addCell(content17);
			
			c++;
			
			detail_detaillist.remove(0);
			for(ProduceBillDetail_Detail detail_detail : detail_detaillist){
				Label tempcontent10 = new Label(9, c, detail_detail.getColor(), titleFormat2);
				Label tempcontent11 = new Label(10, c, detail_detail.getSize(), titleFormat2);
				Label tempcontent12 = new Label(11, c, detail_detail.getPlan_quantity()+"", titleFormat2);
				Label tempcontent13 = new Label(12, c, detail_detail.getQuantity()+"", titleFormat2);
				Label tempcontent14 = new Label(13, c, detail_detail.getPrice()+price_fx, titleFormat2);
				Label tempcontent15 = new Label(14, c, detail_detail.getAmount()+"", titleFormat2);
				wsheet.addCell(new Label(0, c, "", titleFormat2));
				wsheet.addCell(new Label(1, c, "", titleFormat2));
				wsheet.addCell(new Label(2, c, "", titleFormat2));
				wsheet.addCell(new Label(3, c, "", titleFormat2));
				wsheet.addCell(new Label(4, c, "", titleFormat2));
				wsheet.addCell(new Label(5, c, "", titleFormat2));
				wsheet.addCell(new Label(6, c, "", titleFormat2));
				wsheet.addCell(new Label(7, c, "", titleFormat2));
				wsheet.addCell(new Label(8, c, "", titleFormat2));
				wsheet.addCell(tempcontent10);
				wsheet.addCell(tempcontent11);
				wsheet.addCell(tempcontent12);
				wsheet.addCell(tempcontent13);
				wsheet.addCell(tempcontent14);
				wsheet.addCell(tempcontent15);
				wsheet.addCell(new Label(15, c, "", titleFormat2));
				wsheet.addCell(new Label(16, c, "", titleFormat2));
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
			int width10 = content10.getContents().getBytes().length;
			int width11 = content11.getContents().getBytes().length;
			int width12 = content12.getContents().getBytes().length;
			int width13 = content13.getContents().getBytes().length;
			int width14 = content14.getContents().getBytes().length;
			int width15 = content15.getContents().getBytes().length;
			int width16 = content16.getContents().getBytes().length;
			int width17 = content17.getContents().getBytes().length;
			if(width17<=8){
				width17=8;//备注列宽不得少于8
			}
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
			if (columnBestWidth[9] < width10) {
				columnBestWidth[9] = width10;
			}
			if (columnBestWidth[10] < width11) {
				columnBestWidth[10] = width11;
			}
			if (columnBestWidth[11] < width12) {
				columnBestWidth[11] = width12;
			}
			if (columnBestWidth[12] < width13) {
				columnBestWidth[12] = width13;
			}
			if (columnBestWidth[13] < width14) {
				columnBestWidth[13] = width14;
			}
			if (columnBestWidth[14] < width15) {
				columnBestWidth[14] = width15;
			}
			if (columnBestWidth[15] < width16) {
				columnBestWidth[15] = width16;
			}
			if (columnBestWidth[16] < width17) {
				columnBestWidth[16] = width17;
			}
		}
		//加合计行
		Label totalcontent13 = new Label(12, c, bill.getQuantity()+"", headFormat);
		Label totalcontent15 = new Label(14, c, bill.getAmount()+"", headFormat);
		Label totalcontent16 = new Label(15, c, bill.getDeduct()+"", headFormat);
		int total_width13 = totalcontent13.getContents().getBytes().length;
		int total_width15 = totalcontent15.getContents().getBytes().length + 2;
		int total_width16 = totalcontent16.getContents().getBytes().length+1;
		wsheet.addCell(new Label(0, c, "合计", headFormat));
		wsheet.addCell(totalcontent13);
		wsheet.addCell(totalcontent15);
		wsheet.addCell(totalcontent16);
		wsheet.mergeCells(0, c, 1, c);

		if (columnBestWidth[12] < total_width13) {
			columnBestWidth[12] = total_width13;
		}
		if (columnBestWidth[14] < total_width15) {
			columnBestWidth[14] = total_width15;
		}
		if (columnBestWidth[15] < total_width16) {
			columnBestWidth[15] = total_width16;
		}
		
		c = c+1;
		double amounttemp = bill.getAmount() - bill.getDeduct();
		wsheet.addCell(new Label(0, c, "总金额（减去扣款）： "+ amounttemp +"元", headFormat));
		c = c+1;
		wsheet.addCell(new Label(0, c, "地税扣款： "+ bill.getRate_deduct() +"元", headFormat));

		c = c+1;
		wsheet.addCell(new Label(0, c, "合计应付金额： "+ bill.getPayable_amount() +"元", headFormat));
		
		
		for (int p = 0; p < columnBestWidth.length; ++p) {
			wsheet.setColumnView(p, columnBestWidth[p]);
		}
		
		
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}
}

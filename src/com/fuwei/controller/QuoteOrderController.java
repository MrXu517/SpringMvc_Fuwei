package com.fuwei.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.fuwei.entity.Quote;
import com.fuwei.entity.QuoteOrder;
import com.fuwei.entity.QuoteOrderDetail;
import com.fuwei.entity.QuotePrice;
import com.fuwei.entity.Sample;
import com.fuwei.entity.User;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.QuoteOrderDetailService;
import com.fuwei.service.QuoteOrderService;
import com.fuwei.service.QuoteService;
import com.fuwei.util.DateTool;
import com.fuwei.util.ExportExcel;
import com.fuwei.util.SerializeTool;

@RequestMapping("/quoteorder")
@Controller
public class QuoteOrderController extends BaseController {

	@Autowired
	QuoteService quoteService;
	@Autowired
	QuoteOrderService quoteOrderService;
	@Autowired
	QuoteOrderDetailService quoteOrderDetailService;
	@Autowired
	AuthorityService authorityService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(String ids, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "quoteorder/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有添加报价单的权限", null);
		}
		
		String excelfile_name = "报价单"+DateTool.formateDate(new Date(), "yyyyMMddHHmmss") + ".xls";
		String uploadSite = Constants.UPLOADSite;
		Boolean excel = false;
		QuoteOrder quoteOrder = new QuoteOrder();
		try {
			// 自动生成报价单号
			quoteOrder.setCreated_at(DateTool.now());
			quoteOrder.setUpdated_at(DateTool.now());
			quoteOrder.setCreated_user(user.getId());

			List<QuoteOrderDetail> detaillist = new ArrayList<QuoteOrderDetail>();
			List<Quote> quotelist = quoteService.batch_getList(ids);
			if (quotelist == null || quotelist.size() <= 0) {
				throw new Exception("报价单中至少得有一条报价记录");
			}
			int salesmanId = quotelist.get(0).getQuotePrice().getSalesmanId();
			for (Quote quote : quotelist) {
				// 判断业务员是否一致
				if (salesmanId != quote.getQuotePrice().getSalesmanId()) {
					throw new Exception("业务员不一致");
				}
				Sample sample = quote.getSample();
				QuoteOrderDetail detail = new QuoteOrderDetail();
				detail.setCproductN(quote.getQuotePrice().getCproductN());
				detail.setPrice(quote.getQuotePrice().getPrice());
				detail.setMemo(quote.getQuotePrice().getMemo());
				detail.setSampleId(quote.getSampleId());
				detail.setName(sample.getName());
				detail.setImg(sample.getImg());
				detail.setImg_s(sample.getImg_s());
				detail.setImg_ss(sample.getImg_ss());
				detail.setMaterial(sample.getMaterial());
				detail.setMachine(sample.getMachine());
				detail.setWeight(sample.getWeight());
				detail.setSize(sample.getSize());
				detail.setCost(sample.getCost());
				detail.setProductNumber(sample.getProductNumber());
				detail.setMachine(sample.getMachine());
				detail.setCharge_user(sample.getCharge_user());
				detail.setDetail(sample.getDetail());
				detaillist.add(detail);
			}
			quoteOrder.setSalesmanId(salesmanId);
			quoteOrder.setDetaillist(detaillist);
			// 生成excel的文件名
			quoteOrder.setExcelUrl(excelfile_name);//先设一次excel文件名，之后再设一次（把quoteOrderId加上去）
			excel = true;
			int quoteOrderId = quoteOrderService.add(quoteOrder, ids,uploadSite);
			excelfile_name = quoteOrder.getExcelUrl();
			return this.returnSuccess("id",quoteOrderId);
		} catch (Exception e) {
			if(excel){
				ExportExcel.deleteExcel(excelfile_name, uploadSite);
			}
			throw e;
		}

	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(Integer page, String start_time, String end_time,Integer companyId,Integer salesmanId,
			String sortJSON, HttpSession session, HttpServletRequest request)
			throws Exception {
		String lcode = "quoteorder/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看报价单列表的权限", null);
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
		if(sortList == null){
			sortList = new ArrayList<Sort>();
		}
		Sort sort = new Sort();
		sort.setDirection("desc");
		sort.setProperty("created_at");
		sortList.add(sort);
		pager = quoteOrderService.getList(pager, start_time_d, end_time_d,companyId,salesmanId,
				sortList);
		if (pager != null & pager.getResult() != null) {
			List<QuoteOrder> quoteorderlist = (List<QuoteOrder>) pager
					.getResult();
			for (QuoteOrder quoteorder : quoteorderlist) {
				quoteorder.setCompanyId(SystemCache.getSalesman(
						quoteorder.getSalesmanId()).getCompanyId());
			}
		}
		request.setAttribute("start_time", start_time_d);
		request.setAttribute("end_time", end_time_d);
		request.setAttribute("salesmanId", salesmanId);
		if(companyId==null & salesmanId!=null){
			companyId = SystemCache.getSalesman(salesmanId).getCompanyId();
		}
		request.setAttribute("companyId", companyId);
		request.setAttribute("pager", pager);
		return new ModelAndView("quoteorder/index");
	}
	
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView detail(@PathVariable Integer id,HttpSession session, HttpServletRequest request) throws Exception{
		String lcode = "quoteorder/detail";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if(!hasAuthority){
			throw new PermissionDeniedDataAccessException("没有查看报价单详情的权限", null);
		}
		
		if(id == null){
			throw new Exception("缺少报价单ID");
		}
		QuoteOrder quoteOrder = quoteOrderService.get(id);
		quoteOrder.setCompanyId(SystemCache.getSalesman(quoteOrder.getSalesmanId()).getCompanyId());
		List<QuoteOrderDetail> detaillist = quoteOrderDetailService.getListByQuoteOrder(id);
		quoteOrder.setDetaillist(detaillist);
		request.setAttribute("quoteorder", quoteOrder);
		return new ModelAndView("quoteorder/detail");
	}
	
}

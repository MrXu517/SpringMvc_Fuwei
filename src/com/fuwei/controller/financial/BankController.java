package com.fuwei.controller.financial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fuwei.commons.SystemCache;
import com.fuwei.commons.SystemContextUtils;
import com.fuwei.controller.BaseController;
import com.fuwei.entity.User;
import com.fuwei.entity.financial.Bank;
import com.fuwei.service.AuthorityService;
import com.fuwei.service.financial.BankService;
import com.fuwei.util.DateTool;

@RequestMapping("/bank")
@Controller
public class BankController extends BaseController {

	@Autowired
	BankService bankService;
	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView Index(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "bank/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有账户管理的权限", null);
		}
		List<Bank> banklist = bankService.getList();
		request.setAttribute("banklist", banklist);
		return new ModelAndView("financial/bank/list");

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(Bank bank, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "bank/add";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有添加账户的权限", null);
		}
		bank.setName(chinese2English(bank.getName()));
		bank.setCreated_at(DateTool.now());
		bank.setUpdated_at(DateTool.now());
		bank.setCreated_user(user.getId());
		int success = bankService.add(bank);

		return this.returnSuccess();

	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable int id,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "bank/delete";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有删除账户的权限", null);
		}
		int success = bankService.remove(id);
		return this.returnSuccess();

	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Bank get(@PathVariable int id, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String lcode = "bank/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看账户列表的权限", null);
		}
		Bank bank = bankService.get(id);
		return bank;
	}

	@RequestMapping(value = "/list_json", method = RequestMethod.GET)
	@ResponseBody
	public List<Bank> get(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String lcode = "bank/index";
		Boolean hasAuthority = SystemCache.hasAuthority(session, lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有查看账户列表的权限", null);
		}
		List<Bank> banklist = bankService.getList();
		return banklist;
	}

	@RequestMapping(value = "/put", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(Bank bank, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "bank/edit";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有编辑账户的权限", null);
		}
		bank.setName(chinese2English(bank.getName()));
		bank.setUpdated_at(DateTool.now());
		int success = bankService.update(bank);

		return this.returnSuccess();

	}

	// 下载导入模板
	@RequestMapping(value = "/import_download", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> import_download(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 导出
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		createModuleFile(os);
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		String fileName = "银行账户批量导入模板";

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

	// 批量导入
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView batch_add(HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "bank/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入账户的权限", null);
		}
		return new ModelAndView("financial/bank/import");
	}

	// 批量导入银行账户
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> batch_add(
			@RequestParam("file") CommonsMultipartFile file,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = SystemContextUtils.getCurrentUser(session).getLoginedUser();
		String lcode = "bank/import";
		Boolean hasAuthority = authorityService.checkLcode(user.getId(), lcode);
		if (!hasAuthority) {
			throw new PermissionDeniedDataAccessException("没有批量导入账户的权限", null);
		}
		List<Bank> banklist = readBankFile(file);
		for (Bank bank : banklist) {
			bank.setCreated_at(DateTool.now());
			bank.setUpdated_at(DateTool.now());
			bank.setCreated_user(user.getId());
		}
		if (banklist == null || banklist.size() <= 0) {
			throw new Exception("请至少上传一条记录");
		}
		bankService.batch_add(banklist);
		return this.returnSuccess();
	}

	public void createModuleFile(OutputStream os) throws Exception {
		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet wsheet = wbook.createSheet("Sheet1", 0); // 工作表名称
		// 设置Excel字体
		WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.BOLD, false,
				jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
		titleFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK); // BorderLineStyle边框
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		String[] title = { "收款人名称", "开户行", "帐号", "个人0或企业1"};
		// 设置Excel表头
		for (int i = 0; i < title.length; i++) {			
			Label excelTitle = new Label(i, 0, title[i], titleFormat);
			wsheet.addCell(excelTitle);
		}
		wsheet.setRowView(1, 400);
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}

	public List<Bank> readBankFile(CommonsMultipartFile file) throws Exception {
		String nameString = file.getOriginalFilename();
		if (nameString.lastIndexOf(".") == -1
				|| nameString.lastIndexOf(".") == 0) {
			throw new Exception("请上传有效的97-2003版Excel文件，包括 以.xls为扩展名的文件");
		} else {
			String extString = nameString.substring(
					nameString.lastIndexOf(".") + 1, nameString.length());
			extString = extString.toLowerCase();
			if (!extString.equals("xls") && !extString.equals("xlsx")) {
				throw new Exception("请上传有效的97-2003版Excel文件，包括 以.xls为扩展名的文件");
			}
		}

		InputStream is = file.getInputStream();
		Workbook rb = Workbook.getWorkbook(is); // 从文件流中获取Excel工作区对象（WorkBook）
		Sheet[] sheet = rb.getSheets();

		List<Bank> bankList = new ArrayList<Bank>();

		for (int i = 0; i < sheet.length; i++) {
			Sheet rs = rb.getSheet(i);
			int rows = rs.getRows();
			for (int j = 1; j < rows; j++) {// 从第二行开始
				Cell[] cells = rs.getRow(j);
				// 每行增加一条记录
				Bank bank = new Bank();
				String name = "";
				if (cells[0].getType() == CellType.EMPTY) {
					continue;// 收款方名称为空的直接跳过
				} else {
					name = chinese2English(cells[0].getContents().trim());
					if (name.equals("")) {
						continue;// 收款方名称为空的直接跳过
					}
				}
				bank.setName(name);
				String bank_name = "";
				if (cells[1].getType() != CellType.EMPTY) {
					bank_name = chinese2English(cells[1].getContents().trim());
				}
				bank.setBank_name(bank_name);
				String bank_no = "";
				if (cells[2].getType() != CellType.EMPTY) {
					bank_no = cells[2].getContents().trim();
				}
				bank.setBank_no(bank_no);
				boolean is_enterprise = true;
				if (cells[3].getType() != CellType.EMPTY) {
					String temp = cells[3].getContents().trim();
					if(temp.equals("1") || temp.equals("true")){
						is_enterprise = true;
					}else{
						is_enterprise = false;
					}
				
				}
				bank.setIs_enterprise(is_enterprise);

				bankList.add(bank);
			}
		}
		is.close();
		return bankList;

	}
	
	public String chinese2English(String str){
		String[] regs = { "！", "，", "。 ","；","（","）", "!", ",", ".", ";" ,"(",")"};
		for ( int i = 0; i < regs.length / 2; i++ )
		{
		    str = str.replaceAll (regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
}

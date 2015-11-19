package com.fuwei.print;

import java.io.File;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class PrintExcel {
	public static void printExcel(String path,Boolean delete) {
		new Thread(new printThread(path,delete)).start();
	}
}
class printThread implements Runnable{
	private String filePath;
	private Boolean delete = false;
	public printThread(String filePath,Boolean delete){
		this.filePath=filePath;
		this.delete = delete;
	}
	public void run() {
//		PrintExcel.printExcel(filePath);
		ComThread.InitSTA();
		ActiveXComponent xl = new ActiveXComponent("Excel.Application");
		try {
			// System.out.println("version=" + xl.getProperty("Version"));
			// 不打开文档
			Dispatch.put(xl, "Visible", new Variant(false));
			Dispatch workbooks = xl.getProperty("Workbooks").toDispatch();
			// 打开文档
			Dispatch excel = Dispatch.call(workbooks, "Open", filePath)
					.toDispatch();
			// 开始打印
			Dispatch.get(excel, "PrintOut");
		} catch (Exception e) {
			
		} finally {
			// 始终释放资源
			ComThread.Release();
		}
		
		File file=new File(filePath);
		if(this.delete && file.exists()){
			file.delete();
		}
	}
	
}
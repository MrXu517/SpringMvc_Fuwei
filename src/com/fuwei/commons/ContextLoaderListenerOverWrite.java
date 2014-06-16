package com.fuwei.commons;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/**
 * @author zxd
 * 
 */
public class ContextLoaderListenerOverWrite extends ContextLoaderListener {

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);

		/*
		 * ApplicationContext applicationContext =
		 * WebApplicationContextUtils.getWebApplicationContext
		 * (event.getServletContext()); dicItemService=(DicItemService)
		 * applicationContext.getBean("dicItemService"); orgService=(OrgService)
		 * applicationContext.getBean("orgService");
		 * 
		 * GlobalDefine.CacheBasisData.orgForMap=orgService.getOrgIdForLevel(2);
		 * 
		 * //字典表 Map<String,Map<String,String>> dicItem=new
		 * HashMap<String,Map<String,String>>(); List<String>
		 * dicTypeList=dicItemService.getDicTypeList(); for(String
		 * typeId:dicTypeList){ Map<String,String>
		 * map=dicItemService.getDicItemByDicType(typeId); dicItem.put(typeId,
		 * map); } GlobalDefine.CacheBasisData.dicItem=dicItem;
		 */
	}
}

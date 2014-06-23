package com.fuwei.commons;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.keepsoft.commons.ListAdapter;

public class Pager {
	private Integer pageNo = 1;// 
	private Integer pageSize = 15;// 
	private Integer totalPage = 0;//
	private Integer totalCount = 0;//
	private List<?> result;//
	private String[] flow;//
	
	private String risk;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}


	@XmlJavaTypeAdapter(ListAdapter.class)
	public List<?> getResult() {
		return result;
	}

	public void setResult(@XmlJavaTypeAdapter(ListAdapter.class) List<?> result) {
		this.result = result;
	}

	public String[] getFlow() {
		return flow;
	}

	public void setFlow(String[] flow) {
		this.flow = flow;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}
	
}


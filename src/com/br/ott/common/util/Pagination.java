package com.br.ott.common.util;

/* 
 *  
 *  文件名：Pagination.java
 *  版权：BoRuiCube. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方
 *  创建人：pananz
 *  创建时间：2013-1-22 - 上午11:51:25
 */
public class Pagination {
	// 每页显示记录数
	private int showCount;
	// 总页数
	private int totalPage;
	// 总记录数
	private int totalResult;
	// 当前页
	private int currentPage;
	// 当前记录起始索引
	private int currentResult;
	/**
	 * true:需要分页的地方，传入的参数就是Page实体； false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	 */
	private boolean entityOrField;
	// 最终页面显示的底部翻页导航，详细见：getPageStr();
	private String pageStr;

	// 每页行数选择
	public String[] pageRowSelect = { "10", "15", "20", "50", "100" };
	// 是否显示行数选择
	private boolean isShowRowSelect = true;

	public String[] getPageRowSelect() {
		return pageRowSelect;
	}

	public boolean isShowRowSelect() {
		return isShowRowSelect;
	}

	public void setShowRowSelect(boolean isShowRowSelect) {
		this.isShowRowSelect = isShowRowSelect;
	}

	public int getTotalPage() {
		if (showCount == 0) {
			showCount = 15;
		}
		if (totalResult % showCount == 0)
			totalPage = totalResult / showCount;
		else
			totalPage = totalResult / showCount + 1;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	public int getCurrentPage() {
		if (currentPage <= 0)
			currentPage = 1;
		if (currentPage > getTotalPage())
			currentPage = getTotalPage();
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageStr() {
		StringBuffer sb = new StringBuffer();
		if (totalResult > 0) {
			sb.append("<div class='pagination'>");
			if (currentPage == 1) {
				sb.append(" <span class='start-page' ><span>上一页</span></span>");
			} else {
				sb.append("<a href='javascript:nextPage(" + (currentPage - 1)
						+ ");' class='prev-page'><span>上一页</span></a>");
			}
			int showTag = 5; // 分页标签显示数量
			int startTag = 1;
			if (currentPage > showTag) {
				startTag = currentPage - 1;
			}
			int endTag = startTag + showTag - 1;
			for (int i = startTag; i <= totalPage && i <= endTag; i++) {
				if (currentPage == i)
					sb.append("<span class='current-page'>" + i + "</span>");
				else
					sb.append("<a href='javascript:nextPage(" + i + ");'>" + i
							+ "</a>");
			}
			if (currentPage == totalPage) {
				sb.append(" <span class='end-page'><span>下一页</span></span>");
			} else {
				sb.append("<a href='javascript:nextPage(" + (currentPage + 1)
						+ ");' class='next-page'><span>下一页</span></a>");
			}
			sb.append("<span class='select-page'>第" + currentPage + "页&nbsp;共"
					+ totalPage + "页</span>\n");
			sb.append("<script type=\"text/javascript\">\n");
			sb.append("function nextPage(page){");
			sb.append(" if(true && document.forms[0]){\n");
			sb.append("     var url = document.forms[0].getAttribute(\"action\");\n");
			sb.append("     if(url.indexOf('?')>-1){url += \"&"
					+ (entityOrField ? "currentPage" : "pagination.currentPage")
					+ "=\";}\n");
			sb.append("     else{url += \"?"
					+ (entityOrField ? "currentPage" : "pagination.currentPage")
					+ "=\";}\n");
			sb.append("     document.forms[0].action = url+page;\n");
			sb.append("     document.forms[0].submit();\n");
			sb.append(" }else{\n");
			sb.append("     var url = document.location+';\n");
			sb.append("     if(url.indexOf('?')>-1){\n");
			sb.append("         if(url.indexOf('currentPage')>-1){\n");
			sb.append("             var reg = /currentPage=\\d*/g;\n");
			sb.append("             url = url.replace(reg,'currentPage=');\n");
			sb.append("         }else{\n");
			sb.append("             url += \"&"
					+ (entityOrField ? "currentPage" : "pagination.currentPage")
					+ "=\";\n");
			sb.append("         }\n");
			sb.append("     }else{url += \"?"
					+ (entityOrField ? "currentPage" : "pagination.currentPage")
					+ "=\";}\n");
			sb.append("     document.location = url + page;\n");
			sb.append(" }\n");
			sb.append("}\n");
			sb.append("</script>\n");
		}
		pageStr = sb.toString();
		return pageStr;
	}

	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}

	public int getCurrentResult() {
		currentResult = (getCurrentPage() - 1) * getShowCount();
		if (currentResult < 0)
			currentResult = 0;
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
}

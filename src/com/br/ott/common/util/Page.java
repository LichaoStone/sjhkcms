package com.br.ott.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 该类为公共分页类,用于查询数据时用
 * @author xiaoyaoyao
 * @version [版本号, 2011-06-08]
 */
public class Page<T>
{
	//首页
	private final int firstPage = Constants.FIRST_PAGE;
	
	//每页的数据量
	private int pageData = Constants.PAGE_DATA;
	
	//当前页
	private int curPage = Constants.FIRST_PAGE;
	
	//下一页
	private int nextPage;
	
	//上一页
	private int prePage;
	
	//最后一页
	private int endPage;
	
	//总页数
	private int totalPage;
	
	//总数据量
	private int totalData;
	
	//保存的数据
	@SuppressWarnings("unchecked")
    private List<T> dataList = Collections.EMPTY_LIST;
	
	// 页码
	private List<Integer> pageList = new ArrayList<Integer>();
	
	/**
	 * 含两参数的构造方法
	 * @param totalData 总共的数据量
	 * @param dataList 要显示的数据
	 */
	public Page(HttpServletRequest request, int totalData)
	{
		this.totalData = totalData;
		pageValue(request);
	}
	
	/**
	 * 含两参数的构造方法
	 * @param totalData 总共的数据量
	 * @param dataList 要显示的数据
	 */
	public Page(HttpServletRequest request, int totalData, int pageData)
	{
		this.totalData = totalData;
		this.pageData = pageData;
		pageValue(request);
	}
	
	public int getFirstPage()
	{
		return firstPage;
	}

	public int getCurPage()
	{
		return curPage;
	}

	public int getNextPage()
	{
		return nextPage;
	}

	public int getPrePage()
	{
		return prePage;
	}

	public int getEndPage()
	{
		return endPage;
	}

	public int getTotalPage()
	{
		return totalPage;
	}

	public int getPageData()
	{
		return pageData;
	}

	public int getTotalData()
	{
		return totalData;
	}

	public List<?> getDataList()
	{
		return dataList;
	}
	
	public List<Integer> getPageList()
	{
		return pageList;
	}
	
	private void pageValue(HttpServletRequest request)
	{
		//得到要查询数据页的页码,以字符串形式接收
		String strCurPage = request.getParameter(Constants.CUR_PAGE_NAME);
		//得到每一页要显示多少条数据,以字符串形式接收
		String strDataPage = request.getParameter(Constants.PAGE_DATA_NAME);
		if (null == strCurPage)
		{
			Object objCurPage = request.getAttribute(Constants.CUR_PAGE_NAME);
			strCurPage = objCurPage == null ? null : objCurPage.toString();
		}
		if (null == strDataPage)
		{
			Object objCurPage = request.getAttribute(Constants.PAGE_DATA_NAME);
			strDataPage = objCurPage == null ? null : objCurPage.toString();
		}
		
		//判断传过来的页码是不是Int型的
		if (strCurPage != null && strCurPage.matches(Constants.NUMBER_REGEX))
		{
			this.curPage = Integer.valueOf(strCurPage);
		}
		//判断传过来的页码是不是Int型的
		if (strDataPage != null && strDataPage.matches(Constants.NUMBER_REGEX))
		{
			this.pageData = Integer.valueOf(strDataPage);
		}
		pageList.add(Constants.FIRST_PAGE);
		if (0 != totalData)
		{
			//得到总页数以及最后一页
			this.endPage = this.totalPage = totalData % pageData == 0 ? totalData / pageData : totalData / pageData + 1;
			for (int i = 2;i <= endPage;i ++)
			{
				pageList.add(i);
			}
			
			//判断当前页的页码是否合法
			this.curPage = curPage > totalPage ? totalPage : curPage;
			//得到前一页的页码
			this.prePage = curPage < 2 ? firstPage : curPage - 1;
			//得到后一页的页码
			this.nextPage = curPage == totalPage ? totalPage : curPage + 1;
		}
	}
	
	/**
	 * 将数据添加进来
	 * @param dataList 要在页面上显示的数据
	 */
	public void addData(List<T> dataList)
	{
		if (null != dataList && !dataList.isEmpty())
		{
			this.dataList = dataList;
		}
	}
}
package com.br.ott.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.br.ott.client.common.DistrictCache;
import com.br.ott.client.common.entity.District;
import com.br.ott.common.util.Constants;
import com.br.ott.common.util.DateUtil;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

public class BaseController {
	// AJAX操作成功标识
	protected final static String RETURN_OK = "ok";

	// 列表排序中的排序字段
	private final static String ORDER_COLUMN = "orderColumn";
	// 分页请求中的当前第几页
	private final static String PAGE = "_p";
	// 如果当前页为空，则赋值为1
	private final static String FIRST_PAGE = "1";
	// 每页显示个数
	private final static String PAGE_ROW = "_pageRow";

	// 请求绑定参数（所有省份）
	private final static String P_PROVICES = "provins";
	// 请求绑定参数（省份ID）
	private final static String P_PROID = "proId";
	// 请求绑定参数（省份下的所有城市）
	private final static String P_CITYES = "citys";
	// 请求绑定参数（当前城市ID）
	private final static String P_CITYID = "cityId";

	/**
	 * 日志操作类型：删除
	 */
	protected final static String OPERA_TYPE_DELETE = "3";
	/**
	 * 日志操作类型：修改
	 */
	protected final static String OPERA_TYPE_MODIFY = "2";
	/**
	 * 日志操作类型：新增
	 */
	protected final static String OPERA_TYPE_ADD = "1";
	/**
	 * 日志操作类型：批量导入
	 */
	protected final static String OPERA_TYPE_BATCH_ADD = "4";

	/**
	 * 将Request中的所有参数绑定到Model中(PS:某些情况下要使用到上次请求中的参数，如此不用每次都手动获取)
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	protected Model bindRequestToModel(HttpServletRequest request, Model model) {
		Map<String, String[]> params = request.getParameterMap();
		if (params != null && params.size() > 0) {
			Set<String> keys = params.keySet();
			String[] strs = null;
			for (String key : keys) {
				strs = params.get(key);
				model.addAttribute(key, strs[0]);
			}
		}
		return model;
	}

	/**
	 * 获取请求参数(前提调用了bindRequestToModel方法才能获取到值)
	 * 
	 * @param key
	 * @return
	 */
	protected String getParameter(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}

	/**
	 * 从请求中解析参数并转化为Bean
	 * 
	 * @param request
	 *            请求
	 * @param cls
	 *            需要转化的Bean
	 * @return
	 */
	protected Object resolveBeanFromRequest(HttpServletRequest request,
			Class<?> cls) {
		if (cls == null) {
			return null;
		}

		Map<String, Field> maps = new HashMap<String, Field>();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			maps.put(field.getName(), field);
		}

		Map<String, String[]> params = request.getParameterMap();
		try {
			// 根据传入的Class动态生成pojo对象
			Object obj = cls.newInstance();
			if (params != null && params.size() > 0) {
				Set<String> keys = params.keySet();
				String[] strs = null;
				for (String key : keys) {
					strs = params.get(key);
					Field field = maps.get(key);
					if (field == null) {
						continue;
					}
					// 设置字段可访问（必须，否则报错）
					field.setAccessible(true);
					// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
					if (field.getType().equals(Long.class)
							|| field.getType().equals(long.class)) {
						field.set(obj, com.br.ott.common.util.StringUtil
								.parseLong(strs[0]));
					} else if (field.getType().equals(String.class)) {
						field.set(obj, strs[0]);
					} else if (field.getType().equals(Double.class)
							|| field.getType().equals(double.class)) {
						field.set(obj, com.br.ott.common.util.StringUtil
								.parseDouble(strs[0]));
					} else if (field.getType().equals(Integer.class)
							|| field.getType().equals(int.class)) {
						field.set(obj, com.br.ott.common.util.StringUtil
								.parseInt(strs[0]));
					} else if (field.getType().equals(Float.class)
							|| field.getType().equals(float.class)) {
						field.set(obj, com.br.ott.common.util.StringUtil
								.parseFloat(strs[0]));
					} else if (field.getType().equals(java.util.Date.class)) {
						field.set(obj, DateUtil.parseDate(strs[0],
								"yyyy-MM-dd HH:mm:ss"));
					} else {
						continue;
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取Ajax输出流
	 * 
	 * @return
	 */
	private PrintWriter getDefaultAjaxWriter(HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		try {
			return response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("unable to get writer.");
		}
	}

	/**
	 * 向response写Ajax数据
	 * 
	 * @param result
	 */
	protected void writeAjaxResult(HttpServletResponse response, String result) {
		PrintWriter out = null;
		try {
			out = getDefaultAjaxWriter(response);
			out.write(result);
			out.flush();
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 向response写对象
	 * 
	 * @param response
	 * @param obj
	 */
	protected void writeJson(HttpServletResponse response, Object obj) {
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
	 * 出错时，跳转到错误页面
	 * 
	 * @param model
	 * @param errorMsg
	 * @return
	 */
	protected String newErrorModelView(Model model, String errorMsg) {
		if (model != null) {
			model.addAttribute("error", errorMsg);
		}
		return "error";
	}

	/**
	 * 获取页码
	 * 
	 * @param request
	 * @return
	 */
	protected String getPage(HttpServletRequest request) {
		return getParameter(request, PAGE);
	}

	/**
	 * 获取当前第几页(如为空,则初始化为1)
	 * 
	 * @return
	 */
	protected int getPageNo(HttpServletRequest request) {
		String strPageNo = getPage(request);
		if (StringUtil.isEmpty(strPageNo)) {
			strPageNo = FIRST_PAGE;
		}
		try {
			return Integer.parseInt(strPageNo);
		} catch (Exception e) {
			return 1;
		}
	}

	/**
	 * 每页显示行数
	 * 
	 * @param request
	 * @return
	 */
	protected int getPageRow(HttpServletRequest request) {
		String _pageRow = getParameter(request, PAGE_ROW);
		if (StringUtil.isEmpty(_pageRow)) {
			return Constants.PAGE_DATA;
		}
		try {
			String[] pageRowSelect = PagedModelList.pageRowSelect;

			boolean bool = false;
			for (String str : pageRowSelect) {
				if (_pageRow.equals(str)) {
					bool = true;
					break;
				}
			}

			if (!bool)
				return Constants.PAGE_DATA;

			return Integer.parseInt(_pageRow);
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.PAGE_DATA;
		}
	}

	/**
	 * 获取排序的列字段
	 * 
	 * @param request
	 * @return
	 */
	protected String getOrderColumn(HttpServletRequest request) {
		return getParameter(request, ORDER_COLUMN);
	}

	/**
	 * 绑定所有省份和指定省份下的所有城市
	 * 
	 * @param model
	 * @param proId
	 *            省份ID
	 */
	protected void bindCitysToModel(Model model, String proId) {
		model.addAttribute(P_PROID, proId);
		model.addAttribute(P_PROVICES, DistrictCache.getProvinces());
		model.addAttribute(P_CITYES, DistrictCache.getCitys(StringUtil
				.isEmpty(proId) ? 0 : Integer.valueOf(proId)));
	}

	/**
	 * 绑定所有省份和该城市同属省份的所有城市、以及绑定当前城市区号，当前所属省份ID
	 * 
	 * @param model
	 * @param city
	 */
	protected void bindCitysAllToModel(Model model, String areaCode) {
		// 所有省份
		model.addAttribute(P_PROVICES, DistrictCache.getProvinces());
		// 当前所属城市
		District city = DistrictCache.getCityByAreaCode(areaCode);
		if (city != null) {
			model.addAttribute(P_CITYID, city.getAreacode());
			model.addAttribute(P_PROID, city.getUpid());
			model.addAttribute(P_CITYES, DistrictCache.getCitys(city.getUpid()));
		} else {
			model.addAttribute(P_PROID, 16);
			model.addAttribute(P_CITYES, DistrictCache.getCitys(16));
		}
	}

	/**
	 * 获取所有省份 创建人：陈登民 创建时间：2013-1-21 - 下午2:11:15
	 * 
	 * @param proId
	 * @return 返回类型：List<District>
	 * @exception throws
	 */
	protected List<District> getProvinces() {
		return DistrictCache.getProvinces();
	}

	/**
	 * 获取省份的所有城市 创建人：陈登民 创建时间：2013-1-21 - 下午2:11:15
	 * 
	 * @param proId
	 * @return 返回类型：List<District>
	 * @exception throws
	 */
	protected List<District> getCitys(String proId) {
		return DistrictCache.getCitys(StringUtil.isEmpty(proId) ? 0 : Integer
				.valueOf(proId));
	}

}

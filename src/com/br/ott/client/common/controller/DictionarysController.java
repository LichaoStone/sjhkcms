package com.br.ott.client.common.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.br.ott.base.BaseController;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.common.service.DictionarysService;
import com.br.ott.common.util.Constants.State;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 
 * @author cailz
 * 
 */
@Controller
@RequestMapping(value = "dict")
public class DictionarysController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(DictionarysController.class);
	private static final String BUSI_NAME = "数据字典管理";
	@Autowired
	private DictionarysService dictionarysService;

	@Autowired
	private OperaLogService operaLogService;

	/**
	 * 跳转到第二级别字典界面并为下拉框加载数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "toDict")
	public String toDict(Model model, HttpServletRequest request) {
		Dictionary dictionary = new Dictionary();
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			dictionary = dictionarysService.findByDictById(id);
		}
		model.addAttribute("dict", dictionary);
		return "common/dictInfo";
	}

	/**
	 * 增加2级数据
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addDict", method = RequestMethod.POST)
	public @ResponseBody
	Feedback addDict(HttpServletRequest request, Model model) {
		bindRequestToModel(request, model);
		Dictionary dict = (Dictionary) resolveBeanFromRequest(request,
				Dictionary.class);
		try {
			dict.setCreator(SessionUtil.getCurrentUser().getUserName());
			dict.setStatus(State.VALID);
			dictionarysService.addDictionarys(dict);
			// 加入日记
			operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
					"添加二级字典：" + dict.getDicName());
		} catch (RuntimeException e) {
			e.printStackTrace();
			return Feedback.fail("插入字典数据失败");
		}
		dict = null;
		return Feedback.success("插入字典数据成功");
	}

	@RequestMapping(value = "updateDict", method = RequestMethod.POST)
	public @ResponseBody
	Feedback updateDict(HttpServletRequest request, Model model, Dictionary dict) {
		Dictionary old = dictionarysService.findByDictById(request
				.getParameter("id"));
		if (!old.getDicName().equals(dict.getDicName())) {
			boolean bool = dictionarysService.findDictByNameAndType(
					dict.getDicName(), dict.getDicType());
			if (!bool) {
				return Feedback.fail("字典修改失败,字典名称已使用");
			}
		}
		if (!old.getDicValue().equals(dict.getDicValue())) {
			boolean bool = dictionarysService.findDictByValueAndType(
					dict.getDicValue(), dict.getDicType());
			if (!bool) {
				return Feedback.fail("字典修改失败,字典值已使用");
			}
		}
		try {
			dictionarysService.updateDictionary(dict);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("修改字典数据失败");
		}
		try {
			String diffStr = ObjectUtil.getDiffColumnDescript(old, dict);
			if (null != diffStr) {
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Feedback.success("修改字典数据成功");
	}

	/**
	 * 查看所有的字典数据
	 * 
	 * @param request
	 * @param dict
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findAll")
	public String findAll(HttpServletRequest request, Model model) {
		model = bindRequestToModel(request, model);
		logger.debug("数据字典,当前页码：" + getPageNo(request));
		Dictionary dict = new Dictionary();
		dict.setDicName(getParameter(request, "dicName"));
		dict.setDicValue(getParameter(request, "dicValue"));
		dict.setOrderColumn(getOrderColumn(request));
		PagedModelList<Dictionary> dictList = dictionarysService
				.findAllDictionarys(dict, getPageNo(request),
						getPageRow(request));
		model.addAttribute("dictList", dictList);
		model.addAttribute("dict", dict);
		dictList = null;
		return "common/listDict";
	}

	/**
	 * 查看字典详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "dictDetail")
	public String dictDetail(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(id)) {
			Dictionary dict = dictionarysService.findByDictById(id);
			model.addAttribute("dict", dict);
		}
		return "common/dictDetail";
	}

	/**
	 * 数据字典启用 创建人：pananz 创建时间：2014-10-10 - 上午10:46:25
	 * 
	 * @param id
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "dictClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dictClose(@RequestParam("id") String id, HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("数据字典停用失败,请选择要停用的数据字典");
		}
		try {
			dictionarysService.updateDictStatus("0", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("数据字典停用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"停用数据字典编号为：" + id);
		}
		return Feedback.success("数据字典停用成功");
	}

	/**
	 * 数据字典启用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "dictReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback dictReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		boolean flag = false;
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("数据字典启用失败,请选择要启用的数据字典");
		}
		try {
			dictionarysService.updateDictStatus("1", id);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("数据字典启用失败");
		}
		if (flag) {
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
					"启用数据字典编号为：" + id);
		}
		return Feedback.success("数据字典启用成功");
	}

	/**
	 * 通过check 选中多个数据删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delDict", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delDict(@RequestParam("ids") String ids, HttpServletRequest request) {
		if (StringUtil.isEmpty(ids)) {
			return Feedback.fail("字典类型删除失败,请选择要删除的字典类型");
		}
		try {
			String[] dictids = ids.split(",");
			dictionarysService.delDictionaryList(Arrays.asList(dictids));
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"删除字典编号是：" + ids);
			return Feedback.success("字典类型删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("字典类型删除失败");
		}
	}
	
	/**
	 * 验证输入的名称是否存在
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "findDictByName", method = RequestMethod.GET)
	public void findDictByName(HttpServletRequest request,
			HttpServletResponse response) {
		String dicType = request.getParameter("dicType");
		String dicName = request.getParameter("dicName").trim();
		String oldName = request.getParameter("oldName").trim();
		if (StringUtil.isEmpty(dicName) || oldName.equals(dicName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dictionarysService.findDictByNameAndType(dicName,
				dicType);
		writeAjaxResult(response, String.valueOf(result));
	}

	@RequestMapping(value = "findDictByValue", method = RequestMethod.GET)
	public void findDictByValue(HttpServletResponse response,
			HttpServletRequest request) {
		String dicType = request.getParameter("dicType");
		String dicValue = request.getParameter("dicValue").trim();
		String oldValue = request.getParameter("oldValue").trim();
		if (StringUtil.isEmpty(dicValue) || oldValue.equals(dicValue)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean result = dictionarysService.findDictByValueAndType(dicValue,
				dicType);
		writeAjaxResult(response, String.valueOf(result));
	}

	/**
	 * 导入数据字典
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "getReadReport", method = RequestMethod.POST)
	public void getImportDictInfo(MultipartHttpServletRequest msr,
			HttpServletRequest request, HttpServletResponse response) {
		String msg = "";
		try {
			MultipartFile file = msr.getFile("fileApp");
			msg = dictionarysService.insertDict(file, request);
		} catch (IOException e) {
			e.printStackTrace();
			msg = "读取文件出错";
		}
		writeAjaxResult(response, "{\"msg\":\"" + msg + "\"}");
	}
}

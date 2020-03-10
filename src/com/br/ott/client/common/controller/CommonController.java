package com.br.ott.client.common.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.br.ott.Global;
import com.br.ott.base.BaseController;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.DistrictCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.entity.District;
import com.br.ott.client.common.service.CommonService;
import com.br.ott.client.common.service.DictionarysService;
import com.br.ott.client.partner.entity.Partner;
import com.br.ott.client.partner.service.PartnerService;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.service.DramaNavigationService;
import com.br.ott.common.util.Constants.DicType;
import com.br.ott.common.util.Constants.PartnerState;
import com.br.ott.common.util.DateUtil;
import com.br.ott.common.util.ObjectExcelView;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.UploadFile;

/**
 * 公用控制器
 * 
 * @author 陈登民
 * 
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(CommonController.class);

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private DictionarysService dictionarysService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private DramaNavigationService navigationService;
	private static final long MAX_IMG = 2097152;

	@RequestMapping(value = "changeNavigation")
	public @ResponseBody
	List<DramaNavigation> changeNavigation(HttpServletRequest request) {
		String parentId = request.getParameter("parentId");
		DramaNavigation channel = new DramaNavigation();
		channel.setStatus("1");
		channel.setParentId(parentId);
		List<DramaNavigation> list = navigationService
				.findDramaNavigationByCond(channel);
		return list;
	}

	/**
	 * 上传文件
	 * 
	 * 创建人：pananz 创建时间：2015-6-8 - 上午11:01:42
	 * 
	 * @param file
	 * @param response
	 * @param request
	 *            返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public void uploadFile(MultipartHttpServletRequest file,
			HttpServletResponse response, HttpServletRequest request) {
		String fileId = request.getParameter("fileId");
		MultipartFile imgFile = file.getFile(fileId);
		if (imgFile != null) {
			if (imgFile.getSize() > MAX_IMG) {
				writeAjaxResult(response, "{\"result\":0,\"msg\":\"图片过大\"}");
				return;
			}
		}
		try {
			String secondsDir = Global.EPG_IMG + "/" + DateUtil.getCurrentDate();
			String ImgUrl = UploadFile.uploadFileToServer(imgFile, "",
					secondsDir, "");
			writeAjaxResult(response, "{\"result\":1,\"msg\":\"" + ImgUrl
					+ "\"}");
		} catch (Exception e) {
			writeAjaxResult(response, "{\"result\":0,\"msg\":\"上传出错\"}");
		}
	}

	/**
	 * 切换省份
	 * 
	 * @return
	 */
	@RequestMapping(value = "changeCity")
	public @ResponseBody
	List<District> changeCity(HttpServletRequest request, Model model) {
		String proId = request.getParameter("proId");
		logger.debug("==切换省份，获取城市,省份ID：" + proId);
		return getCitys(proId);
	}

	/**
	 * 转到合作伙伴页面
	 * 
	 * @param request
	 * @param partner
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "partner", method = RequestMethod.GET)
	public String partner(Model model) {
		Map<String, Dictionary> mechantTypes = DictCache
				.getDictValueList(DicType.MERCHANT_TYPE);
		model.addAttribute("mechantTypes", mechantTypes);
		model.addAttribute("partner", new Partner());
		return "common/partner";
	}

	/**
	 * 合作伙伴AJAVA查询
	 * 
	 * @param request
	 * @param response
	 * @param partner
	 */
	@RequestMapping(value = "findPartner", method = RequestMethod.GET)
	public void findPartner(HttpServletRequest request,
			HttpServletResponse response, Partner partner) {
		int skip = 1;
		String page = request.getParameter("page");
		if (!StringUtil.isEmpty(page)) {
			skip = Integer.valueOf(page);
		}
		partner.setReadyOrPass(PartnerState.PARTNER_READY + ","
				+ PartnerState.PARTNER_PASS);
		PagedModelList<Partner> pml = partnerService.findPartner(partner, skip,
				getPageRow(request));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pageCount", pml.getPageCount());
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(pml
				.getPagedModelList());
		jsonObject.put("result", jsonArray.toString());
		pml = null;
		writeJson(response, jsonObject);
	}

	/**
	 * 转到选择字典类型页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "dict", method = RequestMethod.GET)
	public String dict() {
		return "common/dict";
	}

	/**
	 * 字典类型树形结构输出
	 * 
	 * @param response
	 */
	@RequestMapping(value = "buildTreeDict", method = RequestMethod.POST)
	public void buildTreeDict(HttpServletResponse response) {
		writeAjaxResult(response, dictionarysService.buildTreeDict());
	}

	private static Random random = new Random();

	private Color getRandColor(int fc, int bc) {
		return getRandColor(fc, bc, fc);
	}

	private Color getRandColor(int fc, int bc, int interval) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - interval);
		int g = fc + random.nextInt(bc - interval);
		int b = fc + random.nextInt(bc - interval);
		return new Color(r, g, b);
	}

	/**
	 * 生成验证码
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "getVeCode", method = RequestMethod.GET)
	public void getVeCode(HttpServletResponse response,
			HttpServletRequest request) {
		try {
			int codeLength = 4;
			int mixTimes = 230;
			Color bgColor = getRandColor(200, 250);
			Color bfColor = new Color(0, 0, 0);
			boolean ifRandomColor = true;
			boolean ifMixColor = true;

			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			int width = 13 * codeLength + 20, height = 28;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(bgColor);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			g.setColor(new Color(33, 66, 99));
			g.drawRect(0, 0, width - 1, height - 1);
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < mixTimes * codeLength / 10; i++) {
				if (ifMixColor) {
					g.setColor(getRandColor(160, 200));
				}
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, x + xl, y + yl);
			}
			char[] c = new char[62];
			for (int i = 97, j = 0; i < 123; i++, j++) {
				c[j] = (char) i;
			}
			for (int o = 65, p = 26; o < 91; o++, p++) {
				c[p] = (char) o;
			}
			for (int m = 48, n = 52; m < 58; m++, n++) {
				c[n] = (char) m;
			}
			StringBuffer sRand = new StringBuffer();
			for (int i = 0; i < codeLength; i++) {
				String rand = getRandomStr(c);
				sRand.append(rand);
				if (ifRandomColor)
					g.setColor(getRandColor(20, 110, 0));
				else
					g.setColor(bfColor);
				g.drawString(rand, 13 * i + 10, 20);
			}
			HttpSession session = request.getSession(true);
			session.setAttribute("rand", sRand.toString());
			g.dispose();
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (Exception e) {
		}
	}

	private String getRandomStr(char[] c) {
		int x = random.nextInt(62);
		String rand = String.valueOf(c[x]);
		// 1,i,I,l,o,0,O,4
		if ("1".equals(rand) || "i".equals(rand) || "l".equals(rand)
				|| "I".equals(rand) || "O".equals(rand) || "o".equals(rand)
				|| "0".endsWith(rand) || "4".equals(rand)) {
			return getRandomStr(c);
		}
		return rand;
	}

	/**
	 * 校验验证码是否正确
	 * 
	 * 创建人：pananz 创建时间：2015-11-24
	 * 
	 * @param @param request
	 * @param @param response 返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkVdcode", method = RequestMethod.GET)
	public void checkVdcode(HttpServletRequest request,
			HttpServletResponse response) {
		String vdcode = request.getParameter("vdcode");
		if (StringUtil.isEmpty(vdcode)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		String rand = (String) request.getSession().getAttribute("rand");
		if (vdcode.equals(rand)) {
			writeAjaxResult(response, String.valueOf(true));
		} else {
			writeAjaxResult(response, String.valueOf(false));
		}
	}

	/**
	 * 导出到excel（只适合单表基本数据导出）
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel(HttpServletRequest request)
			throws Exception {
		logger.debug("导出到excel");

		/**
		 * 列描述（数据表字段名：Excel表头描述） example name:名称,age:年龄,sex:性别
		 */
		String columns = getParameter(request, "columns");
		String filename = getParameter(request, "filename");// 文件名称，不需要后缀
		String table = getParameter(request, "table");// 表名，要从哪个数据表导出数据
		String where = getParameter(request, "where");// 查询条件，直接拼装成数据库查询的语句,where后面的语句
		String firstDescript = getParameter(request, "firstDescript");// 第一行的总描述

		if (columns == null)
			return null;
		String cols = null;// 查询的数据库字段
		List<String> titles = new ArrayList<String>();// Excel的表头描述
		List<String> colList = new ArrayList<String>();// 存放字段
		Map<String, String> colTypeMap = new HashMap<String, String>();// 存放列的类型（city,opr）
		if (columns.contains(",")) {// 有多个字段
			String[] cols_desc = columns.split(",");
			for (String c_d : cols_desc) {
				if (!c_d.contains(":"))
					continue;
				String[] c_ds = c_d.split(":");
				if (cols == null) {
					cols = c_ds[0];
				} else {
					cols += "," + c_ds[0];
				}
				if (c_ds.length > 2) {
					colTypeMap.put(c_ds[0], c_ds[2]);
				}
				titles.add(c_ds[1]);
				colList.add(c_ds[0]);
			}
		} else {// 单个字段
			if (columns.contains(":")) {
				String[] c_ds = columns.split(":");
				cols = c_ds[0];
				titles.add(c_ds[1]);
				colList.add(c_ds[0]);
				if (c_ds.length > 2) {
					colTypeMap.put(c_ds[0], c_ds[2]);
				}
			}
		}

		ModelAndView mv = new ModelAndView();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("titles", titles);

		/*
		 * PageData pd = new PageData(); pd.put("columns", cols);
		 * pd.put("table", table); pd.put("where", where);
		 */

		List<Map<String, Object>> varOList = commonService.exportExcel(cols,
				table, where);
		List<Map<String, Object>> varList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < varOList.size(); i++) {
			Map<String, Object> vpd = new HashMap<String, Object>();
			for (int k = 0; k < colList.size(); k++) {// 每列数据
				if (colTypeMap.containsKey(colList.get(k))) {
					Object obj = varOList.get(i).get(colList.get(k));
					String type = colTypeMap.get(colList.get(k));
					if (obj instanceof String) {
						String code = (String) obj;
						if ("city".equals(type)) {
							vpd.put("var" + k,
									DistrictCache.getCityByAreaCode(code)
											.getName());
						}
					}
				} else {
					vpd.put("var" + k, varOList.get(i).get(colList.get(k)));
				}
			}
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		dataMap.put("filename", filename);
		dataMap.put("firstDescript", firstDescript);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}
}

package com.br.ott.client.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.mapper.DictionarysMapper;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.common.jackson.TinyTreeBean;
import com.br.ott.common.jackson.TinyTreeUtils;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.Constants.DicType;

/**
 * 系统字典管理service
 * 
 * @author cailz
 * 
 */
@Component
public class DictionarysService {
	/**
	 * 日志操作类型：批量导入
	 */
	private static final String BUSI_NAME_BATCH = "批量导入数据字典";
	protected final static String OPERA_TYPE_BATCH_ADD = "4";
	@Autowired
	private DictionarysMapper dictionarysMapper;
	@Autowired
	private OperaLogService operaLogService;
	private static final String ROOT_GOOS_TYPE_ID = "0";

	/**
	 * 增加字典
	 * 
	 * @param pageNo
	 * @param dictionary
	 * @return
	 */
	public void addDictionarys(Dictionary dict) {
		dictionarysMapper.addDictionarys(dict);
		DictCache.reload(findAllDictionarys());
	}

	/**
	 * 修改数据字典 创建人：pananz 创建时间：2014-10-9 - 下午5:31:19
	 * 
	 * @param dict
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateDictionary(Dictionary dict) {
		dictionarysMapper.updateDictionary(dict);
		DictCache.reload(findAllDictionarys());
	}

	/**
	 * 删除字典
	 * 
	 * @param ids
	 */
	public void delDictionarys(String ids) {
		dictionarysMapper.delDictionarys(ids);
		DictCache.reload(findAllDictionarys());
	}

	/**
	 * 点击的字典详情
	 * 
	 * @param id
	 * @return
	 */
	public Dictionary findByDictById(String id) {
		return dictionarysMapper.findByDictById(id);
	}

	public void delDictionaryList(List<String> list) {
		dictionarysMapper.delDictionaryList(list);
		DictCache.reload(findAllDictionarys());
	}
	
	/**
	 * 字典列表信息
	 */
	public PagedModelList<Dictionary> findAllDictionarys(Dictionary dictionary,
			int skip, int row) {
		int totalCount = dictionarysMapper.findCountDictionarys(dictionary);
		PagedModelList<Dictionary> pml = new PagedModelList<Dictionary>(skip,
				row, totalCount);
		pml.addModels(dictionarysMapper.findAllDictionarys(dictionary,
				new RowBounds((skip - 1) * row, row)));
		return pml;
	}

	/**
	 * 验证输入的常量名是否存在
	 * 
	 * @param name
	 * @return
	 */
	public boolean findDictByName(String name) {
		List<Dictionary> dicts = dictionarysMapper.findDictByName(name);
		if (CollectionUtils.isNotEmpty(dicts)) {
			return false;
		}
		return true;
	}

	/**
	 * 按名称及类型校验是否存在 创建人：pananz 创建时间：2014-9-30 - 上午11:42:23
	 * 
	 * @param name
	 * @param type
	 * @return 返回类型：boolean
	 * @exception throws
	 */
	public boolean findDictByNameAndType(String name, String type) {
		Dictionary dict = new Dictionary();
		dict.setDicType(type);
		dict.setDicName(name);
		List<Dictionary> dicts = dictionarysMapper.findDictByCond(dict);
		if (CollectionUtils.isNotEmpty(dicts)) {
			return false;
		}
		return true;
	}

	public void updateDictStatus(String status, String id) {
		dictionarysMapper.modifyDictStatus(status, id);
		DictCache.reload(findAllDictionarys());
	}

	/**
	 * 按字典值及类型校验是否存在 创建人：pananz 创建时间：2014-9-30 - 上午11:42:43
	 * 
	 * @param value
	 * @param type
	 * @return 返回类型：boolean
	 * @exception throws
	 */
	public boolean findDictByValueAndType(String value, String type) {
		Dictionary dict = new Dictionary();
		dict.setDicValue(value);
		dict.setDicType(type);
		List<Dictionary> dicts = dictionarysMapper.findDictByCond(dict);
		if (CollectionUtils.isNotEmpty(dicts)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证输入的常量类型是否存在
	 * 
	 * @param dicType
	 * @return
	 */
	public boolean findDictByValue(String dicValue) {
		List<Dictionary> dicts = dictionarysMapper.findDictByValue(dicValue);
		if (CollectionUtils.isNotEmpty(dicts)) {
			return false;
		}
		return true;
	}

	/**
	 * 得到所有的数据，用于重新加载缓存
	 * 
	 * @return
	 */
	public List<Dictionary> findAllDictionarys() {
		Dictionary dict= new Dictionary();
		dict.setStatus("1");
		return dictionarysMapper.findAllDictionarys(dict);
	}

	/**
	 * 最大值的二級dicvalue值
	 * 
	 * @return
	 */
	public int findMaxKey() {
		return dictionarysMapper.findMaxKey();
	}

	/**
	 * 读取Excel
	 */
	public List<Dictionary> readReport(InputStream inp) {
		List<Dictionary> dictList = new ArrayList<Dictionary>();
		try {
			String cellStr = null;
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);// 取得第一个sheets
			// 从第二行开始读取数据
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Dictionary dict = new Dictionary();
				Dictionary addDict = new Dictionary();
				Row row = sheet.getRow(i); // 获取行(row)对象
				if (row == null) {
					// row为空的话,不处理
					continue;
				}
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j); // 获得单元格(cell)对象
					// 转换接收的单元格
					cellStr = ConvertCellStr(cell, cellStr);
					// 将单元格的数据添加至一个对象
					addDict = addingDict(j, dict, cellStr);
				}
				// 将添加数据后的对象填充至list中
				dictList.add(addDict);
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inp != null) {
				try {
					inp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dictList;
	}

	private String ConvertCellStr(Cell cell, String cellStr) {
		if (null == cell) {
			return "";
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			// 读取String
			cellStr = cell.getStringCellValue().toString();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 得到Boolean对象的方法
			cellStr = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// 先看是否是日期格式
			if (DateUtil.isCellDateFormatted(cell)) {
				// 读取日期格式
				cellStr = cell.getDateCellValue().toString();
			} else {
				// 读取数字
				// cellStr = String.valueOf(cell.getNumericCellValue());
				double number = cell.getNumericCellValue();
				int num = (int) cell.getNumericCellValue();
				if (number == num) {
					cellStr = String.valueOf(num);
				} else {
					cellStr = String.valueOf(number);
				}
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 读取公式
			cellStr = cell.getCellFormula().toString();
			break;
		}
		return cellStr;
	}

	/**
	 * 获得单元格的数据添加至computer
	 * 
	 * @param j
	 *            列数
	 * @param computer
	 *            添加对象
	 * @param cellStr
	 *            单元格数据
	 * @return
	 */
	private Dictionary addingDict(int j, Dictionary dict, String cellStr) {
		switch (j) {
		case 0:
			break;
		case 1:
			dict.setDicnickName(cellStr);
			break;
		case 2:
			dict.setDicName(cellStr);
			break;
		case 3:
			dict.setDicValue(cellStr);
			break;
		case 4:
			dict.setCreateTime(cellStr);
			break;
		case 5:
			dict.setDicType(cellStr);
			break;
		case 6:
			dict.setUpdatable(cellStr);
			break;
		case 7:
			dict.setCreator(cellStr);
			break;
		case 8:
			dict.setStatus(cellStr);
			break;
		case 9:
			dict.setOrderId(cellStr);
			break;
		case 10:
			dict.setMemo(cellStr);
			break;
		}
		return dict;
	}

	/**
	 * 读取报表的数据后批量插入
	 * 
	 * @throws IOException
	 */
	public String insertDict(MultipartFile file, HttpServletRequest request)
			throws IOException {
		List<Dictionary> list = readReport(file.getInputStream());
		if (CollectionUtils.isEmpty(list)) {
			return "文件内容不能为空";
		}
		int flag = 1;
		int have = 0;
		int success = 0;
		int except = 0;
		boolean flags = false;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbHave = new StringBuffer();
		StringBuffer sbExcept = new StringBuffer();
		StringBuffer logContent = new StringBuffer();
		logContent.append("导入的数据字典数据如下：");
		for (Dictionary dict : list) {
			flag++;
			try {
				boolean bool = findDictByName(dict.getDicName());
				if (!bool) {
					have++;
					sbHave.append(flag + ",");
				} else {
					dictionarysMapper.addDictionarys(dict);
					logContent.append(dict.getDicName() + "/"
							+ dict.getCreator() + ",");
					success++;
					flags = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				except++;
				sbExcept.append(flag + ",");
			}
		}
		if (flags) {
			// 写入系统操作日志
			operaLogService.addOperaLog(
					OPERA_TYPE_BATCH_ADD,
					request,
					BUSI_NAME_BATCH,
					logContent.toString().substring(0,
							logContent.toString().length() - 1));
		}
		sb.append("共" + list.size() + "条数据,");
		if (success > 0) {
			sb.append("成功导入" + success + "条数据,");
			DictCache.reload(findAllDictionarys());
		}
		if (have > 0) {
			sb.append("重复导入失败" + have + "条,第" + sbHave.toString() + "条数据已存在");
		}
		if (except > 0) {
			sb.append("导入失败" + except + "条,第" + sbExcept.toString() + "条数据异常");
		}

		return sb.toString();
	}
	

	/**
	 * 查询类型（按树形结构展现)
	 * 
	 * @return
	 */
	public String buildTreeDict() {
		// List<Dictionary> dicts = findDictionaryByDicType();
		List<Dictionary> dicts = DictCache.getDictList(DicType.TOP_TYPE);
		return TinyTreeUtils.toJson(buildTinyTreeBean(dicts));
	}

	private List<TinyTreeBean> buildTinyTreeBean(List<Dictionary> dicts) {
		// TODO 此方法后续需优化(当前只能用于查询数据量不大的情况)
		TinyTreeBean root = new TinyTreeBean(ROOT_GOOS_TYPE_ID, null);
		Map<String, TinyTreeBean> ptTreeMap = new HashMap<String, TinyTreeBean>();
		ptTreeMap.put(ROOT_GOOS_TYPE_ID, root);
		if (CollectionUtils.isEmpty(dicts)) {
			root.setName("暂无类型");
			return ptTreeMap.get(ROOT_GOOS_TYPE_ID).getChildren();
		}
		for (Dictionary dict : dicts) {
			TinyTreeBean node = new TinyTreeBean(dict.getDicValue(),
					dict.getDicName());
			ptTreeMap.put(dict.getDicValue(), node);
		}
		for (Dictionary pt : dicts) {
			TinyTreeBean parent = ptTreeMap.get(pt.getDicType());
			if (null == parent) {
				continue;
			}
			parent.addChild(ptTreeMap.get(pt.getDicValue()));
		}
		return ptTreeMap.get(ROOT_GOOS_TYPE_ID).getChildren();
	}
}

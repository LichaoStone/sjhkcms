package com.br.ott.test;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.client.operasset.service.HandleAssetsService;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.service.DramaProgramService;
import com.br.ott.common.util.StringUtil;

@Controller
@RequestMapping(value="/tool")
public class TestController {
	
	@Autowired
	private DramaProgramService dramaProgramService;
	
	@Autowired
	private HandleAssetsService handleAssetsService;
	
	/**
	 * 把sjhkcms项目中的ott_jointinfo_othercategory表记录转换为合辑
	 */
	@RequestMapping("/changeCateToAlnum")
	@ResponseBody
	public String changeCateToAlnum(){
		List<Map<String, Object>> cList = this.handleAssetsService.queryOtherCategory();
		if(CollectionUtils.isNotEmpty(cList)){
			//插入记录到ott_drama_program，标记为合辑
			DramaProgram dp = new DramaProgram();
			Iterator<Map<String, Object>> itor = cList.iterator();
			Map<String, Object> paramsMap = null;
			List<Map<String, String>> result = null;
			while(itor.hasNext()){
				paramsMap = itor.next();
				dp.setName((String)paramsMap.get("crNavName"));
				dp.setcProvider("BT");
				dp.setAssetId((String)paramsMap.get("crNavId"));
				dp.setpId("-1");
				dp.setPtype("JC");
				dp.setPcode(StringUtil.getPinYinHeadChar((String)paramsMap.get("crNavName")));
				dp.setRemark((String)paramsMap.get("description"));
				dp.setIsAlbum("1");
				dp.setTtype((String)paramsMap.get("localNavId"));
				dp.setStatus("1");
				this.dramaProgramService.addDramaProgram(dp);
				paramsMap.put("albumId", dp.getId());
				this.handleAssetsService.updateOtherCategory(paramsMap);
				result = this.handleAssetsService.queryJointInfoTypeInfo(dp.getAssetId(), "Category");
				if(CollectionUtils.isEmpty(result))
					this.handleAssetsService.addJointInfoType(dp.getId(), dp.getAssetId(), dp.getAssetId(), "Category");
				result = null;
			}
		}
		cList = null;
		return "测试成功!";
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		
		File file = null;
		Document doc = null;
		Element object = null;
		SAXReader reader = new SAXReader();
		String parentPath = "C:\\Users\\Administrator\\Downloads\\";
		File parantFile = new File(parentPath);
		String[] parentList = parantFile.list();
		File sonFile = null;
		String[] sonList = null;
		List<Element> propertyList = null;
		Map<String, String> map = new HashMap<String, String>();
		for(String fileName : parentList){
			sonFile = new File(parentPath + fileName);
			if(!sonFile.isDirectory()) continue;
			sonList = sonFile.list();
			for(String sonFileName : sonList){
				file = new File(parentPath + fileName + File.separator + sonFileName);
				if(file.isFile()){
					//先判断该xml是否为空
					try {
						doc = reader.read(file);
					} catch (DocumentException e) {
						e.printStackTrace();
						System.out.println(file.getAbsolutePath() + "-->出现DocumentException异常：" + e.getMessage());
					}
					for (Iterator<Element> iter = doc.selectNodes("//Object").iterator(); iter.hasNext();) {
						object = (Element) iter.next();
						propertyList = object.elements("Property");
						map.clear();
					    for (int j = 0, m = propertyList.size(); j < m; j++) {
					    	map.put(propertyList.get(j).attributeValue("Name"), propertyList.get(j).getText().trim());
					    }
					    if(map.get("Status") != null && !"1".equals(map.get("Status"))){
					    	System.out.println("Object的Status不为1的工单：" + file.getAbsolutePath());
					    	break;
					    }
					}
				}
			}
		}
	}
}

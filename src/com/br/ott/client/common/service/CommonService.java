package com.br.ott.client.common.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.common.mapper.CommonMapper;

@Service
public class CommonService {
	
	@Autowired
	private CommonMapper commonMapper;
	
	public List<Map<String, Object>> exportExcel(String columns,String table,String where){
		return commonMapper.exportExcel(columns, table, where);
	}

	/**
	 * 删除集合中某属性相同的对象，比如A和B的name一样，随机删除A或者B。返回List集合
	 * @author lizhenghg 2017-07-24
	 * @param object
	 * @param content  属性名
	 * @return
	 */
	public static ArrayList<?> removeDuplicateObject(List<?> object, final String content) {
		Set<Object> set = new TreeSet<Object>(new Comparator<Object>()
			{
			@Override
			public int compare(Object o1, Object o2) {
				Class<?> clazz1 = o1.getClass();
				Class<?> clazz2 = o2.getClass();
				Method method1 = null;
				Method method2 = null;
				String v1 = null;
				String v2 = null;
				try{
					method1 = clazz1.getDeclaredMethod("get" + content, null);
				    method2 = clazz2.getDeclaredMethod("get" + content, null);
					method1.setAccessible(true);
					method2.setAccessible(true);
					v1 = (String) method1.invoke(o1, null);
				    v2 = (String) method2.invoke(o2, null);
					return v1.compareTo(v2);
				}catch(Exception e){
					return 0;
				}
			}
		});
		set.addAll(object);
		return new ArrayList<Object>(set);
	}
}

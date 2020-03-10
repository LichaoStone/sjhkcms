package com.br.ott.common.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.br.ott.common.annotation.A;
import com.br.ott.common.annotation.FieldName;

/* 
 *  对象操作工具类
 *  文件名：ObjectUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-25 - 下午2:55:35
 */
public class ObjectUtil {
	
	/**
	 * 对象克隆
	 * 创建人：陈登民
	 * 创建时间：2012-12-25 - 下午4:08:55
	 * @param obj
	 * @return
	 * 返回类型：Object
	 * @exception throws
	 */
	public static Object clone(Object obj){
		try {
			return org.apache.commons.beanutils.BeanUtils.cloneBean(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Field[]  getFields(Object oldObj, Object newObj){
		Field[] fields = null;
		if (oldObj == null || newObj == null) {
			return fields;
		}
		Class<?> oldCls = oldObj.getClass();
		Class<?> newCls = newObj.getClass();
		if (!oldCls.equals(newCls)) {
			return fields;
		}
		fields = oldCls.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return null;
		}
		return fields;
	}
	
	/**
	 * 获取两个对象中字段值不一样的字段
	 * 创建人：陈登民
	 * 创建时间：2012-12-25 - 下午4:09:05
	 * @param oldObj
	 * @param newObj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 返回类型：Map<String,Object>
	 * @exception throws
	 */
	public static Map<String, Object> diffColumn(Object oldObj, Object newObj) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = getFields(oldObj, newObj);
		if(fields==null) return null;
		Map<String, Object> maps = new HashMap<String, Object>();
		String key = null;
		for (Field field : fields) {
			if (field == null) {
				continue;
			}
			// 设置字段可访问（必须，否则报错）
			field.setAccessible(true);
			key = field.getName();
			FieldName fieldName = field.getAnnotation(FieldName.class);
			if(fieldName!=null && !"".equals(fieldName.value().trim())){
				key = fieldName.value().trim();
			}
			
			// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
			if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
				if(field.getLong(oldObj)!=field.getLong(newObj)){
					maps.put(key, field.getLong(newObj));
				}
			} else if (field.getType().equals(String.class)) {
				if(field.get(oldObj)==null && field.get(newObj)==null){
					continue;
				}
				if(field.get(oldObj)==null && field.get(newObj)!=null){
					maps.put(key, field.get(newObj));
					continue;
				}
				if(field.get(oldObj)!=null && field.get(newObj)==null){
					maps.put(key, "");
					continue;
				}
				
				if(!field.get(oldObj).toString().equals(field.get(newObj).toString())){
					maps.put(key, field.get(newObj));
				}
			} else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) { 
				if(field.getDouble(oldObj)!=field.getDouble(newObj)){
					maps.put(key, field.getDouble(newObj));
				}
			} else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
				if(field.getInt(oldObj)!=field.getInt(newObj)){
					maps.put(key, field.getInt(newObj));
				}
			} else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
				if(field.getFloat(oldObj)!=field.getFloat(newObj)){
					maps.put(key, field.getFloat(newObj));
				}
			} else if (field.getType().equals(java.util.Date.class)) {
				Date oldDate = (Date) field.get(oldObj);
				Date newDate = (Date) field.get(newObj);
				String oldDateStr = DateUtil.parseDate(oldDate, "yyyy-MM-dd HH:mm:ss");
				String newDateStr = DateUtil.parseDate(newDate, "yyyy-MM-dd HH:mm:ss");
				if(!oldDateStr.equals(newDateStr)){
					maps.put(key, field.get(newObj));
				}
			} else {
				continue;
			}
		}

		return maps;
	}
	
	/**
	 * 获取两个对象中字段值不一样的描述，如：[昵称]修改为：张三
	 * 创建人：陈登民
	 * 创建时间：2012-12-28 - 上午11:13:38
	 * @param oldObj
	 * @param newObj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 返回类型：String
	 * @exception throws
	 */
	public static String getDiffColumnDescript(Object oldObj, Object newObj, List<String> ignoreFields) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = getFields(oldObj, newObj);
		if(fields==null) return null;
		StringBuffer sb = new StringBuffer();
		String key = null;
		for (Field field : fields) {
			if (field == null) {
				continue;
			}
			if(ignoreFields != null){
				if(ignoreFields.contains(field.getName())){
					continue;
				}
			}
			// 设置字段可访问（必须，否则报错）
			field.setAccessible(true);
			key = field.getName();
			FieldName fieldName = field.getAnnotation(FieldName.class);
			if(fieldName!=null && !"".equals(fieldName.value().trim())){
				key = fieldName.value().trim();
			}
			
			// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
			if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
				if(field.getLong(oldObj)!=field.getLong(newObj)){
					sb.append(",["+key+"]修改为:"+field.getLong(newObj));
				}
			} else if (field.getType().equals(String.class)) {
				if(field.get(oldObj)==null && field.get(newObj)==null){
					continue;
				}
				if(field.get(oldObj)==null && field.get(newObj)!=null){
					sb.append(",["+key+"]修改为:"+field.get(newObj));
					continue;
				}
				if(field.get(oldObj)!=null && field.get(newObj)==null){
					sb.append(",["+key+"]修改为空");
					continue;
				}
				
				if(!field.get(oldObj).toString().equals(field.get(newObj).toString())){
					sb.append(",["+key+"]修改为:"+field.get(newObj));
				}
			} else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) { 
				if(field.getDouble(oldObj)!=field.getDouble(newObj)){
					sb.append(",["+key+"]修改为:"+field.getDouble(newObj));
				}
			} else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
				if(field.getInt(oldObj)!=field.getInt(newObj)){
					sb.append(",["+key+"]修改为:"+field.getInt(newObj));
				}
			} else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
				if(field.getFloat(oldObj)!=field.getFloat(newObj)){
					sb.append(",["+key+"]修改为:"+field.getFloat(newObj));
				}
			} else if (field.getType().equals(java.util.Date.class)) {
				Date oldDate = (Date) field.get(oldObj);
				Date newDate = (Date) field.get(newObj);
				String oldDateStr = DateUtil.parseDate(oldDate, "yyyy-MM-dd HH:mm:ss");
				String newDateStr = DateUtil.parseDate(newDate, "yyyy-MM-dd HH:mm:ss");
				if(!oldDateStr.equals(newDateStr)){
					sb.append(",["+key+"]修改为:"+newDateStr);
				}
			} else {
				continue;
			}
		}
		String tmp = sb.toString();
		if(tmp.startsWith(",") && tmp.length()>=2){
			tmp = tmp.substring(1, tmp.length());
		} else {
			tmp = null;
		}
		
		return tmp;
	}
	
	public static String getDiffColumnDescript(Object oldObj, Object newObj) throws IllegalArgumentException, IllegalAccessException {
		return getDiffColumnDescript(oldObj, newObj, null);
	}
	
	public static void main(String[] args) {
		A a = new A();
		a.setAge(18);
		a.setUsername("张三");
		a.setNickname("凯撒大帝");
		a.setDate(new Date());
		a.setTemp("临时的");
		
		try {
			// 克隆对象a
			A b = (A)clone(a);
			
			b.setAge(19);
			b.setUsername("张三");
			b.setNickname("老佛爷");
			b.setDate(DateUtil.addDay(new Date(), 1));
			
//			Map<String, Object> maps = ObjectUtil.diffColumn(a, b);
//			for (String key : maps.keySet()) {
//				System.out.println(key+" 有变化，变化的值是 "+maps.get(key).toString());
//			}
			String str = getDiffColumnDescript(a, b, null);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.br.ott.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Comparator;

/**
 * 对象升降序排序类
 * 
 * @author lizhenghg 2016-12-2
 * 
 */

public class BaseComparator<T> implements Comparator<T> {

	private Map<String, String> map;

	private Class<?> clazz1;

	private Class<?> clazz2;

	private Method met1 = null;

	private Method met2 = null;

	private Field field = null;

	public BaseComparator(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public int compare(T o1, T o2) {
		return toCompare(o1, o2);
	}

	public int toCompare(Object o1, Object o2) {
		if (map == null)
			return 0;
		clazz1 = o1.getClass();
		clazz2 = o2.getClass();

		StringBuilder builder = new StringBuilder(12);

		String property = null;
		String sequence = null;

		for (Map.Entry<String, String> entry : map.entrySet()) {
			property = entry.getKey();
			sequence = entry.getValue();
		}
		builder.append(property);
		builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
		try {
			met1 = clazz1.getDeclaredMethod("get" + builder.toString(), null);
			met2 = clazz2.getDeclaredMethod("get" + builder.toString(), null);
			met1.setAccessible(true);
			met2.setAccessible(true);

			field = clazz1.getDeclaredField(property);

			Object obj1 = met1.invoke(o1, null);
			Object obj2 = met2.invoke(o2, null);

			BigDecimal big1 = null;
			BigDecimal big2 = null;

			if(obj1 == null && obj2 == null)
				return 0;
			if(obj1 == null && obj2 != null){
				if("asc".equalsIgnoreCase(sequence))
					return -1;
				else
					return 1;
			}
			if(obj1 != null && obj2 == null){
				if("asc".equalsIgnoreCase(sequence))
					return 1;
				else
					return -1;
			}
			
			if (field.getType() == String.class || field.getType() == short.class || field.getType() == Short.class || field.getType() == int.class || field.getType() == Integer.class
					|| field.getType() == long.class || field.getType() == Long.class || field.getType() == float.class || field.getType() == Float.class || field.getType() == double.class || field.getType() == Double.class
					|| field.getType() == java.math.BigDecimal.class) {
				
				if (field.getType() == String.class) {
					big1 = new BigDecimal((String) obj1);
					big2 = new BigDecimal((String) obj2);
				} else if (field.getType() == short.class || field.getType() == Short.class) {
					big1 = new BigDecimal((Short) obj1);
					big2 = new BigDecimal((Short) obj2);
				} else if (field.getType() == int.class || field.getType() == Integer.class) {
					big1 = new BigDecimal((Integer) obj1);
					big2 = new BigDecimal((Integer) obj2);
				} else if (field.getType() == long.class || field.getType() == Long.class) {
					big1 = new BigDecimal((Long) obj1);
					big2 = new BigDecimal((Long) obj2);
				} else if (field.getType() == float.class || field.getType() == Float.class) {
					big1 = new BigDecimal((Float) obj1);
					big2 = new BigDecimal((Float) obj2);
				} else if (field.getType() == double.class || field.getType() == Double.class){
					big1 = new BigDecimal((Double) obj1);
					big2 = new BigDecimal((Double) obj2);
				} else {
					big1 = (BigDecimal)obj1;
					big2 = (BigDecimal)obj2;
				}
				if(big1.compareTo(big2) == 1){
					if("asc".equalsIgnoreCase(sequence))
						return 1;
					else
						return -1;
				}else if(big1.compareTo(big2) == -1){
					if("asc".equalsIgnoreCase(sequence))
						return -1;
					else
						return 1;
				}else{
					return 0;
				}
			} else if (field.getType() == java.sql.Timestamp.class || field.getType() == java.util.Date.class || field.getType() == java.sql.Date.class) {
				
				long l1 = 0l;
				long l2 = 0l;
				
				if (field.getType() == java.sql.Timestamp.class) {
					l1 = ((Timestamp) obj1).getTime();
					l2 = ((Timestamp) obj2).getTime();
				}else if (field.getType() == java.util.Date.class){
					l1 = ((java.util.Date) obj1).getTime();
					l2 = ((java.util.Date) obj2).getTime();
				}else {
					l1 = ((java.sql.Date) obj1).getTime();
					l2 = ((java.sql.Date) obj2).getTime();
				}
				
				if (l1 > l2){
					if("asc".equalsIgnoreCase(sequence))
						return 1;
					else
						return -1;
				}else if(l1 < l2){
					if("asc".equalsIgnoreCase(sequence))
						return -1;
					else
						return 1;
				}else{
					return 0;
				}
			} else {
				return 0;
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
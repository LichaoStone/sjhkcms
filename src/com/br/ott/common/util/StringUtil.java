/*
 * 文 件 名:  StringUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cKF46828
 * 修改时间:  2011-7-5
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.br.ott.common.util;

import it.sauronsoftware.base64.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

/**
 * <字符串操作>
 * 
 * @author cKF46828
 * @version [版本号, 2011-7-5]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class StringUtil {

	// private static LogUtil logUtil = new LogUtil(StringUtil.class);
	private static Logger log = Logger.getLogger(StringUtil.class);

	private StringUtil() {
	}

	/**
	 * 是否为null或空字符串
	 * 
	 * @param str
	 * @return [参数说明]
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为null或空字符串 创建人：pananz 创建时间：2013-1-5 - 上午11:21:15
	 * 
	 * @param str
	 * @return 返回类型：boolean
	 * @exception throws
	 */
	public static boolean isNotEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * <判断是否为手机号码>
	 * 
	 * @param str
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		String reg = "1[3,5,8]{1}\\d{9}";
		return phoneNumber.matches(reg);
	}

	/**
	 * <判断是否是数字>
	 * 
	 * @param str
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNumber(String str) {
		String reg = "[0-9]+";
		return str.matches(reg);
	}

	/**
	 * 字符串转为整数(如果转换失败,则返回 -1)
	 * 
	 * @param str
	 * @return [参数说明]
	 * @return int [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static int stringToInt(String str) {
		if (isEmpty(str)) {
			return -1;
		}
		try {
			return Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * 字体串转为boolean (如果转换失败,则返回false)
	 * 
	 * @param str
	 * @return [参数说明]
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean stringToBoolean(String str) {
		if (isEmpty(str)) {
			return false;
		}
		try {
			return Boolean.parseBoolean(str.trim());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * boolean转为字体串
	 * 
	 * @param str
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String BooleanToString(Boolean bool) {
		String booleanString = "false";
		if (bool) {
			booleanString = "true";
		}
		return booleanString;
	}

	/**
	 * <从异常中获取调用栈>
	 * 
	 * @param ex
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getExceptionStackTrace(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		return result;
	}

	/**
	 * <Unicode转化为中文>
	 * 
	 * @param dataStr
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String decodeUnicode(String dataStr) {
		final StringBuffer buffer = new StringBuffer();
		String tempStr = "";
		String operStr = dataStr;
		if (operStr != null && operStr.indexOf("\\u") == -1) {
			return buffer.append(operStr).toString();
		}
		if (operStr != null && !operStr.equals("")
				&& !operStr.startsWith("\\u")) {
			// tempStr = operStr.substring(0, operStr.indexOf("\\u"));
			tempStr = StringUtil.substring(operStr, 0, operStr.indexOf("\\u"));
			// operStr字符一定是以unicode编码字符打头的字符串
			// operStr = operStr.substring(operStr.indexOf("\\u"),
			// operStr.length());
			operStr = StringUtil.substring(operStr, operStr.indexOf("\\u"),
					operStr.length());
		}
		buffer.append(tempStr);
		// 循环处理,处理对象一定是以unicode编码字符打头的字符串
		while (operStr != null && !operStr.equals("")
				&& operStr.startsWith("\\u")) {
			// tempStr = operStr.substring(0, 6);
			tempStr = StringUtil.substring(operStr, 0, 6);
			// operStr = operStr.substring(6, operStr.length());
			operStr = StringUtil.substring(operStr, 6, operStr.length());
			String charStr = "";
			// charStr = tempStr.substring(2, tempStr.length());
			charStr = StringUtil.substring(tempStr, 2, tempStr.length());
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串
			buffer.append(String.valueOf(letter));
			if (operStr.indexOf("\\u") == -1) {
				buffer.append(operStr);
			} else { // 处理operStr使其打头字符为unicode字符
				// tempStr = operStr.substring(0, operStr.indexOf("\\u"));
				tempStr = StringUtil.substring(operStr, 0,
						operStr.indexOf("\\u"));
				// operStr = operStr.substring(operStr.indexOf("\\u"),
				// operStr.length());
				operStr = StringUtil.substring(operStr, operStr.indexOf("\\u"),
						operStr.length());
				buffer.append(tempStr);
			}
		}
		return buffer.toString();
	}

	/**
	 * 字条串截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String substring(String str, int start, int end) {
		if (isEmpty(str)) {
			return "";
		}
		int len = str.length();
		if (start > end) {
			return "";
		}
		if (start > len) {
			return "";
		}
		if (end > len) {
			return str.substring(start, len);
		}
		return str.substring(start, end);
	}

	/**
	 * 字条串截取
	 * 
	 * @param str
	 *            源字符串
	 * @param start
	 *            开始位置
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String substring(String str, int start) {
		if (isEmpty(str)) {
			return "";
		}
		int len = str.length();
		if (start > len) {
			return "";
		}
		return str.substring(start);
	}

	/**
	 * 流转换成字符串
	 * 
	 * @param input
	 *            [输入流]
	 * @return [参数说明]
	 * @return String [字符串]
	 */
	public static String streamToString(InputStream input) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(input, "UTF-8");
			br = new BufferedReader(isr);
			String temp = br.readLine();
			while (null != temp && !"".equals(temp)) {
				sb.append(temp);
				temp = br.readLine();
			}
		} catch (IOException e) {
			log.error("流转换string错误！", e);
			return "";
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Request转换字符串
	 * 
	 * @param request
	 *            [Request]
	 * @return String [字符串]
	 */
	public static String requestGetStreamToString(HttpServletRequest request) {
		InputStream input = null;
		try {
			input = request.getInputStream();
			return streamToString(input);
		} catch (IOException e) {
			log.error("获得输入流失败！" + e.getMessage());
			return "";
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static long parseLong(String str) {
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int parseInt(String str) {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static float parseFloat(String str) {
		try {
			return Float.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double parseDouble(String str) {
		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String transformBase64(String message) {
		if (isEmpty(message)) {
			return "";
		}
		return Base64.encode(message, "UTF-8");
	}

	public static String decodeformBase64(String message) {
		if (isEmpty(message)) {
			return "";
		}
		return Base64.decode(message, "UTF-8");
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}

		return false;

	}

	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c) == true) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
					System.out.println(c);
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}

	}

	public static String getJsonStr(JSONObject json, String str) {
		return json.get(str) == null ? "" : (String) json.get(str);
	}

	/**
	 * XML流转成对象 TODO 创建人：pananz 创建时间：2015-1-27 - 下午2:30:33
	 * 
	 * @param clazzMap
	 * @param request
	 * @return 返回类型：Object
	 * @exception throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object xml2Bean(Map<String, Class> clazzMap,
			HttpServletRequest request) {
		XStream xstream = new XStream();
		for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
			xstream.alias(m.getKey(), m.getValue());
		}
		String xmlStr = requestGetStreamToString(request);
		log.debug("请求参数：" + xmlStr);
		Object bean = xstream.fromXML(xmlStr);
		return bean;
	}

	/**
	 * XML字符串转成对象 TODO 创建人：pananz 创建时间：2015-1-27 - 下午2:30:54
	 * 
	 * @param clazzMap
	 * @param xml
	 * @return 返回类型：Object
	 * @exception throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object xml2Bean(Map<String, Class> clazzMap, String xml) {
		XStream xstream = new XStream();
		for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
			xstream.alias(m.getKey(), m.getValue());
		}
		Object bean = xstream.fromXML(xml);
		return bean;
	}

	/**
	 * 对象转成XML字符串 TODO 创建人：pananz 创建时间：2015-1-27 - 下午2:31:23
	 * 
	 * @param obj
	 * @return 返回类型：String
	 * @exception throws
	 */
	public static String bean2XML(Object obj) {
		XStream xstream = new XStream();
		return xstream.toXML(obj);
	}

	/**
	 * 取得中文首字母
	 * 
	 * 创建人：pananz 创建时间：2016-10-18
	 * 
	 * @param @param str
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}
}

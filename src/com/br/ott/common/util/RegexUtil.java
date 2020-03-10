package com.br.ott.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式Util
 * 
 * @author micro-k
 * 
 */
public final class RegexUtil {
	public static final String NORMAL = "^[A-Za-z0-9]+$";

	/**
	 * 
	 * @param regEx
	 *            表达式
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean match(String regEx, String str) {
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str);
		return mat.find();
	}

	public static void main(String[] args) {
		System.out.println(match(NORMAL, "dd"));
	}
}

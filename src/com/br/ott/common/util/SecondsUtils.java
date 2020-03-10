package com.br.ott.common.util;

import java.util.Date;

public final class SecondsUtils {

	private static final Long MILLIS_PER_SECOND = 1000L;

	private SecondsUtils() {
	}

	/**
	 * 获取当前时间的秒数.
	 */
	public static Long seconds() {
		return seconds(System.currentTimeMillis());
	}

	/**
	 * 把日期型转换为秒数.
	 */
	public static Long seconds(Date date) {
		return seconds(date.getTime());
	}

	/**
	 * 把毫秒数转换为秒数.
	 */
	public static Long seconds(Long millis) {
		return millis / MILLIS_PER_SECOND;
	}

	public static Long minutes(Long millis) {
		return millis / (MILLIS_PER_SECOND * 60);
	}

	/**
	 * 获取当前时间的毫秒数.
	 */
	public static Long millis() {
		return millis(seconds());
	}

	/**
	 * 把秒数转换为毫秒数.
	 */
	public static Long millis(Long seconds) {
		return seconds * MILLIS_PER_SECOND;
	}

	/**
	 * 把秒数转换为日期型.
	 */
	public static Date date(Long seconds) {
		return new Date(millis(seconds));
	}

	public static Date getDate(Long millis) {
		return new Date(millis);
	}
}

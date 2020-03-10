/*
 * 文 件 名:  DateUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  wKF46829
 * 修改时间:  2011-4-19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.br.ott.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * 日期工具类
 * 
 * @author wKF46829
 * @version [版本号, 2011-4-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class DateUtil {

	private DateUtil() {

	}

	private static SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * @return String
	 */
	public static String getCurrentmin() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	public static String getCurrentH() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		return sdf.format(new Date());
	}

	public static String getCurrentYMD() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * @return String
	 */
	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getCurrentDateTime2() {
		return new Date().getTime() + "";
	}

	/**
	 * @return String
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	/**
	 * @return String
	 */
	public static String parseDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date2 = null;
		try {
			date2 = sdf.parse(date);
		} catch (ParseException e) {
			return "";
		}
		return sdf.format(date2);
	}

	public static String parseDate2(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date2 = null;
		try {
			date2 = sdf.parse(date);
		} catch (ParseException e) {
			return "";
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		return sdf2.format(date2);
	}

	public static String parseDate3(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date2 = null;
		try {
			date2 = sdf.parse(date);
		} catch (ParseException e) {
			return "";
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		return sdf2.format(date2);
	}

	public static Date parseDate(String source, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(source);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String parseDate(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 *            [格式化前的时间]
	 * @return String [格式化后的时间]
	 */
	public static String formatTime(String time) {
		try {
			Date date = new Date(Long.parseLong(time));
			Date currentTime = new Date();
			SimpleDateFormat sdf = null;
			if (getYear(date) == getYear(currentTime)) {
				if (getMonth(date) == getMonth(currentTime)
						&& getDay(date) == getDay(currentTime)) {
					sdf = new SimpleDateFormat("HH:mm");
					return sdf.format(date);
				}
				sdf = new SimpleDateFormat("M月d日");
				return sdf.format(date);
			} else {
				sdf = new SimpleDateFormat("yyyy年M月d日");
				return sdf.format(date);
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 *            [格式化前的时间]
	 * @return String [格式化后的时间]
	 */
	public static String formatTime2(String time) {
		try {
			Date date = new Date(Long.parseLong(time));
			Date currentTime = new Date();
			SimpleDateFormat sdf = null;
			if (getYear(date) == getYear(currentTime)) {
				sdf = new SimpleDateFormat("M月d日 HH:mm");
				return sdf.format(date);
			} else {
				sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm");
				return sdf.format(date);
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 从指定字符串解析时间格式获取当月总共天数
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthMaxDay(String date) throws ParseException {
		return getMonthMaxDay(formatDate(date));
	}

	/**
	 * 从指定时间中获取当月总共天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonthMaxDay(Date date) {
		Calendar c = setCalendar(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 从指定时间中获取年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		return setCalendar(date).get(Calendar.YEAR);
	}

	/**
	 * 从指定时间中获取月份
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		return setCalendar(date).get(Calendar.MONTH) + 1;
	}

	/**
	 * 从指定时间中获取日期
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		return setCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 从指定时间中获取当前星期几
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		return setCalendar(date).get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 从指定时间中获取小时
	 * 
	 * @param date
	 * @return
	 */
	public static int getHours(Date date) {
		return setCalendar(date).get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 从指定时间中获取分钟
	 * 
	 * @param date
	 * @return
	 */
	public static int getMinutes(Date date) {
		return setCalendar(date).get(Calendar.MINUTE);
	}

	/**
	 * 从指定时间中获取秒钟
	 * 
	 * @param date
	 * @return
	 */
	public static int getSeconds(Date date) {
		return setCalendar(date).get(Calendar.SECOND);
	}

	/**
	 * 获取指定时间后的星期几的时间
	 * 
	 * @param date
	 *            指定时间
	 * @param num
	 *            星期几
	 * @return
	 */
	public static Date getWeekByNum(Date date, int num) {
		Calendar c = setCalendar(date);
		c.set(Calendar.DAY_OF_WEEK, num);
		return c.getTime();
	}

	/**
	 * 设置日历时间
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar setCalendar(Date date) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		return c;
	}

	public static Date setCalendar(Date date, int day) {
		Calendar c = setCalendar(date);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	/**
	 * 传入指定时间,添加指定天数
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date addDay(Date date, int num) {
		Calendar c = setCalendar(date);
		c.add(Calendar.DAY_OF_MONTH, num);
		return c.getTime();
	}
	
	public static String getCurrentHHmm(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		return sdf.format(date);
	}
	
	/**
	 * 传入指定时间,添加指定月份
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date addMonth(Date date, int num) {
		Calendar c = setCalendar(date);
		c.add(Calendar.MONTH, num);
		return c.getTime();
	}

	/**
	 * 设置时间的时分秒
	 * 
	 * @param date
	 *            指定时间
	 * @param hour
	 *            小时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @return
	 */
	public static Date setTime(Date date, int hour, int minute, int second) {
		Calendar c = setCalendar(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return c.getTime();
	}

	public static Date set(Date date, int pattern, int param) {
		Calendar c = setCalendar(date);
		c.set(pattern, param);
		return c.getTime();
	}

	/**
	 * 将字符串类型转换成指定格式的日期类型
	 * 
	 * @param date
	 *            指定日期字符串
	 * @param format
	 *            指定格式
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String date, String pattern)
			throws ParseException {
		if (null == pattern || "".equals(pattern)) {
			return formatDate(date);
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		return sdf.parse(date);
	}

	/**
	 * 根据默认格式转换日期格式
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}

	/**
	 * 将OBJECT转换为DATE类型
	 * 
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(Object obj) throws ParseException {
		if (obj instanceof Date) {
			return (Date) obj;
		}
		throw new ParseException("convert is error", 1);
	}

	/**
	 * 将DATE类型转换为指定的字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将日期类型转换字符串
	 * 
	 * @param date
	 * @param pattern
	 *            'yyyy-MM-dd HH:mm:ss'
	 * @return
	 */
	public static String toString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static Date getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(getCurrentDateTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按指定规则进行添加
	 * 
	 * @param date
	 * @param field
	 * @param num
	 * @return
	 */
	public static Date add(Date date, int field, int num) {
		Calendar c = setCalendar(date);
		c.add(field, num);
		return c.getTime();
	}

	/**
	 * 取得当前日期是多少周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

		return getWeekOfYear(c.getTime());
	}

	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);

		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 取得指定日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 取得当前时间的上一个月第一天开始时间 创建人：pananz 创建时间：2012-12-24 - 上午11:27:27
	 * 
	 * @return 返回类型：Date
	 * @exception throws
	 */
	public static Date getFirstDateOfLastMonth() {
		Date lastMonth = DateUtil.addMonth(new Date(), -1);
		Date lastDate = DateUtil.setCalendar(lastMonth, 1);
		return DateUtil.setTime(lastDate, 0, 0, 0);
	}

	/**
	 * 取得当前时间的上一个月最后一天结尾时间 创建人：pananz 创建时间：2012-12-24 - 上午11:27:41
	 * 
	 * @return 返回类型：Date
	 * @exception throws
	 */
	public static Date getLastDateOfLastMonth() {
		Date lastMonth = DateUtil.addMonth(new Date(), -1);
		int max = DateUtil.getMonthMaxDay(lastMonth);
		Date lastDate = DateUtil.setCalendar(lastMonth, max);
		return DateUtil.setTime(lastDate, 23, 59, 59);
	}

	/**
	 * 取得当前时间的前一天开始时间 创建人：pananz 创建时间：2012-12-24 - 上午11:33:55
	 * 
	 * @return 返回类型：Date
	 * @exception throws
	 */
	public static Date getFirstDateOfLastDate() {
		Date date = DateUtil.addDay(new Date(), -1);
		return DateUtil.setTime(date, 0, 0, 0);
	}

	/**
	 * 取得当前时间的前一天结尾时间 创建人：pananz 创建时间：2012-12-24 - 上午11:34:54
	 * 
	 * @return 返回类型：Date
	 * @exception throws
	 */
	public static Date getLastDateOfLastDate() {
		Date date = DateUtil.addDay(new Date(), -1);
		return DateUtil.setTime(date, 23, 59, 59);
	}

	/**
	 * 取得当前时间的前一个小时开始时间 创建人：pananz 创建时间：2012-12-24 - 上午11:37:41
	 * 
	 * @return 返回类型：Date
	 * @exception throws
	 */
	public static Date getFirstDateOfLastHour() {
		Date now = new Date();
		int hour = DateUtil.getHours(now) - 1;
		Date date = null;
		if (hour == -1) {// 0时，计算前一天的最后一个小时的数据
			date = DateUtil.addDay(now, -1);
			hour = 23;
		} else {
			date = now;
		}
		return DateUtil.setTime(date, hour, 0, 0);
	}

	/**
	 * 取得当前时间的前一个小时结尾时间 创建人：pananz 创建时间：2012-12-24 - 上午11:38:07
	 * 
	 * @return 返回类型：Date
	 * @exception throws
	 */
	public static Date getLastDateOfLastHour() {
		Date now = new Date();
		int hour = DateUtil.getHours(now) - 1;
		Date date = null;
		if (hour == -1) {// 0时，计算前一天的最后一个小时的数据
			date = DateUtil.addDay(now, -1);
			hour = 23;
		} else {
			date = now;
		}
		return DateUtil.setTime(date, hour, 59, 59);
	}

	public static long dateDiff(Date dateStart, Date dateEnd) {
		return dateEnd.getTime() - dateStart.getTime();
	}
	
	/**
	 * 获取当前时间戳
	 * 创建人：lizhenghg
	 * 创建时间：2016-11-08 - 上午11:38:07
	 * @return
	 * 返回类型：String
	 * @exception throws
	 */
	public static final synchronized String getTimeSequence()
	{
		return getTimeSequence(20);
	}
	
	public static final String getTimeSequence(int pLength)
	{
		int tmpLength = pLength;
		if(tmpLength < 17)
			tmpLength = 17;
		if(tmpLength > 20)
			tmpLength = 20;
		String temp = formater.format(new Date());
		return temp + randomNum(tmpLength - 17);
	}
	
	public static String randomNum(int length)
	{
		StringBuffer suf = new StringBuffer();
		int i = 0;
		for(; i < length; i++){
			int t = (int)(Math.random()*10.0D);
			suf.append(String.valueOf(t));
		}
		return suf.toString();
	}
}

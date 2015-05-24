/**
 * 
 */
package com.yuhen.saleapp.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 日期处理工具类
 * @author ps
 * 
 */
public class DateUtil {
	/**
	 * 获取当前日期，默认格式为yyyyMMdd
	 * 
	 * @return 当前日期
	 */
	public static String today() {
		return today("yyyyMMdd");
	}

	/**
	 * 获取当前日期
	 * 
	 * @param format
	 *            日期格式
	 * @return 当前日期
	 */
	public static String today(String format) {
		return DateFormatUtils.format(Calendar.getInstance(), format);
	}

	/**
	 * 获取当前日期时间，默认格式为yyyyMMddHHmmss
	 * 
	 * @return 当前日期时间
	 */
	public static String currentTime() {
		return today("yyyyMMddHHmmss");
	}

	/**
	 * 封装{DateUtils.parseDate}方法
	 * 
	 * @param str
	 *            转换的日期字符串，不能为空
	 * @param parsePatterns
	 *            日期格式
	 * @return
	 */
	public static Date parseDate(String str, String[] parsePatterns) {
		Date date = null;
		try {
			date = DateUtils.parseDate(str, parsePatterns);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}
}

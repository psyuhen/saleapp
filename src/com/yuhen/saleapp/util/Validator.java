/**
 * 
 */
package com.yuhen.saleapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 客户端检验类
 * 
 * @author ps
 * 
 */
public class Validator {
	/**
	 * 校验密码是否为数字、字母、下划线
	 * 
	 * @param pwd
	 * @return
	 */
	public static boolean isPassword(String pwd) {
		String regex = "^[A-Za-z]\\w";
		return match(regex, pwd);
	}

	/**
	 * 校验密码长度为6到20位
	 * 
	 * @param pwd
	 * @return
	 */
	public static boolean isPwdLength(String pwd) {
		String regex = "^.{6,20}$";
		return match(regex, pwd);
	}

	/**
	 * 检验手机号码
	 * @param mobile
	 * @return
	 */
	public static boolean isMoblie(String mobile) {
		String regex = "^0?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
		return match(regex, mobile);
	}
	
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.lookingAt();
	}
}

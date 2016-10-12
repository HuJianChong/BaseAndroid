package com.hjc.baselibrary.utils;

import android.text.TextUtils;

/***
 *@author Johnny Zou
 *@link kunlun.zou@inveno.cn
 *@date 2015年9月24日
 *@Description: 字符过滤
 **/

public class StringFilter {
	
	/**
	 * 
	 * @Description: 过滤非数字
	 * @param number
	 * @return String
	 * @throws
	 */
	public static String filterNumber(String number) {
		if (TextUtils.isEmpty(number)) {
		}
		
        number = number.replaceAll("[^(0-9)]", "");
        return number;
    }
	
	/**
	 * 
	 * @Description: 过滤非字母
	 * @param alph
	 * @return String
	 * @throws
	 */
	public static String filterAlphabet(String alph) {
		if (TextUtils.isEmpty(alph)) {
		}
		
        alph = alph.replaceAll("[^(A-Za-z)]", "");
        return alph;
    }
	
	/**
	 * 
	 * @Description: 过滤非中文
	 * @param chin
	 * @return String
	 * @throws
	 */
	public static String filterChinese(String chin) {
		if (TextUtils.isEmpty(chin)) {
		}
		
        chin = chin.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
        return chin;
    }
	
	/**
	 * 
	 * @Description: 过滤非字母、数字、中文
	 * @param character
	 * @return String
	 * @throws
	 */
	public static String filterAlphNumChiese(String character){
		if (TextUtils.isEmpty(character)) {
		}
			
        character = character.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
        return character;
	}
}

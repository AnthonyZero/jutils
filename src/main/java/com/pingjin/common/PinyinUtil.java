package com.pingjin.common;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * 中文转英文
 * @author pingjin create 2018年4月11日
 *
 */
public class PinyinUtil {
	
	/**
	 * 汉字转拼音，英文字符不变
	 * 
	 * @param name 汉字
	 * @return 拼音
	 */
	public static String hanyuToPinyin(String name) {
		StringBuffer pybf = new StringBuffer();
		char[] nameChar = name.toCharArray();

		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		for (int i = 0; i < nameChar.length; i++) {
			try {
				if (nameChar[i] > 128) {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
				} else {
					pybf.append(nameChar[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pybf.toString();
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * 
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					} else {
						pybf.append(arr[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPinYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);//

		char[] input = inputString.trim().toCharArray();
		StringBuffer output = new StringBuffer("");

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					output.append(temp[0]);
				} else {
					output.append(input[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
}

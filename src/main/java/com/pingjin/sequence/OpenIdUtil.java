/**
 * 
 */
package com.pingjin.sequence;

import java.io.UnsupportedEncodingException;
import java.util.Stack;

import com.pingjin.encrypt.Md5Util;
import com.pingjin.random.UuidUtil;


/**
 * 生成openId
 * @author pingjin create 2018年4月11日
 *
 */
public class OpenIdUtil {
	private static final char[] S_BASE62CHAR = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9' };
	private static final char[] S_BASE36CHAR = { 'A', 'B', 'C', 'D', 'E', 'F',
		'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
		'T', 'U', 'V', 'W', 'X', 'Y', 'Z',  '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9' };

	/**
	 * 初始OpenId
	 * 
	 * @return
	 */
	public static String getInitOpenId() {
		return UuidUtil.randomUUID();
	}

	/**
	 * 生成OpenId
	 * 
	 * @param initOpenId
	 * @param memId
	 * @return
	 */
	public static String buildOpenId(String initOpenId, Long memId) {
		return initOpenId + getMD5(getOpenIdMD5Key(initOpenId, memId));
	}

	/**
	 * OpenId MD5 Key
	 * 
	 * @param openId
	 * @param memId
	 * @return
	 */
	private static String getOpenIdMD5Key(String openId, Long memId) {
		return openId + "-" + memId + "-" + System.currentTimeMillis();
	}

	/**
	 * 获取MD5
	 * 
	 * @param key
	 * @return
	 */
	public static String getMD5(String key) {
		try {
			return Md5Util.getMd5Hexs(Md5Util.digest(key.getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转换为62进制
	 * 
	 * @param number
	 * @return
	 */
	public static String to62String(long number) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(S_BASE62CHAR[new Long((rest - (rest / 62) * 62))
					.intValue()]);
			rest = rest / 62;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.toString();

	}

	/**
	 * 转换为36进制
	 * 
	 * @param number
	 * @return
	 */
	public static String to36String(long number) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(S_BASE36CHAR[new Long((rest - (rest / 36) * 36))
					.intValue()]);
			rest = rest / 36;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.toString();

	}

	/**
	 * 62进制转为10进制
	 * 
	 * @param sixty_str
	 * @return
	 */
	public static long to10long62(String sixty_str) {
		long multiple = 1;
		long result = 0;
		Character c;
		for (int i = 0; i < sixty_str.length(); i++) {
			c = sixty_str.charAt(sixty_str.length() - i - 1);
			result += _62_value(c) * multiple;
			multiple = multiple * 62;
		}
		return result;
	}

	private static int _62_value(Character c) {
		for (int i = 0; i < S_BASE62CHAR.length; i++) {
			if (c == S_BASE62CHAR[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 36进制转为10进制
	 * 
	 * @param sixty_str
	 * @return
	 */
	public static long to10long36(String sixty_str) {
		long multiple = 1;
		long result = 0;
		Character c;
		for (int i = 0; i < sixty_str.length(); i++) {
			c = sixty_str.charAt(sixty_str.length() - i - 1);
			result += _36_value(c) * multiple;
			multiple = multiple * 36;
		}
		return result;
	}

	private static int _36_value(Character c) {
		for (int i = 0; i < S_BASE36CHAR.length; i++) {
			if (c == S_BASE36CHAR[i]) {
				return i;
			}
		}
		return -1;
	}
	
	public static void main(String[] agrs) {
		String openId = buildOpenId(getInitOpenId(), 43154314321L);
		System.out.println(openId);
		System.out.println(openId.length());
	}
}

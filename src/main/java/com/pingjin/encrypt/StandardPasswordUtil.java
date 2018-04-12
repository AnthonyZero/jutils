/**
  *
 *
 */
package com.pingjin.encrypt;

import com.pingjin.common.StringUtil;

/**
 * 经典加密、解密工具
 * @author pingjin create 2018年4月11日
 *
 */
public class StandardPasswordUtil {
	private static final String coder = "ZX1~CV!3B@N5MA#2S$D4F%G6HJ8K&L0*QW(0E)R_7TYU\\I|OP}qw{ert]yu-i[opa/sd?fg>hjk<lzxcvbnm+Z";

	/**
	 * 加密算法
	 * 倒数循环需要加密的字符串，碰到与coder相同的字符 取序号后一位为加密之后的字符
	 * 最后将得到字符串 appendOddEven函数处理 得到最后的加密字符串
	 * @param plaintext
	 * @return
	 */
	public static String encrypt(String plaintext) {
		if (StringUtil.hasText(plaintext)) {
			StringBuffer result = new StringBuffer();
			int length = plaintext.length();
			for (int i = length - 1; i > -1; i--) {
				char subPlaitext = plaintext.charAt(i);
				int coderLength = coder.length();
				for (int j = 0; j < coderLength; j++) {
					char subCoder = coder.charAt(j);
					if (subPlaitext == subCoder) {
						subPlaitext = coder.charAt(j + 1);
						break;
					}
				}
				result.append(subPlaitext);
			}
			return appendOddEven(result.toString());
		}

		return "";
	}

	/**
	 * 解密算法
	 * 
	 * @param ciphertext
	 * @return
	 */
	public static String decrypt(String ciphertext) {
		if (StringUtil.hasText(ciphertext)) {
			String decrypt = appendOddEven(ciphertext);
			StringBuffer result = new StringBuffer();
			for (int i = decrypt.length() - 1; i > -1; i--) {
				char subDecrypt = decrypt.charAt(i);
				for (int j = 0; j < coder.length(); j++) {
					char subCoder = coder.charAt(j);
					if (subDecrypt == subCoder) {
						subDecrypt = coder.charAt(j - 1);
						break;
					}
				}
				result.append(subDecrypt);
			}
			return result.toString();
		}
		
		return "";
	}
	
	private static String appendOddEven(String resouce) {
		if (StringUtil.hasText(resouce)) {
			StringBuffer encrypt = new StringBuffer();
			for (int i = 1; i < resouce.length(); i = i + 2) {
				encrypt.append(resouce.charAt(i)).append(resouce.charAt(i - 1));
			}
			if ((resouce.length() & 1) > 0) {
				encrypt.append(resouce.charAt(resouce.length() - 1));
			}
			return encrypt.toString();
		}
		
		return "";
	}
	
	public static void main(String[] args) {
		String str = "ad35s43c$898`@qw.ds**2";
		String s = encrypt(str);
		System.out.println(s);
		System.out.println(decrypt(s));
		System.out.println(decrypt(s).equals(str));
	}
}

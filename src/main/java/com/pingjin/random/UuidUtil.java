package com.pingjin.random;

import java.util.UUID;

/**
 * 生成UUID
 * @author pingjin create 2018年4月11日
 *
 */
public class UuidUtil {

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static final String randomUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "").toUpperCase();
		return uuid;
	}

}

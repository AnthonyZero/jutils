package com.pingjin.sequence;

import java.util.HashMap;
import java.util.Map;

import com.pingjin.common.SnowflakeIdWorker;

/**
 * 生成唯一ID
 * @author pingjin create 2018年4月11日
 *
 */
public class SequenceUtil {

	private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

	/**
	 * 增加sequenceId值
	 * 
	 * @return
	 */
	public static long getSeqId() {
		return idWorker.nextId();
	}

	public static void main(String[] args) {
        Map<Long,Object> map = new HashMap<>();
        for (int i = 0; i < 10000000; i++){
        	map.put(getSeqId(), "value");
        }
        System.out.println(map.size());
	}
}

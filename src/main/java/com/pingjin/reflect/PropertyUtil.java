package com.pingjin.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用反射 操作类的成员变量
 * @author pingjin create 2018年5月14日
 *
 */
public class PropertyUtil {
	
	/**
	 * 获取对象的所有属性成员及值
	 * @param obj
	 * @return
	 */
	public static Map<String,String> getKeyAndValue(Object obj) {
		Map<String,String> map = new HashMap<>();
		List<Field> fields = new ArrayList<>() ;
		Class<?> cla = obj.getClass();
		while (cla != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
			fields.addAll(Arrays.asList(cla.getDeclaredFields()));
		    cla = cla.getSuperclass(); //得到父类,然后赋给自己
		}
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			field.setAccessible(true);
			Object val = new Object();
			String fieldName = field.getName();
            try {
                val = field.get(obj);   
                if (val == null) {
                	map.put(fieldName, "");
                } else {
                	// 得到此属性的值
                    map.put(fieldName, val.toString());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
		}
		return map;
	}
}

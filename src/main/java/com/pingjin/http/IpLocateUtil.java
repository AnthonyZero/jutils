package com.pingjin.http;

import com.pingjin.http.qqwry.IPZone;
import com.pingjin.http.qqwry.QQWry;

import java.io.IOException;

public class IpLocateUtil {

    private static QQWry QQ_WRY;

    static {
        try {
            QQ_WRY = new QQWry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProvince(String ip) {
        if (QQ_WRY == null) {
            return null;
        }
        IPZone ipzone = QQ_WRY.findIP(ip);
        String mainInfo = ipzone.getMainInfo();
        if (mainInfo.contains("省")) {
            return mainInfo.split("省")[0];
        } else {
            if (mainInfo.contains("北京")) {
                return "北京";
            } else if (mainInfo.contains("天津")) {
                return "天津";
            } else if (mainInfo.contains("上海")) {
                return "上海";
            } else if (mainInfo.contains("重庆")) {
                return "重庆";
            } else if (mainInfo.contains("广西")) {
                return "广西";
            } else if (mainInfo.contains("内蒙古")) {
                return "内蒙古";
            } else if (mainInfo.contains("新疆")) {
                return "新疆";
            } else if (mainInfo.contains("宁夏")) {
                return "宁夏";
            } else if (mainInfo.contains("西藏")) {
                return "西藏";
            } else {
                return "";
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(getProvince("113.13.72.218"));
    }
}

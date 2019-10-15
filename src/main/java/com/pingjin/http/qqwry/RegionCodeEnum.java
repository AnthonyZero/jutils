package com.pingjin.http.qqwry;

/**
 * 行政区域编码
 */
public enum RegionCodeEnum {

    BeiJing(11, "北京"),
    TianJin(12, "天津"),
    ;

    private int code;
    private String name;
    RegionCodeEnum(int code, String name) {
        this.code = code;
        this.name =  name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCode(String name) {
        RegionCodeEnum[] codeEnums = RegionCodeEnum.values();
        for (RegionCodeEnum codeEnum : codeEnums) {
            if (codeEnum.name.equals(name)) {
                return codeEnum.code;
            }
        }
        return 0;
    }
}

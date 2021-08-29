package com.pingjin.model;

import lombok.Data;

@Data
public class TypedID {
    /**
     * 类型 symbol tag
     */
    private String type;
    /**
     * ID
     */
    private String id;
}

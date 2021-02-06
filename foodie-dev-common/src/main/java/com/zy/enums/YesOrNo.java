package com.zy.enums;

/**
 * 是否枚举
 */
public enum YesOrNo {
    YES(1,"是"),
    NO(-1,"否");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}

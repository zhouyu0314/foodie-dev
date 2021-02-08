package com.zy.enums;

/**
 * 商品评价等级枚举
 */
public enum CommentLevel {
    GOOD(1,"好评"),
    NORMAL(2,"中评"),
    BAD(3,"差评");

    public final Integer type;
    public final String vale;

    CommentLevel(Integer type, String vale) {
        this.type = type;
        this.vale = vale;
    }
}

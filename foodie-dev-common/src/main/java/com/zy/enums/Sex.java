package com.zy.enums;

public enum Sex {
    MALE(1, "男"),
    FEMALE(2, "女"),
    SECRET(3, "保密");

    public final Integer type;
    public final String vale;

    Sex(Integer type, String vale) {
        this.type = type;
        this.vale = vale;
    }
}

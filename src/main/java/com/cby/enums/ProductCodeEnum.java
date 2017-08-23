package com.cby.enums;

/**
 * Created by Ma on 2017/6/1.
 */
public enum ProductCodeEnum {
//    短期公共交通
    PRODUCT_01(01,"PC0000000163"),
//    特定意外
    PRODUCT_02(02,"PC0000000238"),

    ;

    private Integer code;
    private String message;

    ProductCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

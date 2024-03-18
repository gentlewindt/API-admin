package com.gentlewind.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author gentlewind
 */

// 定义一个泛型类BaseResponse，用于封装返回的数据
@Data
public class BaseResponse<T> implements Serializable {

    // 定义一个私有的int类型的code属性，用于封装返回的状态码
    private int code;

    // 定义一个私有的泛型T类型的data属性，用于封装返回的数据
    private T data;

    // 定义一个私有的String类型的message属性，用于封装返回的消息
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

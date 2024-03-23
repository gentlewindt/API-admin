package com.gentlewind.sdk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息表
 *
 * @TableName interfaceinfo
 */
@Data
public class InterfaceInfo implements Serializable {

    private static final long serialVersionUID = -6629454121892810535L;
    /**
     * id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0 关闭 1 开启）
     */
    private Integer status;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 创建人姓名
     */
    private String userName;

    /**
     * 请求参数
     */
    private String requestParams;
}
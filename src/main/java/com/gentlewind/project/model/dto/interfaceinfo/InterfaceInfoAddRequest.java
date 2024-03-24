package com.gentlewind.project.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;


/**
 * 创建请求
 *
 * @TableName product
 */

// 添加接口请求
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 接口名
     */
    private String name;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;


    /**
     * 请求头
     */
    private String requestHeader;


    /**
     * 响应头
     */
    private String responseHeader;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
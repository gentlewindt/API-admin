package com.gentlewind.project.model.dto.post;

import com.gentlewind.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author gentlewind
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {

    // 用户可能根据哪些字段进行查询
    /**
     * 主键
     */
    private Long id;

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
     * 接口状态{0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求头
     */
    private String requestHeader;


    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 用户名
     */
    private String userId;
}
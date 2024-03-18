package com.gentlewind.project.model.dto.post;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class PostUpdateRequest implements Serializable {

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
     * 更新时间
     */
    private Date update_time;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer is_deleted;

    /**
     * 用户名
     */
    private String userId;
}
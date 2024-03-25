package com.gentlewind.project.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gentlewind.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author gentlewind
 */

// 查询接口请求
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 调用总次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
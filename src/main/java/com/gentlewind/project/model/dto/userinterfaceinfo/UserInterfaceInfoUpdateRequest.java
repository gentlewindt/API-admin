package com.gentlewind.project.model.dto.userinterfaceinfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */

// 更新接口请求
@Data
@Slf4j
public class UserInterfaceInfoUpdateRequest implements Serializable {
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
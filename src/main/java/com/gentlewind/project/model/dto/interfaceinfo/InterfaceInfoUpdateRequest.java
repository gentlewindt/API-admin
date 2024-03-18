package com.gentlewind.project.model.dto.interfaceinfo;
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
public class InterfaceInfoUpdateRequest implements Serializable {
    // 哪些是用户可能修改的字段？
    /**
     * 主键
     */
    private Long id;//需要知道是哪个接口，保留

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




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
package com.gentlewind.project.model.dto.interfaceinfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 接口调用请求
 *
 * @TableName product
 */

// 更新接口请求
@Data
@Slf4j
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;//需要知道是哪个接口，保留



    /**
     * 用户的请求参数
     */
    private String userRequestParams;



    private static final long serialVersionUID = 1L;

}
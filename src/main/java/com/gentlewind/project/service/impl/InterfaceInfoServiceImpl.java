package com.gentlewind.project.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gentlewind.apicommon.model.entity.InterfaceInfo;
import com.gentlewind.project.common.ErrorCode;
import com.gentlewind.project.exception.BusinessException;
import com.gentlewind.project.mapper.InterfaceInfoMapper;
import com.gentlewind.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author gentlewindli
 * @description 针对表的数据库操作Service实现
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {  //add : 是否是新增

        // 判断接口信息对象是否为空，为空则抛出参数错误的异常
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 下载generateallsetter插件，alt+enter InterfaceInfo可以生成所有的get方法
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName(); // 获取接口信息对象InterfaceInfo的name属性（即名称）
        String method = interfaceInfo.getMethod();
        String description = interfaceInfo.getDescription();
        Integer status = interfaceInfo.getStatus();
        String requestHeader = interfaceInfo.getRequestHeader();
        Date update_time = interfaceInfo.getUpdate_time();
        String responseHeader = interfaceInfo.getResponseHeader();
        Date create_time = interfaceInfo.getCreate_time();
        Integer is_deleted = interfaceInfo.getIs_deleted();
        String userId = interfaceInfo.getUserId();
        // 创建时，所有参数必须非空

        // 如果是添加操作，判断接口信息对象的属性是否为空，为空则抛出参数错误的异常
        if(add){
            if(StringUtils.isAnyBlank(name, method, description, requestHeader, responseHeader, userId)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
             }
            }

        // 如果接口信息对象的名称不为空且长度大于50，抛出参数错误的异常
        if(StringUtils.isNotBlank(name) && name.length() > 50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }

        }
}





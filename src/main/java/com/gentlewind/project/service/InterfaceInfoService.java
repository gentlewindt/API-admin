package com.gentlewind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gentlewind.apicommon.model.entity.InterfaceInfo;

/**
* @author gentlewind
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-03-16 14:05:07
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) ;

}

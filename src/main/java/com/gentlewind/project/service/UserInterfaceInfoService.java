package com.gentlewind.project.service;


import com.gentlewind.project.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gentlewind
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-03-24 21:47:29
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) ;

}

package com.gentlewind.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gentlewind.project.common.ErrorCode;
import com.gentlewind.project.exception.BusinessException;
import com.gentlewind.project.model.entity.InterfaceInfo;
import com.gentlewind.project.model.entity.UserInterfaceInfo;
import com.gentlewind.project.service.UserInterfaceInfoService;
import com.gentlewind.project.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 22939
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2024-03-24 21:47:29
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userinterfaceInfo, boolean add) {  //add : 是否是新增

        // 判断接口信息对象是否为空，为空则抛出参数错误的异常
        if (userinterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 如果是添加操作
        if(add){
            if(userinterfaceInfo.getInterfaceInfoId()<=0 || userinterfaceInfo.getUserId()<=0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }

        if(userinterfaceInfo.getLeftNum()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }

    }

}





package com.gentlewind.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        // 判断
        if(interfaceInfoId <= 0 || userId <=0 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 使用UpdateWrapper对象,根据指定的条件来更新表中的记录。
        // 创建了一个 UpdateWrapper 对象，用于构建更新操作的条件。泛型参数 UserInterfaceInfo 指定了更新的目标表。
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        // 通过 eq 方法设置了更新条件，即 interfaceInfoId 字段等于给定的 interfaceInfoId 变量的值。
        // "interfaceInfoId"：表示要匹配的数据库字段名，即表中的列名。
        // interfaceInfoId：表示要匹配的值，即要更新的记录的 interfaceInfoId 字段的值。
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        // setSql 方法用于设置要更新的 SQL 语句。这里通过 SQL 表达式实现了两个字段的更新操作：
        updateWrapper.setSql("leftNum = leftNum -1 , totalNum = totalNum +1 ");
        // 最后，调用update方法执行更新操作，并返回更新是否成功的结果
        return this.update(updateWrapper);

        // TODO: 2024/3/25 添加事务与锁

    }

}





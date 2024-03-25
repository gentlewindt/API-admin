package com.gentlewind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gentlewind.project.annotation.AuthCheck;
import com.gentlewind.project.common.*;
import com.gentlewind.project.constant.CommonConstant;
import com.gentlewind.project.exception.BusinessException;
import com.gentlewind.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.gentlewind.project.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.gentlewind.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.gentlewind.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.gentlewind.project.model.entity.InterfaceInfo;
import com.gentlewind.project.model.entity.User;
import com.gentlewind.project.model.enums.InterfaceInfoStatusEnum;
import com.gentlewind.project.service.InterfaceInfoService;
import com.gentlewind.project.service.UserService;
import com.gentlewind.sdk.client.ApiClient;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口管理
 *
 * @author gentlewind
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private ApiClient apiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String content = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    // endregion


    /**
     * 发布接口
     *
     * @param
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin") //权限校验切面注解
    // HttpServletRequest：客户端的 HTTP 请求,通过它可以获取请求的各种信息，包括请求头、请求参数、请求体等，以便在服务器端进行处理
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        if(idRequest == null || idRequest.getId() <= 0 ){ //  如果id为null或小于等于0
            throw new BusinessException(ErrorCode.PARAMS_ERROR); // 抛出业务异常，表示请求参数错误
        }

        // 1. 校验接口是否存在
        long id = idRequest.getId(); // 获取idRequest对象的id
        InterfaceInfo interfaceInfo  = interfaceInfoService.getById(id); // 根据id查询接口信息的数据
        if(interfaceInfo == null ){ // 如果查询结果为空
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR); // 抛出业务异常，表示未找到数据
        }

        // 2. 判断接口是否可调用
        com.gentlewind.sdk.model.User user = new com.gentlewind.sdk.model.User(); // 创建一个User对象（模拟一个用户）
        user.setUsername("test"); // 设置User对象的username属性为test
        String username = ApiClient.getUserNameByPost(user); // 通过getUserNameByPost方法传入user的username属性
        if(StringUtils.isBlank(username)){  // 如果username为空或空白字符串
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败"); // 抛出系统错误的业务异常，表示系统内部异常，并附带错误信息"接口验证失败"
        }

        // 3. 操作数据库
        InterfaceInfo interfaceInfo1 = new InterfaceInfo();
        interfaceInfo1.setId(id);
        interfaceInfo1.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue()); // 修改接口数据库中的状态字段为上线
        boolean result = interfaceInfoService.updateById(interfaceInfo1); // 调用interfaceInfoService的updateById方法，传入interfaceInfo对象，并将返回的结果赋值给result变量
        return ResultUtils.success(result); // 返回一个成功的响应，响应体中携带result值

    }
    /**
     * 下线接口
     *
     * @param
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        // 1. 校验接口是否存在
        long id = idRequest.getId(); // 获取idRequest对象的id
        InterfaceInfo interfaceInfo  = interfaceInfoService.getById(id); // 根据id查询接口信息的数据
        if(interfaceInfo == null ){ // 如果查询结果为空
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR); // 抛出业务异常，表示未找到数据
        }

        // 2. 判断接口是否可调用
        com.gentlewind.sdk.model.User user = new com.gentlewind.sdk.model.User(); // 创建一个User对象（模拟一个用户）
        user.setUsername("test"); // 设置User对象的username属性为test
        String username = ApiClient.getUserNameByPost(user); // 通过getUserNameByPost方法传入user的username属性
        if(StringUtils.isBlank(username)){  // 如果username为空或空白字符串
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败"); // 抛出系统错误的业务异常，表示系统内部异常，并附带错误信息"接口验证失败"
        }

        // 3. 操作数据库
        InterfaceInfo interfaceInfo1 = new InterfaceInfo();
        interfaceInfo1.setId(id);
        interfaceInfo1.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue()); // 修改接口数据库中的状态字段为上线
        boolean result = interfaceInfoService.updateById(interfaceInfo1); // 调用interfaceInfoService的updateById方法，传入interfaceInfo对象，并将返回的结果赋值给result变量
        return ResultUtils.success(result); // 返回一个成功的响应，响应体中携带result值

    }



    /**
     * 测试调用接口
     *
     * @param
     * @return
     */
    @PostMapping("/invoke")
    // 以为不确定返回信息，返回一个对象好了
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) {
        if(interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <=0 ){ // 如果查询结果为空
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR); // 抛出业务异常，表示未找到数据
        }

        // 获取数据
        long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();

        // 判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null ){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        if(interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }

        // 获取当前登录用户的ak和sk，这样相当于用户自己的这个身份去调用，
        // 也不会担心它刷接口，因为知道是谁刷了这个接口，会比较安全
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 我们只需要进行测试调用，所以我们需要解析传递过来的参数。
        Gson gson = new Gson();
        //使用Gson的方法将请求参数转换为User对象
        com.gentlewind.sdk.model.User user = gson.fromJson(userRequestParams,com.gentlewind.sdk.model.User.class);
        // 传入User对象获取用户名称
        String usernameByPost = ApiClient.getUserNameByPost(user);
        // 返回成功响应，包含调用的结果
        return ResultUtils.success(usernameByPost);

    }
}

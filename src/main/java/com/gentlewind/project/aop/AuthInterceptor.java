package com.gentlewind.project.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.gentlewind.project.service.UserService;
import com.gentlewind.project.annotation.AuthCheck;
import com.gentlewind.project.common.ErrorCode;
import com.gentlewind.project.exception.BusinessException;
import com.gentlewind.project.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限校验 AOP
 *
 * @author gentlewind
 */

//鉴权拦截器，判断用户的角色（role），是否为管理员，用户，封号三种情况
@Aspect // @Aspect表示这是一个切面类
@Component // @Component表示这是一个组件类
public class AuthInterceptor {

    @Resource //自动注入，相当于@Autowired （@Resource 注解用于注入 UserService，它告诉 Spring 容器需要将一个 UserService 的实例注入到该字段中。）
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)") // @Around表示这是一个环绕通知，它可以在目标方法执行之前和之后分别执行一些操作
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable { // 是在方法声明中指定的异常声明，它表示方法可能会抛出任何类型的异常
        // "ProceedingJoinPoint"是AOP中的一个接口，它是JoinPoint的子接口，表示可以执行目标方法
        //  AuthCheck包含了权限检查所需的信息
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNotBlank).collect(Collectors.toList()); //StringUtils::isNotBlank相当于StringUtils.isNotBlank(),更加简洁
        String mustRole = authCheck.mustRole();

        //下面两段代码可以拿到request对象
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes(); // 通过静态方法，获取关于当前请求的的属性
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest(); // ，Spring会将请求的上下文信息存储在ServletRequestAttributes对象中，包括请求参数、请求头、Session等信息。通过ServletRequestAttributes对象，您可以轻松地访问和操作这些请求属性，而无需直接操作原始的HttpServletRequest对象
        // 当前登录用户，登录的时候，已经在会话中放入了登录态，因此可以获取到
        // 从这里可以拿到request对象的请求参数，请求地址，请求头等
        User user = userService.getLoginUser(request);

        // 这段代码的作用是进行权限验证。首先，它检查anyRole是否为空，如果不为空，则获取当前用户的角色 userRole，然后判断 userRole 是否包含在 anyRole 中。如果不包含，则抛出 BusinessException 异常，表示没有权限。
        //这段代码的逻辑是：
        //如果 anyRole 不为空，表示需要验证用户是否拥有其中的某个角色。
        //如果当前用户的角色 userRole 不在 anyRole 中，则抛出权限不足的异常
        if (CollectionUtils.isNotEmpty(anyRole)) {
            String userRole = user.getUserRole();
            if (!anyRole.contains(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 必须有所有权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            String userRole = user.getUserRole();
            if (!mustRole.equals(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}


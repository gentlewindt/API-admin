package com.gentlewind.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 *
 * @author gentlewind
 */

// 定义一个注解接口，用于标记需要权限校验的方法
@Target(ElementType.METHOD) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface AuthCheck { // @interface表示这是一个注解的接口

    /**
     * 有任何一个角色
     *
     * @return
     */
    // 注解元素anyRole，接收一个String数组类型的参数，默认值为空数组
    // 只要有数组中角色的任意一个，就通过权限，因此是数组
    String[] anyRole() default "";

    /**
     * 必须有某个角色
     *
     * @return
     */
    // 注解元素mustRole，接收一个String类型的参数，默认值为空字符串
    // 必须拥有某个角色，权限才通过，因此是字符串
    String mustRole() default "";

}


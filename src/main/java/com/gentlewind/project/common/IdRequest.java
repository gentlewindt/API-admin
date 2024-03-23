package com.gentlewind.project.common;

import lombok.Data;

import java.io.Serializable;

// 封装id这个参数，将一个基本类型封装成一个对象，这样便于进行json参数传递
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

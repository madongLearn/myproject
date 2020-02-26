package com.github.myproject.util.checker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 参数校验 注解
 *
 * @Author by dongma on 2019/4/19.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RUNTIME)
public @interface CheckParams {

    /**
     * 字段校验规则，格式：字段名+校验规则+冒号+错误信息，例如：tokenId:tokenId不能为空
     */
    String[] value();

}
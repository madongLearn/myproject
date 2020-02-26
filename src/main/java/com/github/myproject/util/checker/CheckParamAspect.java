package com.github.myproject.util.checker;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.BiFunction;

/**
 * 参数校验 切面
 *
 * @author dongma
 * @date 2019/4/19
 */
@Aspect
@Component
public class CheckParamAspect {

    private static final Logger logger = LoggerFactory.getLogger(CheckParamAspect.class);
    private static final String SEPARATOR = ":";

    @Around(value = "@annotation(com.github.myproject.util.checker.CheckParams)")
    public Object check(ProceedingJoinPoint point) throws Throwable {
        Object obj;
        // 参数校验
        String msg = doCheck(point);
        if (!StringUtils.isEmpty(msg)) {
            throw new IllegalArgumentException(msg);
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }

    /**
     * 参数校验
     *
     * @param point ProceedingJoinPoint
     * @return 错误信息
     */
    private String doCheck(ProceedingJoinPoint point) {
        // 获取方法参数值
        Object[] arguments = point.getArgs();
        // 获取方法
        Method method = getMethod(point);
        // 默认的错误信息
        String methodInfo = StringUtils.isEmpty(method.getName()) ? "" : " while calling " + method.getName();
        String msg = "";
        if (isCheck(method, arguments)) {
            CheckParams annotation = method.getAnnotation(CheckParams.class);
            String[] fields = annotation.value();
            // 只支持对第一个参数进行校验
            Object vo = arguments[0];
            if (vo == null) {
                msg = "param can not be null";
            } else {
                for (String field : fields) {
                    // 解析字段
                    FieldInfo info = resolveField(field, methodInfo);
                    // 获取字段的值
                    Object value = ReflectionUtil.invokeGetter(vo, info.field);
                    // 执行校验规则
                    Boolean isValid = info.optEnum.fun.apply(value, info.operatorNum);
                    msg = isValid ? msg : info.innerMsg;
                }
            }
        }
        return msg;
    }
    /**
     * 解析字段
     *
     * @param fieldStr   字段字符串
     * @param methodInfo 方法信息
     * @return 字段信息实体类
     */
    private FieldInfo resolveField(String fieldStr, String methodInfo) {
        FieldInfo fieldInfo = new FieldInfo();
        String innerMsg = "";
        // 解析提示信息
        if (fieldStr.contains(SEPARATOR)) {
            innerMsg = fieldStr.split(SEPARATOR)[1];
            fieldStr = fieldStr.split(SEPARATOR)[0];
        }
        // 解析操作
        fieldInfo.optEnum = Operator.NOT_NULL;
        fieldInfo.field = fieldStr;
        fieldInfo.operatorNum = "";
        fieldInfo.operator = fieldInfo.optEnum.value;
        // 处理错误信息
        String defaultMsg = fieldInfo.field + " must " + fieldInfo.operator + " " + fieldInfo.operatorNum + methodInfo;
        fieldInfo.innerMsg = StringUtils.isEmpty(innerMsg) ? defaultMsg : innerMsg;
        return fieldInfo;
    }

    /**
     * 是否不为空
     *
     * @param value       字段值
     * @param operatorNum 操作数
     * @return 是否不为空
     */
    private static Boolean isNotNull(Object value, String operatorNum) {
        Boolean isNotNull = Boolean.TRUE;
        Boolean isStringNull = (value instanceof String) && StringUtils.isEmpty((String) value);
        Boolean isCollectionNull = (value instanceof Collection) && CollectionUtils.isEmpty((Collection) value);
        if (value == null) {
            isNotNull = Boolean.FALSE;
        } else if (isStringNull || isCollectionNull) {
            isNotNull = Boolean.FALSE;
        }
        return isNotNull;
    }

    /**
     * 判断是否符合参数规则
     *
     * @param method    方法
     * @param arguments 方法参数
     * @return 是否符合
     */
    private Boolean isCheck(Method method, Object[] arguments) {
        Boolean isCheck = Boolean.TRUE;
        // 只允许有一个参数
        if (!method.isAnnotationPresent(CheckParams.class)
                || arguments == null
                || arguments.length != 1) {
            isCheck = Boolean.FALSE;
        }
        return isCheck;
    }

    /**
     * 获取方法
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 方法
     */
    protected static Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                logger.error("" + e);
            }
        }
        return method;
    }

    /**
     * 字段信息
     */
    class FieldInfo {
        /**
         * 字段
         */
        String field;
        /**
         * 提示信息
         */
        String innerMsg;
        /**
         * 操作符
         */
        String operator;
        /**
         * 操作数
         */
        String operatorNum;
        /**
         * 操作枚举
         */
        Operator optEnum;
    }

    /**
     * 操作枚举，封装操作符和对应的校验规则
     */
    enum Operator {
        /**
         * 不为空
         */
        NOT_NULL("not null", CheckParamAspect::isNotNull);

        private String value;

        /**
         * BiFunction：接收字段值(Object)和操作数(String)，返回是否符合规则(Boolean)
         */
        private BiFunction<Object, String, Boolean> fun;

        Operator(String value, BiFunction<Object, String, Boolean> fun) {
            this.value = value;
            this.fun = fun;
        }
    }

}
package com.github.myproject.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtil {

    public static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(false)
            .buildValidatorFactory()
            .getValidator();

    public static Validator fastFailValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    //fastfail模式检查参数
    public static <T> void fastFailValidate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = fastFailValidator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new RuntimeException(String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage()));
        }
    }

    public static <T> void validate(T obj, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj, groups);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            String message = constraintViolations.stream().map(c -> c.getMessage()).collect(Collectors.joining(","));
            throw new RuntimeException(String.format("参数校验失败:%s", message));
        }
    }

    private static class ValidatorHolder {
        public static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> void validateParam(T t, Class<?>... groups) {
        Validator validator = ValidatorHolder.VALIDATOR;
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(t, groups);
        if (null == constraintViolationSet || constraintViolationSet.isEmpty()) {
            return;
        }
        throw new IllegalArgumentException(constraintViolationSet.iterator().next().getMessage());
    }

}

package com.atguigu.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author zhp
 * @date 2023-01-18 21:26
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ListValueConstranitValidator.class})
public @interface ListValue {

    /**
     * @return the error message template
     */
    String message() default "{com.atguigu.common.valid.ListValue.message}";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default { };

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default { };


    /**
     * 读取的默认整型数组
     * @return
     */
    int[] vals() default {};
}

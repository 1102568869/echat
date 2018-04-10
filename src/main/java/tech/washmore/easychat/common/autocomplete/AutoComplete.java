package tech.washmore.easychat.common.autocomplete;

import java.lang.annotation.*;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * @since 2018/3/15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AutoComplete {
    AutoCompleteType type() default AutoCompleteType.All;

    AutoCompleteTarget target() default AutoCompleteTarget.CurrentLoginUser;
}

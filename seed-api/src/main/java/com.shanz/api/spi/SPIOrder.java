package com.shanz.api.spi;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPIOrder {

    /**
     * 排序顺序
     *
     * @return sortNo
     */
    int order() default 0;
}

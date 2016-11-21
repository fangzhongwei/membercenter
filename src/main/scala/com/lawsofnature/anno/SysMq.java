package com.lawsofnature.anno;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by fangzhongwei on 2016/11/21.
 */
@Retention(RUNTIME)
@BindingAnnotation
public @interface SysMq {
}

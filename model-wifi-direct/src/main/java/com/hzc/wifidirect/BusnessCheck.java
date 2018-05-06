package com.hzc.wifidirect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by huangzongcheng on 2018/5/6.21:46
 * 328854225@qq.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusnessCheck {

}

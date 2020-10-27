package com.cloud.threadlocal;

import org.springframework.util.ObjectUtils;

/**
 * @version v1.0
 * @author: TianXiang
 * @description: 线程局部变量, 每个线程维护一份副本
 * @date: 2020/10/22
 */
public class CommonContext {

    private static final ThreadLocal<CommonContext> threadLocal = new ThreadLocal();

    private String name;


    public static CommonContext getContext() {
        CommonContext commonContext = threadLocal.get();
        if(ObjectUtils.isEmpty(commonContext)) {
            commonContext = new CommonContext();
            threadLocal.set(commonContext);
        }
        return commonContext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

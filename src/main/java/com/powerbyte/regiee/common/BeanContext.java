package com.powerbyte.regiee.common;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/7 17:33
 */
public class BeanContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}

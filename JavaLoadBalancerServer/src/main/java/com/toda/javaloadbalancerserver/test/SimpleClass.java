package com.toda.javaloadbalancerserver.test;

public class SimpleClass {

    private final ThreadLocal<String> sstThreadLocal = new ThreadLocal<>();

    private static final SimpleClass instance = new SimpleClass();

    public void setSst(final String sst) {
        sstThreadLocal.set(sst);
    }

    public String getSstThreadLocal() {
        return sstThreadLocal.get();
    }

    public static SimpleClass getCurrentContext() {
        return instance;
    }

    public void clear() {
        sstThreadLocal.remove();
    }
}

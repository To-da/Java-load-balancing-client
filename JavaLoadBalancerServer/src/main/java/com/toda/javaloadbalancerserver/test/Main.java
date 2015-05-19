package com.toda.javaloadbalancerserver.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleClass.getCurrentContext().setSst("foo");

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        final List<Future<String>> futures = executorService.invokeAll(Arrays.asList(
                new Thready(),
                new Thready()
        ));

        System.out.println(futures.get(0).get());
        System.out.println(futures.get(0).get());
        System.out.println(SimpleClass.getCurrentContext() + ":" + SimpleClass.getCurrentContext().getSstThreadLocal());
        final List<Future<String>> futures2 = executorService.invokeAll(Arrays.asList(
                new Thready(),
                new Thready()
        ));
        System.out.println(futures2.get(0).get());
        System.out.println(futures2.get(0).get());
        System.out.println(SimpleClass.getCurrentContext() + ":" + SimpleClass.getCurrentContext().getSstThreadLocal());

        executorService.shutdown();
    }


    public static class Thready implements Callable<String> {

        @Override
        public String call() throws Exception {
            try {

                SimpleClass.getCurrentContext().setSst(this.toString());
                return SimpleClass.getCurrentContext() + ":" + SimpleClass.getCurrentContext().getSstThreadLocal();
            }           finally {
                SimpleClass.getCurrentContext().clear();
            }
        }
    }

}

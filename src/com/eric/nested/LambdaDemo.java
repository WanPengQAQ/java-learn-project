package com.eric.nested;

import java.util.function.IntConsumer;

/**
 * @Classname LambdaDemo
 * @Date 2021/11/26 4:01 下午
 * @Created by eric
 */
public class LambdaDemo {
    public static void main(String[] args) {
        LambdaDemo.repead(10, (i) -> System.out.println(i));
        LambdaDemo.repead(5, System.out::println);
    }

    private static void repead(int i, IntConsumer action) {
        for (int j = 0; j < i; j++) {
            action.accept(j);
        }
    }

}

package com.eric.stream;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @Classname StreamAdvanceOfFlatMapDemo
 * @Date 2021/11/28 5:19 下午
 * @Created by eric
 * 参考资料:https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/#processing-order 博客
 *        https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html Java文档
 *        https://github.com/winterbe/java8-tutorial#streams github教程
 */
public class StreamAdvanceOfFlatMapDemo {
    public static void main(String[] args) {
        /**
         * Advanced Operations
         * 流支持很多神奇操作，包括下面的几个高级操作, collect, flatMap 和 reduce。
         */
        /**
         *  通过map可以把流中的一个元素转换为另一个元素(1对1), 但如果想把一个元素映射到n个元素就需要使用flatMap操作(1对0 or 1对n)。
         */

        /**
         * 节省代码行数的技巧, 创建多个对象的方法:
         * IntStream.range(1, 4) = [1, 4)
         */
        List<Foo> foos = new ArrayList<>();
        // create foos
        IntStream.range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));
        // create bars
        foos.forEach(f -> IntStream.range(1, 4)
                .forEach(i -> f.bars.add(new Bar("Bar" + i + "<-" + f.name))));
        System.out.println();

        /** 此时每一个Foo都有3个Bar, 为了遍历每一个Foo中的Bar, 此时需要使用flatMap
         *  flatMap的参数是接受一个函数(函数式接口), 此函数返回一个stream
         */
        foos.stream()
                .flatMap(f -> f.bars.stream())
                .forEach(System.out::println);
        System.out.println();

        /**
         * flatMap还可以于Optional组合起来处理null的检查
         */
        // 为了打印outer.nested.inner.foo我们需要做非常丑陋的null检查
        Outer outer = new Outer();
        if (outer != null && outer.nested != null && outer.nested.inner != null) {
            System.out.println(outer.nested.inner.foo);
        }
        // 可以用以下的代码代替(似乎并没有好很多...)
        Optional.of(new Outer())
                .flatMap(o -> Optional.ofNullable(o.nested))
                .flatMap(n -> Optional.ofNullable(n.inner))
                .flatMap(i -> Optional.ofNullable(i.foo))
                .ifPresent(System.out::println);


    }
}

class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    Foo(String name) {
        this.name = name;
    }
}

class Bar {
    String name;

    Bar(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

/**
 * outer.nested.inner.foo
 */
class Outer {
    Nested nested;
}

class Nested {
    Inner inner;
}

class Inner {
    String foo;
}

package com.eric.stream;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @Classname StreamDemo
 * @Date 2021/11/27 11:42 下午
 * @Created by eric
 *
 * 参考资料:https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/#processing-order 博客
 *        https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html Java文档
 *        https://github.com/winterbe/java8-tutorial#streams github教程
 */
public class StreamBasicsDemo {
    public static void main(String[] args) {

        List<String> singleCharList = Arrays.asList("a", "a1","a2", "b", "c", "d", "e", "f", "g");


        /**
         *  stream分为两种操作, 一种是中间操作返回的是一个stream，一种是终止操作返回void或者一个非stream的结果。
         *  例如: filter, map, sorted仍然返回一个stream
         *       forEach返回void（terminal operation）
         */
        singleCharList.stream().
                filter(c -> c.startsWith("a")).
                map(String::toUpperCase).
                sorted().
                forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------");

        /**
         *  中间操作的一个特点是懒惰执行，只有当出现一个终止操作的时候，stream才会被执行。
         *  so，下面的操作不会打印任何的结果
         */
        singleCharList.stream().
                filter(c -> {
            System.out.println(c);
            return true;
        });
        System.out.println("---------------------------------------------------------------------");

        /**
         *  下面加上终止操作forEach
         */
        singleCharList.stream().filter(c -> {
            System.out.println("filter " + c);
            return true;
        }).forEach(c -> System.out.println("forEach " + c));
        /**
         *  注意观察执行的顺序。
         *  filter a
         *  forEach a
         *  filter a1
         *  forEach a1
         *  filter a2
         *  forEach a2
         *  原始集合的元素一个接着一个流入stream，然后流出。这样的执行顺序可能会导致流被提前终止。看下面的例子anyMatch短路终止操作。
         */
        System.out.println("---------------------------------------------------------------------");

        /**
         *  anyMatch是一个 short-circuiting terminal operation，短路终止操作。
         *  anyMatch可以用于检查流中的元素是否存在。
         *  只要有一个元素满足就会返回true，并且终止流的操作
         */
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map:" + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });
        /**
         * map:d2
         * anyMatch: D2
         * map:a2
         * anyMatch: A2
         * 执行到"a2"的时候由于anyMatch为true，流就提前终止了。
         */
        System.out.println("---------------------------------------------------------------------");

        /**
         *  stream操作顺序的问题！。
         */
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("A");   // 只有以A开头的才能走到下一步
                })
                .forEach(s -> System.out.println("forEach: " + s));
        System.out.println();
        /**
         * map: d2
         * filter: D2
         * map: a2
         * filter: A2
         * forEach: A2
         * map: b1
         * filter: B1
         * map: b3
         * filter: B3
         * map: c
         * filter: C
         * 先map再filter, 输出A2, 共有11个操作
         */

        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        /**
         * filter: d2
         * filter: a2
         * map: a2
         * forEach: A2
         * filter: b1
         * filter: b3
         * filter: c
         * 先filter在map, 输出A2, 共有7个操作。
         *
         * 两个操作性能高下立判
         */
        System.out.println("---------------------------------------------------------------------");

        /**
         *  sort是一个特殊的中间操作intermediate operation，他是一个stateful的操作
         */
        Stream.of("d2", "a2", "b1", "b3", "c")
                .sorted((s1, s2) -> {
                    System.out.println("sort: " + s1 + "," + s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        System.out.println();
        /**
         * sort: a2,d2
         * sort: b1,a2
         * sort: b1,d2
         * sort: b1,a2
         * sort: b3,b1
         * sort: b3,d2
         * sort: c,b3
         * sort: c,d2
         * filter: a2
         * map: a2
         * forEach: A2
         * filter: b1
         * filter: b3
         * filter: c
         * filter: d2
         * sort集中执行了8次，sort就像是一个临时的水桶，将所有的stream放入水桶以另一种顺序输出。上面这个可以被优化为下面的操作。s
         */
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .sorted((s1, s2) -> {
                    System.out.println("sort: " + s1 + "," + s2);
                    return s1.compareTo(s2);
                })
                .forEach(s -> System.out.println("forEach: " + s));
        /**
         * filter: d2
         * filter: a2
         * map: a2
         * filter: b1
         * filter: b3
         * filter: c
         * forEach: A2
         *
         * 在这个顺序中，sort没有被调用, 因为通过map的元素只有一个, 这是不需要排序的，相比于之前的顺序这样做大大减少了操作次数。
         */
        System.out.println("---------------------------------------------------------------------");

        /**
         * Reusing Streams
         * Java 8 的流是不可以重复使用的，一旦使用的终止操作流就会被关闭
         */

        Stream<String> reuseStream = Stream.of("a", "b", "c")
                .filter(s -> !s.startsWith("b"));
        //reuseStream.forEach(System.out::println); // OK
        //reuseStream.forEach(System.out::println); // Exception

        /**
         * 为了克服这个不可重复使用流的问题, 使用stream supplier创建一个流，其中对stream的所有中间操作都已经保存在supplier之中。
         * 仅需使用get()从中拿出中间结果的流, 然后继续操作流。
         */
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("a", "b", "c").filter(s -> !s.startsWith("b"));

        streamSupplier.get().forEach(System.out::println);
        System.out.println("------------");
        streamSupplier.get().forEach(System.out::println);
        System.out.println("---------------------------------------------------------------------");

    }
}

package com.eric.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Classname StreamAdvanceDemo
 * @Date 2021/11/28 4:48 下午
 * @Created by eric
 *
 * 参考资料:https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/#processing-order 博客
 *        https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html Java文档
 *        https://github.com/winterbe/java8-tutorial#streams github教程
 */

public class StreamAdvanceOfCollectDemo {
    public static void main(String[] args) {

        List<Person> persons =
                Arrays.asList(
                        new Person("Max", 18),
                        new Person("Peter", 23),
                        new Person("Pamela", 23),
                        new Person("David", 12),
                        new Person("David", 22) // 重复元素
                );

        /**
         * Advanced Operations
         * 流支持很多神奇操作，包括下面的几个高级操作, collect, flatMap 和 reduce。
         */
        /**
         * collect是一个终止流的操作，他可以将stream转换为不同类型的结果，例如List, Set, Map等。
         *
         */
        // toList
        List<Person> filtered = persons.stream()
                .filter(p -> p.name.startsWith("P"))
                .collect(Collectors.toList());
        System.out.println(filtered);
        System.out.println("-----------------------------------------------------------------------------------------");
        /**
         *  [Peter, Pamela]
         */

        List<String> nameList = persons.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
        System.out.println(nameList);
        System.out.println("-----------------------------------------------------------------------------------------");
        /**
         * [Max, Peter, Pamela, David, David]
         */

        // toSet
        Set<String> nameSet = persons
                .stream()
                .map(Person::getName)
                .collect(Collectors.toSet());
        System.out.println(nameSet);
        System.out.println("-----------------------------------------------------------------------------------------");
        /**
         * [Pamela, Max, David, Peter] 去重复
         */

        // group by age
        Map<Integer, List<Person>> groupByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age));

        groupByAge.forEach((age, personsList) -> System.out.println("age:" + age + " " + personsList));
        System.out.println("-----------------------------------------------------------------------------------------");



    }
}

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

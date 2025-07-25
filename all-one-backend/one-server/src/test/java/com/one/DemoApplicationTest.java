package com.one;

import com.one.vo.StudentVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SpringBootTest
class DemoApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void testUseLambda() {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        nums.stream()
                .filter(num -> num % 2 == 0)
                .map(num -> num * 2)
                .forEach(System.out::println);
    }

    @Test
    void testStream() {
        List<String> list = Arrays.asList("apple", "banana", "orange", "peach");
        long count = list.stream()
                .filter(str -> str.startsWith("a"))
                .map(String::toUpperCase)
                .count();

        System.out.println(count);
    }

    @Test
    void testCurry() {
        // 接收一个字符串类型参数并返回对应整数长度值
        Function<String, Integer> stringToLength = str -> str.length();
        System.out.println(stringToLength.apply("Hello World!"));
        // 输出结果：12
    }

    @Test
    void testMethodsUsage() {
        // ClassName::staticMethodName
        // objectReference::instanceMethodName
        // SuperClassName::instanceMethodName
        // ClassName::new
    }

    @Test
    void testOptional() {
        Optional<String> optional1 = Optional.of("Hello");
        Optional<String> optional2 = Optional.ofNullable(null);
        Optional<String> optional3 = Optional.ofNullable("Hello");
        if (optional3.isPresent()) {
            System.out.println("Value is " + optional3.get());
        }
        Optional<String> optional4 = Optional.ofNullable("Hello");
        String value4 = optional4.get();
        String defaultValue = "World";
        Optional<String> optional5 = Optional.empty();
        String value5 = optional5.orElse(defaultValue); // value = "World"
        Supplier<String> supplier = () -> "World";
        Optional<String> optional6 = Optional.empty();
        String value6 = optional6.orElseGet(supplier); // value = "World"
        Function<String, String> function = (s) -> s + " World!";
        Optional<String> optional7 = Optional.of("Hello");
        Optional<String> result7 = optional6.map(function); // result = Optional[Hello World!]
        Predicate<String> predicate = (s) -> s.contains("l");
        Optional<String> optional8 = Optional.of("Hello");
        Optional<String> result8 = optional8.filter(predicate); // result = Optional[Hello]


    }

    @Test
    void testParallelStreams() {
    }

    @Test
    void TestInvoke() {
        Class<?> stu1 = StudentVO.class;
        try {
            Class<?> stu2 = Class.forName("com.one.vo.StudentVO");
            System.out.println(stu1 == stu2);
            // 获取要调用的方法的Method对象

            Method method = stu2.getMethod("setName", String.class);

            // 创建要调用方法的对象
            Object obj = stu2.newInstance();

            // 调用方法

            String result = (String) method.invoke(obj, "lucy");

            System.out.println(result);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }


}

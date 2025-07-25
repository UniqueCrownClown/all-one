package com.one;

import com.one.po.User;
import com.one.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setName("Lucy");
        user.setPhone("18688990011");

        user.setSex("0");
        user.setIdNumber("123456789012345678");
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    public void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }

    @Test
    public void testSelectByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        ids.add(4L);
        ids.add(5L);
        List<User> users = userMapper.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setName("jack");
        userMapper.updateById(user);
    }

    @Test
    public void testDelete() {
        userMapper.deleteById(5L);
    }

    @Test
    public void testpassword() {
        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("password = {}", password);
    }
}

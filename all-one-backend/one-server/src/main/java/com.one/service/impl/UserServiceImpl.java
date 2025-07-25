package com.one.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.po.User;
import com.one.mapper.UserMapper;
import com.one.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

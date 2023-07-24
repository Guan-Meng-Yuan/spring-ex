package com.guanmengyuan.spring.ex.test.service.impl;

import com.guanmengyuan.spring.ex.test.mapper.UserMapper;
import com.guanmengyuan.spring.ex.test.model.domain.User;
import com.guanmengyuan.spring.ex.test.service.UserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

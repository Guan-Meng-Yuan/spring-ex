package com.guanmengyuan.spring.cloud.example.provider.service;

import com.guanmengyuan.spring.cloud.example.api.UserApi;
import com.guanmengyuan.spring.cloud.example.model.domain.User;
import com.guanmengyuan.spring.cloud.example.provider.mapper.UserMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class UserService extends ServiceImpl<UserMapper, User> implements UserApi {
}

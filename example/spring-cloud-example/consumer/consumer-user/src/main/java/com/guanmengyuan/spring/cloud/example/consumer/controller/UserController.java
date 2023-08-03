package com.guanmengyuan.spring.cloud.example.consumer.controller;

import com.guanmengyuan.spring.cloud.example.api.UserApi;
import com.guanmengyuan.spring.cloud.example.model.domain.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试接口
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @DubboReference
    private UserApi userApi;

    /**
     * 用户列表
     *
     * @return 用户列表
     */
    @GetMapping
    public List<User> list() {
        return userApi.list();
    }

    /**
     * 新增用户
     *
     * @param user 用户参数
     * @return 保存结果
     */
    @PostMapping
    public Boolean save(@RequestBody User user) {
        return true;
    }

}

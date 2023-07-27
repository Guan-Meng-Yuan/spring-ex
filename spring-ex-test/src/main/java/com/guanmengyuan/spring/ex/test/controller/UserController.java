package com.guanmengyuan.spring.ex.test.controller;


import com.guanmengyuan.spring.ex.test.model.domain.User;
import com.guanmengyuan.spring.ex.webflux.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController extends BaseController<User> {

}

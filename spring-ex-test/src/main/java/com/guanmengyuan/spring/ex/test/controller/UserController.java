package com.guanmengyuan.spring.ex.test.controller;


import com.guanmengyuan.spring.ex.alipay.service.AliPayService;
import com.guanmengyuan.spring.ex.common.model.dto.req.PageReq;
import com.guanmengyuan.spring.ex.test.model.domain.User;
import com.guanmengyuan.spring.ex.test.service.UserService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AliPayService aliPayService;

    /**
     * 新增用户
     *
     * @param user 用户参数
     * @return 新增结果
     */
    @PostMapping
    public Boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 用户分页
     *
     * @param pageReq 分页参数
     * @return
     */
    @GetMapping("page")
    public Page<User> getUser(@ParameterObject PageReq pageReq) {
        return userService.page(Page.of(pageReq.getCurrent(), pageReq.getPageSize()));
    }
}

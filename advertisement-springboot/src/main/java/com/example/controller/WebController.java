package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    UserService userService;
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        // 使用 UserService 登录
        User loggedInUser = userService.login(user);
        if (loggedInUser == null) {
            return Result.error("500","账号或密码错误");
        }
        return Result.success(loggedInUser);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return Result.error("500","角色不能为空");
        }
        userService.register(user);
        return Result.success();
    }
}

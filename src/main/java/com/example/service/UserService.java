package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.RoleEnum;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper; // 改为 userMapper

    // 登录逻辑
    public User login(User user) { // 改为 User
        String username = user.getName();
        // 根据账号查询数据
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser == null) {
            throw new CustomException("500", "账号不存在");
        }
        // 校验密码
        if (!dbUser.getPassword().equals(user.getPassword())||!dbUser.getRole().equals(user.getRole())) {
            throw new CustomException("500", "账号或者密码错误");
        }
        return dbUser;
    }

    // 注册逻辑
    // 修改register方法
    public void register(User user) {
        // 校验用户账号是否存在
        User dbUser = userMapper.selectByUsername(user.getName());
        if (dbUser != null) {
            throw new CustomException("500", "账号已存在");
        }
        // 校验密码是否为空
        if (ObjectUtil.isEmpty(user.getPassword())) {
            throw new CustomException("500", "密码不能为空");
        }
        // 设置默认角色为普通用户
        if (ObjectUtil.isEmpty(user.getRole())) {
            user.setRole(RoleEnum.USER.name());
        }

        // 将用户插入数据库
        userMapper.insert(user);
    }


    // 添加账户
    public void add(User user) { // 改为 User
        User dbUser = userMapper.selectByUsername(user.getName());
        if (dbUser != null) {
            throw new CustomException("500", "账号已存在");
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword("123");  // 默认密码
        }
        if (ObjectUtil.isEmpty(user.getRole())) {
            user.setRole(RoleEnum.USER.name());  // 默认角色为普通用户
        }
        userMapper.insert(user);
    }

    // 删除账户
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    // 批量删除账户
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    // 更新账户信息
    public void updateById(User user) { // 改为 User
        userMapper.updateById(user);
    }

    // 根据ID查询账户
    public User selectById(Integer id) { // 改为 User
        return userMapper.selectById(id);
    }

    // 查询所有账户
    public List<User> selectAll(String name) { // 改为 User
        return userMapper.selectAll(name);
    }

    // 分页查询账户
    public PageInfo<User> selectPage(String name, Integer pageNum, Integer pageSize) { // 改为 User
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = this.selectAll(name);
        return PageInfo.of(list);
    }

    public User selectByName(String name) {
        return userMapper.selectByName(name);
    }
}

package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    // 根据用户名查询用户
    @Select("select * from user where name = #{name}")
    User selectByUsername(String name);

    // 插入用户
    @Insert("insert into user (name, password, role) values (#{name}, #{password}, #{role})")
    void insert(User user);

    // 根据ID删除用户
    @Delete("delete from user where id = #{id}")
    void deleteById(Integer id);

    // 更新用户信息
    @Update("update user set name = #{name}, password = #{password}, role = #{role} where id = #{id}")
    void updateById(User user);

    // 根据ID查询用户
    @Select("select * from user where id = #{id}")
    User selectById(Integer id);

    // 查询所有用户，按名字模糊查询
    @Select("select * from user where name like CONCAT('%', #{name}, '%')")
    List<User> selectAll(String name);

    @Select("select * from user where name = #{name}")
    User selectByName(String name);
}

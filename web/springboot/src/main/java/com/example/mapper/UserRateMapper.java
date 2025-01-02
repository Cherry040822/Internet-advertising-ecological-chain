package com.example.mapper;

import com.example.entity.userRate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserRateMapper {
    @Select("SELECT category, grade FROM user_rate WHERE user = #{user} ORDER BY category")
    List<userRate> getUserRatesByUser(String user);

    @Select("SELECT user, category, grade FROM user_rate")
    List<userRate> getallUserRates();
}
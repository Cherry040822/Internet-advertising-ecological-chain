package com.example.service;


import com.example.entity.userRate;
import com.example.mapper.UserRateMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRateService {
    @Resource
    private UserRateMapper userRateMapper;
    //获取当前用户评分列表 （用户 -> 分类 ：得分）
    public List<userRate> getUserRatesByUser(String user) {
        return userRateMapper.getUserRatesByUser(user);
    }

    //获取所以用户的评分
    public List<userRate> getallUserRates() {
        return userRateMapper.getallUserRates();
    }

    //协同过滤算法
    public List<Map<String, Object>> getrecommend(String user) {
        //先用户 对 物品 评分
        // 1   1：2  2：2  3：3
        // 2   1：6  3：5  4：2
        //当前用户评分
        List<userRate> userRates = getUserRatesByUser(user);
        //全部用户评分
        List<userRate> alluserRates = getallUserRates();
        //物品同现矩阵：同时买了物品a和b的人有多少个
        Map<String, Map<String, Integer>> sameshowMatrix = new HashMap<>();
        //用户评分矩阵：用户a 对物品a 的评分
        Map<String, Map<String, Integer>> userItemScores = new HashMap<>();


        //物品同现矩阵
        for(userRate rate : alluserRates) {
            String nowuser = rate.getUser(); //当前用户
            String category = rate.getCategory();  //分类
            int grade = rate.getGrade();  //评分

            //Map<String, Map<String, Integer>> 用户a 对物品a 的评分
            userItemScores.putIfAbsent(nowuser, new HashMap<>());
            userItemScores.get(nowuser).put(category, grade);

            //将当前物品和其他物品的同现次数加一
            for (Map.Entry<String, Integer> entry : userItemScores.get(nowuser).entrySet()) {
                String otherCategory = entry.getKey(); //当前分类
                sameshowMatrix.putIfAbsent(category, new HashMap<>());
                //没出现就1出现就加1
                sameshowMatrix.get(category).merge(otherCategory, 1, Integer::sum);
            }
        }

        //计算推荐分值
        Map<String, Double> recommendScores = new HashMap<>();
        for (userRate rate : userRates) {
            //当前评分分类
            String ratedCategory = rate.getCategory();
            int ratedGrade = rate.getGrade();

            // 跳过没有同现关系的物品
            if (!sameshowMatrix.containsKey(ratedCategory)) continue;

            for (Map.Entry<String, Integer> entry : sameshowMatrix.get(ratedCategory).entrySet()) {
                //当前推荐的物品
                String recommendedCategory = entry.getKey();
                int coOccurrenceCount = entry.getValue();

                //推荐分值 = 用户评分 × 物品同现次数
                recommendScores.merge(
                        recommendedCategory,
                        ratedGrade * coOccurrenceCount * 1.0,
                        Double::sum
                );
            }
        }

//        //过滤当前用户已评分的物品
//        Set<String> ratedCategories = userRates.stream()
//                .map(userRate::getCategory)
//                .collect(Collectors.toSet());
//        recommendScores.keySet().removeAll(ratedCategories);

        //排序并返回推荐结果
        List<Map<String, Object>> recommend = recommendScores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .map(entry -> {
                    Map<String, Object> recommendation = new HashMap<>();
                    recommendation.put("分类", entry.getKey());
                    recommendation.put("分数", entry.getValue());
                    return recommendation;
                })
                .collect(Collectors.toList());

        return recommend;

    }

}

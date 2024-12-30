package com.example.controller;

import com.example.common.Result;
import com.example.entity.userRate;
import com.example.service.UserRateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userRate")
public class UserRateController {

    @Resource
    private UserRateService userRateService;

    @GetMapping("/get")
    public Result getUserRates(@RequestParam String user) {
        try {
            List<userRate> rates = userRateService.getUserRatesByUser(user);
            if (rates == null || rates.isEmpty()) {
                return Result.success("当前用户无评分数据");
            }
            System.out.println("列表" + rates);
            return Result.success(rates);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500", "获取用户评分失败: " + e.getMessage());
        }
    }

    @GetMapping("/recommend")
    public Result recommend(@RequestParam String user) {
        try{
            List<Map<String, Object>> recommend = userRateService.getrecommend(user);

            System.out.println("hhhhhhhhhh" + recommend);
            return Result.success(recommend);
        }catch (Exception e){
            return Result.error("500","推荐失败: " + e.getMessage());
        }
    }
}

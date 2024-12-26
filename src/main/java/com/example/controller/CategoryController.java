package com.example.controller;

import com.example.entity.Category;
import com.example.service.CategoryService;
import jakarta.annotation.Resource;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    /*查询所有notice数据*/
    @GetMapping("/selectAll")
    public Result selectAll(Category category){
        List<Category> list=categoryService.selectAll(category);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        int deletedRows=categoryService.deleteById(id);
        if (deletedRows == 0) {
            // 如果没有删除记录，说明广告表中已经存在该类别
            return Result.failure("该类别已经被使用，不可删除");
        }

        return Result.success("操作成功");
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteById(@RequestBody List<Integer> ids){
        categoryService.deleteBatch(ids);
        return Result.success();
    }

}

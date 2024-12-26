package com.example.mapper;

import com.example.entity.Advertisement;
import com.example.entity.Category;

import java.util.List;

public interface CategoryMapper {

    List<Category> selectAll(Category category);
    void insert(Category category);
    int deleteById(Integer id);
}

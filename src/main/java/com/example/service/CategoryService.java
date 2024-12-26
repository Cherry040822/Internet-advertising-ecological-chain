package com.example.service;

import com.example.entity.Category;
import com.example.mapper.AdvertisementMapper;
import com.example.mapper.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    public List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

    public void add(Category category) {
        categoryMapper.insert(category);
    }

    public int deleteById(Integer id) {
        int deletedRows =categoryMapper.deleteById(id);
        return deletedRows;
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id :ids){
            this.deleteById(id);
        }
    }
}

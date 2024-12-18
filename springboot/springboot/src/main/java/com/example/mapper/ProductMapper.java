package com.example.mapper;

import com.example.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ProductMapper {

    //获取所有商品
    List<Product> selectAllProducts();
    // 根据名字获取商品
    Product selectProductByname(String name);
    // 根据商品类别获取商品
    List<Product> selectProductsByCategory(String category);
    //获取所有类别
    List<String> selectAllCategory();
    //根据Id获取商品
    Product selectProductById(Integer productId);

}

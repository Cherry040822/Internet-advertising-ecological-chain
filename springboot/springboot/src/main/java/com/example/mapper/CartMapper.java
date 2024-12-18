package com.example.mapper;

import com.example.entity.CartItem;
import com.example.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {
    void insert(Product product);
    List<CartItem> selectAll();
    void update(CartItem cartItem);
    void delete(Integer productId);
    Integer exists(Integer productId);
}

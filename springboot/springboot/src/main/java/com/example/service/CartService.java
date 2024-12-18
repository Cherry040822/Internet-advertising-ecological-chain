package com.example.service;

import com.example.common.Result;
import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.mapper.CartMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Resource
    private CartMapper cartMapper;
    public Result insert(Product product) {
        int count = cartMapper.exists(product.getProductId());
        if (count > 0) {
            return Result.error("该商品已经加入购物车！");
        }
        cartMapper.insert(product);
        return Result.success("商品成功加入购物车！");
    }

    public List<CartItem>selectAll(){
        return cartMapper.selectAll();
    }
    public void update(CartItem cartItem) {
        cartMapper.update(cartItem);
    }
    public void delete(Integer productId) {
        cartMapper.delete(productId);
    }
}

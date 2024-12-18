package com.example.controller;

import com.example.common.Result;
import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
@Resource
    private CartService cartService;
@PostMapping("/insert")
    public Result insert(@RequestBody Product product) {
        cartService.insert(product);
        return Result.success();
}
@GetMapping("/selectAll")
    public Result selectAll() {
    List<CartItem> cartItems = cartService.selectAll();
    return Result.success(cartItems);
}
    @PostMapping("/increasequantity")
    public void increasequantity(@RequestBody CartItem cartItem){
       cartService.update(cartItem);
    }
    @PostMapping("/decreasequantity")
    public void decreasequantity(@RequestBody CartItem cartItem){
        cartService.update(cartItem);
    }
    // 删除购物车中的菜品
    @DeleteMapping("/delete/{productId}")
    public void removeFromCart(@PathVariable Integer productId) {
        cartService.delete(productId);
    }

}

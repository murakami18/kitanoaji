package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.CartItem;

@Mapper
public interface CartItemMapper {

	// 特定のカートに特定の商品がすでに入っているか確認する 
	@Select("SELECT cart_id AS cartId, product_id AS productId, quantity FROM CartItems " +
			"WHERE cart_id = #{cartId} AND product_id = #{productId}")
	CartItem findByCartIdAndProductId(@Param("cartId") int cartId, @Param("productId") int productId);

	// カートに新しい商品を追加する 
	@Insert("INSERT INTO CartItems (cart_id, product_id, quantity) VALUES (#{cartId}, #{productId}, #{quantity})")
	void insert(CartItem cartItem);

	// すでにある商品の数量を更新する 
	@Update("UPDATE CartItems SET quantity = #{quantity} WHERE cart_id = #{cartId} AND product_id = #{productId}")
	void updateQuantity(@Param("cartId") int cartId, @Param("productId") int productId,
			@Param("quantity") int quantity);
}

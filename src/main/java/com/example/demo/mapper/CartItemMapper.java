package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.CartItem;

@Mapper
public interface CartItemMapper {

	// 特定のカートに特定の商品がすでに入っているか確認する 
	@Select("SELECT cart_id AS cartId, product_id AS productId, quantity FROM cart_items " +
			"WHERE cart_id = #{cartId} AND product_id = #{productId}")
	CartItem findByCartIdAndProductId(@Param("cartId") int cartId, @Param("productId") int productId);

	// カートに新しい商品を追加する 
	@Insert("INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (#{cartId}, #{productId}, #{quantity})")
	void insert(CartItem cartItem);

	// すでにある商品の数量を更新する 
	@Update("UPDATE cart_items SET quantity = #{quantity} WHERE cart_id = #{cartId} AND product_id = #{productId}")
	void updateQuantity(@Param("cartId") int cartId, @Param("productId") int productId,
			@Param("quantity") int quantity);

	//カートIDから全商品を取得するメソッド
	@Select("SELECT ci.cart_id AS cartId, ci.product_id AS productId, ci.quantity, p.name, p.price " +
			"FROM cart_items ci " +
			"JOIN products p ON ci.product_id = p.id " +
			"WHERE ci.cart_id = #{cartId} " +
			"ORDER BY ci.product_id ASC")
	List<CartItem> findByCartId(int cartId);

	//データベースから商品を削除するメソッド
	@Delete("DELETE FROM cart_items WHERE cart_id = #{cartId} AND product_id = #{productId}")
	void deleteByCartIdAndProductId(int cartId, int productId);

	// カート内の全商品を削除する
	@Delete("""
			    DELETE FROM cart_items
			    WHERE cart_id = #{cartId}
			""")
	void clearCartItems(@Param("cartId") int cartId);
}

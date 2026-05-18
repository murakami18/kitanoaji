package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entity.Cart;

@Mapper
public interface CartMapper {

	@Select("""
				SELECT
					id,
					user_id AS userId,
					game_result AS gameResult
				FROM carts
				WHERE user_id = #{userId}
			""")
	Cart findByUserId(int userId);

	@Insert("""
				INSERT INTO carts (
					user_id,
					game_result
				)
				VALUES (
					#{userId},
					#{gameResult}
				)
			""")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Cart cart);

	// ルーレット結果保存
	@Update("""
				UPDATE carts
				SET game_result = #{gameResult}
				WHERE id = #{cartId}
			""")
	void updateGameResult(
			@Param("cartId") int cartId,
			@Param("gameResult") String gameResult);
}
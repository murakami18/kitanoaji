package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.example.demo.entity.Cart;

@Mapper
public interface CartMapper {

	// ユーザーIDからカート情報を取得する 
	@Select("SELECT id, user_id AS userId, game_result AS gameResult FROM Carts WHERE user_id = #{userId}")
	Cart findByUserId(int userId);

	// 新しいカートを作成する 
	// Optionsを使用することで、DBで自動採番された id を Entity に書き戻せます
	@Insert("INSERT INTO Carts (user_id, game_result) VALUES (#{userId}, #{gameResult})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Cart cart);
}

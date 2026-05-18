package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {
	boolean existsByEmail(String email);

	/** メールアドレスでユーザを検索する */
	@Select("SELECT * FROM users WHERE email = #{email}")
	User findByEmail(String email);

	/** ユーザを登録する */

	@Insert("""
			INSERT INTO users
			(name, email, password, category_id, region_id)
			VALUES
			(#{name}, #{email}, #{password}, #{categoryId}, #{regionId})
			""")
	void insert(User user);
}

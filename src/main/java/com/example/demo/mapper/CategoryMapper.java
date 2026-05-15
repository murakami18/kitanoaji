package com.example.demo.mapper;

import java.util.Locale.Category;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
	Category findById(Integer id);

}

package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Category;

@Mapper
public interface CategoryMapper {
	Category findById(Integer id);

}

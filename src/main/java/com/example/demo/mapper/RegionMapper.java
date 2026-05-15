package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Region;

@Mapper
public interface RegionMapper {
	Region findById(Integer id);
}

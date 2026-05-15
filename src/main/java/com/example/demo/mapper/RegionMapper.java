package com.example.demo.mapper;

import javax.swing.plaf.synth.Region;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMapper {
	Region findById(Integer id);
}

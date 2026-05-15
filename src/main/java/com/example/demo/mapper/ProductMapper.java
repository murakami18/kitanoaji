package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Product;

@Mapper
public interface ProductMapper {

	/** 全商品を取得する */
	List<Product> findAll();

	//**カテゴリIDを取得する*/
	List<Product> findByCategoryId(Integer categoryId);

	//*リージョンIDを取得する*/
	List<Product> findByRegionId(Integer regionId);

	//*リージョンIDとカテゴリIDを取得する*/
	List<Product> findByRegionIdAndCategoryId(Integer regionId, Integer categoryId);

	/** IDで商品を1件取得する */
	Product findById(int id);
}

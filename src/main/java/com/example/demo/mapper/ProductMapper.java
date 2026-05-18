package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 💡 追加

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

	// ==========================================
	// 💡 以下に複数カテゴリ検索用のメソッドを追加
	// ==========================================

	/** 複数のカテゴリID（のいずれか）に合致する商品を取得する */
	List<Product> findByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

	/** リージョンIDと複数のカテゴリIDに合致する商品を取得する */
	List<Product> findByRegionIdAndCategoryIds(
			@Param("regionId") Integer regionId,
			@Param("categoryIds") List<Integer> categoryIds);
}
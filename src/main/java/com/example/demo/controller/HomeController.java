package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.RegionMapper;

@Controller
public class HomeController {
	private final ProductMapper productMapper;
	private final RegionMapper regionMapper;
	private final CategoryMapper categoryMapper;

	public HomeController(ProductMapper productMapper, RegionMapper regionMapper, CategoryMapper categoryMapper) {
		this.productMapper = productMapper;
		this.regionMapper = regionMapper;
		this.categoryMapper = categoryMapper;
	}

	@GetMapping("/home")
	public String homeView(@RequestParam(name = "regionId", required = false) Integer regionId,
			@RequestParam(name = "categoryId", required = false) Integer categoryId, Model model) {
		List<Product> products;

		if (regionId != null && categoryId != null) {
			// regionId・categoryId 両方指定された場合は AND 条件で絞り込み
			products = productMapper.findByRegionIdAndCategoryId(regionId, categoryId);

		} else if (regionId != null) {
			// regionId のみ指定
			products = productMapper.findByRegionId(regionId);

		} else if (categoryId != null) {
			// categoryId のみ指定
			products = productMapper.findByCategoryId(categoryId);

		} else {
			// 両方未指定の場合は全件取得
			products = productMapper.findAll();
		}
		// 絞り込み中のリージョン名をバナー表示用にモデルへ渡す
		if (regionId != null) {
			Region selectedRegion = regionMapper.findById(regionId);
			model.addAttribute("selectedRegion", selectedRegion);
		}

		// 絞り込み中のカテゴリー名をバナー表示用にモデルへ渡す
		if (categoryId != null) {
			Category selectedCategory = categoryMapper.findById(categoryId);
			model.addAttribute("selectedCategory", selectedCategory);
		}
		model.addAttribute("products", products);
		return "home";
	}

}

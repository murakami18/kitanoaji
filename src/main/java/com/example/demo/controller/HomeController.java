package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

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
	public String homeView(
			@RequestParam(name = "regionId", required = false) Integer regionId,
			@RequestParam(name = "categoryIds", required = false) List<Integer> categoryIds,
			HttpSession session,
			Model model) {

		List<Product> products;
		boolean hasCategories = (categoryIds != null && !categoryIds.isEmpty());

		if (regionId != null && hasCategories) {
			// regionId と 複数の categoryIds で絞り込み
			products = productMapper.findByRegionIdAndCategoryIds(regionId, categoryIds);
		} else if (regionId != null) {
			// regionId のみ指定
			products = productMapper.findByRegionId(regionId);
		} else if (hasCategories) {
			// 複数の categoryIds のみ指定
			products = productMapper.findByCategoryIds(categoryIds);
		} else {
			// 両方未指定の場合は全件取得
			products = productMapper.findAll();
		}

		// 絞り込み中のリージョン名をバナー表示用にモデルへ渡す
		if (regionId != null) {
			Region selectedRegion = regionMapper.findById(regionId);
			model.addAttribute("selectedRegion", selectedRegion);
		}

		// 絞り込み中のカテゴリー（複数）をバナー表示用にモデルへ渡す
		if (hasCategories) {
			List<Category> selectedCategories = new ArrayList<>();
			for (Integer id : categoryIds) {
				Category cat = categoryMapper.findById(id);
				if (cat != null) {
					selectedCategories.add(cat);
				}
			}
			model.addAttribute("selectedCategories", selectedCategories);
		}

		model.addAttribute("products", products);
		return "home";
	}
}
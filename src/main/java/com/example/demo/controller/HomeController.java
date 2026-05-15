package com.example.demo.controller;

import java.util.ArrayList;
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
	public String homeView(
			@RequestParam(name = "regionId", required = false) Integer regionId,
			// 💡 引数を Integer から List<Integer> に変更し、名前を categoryIds に
			@RequestParam(name = "categoryIds", required = false) List<Integer> categoryIds,
			Model model) {

		List<Product> products;

		// 💡 カテゴリが空リストで送られてきた場合（[]）の扱いを null と同じにするための判定
		boolean hasCategories = (categoryIds != null && !categoryIds.isEmpty());

		if (regionId != null && hasCategories) {
			// regionId と 複数の categoryIds で絞り込み（新メソッド）
			products = productMapper.findByRegionIdAndCategoryIds(regionId, categoryIds);

		} else if (regionId != null) {
			// regionId のみ指定
			products = productMapper.findByRegionId(regionId);

		} else if (hasCategories) {
			// 複数の categoryIds のみ指定（新メソッド）
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

		// 💡 絞り込み中のカテゴリー（複数）をバナー表示用にモデルへ渡す
		if (hasCategories) {
			List<Category> selectedCategories = new ArrayList<>();
			for (Integer id : categoryIds) {
				Category cat = categoryMapper.findById(id);
				if (cat != null) {
					selectedCategories.add(cat);
				}
			}
			// 修正後のHTMLで使う「selectedCategories」という名前でモデルに追加
			model.addAttribute("selectedCategories", selectedCategories);
		}

		model.addAttribute("products", products);
		return "home";
	}
}
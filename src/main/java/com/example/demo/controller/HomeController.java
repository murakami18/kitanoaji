package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.entity.User;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.RegionMapper;
import com.example.demo.service.UseGachaService;

@Controller
public class HomeController {

	private final ProductMapper productMapper;
	private final RegionMapper regionMapper;
	private final CategoryMapper categoryMapper;
	private final UseGachaService useGachaService;

	public HomeController(ProductMapper productMapper, RegionMapper regionMapper, CategoryMapper categoryMapper,
			UseGachaService useGachaService) {
		this.productMapper = productMapper;
		this.regionMapper = regionMapper;
		this.categoryMapper = categoryMapper;
		this.useGachaService = useGachaService;

	}

	@GetMapping("/home")
	public String homeView(
			@RequestParam(name = "regionId", required = false) Integer regionId,
			@RequestParam(name = "categoryIds", required = false) List<Integer> categoryIds,
			HttpSession session,
			Model model) {

		useGachaService.setFalse(session);

		boolean hasCategories = (categoryIds != null && !categoryIds.isEmpty());

		// 両方のパラメータが存在する場合、カテゴリ優先でリダイレクト（URLをクリーンにする）
		if (regionId != null && hasCategories) {
			String params = categoryIds.stream()
					.map(id -> "categoryIds=" + id)
					.collect(Collectors.joining("&"));
			return "redirect:/home?" + params;
		}

		// 排他制御：どちらか一方のみ有効にする（カテゴリ優先）
		if (hasCategories) {
			regionId = null;
		} else {
			categoryIds = null;
			hasCategories = false;
		}

		User loginUser = (User) session.getAttribute("loginUser");
		boolean isLoggedIn = (loginUser != null);

		List<Product> recommendProducts = new ArrayList<>();

		if (isLoggedIn) {
			Integer userCategoryId = loginUser.getCategoryId();

			if (userCategoryId != null) {
				List<Integer> searchCategoryIds = Collections.singletonList(userCategoryId);
				recommendProducts = productMapper.findByCategoryIds(searchCategoryIds);
			}

			if (recommendProducts == null || recommendProducts.isEmpty()) {
				List<Product> allProducts = productMapper.findAll();
				if (allProducts != null && !allProducts.isEmpty()) {
					List<Product> shuffledList = new ArrayList<>(allProducts);
					Collections.shuffle(shuffledList);
					recommendProducts = shuffledList.stream().limit(10).collect(Collectors.toList());
				} else {
					recommendProducts = new ArrayList<>();
				}
			}
		} else {
			recommendProducts = null;
		}
		model.addAttribute("recommendProducts", recommendProducts);

		List<Product> products;

		if (regionId != null) {
			products = productMapper.findByRegionId(regionId);
		} else if (hasCategories) {
			products = productMapper.findByCategoryIds(categoryIds);
		} else {
			products = productMapper.findAll();
		}
		model.addAttribute("products", products);

		if (regionId != null) {
			Region selectedRegion = regionMapper.findById(regionId);
			model.addAttribute("selectedRegion", selectedRegion);
		}

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

		return "home";
	}
}
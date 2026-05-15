package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.RegionMapper;

@Controller
public class ProductController {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private RegionMapper regionMapper;

	@GetMapping("/product/compare")
	public String showComparePage() {
		return "product_compare";
	}

	@GetMapping("/product/{id}")
	public String showDetail(@PathVariable Integer id, Model model) {

		Product product = productMapper.findById(id);

		Category category = categoryMapper.findById(product.getCategoryId());

		Region region = regionMapper.findById(product.getRegionId());

		model.addAttribute("product", product);

		model.addAttribute("categoryName", category.getName());

		model.addAttribute("regionName", region.getName());

		return "product_detail";
	}
}
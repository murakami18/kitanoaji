package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String compare(@RequestParam int id, Model model) {

		Product base = productMapper.findById(id);

		List<Product> rawList = productMapper.findByCategoryId(base.getCategoryId());

		List<Product> list = (rawList != null) ? rawList : new ArrayList<Product>();
		List<Product> candidates = list.stream()
				.filter((Product p) -> p.getId() != id)
				.sorted(Comparator.comparingInt((Product p) -> Math.abs(p.getPrice() - base.getPrice())))
				.limit(2)
				.collect(Collectors.toList());

		List<Product> compareList = new ArrayList<>();
		compareList.add(base);
		compareList.addAll(candidates);

		Category category = categoryMapper.findById(base.getCategoryId());
		Region region = regionMapper.findById(base.getRegionId());

		model.addAttribute("compareList", compareList);
		model.addAttribute("categoryName", category.getName());
		model.addAttribute("regionName", region.getName());

		return "compare";
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
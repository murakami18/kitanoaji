package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;

@Controller
public class HomeController {
	private final ProductMapper productMapper;

	public HomeController(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	@GetMapping("/home")
	public String homeView(Model model) {
		List<Product> products = productMapper.findAll();
		model.addAttribute("products", products);
		return "home";
	}

}

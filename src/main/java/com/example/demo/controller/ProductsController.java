package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsController {

	@GetMapping("/products")
	public String showProducts(Model model) {
		return "products/list";
	}
}
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;

@Service
public class ProductService {
	@Autowired
	private ProductMapper productMapper;

	public Product findById(int id) {
		return productMapper.findById(id);
	}

}
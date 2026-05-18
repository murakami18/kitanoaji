package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

@Controller
public class MyPageController {

	@Autowired
	private UserMapper userMapper;

	@GetMapping("/mypage")
	public String showMyPage(Model model) {

		User user = userMapper.findById(1);

		System.out.println(user);
		System.out.println(userMapper.findByEmail("test@test.com"));

		model.addAttribute("user", user);

		return "mypage";
	}

}
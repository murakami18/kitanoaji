package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.User;

@Controller
public class MyPageController {

	@GetMapping("/mypage")
	public String showMyPage(HttpSession session) {

		// 1. セッションからログインユーザー情報を取得
		User loginUser = (User) session.getAttribute("loginUser");

		// 2. ログイン状態でなければ（nullなら） /login にリダイレクト
		if (loginUser == null) {
			return "redirect:/login";
		}

		return "mypage";
	}

}
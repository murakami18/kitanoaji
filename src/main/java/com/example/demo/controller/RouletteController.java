package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.RouletteService;

@Controller
public class RouletteController {

	private final RouletteService rouletteService;

	public RouletteController(
			RouletteService rouletteService) {

		this.rouletteService = rouletteService;
	}

	@PostMapping("/roulette")
	public String roulette(
			HttpSession session,
			Model model) {

		User loginUser = (User) session.getAttribute("loginUser");

		// ログインしていない
		if (loginUser == null) {

			return "redirect:/login";
		}

		String result = rouletteService.challenge(
				loginUser.getId());

		session.setAttribute(
				"rouletteResult",
				result);

		model.addAttribute(
				"result",
				result);

		// 既に実行済み
		if (result.equals("already")) {
			return "already";
		}

		// ハズレ
		if (result.equals("lose")) {
			return "roulette_lose";
		}

		// 当たり
		if (result.equals("win")) {
			return "roulette_win";
		}

		// 通常
		return "purchase_confirm";
	}
}
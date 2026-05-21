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

		boolean useRoulette = rouletteService.init(session);

		String result = rouletteService.challenge(
				loginUser.getId(), session);

		model.addAttribute(
				"result",
				result);

		// 既に実行済み「２かいはできないよ」
		if (result.equals("already")) {
			return "roulette/already";
		}

		// ハズレ
		if (result.equals("lose")) {
			return "roulette/lose";
		}

		// 当たり
		if (result.equals("win")) {
			rouletteService.setTrue(session);
			return "roulette/win";
		}

		if (result.equals("normal")) {
			return "purchase_confirm";
		}

		// 通常
		return "purchase/complete";
	}
}
package com.example.demo.service;

import java.util.Random;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.demo.controller.UserController;
import com.example.demo.entity.Cart;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;

@Service
public class RouletteService {

	//    private final UserController userController;

	private final CartMapper cartMapper;
	private final CartItemMapper cartItemMapper;
	private final UseGachaService useGachaService;
	private static final String SESSION_KEY = "useRoulette";

	public RouletteService(
			CartMapper cartMapper,
			CartItemMapper cartItemMapper,
			UseGachaService useGachaService, UserController userController) {

		this.cartMapper = cartMapper;
		this.cartItemMapper = cartItemMapper;
		this.useGachaService = useGachaService;
		//		this.userController = userController;
	}

	public boolean init(HttpSession session) {
		Object value = session.getAttribute(SESSION_KEY);

		if (value == null) {
			session.setAttribute(SESSION_KEY, false);
			return false;
		}

		return (boolean) value;
	}

	public boolean get(HttpSession session) {
		return init(session);
	}

	public boolean setTrue(HttpSession session) {
		session.setAttribute(SESSION_KEY, true);
		return true;
	}

	//useGachaがfalseの時のメソッド
	public boolean setFalse(HttpSession session) {
		session.setAttribute(SESSION_KEY, false);
		return false;
	}

	public void clear(HttpSession session) {
		session.removeAttribute("useRoulette");
	}

	public String challenge(int userId, HttpSession session) {

		Cart cart = cartMapper.findByUserId(userId);

		// カートが存在しない場合は新規作成
		if (cart == null) {

			cart = new Cart();

			cart.setUserId(userId);

			// 初期状態
			cart.setGameResult(null);

			cartMapper.insert(cart);
		}

		// 既に実行済み
		if (cart.getGameResult() != null) {
			return "already";
		}

		Random random = new Random();

		int value = random.nextInt(100);

		// 10%
		if (value < 10) {

			cartMapper.updateGameResult(
					cart.getId(),
					"win");

			return "win";
		}

		// 10%
		if (value < 20) {

			cartMapper.updateGameResult(
					cart.getId(),
					"lose");

			// カートの商品削除
			cartItemMapper.clearCartItems(
					cart.getId());

			useGachaService.clear(session);

			return "lose";
		}

		// 80%
		cartMapper.updateGameResult(
				cart.getId(),
				"normal");

		return "normal";
	}
}

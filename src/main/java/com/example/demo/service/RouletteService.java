package com.example.demo.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Cart;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;

@Service
public class RouletteService {

	private final CartMapper cartMapper;
	private final CartItemMapper cartItemMapper;

	public RouletteService(
			CartMapper cartMapper,
			CartItemMapper cartItemMapper) {

		this.cartMapper = cartMapper;
		this.cartItemMapper = cartItemMapper;
	}

	public String challenge(int userId) {

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

		int value = random.nextInt(11, 20);

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

			return "lose";
		}

		// 80%
		cartMapper.updateGameResult(
				cart.getId(),
				"normal");

		return "normal";
	}
}

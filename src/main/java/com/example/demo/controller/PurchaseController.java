package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

	private final CartService cartService;
	private final OrderService orderService;

	public PurchaseController(
			CartService cartService,
			OrderService orderService) {

		this.cartService = cartService;
		this.orderService = orderService;
	}

	/**
	 * 購入画面（確認画面）
	 */
	@GetMapping
	public String purchase(
			HttpSession session,
			Model model) {

		// ログインユーザー取得
		User loginUser = (User) session.getAttribute("loginUser");

		// 未ログイン
		if (loginUser == null) {
			return "redirect:/login";
		}

		// カート商品取得
		List<CartItem> cartItems = cartService.getCartFromDb(
				loginUser.getId());

		// カートが空
		if (cartItems == null || cartItems.isEmpty()) {
			return "redirect:/cart";
		}

		// 合計金額
		int totalPrice = cartItems.stream()
				.mapToInt(CartItem::getSubtotal)
				.sum();

		// カート情報取得
		Cart cart = cartService.getCartByUserId(
				loginUser.getId());

		// HTMLへ渡す
		model.addAttribute(
				"cartItems",
				cartItems);

		model.addAttribute(
				"totalPrice",
				totalPrice);

		model.addAttribute(
				"cart",
				cart);

		return "purchase/checkout";
	}

	/**
	 * 購入確定
	 */
	@PostMapping("/check")
	public String confirm(
			@RequestParam(name = "customerName") String customerName,

			@RequestParam(name = "address") String address,

			@RequestParam(name = "useGacha", required = false) boolean useGacha,

			HttpSession session,
			Model model) {

		// ログインユーザー取得
		User loginUser = (User) session.getAttribute("loginUser");

		// 未ログイン
		if (loginUser == null) {
			return "redirect:/login";
		}

		// カート取得
		List<CartItem> cart = cartService.getCartFromDb(
				loginUser.getId());

		// カート空
		if (cart.isEmpty()) {
			return "redirect:/cart";
		}

		// 注文処理
		int orderId = orderService.placeOrder(
				loginUser.getId(),
				cart);

		// カート全削除
		cartService.removeAllItemFromDb(
				loginUser.getId());

		// 完了画面へ
		model.addAttribute(
				"orderId",
				orderId);

		return "purchase/complete";
	}
}
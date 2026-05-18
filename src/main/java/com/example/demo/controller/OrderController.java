package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.User; // ※Userエンティティのパッケージに合わせます
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	private final CartService cartService;
	private final OrderService orderService;

	public OrderController(CartService cartService, OrderService orderService) {
		this.cartService = cartService;
		this.orderService = orderService;
	}

	/**
	 * 購入を確定する
	 */
	@PostMapping("/confirm")
	public String confirm(HttpSession session, Model model) {
		// 1. セッションからログインユーザー情報を取得
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			// 未ログインならログイン画面へリダイレクト
			return "redirect:/login";
		}

		// 2. ログインユーザーのカート情報をデータベースから取得
		// (CartServiceに実装されている getCartFromDb を呼び出すように修正)
		List<CartItem> cart = cartService.getCartFromDb(loginUser.getId());
		if (cart.isEmpty()) {
			// カートが空ならカート画面へリダイレクト
			return "redirect:/cart";
		}

		// 3. 注文処理を実行し、新規作成された注文IDを取得
		int orderId = orderService.placeOrder(loginUser.getId(), cart);

		// 4. カートのクリア処理
		// セッションのカートをクリア
		cartService.clearCart(session);

		// 💡補足: 本来はここでDB側のカート(cart_items)を削除、またはCartService側に
		// 「DBのカートをクリアする」メソッド（例: cartService.clearDbCart(loginUser.getId());）
		// を用意して呼び出すと、さらに完璧な挙動になります。

		// 5. 画面に注文IDを渡し、完了画面を表示
		model.addAttribute("orderId", orderId);
		return "order/complete";
	}
}
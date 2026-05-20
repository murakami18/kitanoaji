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
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

	private final CartService cartService;
	private final OrderService orderService;
	private final ProductMapper productMapper;

	public PurchaseController(
			CartService cartService,
			OrderService orderService, ProductMapper productMapper) {

		this.cartService = cartService;
		this.orderService = orderService;
		this.productMapper = productMapper;
	}

	/**
	 * 購入画面（確認画面）
	 */
	@GetMapping
	public String purchase(
			@RequestParam(name = "useGacha", defaultValue = "false") boolean useGacha,
			HttpSession session, Model model) {

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
		// 3. 合計金額の計算
		int totalPrice = cartItems.stream().mapToInt(CartItem::getSubtotal).sum();

		// ★ガチャチェックが入っていたら+100円
		if (useGacha) {
			totalPrice += 100;
		}
		Cart cart = cartService.getCartByUserId(loginUser.getId());

		// 4. HTML(Thymeleaf)にデータを渡す
		model.addAttribute(
				"cartItems",
				cartItems);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("cart", cart);
		model.addAttribute("useGacha", useGacha); // ★購入確認画面にも引き継ぐ
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

		// 注文処理を実行
		int orderId = orderService.placeOrder(loginUser.getId(), cart);

		// カート全削除
		cartService.removeAllItemFromDb(
				loginUser.getId());

		// 完了画面へ
		model.addAttribute(
				"orderId",
				orderId);

		return "purchase/complete";
	}

	@PostMapping("/add")
	public String addToCart(
			@RequestParam("productId") int productId,
			@RequestParam(value = "quantity", defaultValue = "1") int quantity,
			HttpSession session) {

		Product product = productMapper.findById(productId);
		if (product != null) {
			User loginUser = (User) session.getAttribute("loginUser");
			if (loginUser != null) {
				// ログインしている場合：データベースへ保存
				cartService.addItemToDb(loginUser.getId(), product, quantity);
			} else {
				// ログインしていない場合：セッションへ保存
				cartService.addItemToSession(session, product, quantity);
			}
		}
		return "redirect:/purchase";
	}
}
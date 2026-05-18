package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;
	private final ProductMapper productMapper;

	public CartController(CartService cartService, ProductMapper productMapper) {
		this.cartService = cartService;
		this.productMapper = productMapper;
	}

	/** カート一覧を表示する */
	@GetMapping
	public String showCart(HttpSession session, Model model) {
		List<CartItem> cart = new ArrayList<>();

		// セッションからログインユーザー情報を取得
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null) {
			// ログインしている場合：データベースからカート情報を取得
			cart = cartService.getCartFromDb(loginUser.getId());
		} else {
			// ログインしていない場合：セッションからカート情報を取得
			cart = cartService.getCartFromSession(session);
		}

		// 合計金額の計算
		int total = 0;
		if (cart != null && !cart.isEmpty()) {
			total = cart.stream().mapToInt(CartItem::getSubtotal).sum();
		}

		model.addAttribute("cart", cart);
		model.addAttribute("total", total);
		return "cart/index";
	}

	/** カートに商品を追加する */
	@PostMapping("/add")
	public String addToCart(
			@RequestParam("productId") int productId,
			@RequestParam(value = "quantity", defaultValue = "1") int quantity, // ★引数に個数(quantity)を追加
			HttpSession session) {

		Product product = productMapper.findById(productId);

		if (product != null) {
			User loginUser = (User) session.getAttribute("loginUser");

			if (loginUser != null) {
				// ログインしている場合：データベースへ保存（引数に quantity を追加）
				cartService.addItemToDb(loginUser.getId(), product, quantity);
			} else {
				// ログインしていない場合：セッションへ保存（引数に quantity を追加）
				cartService.addItemToSession(session, product, quantity);
			}
		}
		return "redirect:/cart";
	}

	/** カートから商品を削除する */
	@PostMapping("/remove")
	public String removeFromCart(@RequestParam("productId") int productId, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null) {
			// ログインしている場合：データベースから削除
			cartService.removeItemFromDb(loginUser.getId(), productId);
		} else {
			// ログインしていない場合：セッションから削除
			cartService.removeItemFromSession(session, productId);
		}

		return "redirect:/cart";
	}
}
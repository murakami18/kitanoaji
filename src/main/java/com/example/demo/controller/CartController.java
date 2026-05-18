package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.CartService;

@Controller
@RequestMapping("/cart") // パスは適宜合わせてください
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
		List<CartItem> cart = cartService.getCart(session);
		int total = cart.stream().mapToInt(CartItem::getSubtotal).sum();
		model.addAttribute("cart", cart);
		model.addAttribute("total", total);
		return "cart/index";
	}

	@PostMapping("/add")
	public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
		Product product = productMapper.findById(productId);

		if (product != null) {
			// セッションからログインユーザー情報を取得
			// ※Userクラスはご自身のEntityクラス名に合わせてください
			User loginUser = (User) session.getAttribute("loginUser");

			if (loginUser != null) {
				// ログインしている場合：データベースへ保存
				//cartService.addItemToDb(loginUser.getId(), product);
			} else {
				// ログインしていない場合：セッションへ保存
				cartService.addItemToSession(session, product);
			}
		}
		return "redirect:/cart";
	}

	/** カートから商品を削除する */
	@PostMapping("/remove")
	public String removeFromCart(@RequestParam("productId") int productId,
			HttpSession session) {
		cartService.removeItem(session, productId);
		return "redirect:/cart";
	}
}

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
import com.example.demo.service.RouletteService;
import com.example.demo.service.UseGachaService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

	private final CartService cartService;
	private final OrderService orderService;
	private final UseGachaService useGachaService;
	private final ProductMapper productMapper;
	private final RouletteService rouletteService;

	public PurchaseController(
			CartService cartService,
			OrderService orderService,
			UseGachaService useGachaService,
			ProductMapper productMapper,
			RouletteService rouletteService) {

		this.cartService = cartService;
		this.orderService = orderService;
		this.useGachaService = useGachaService;
		this.productMapper = productMapper;
		this.rouletteService = rouletteService;
	}

	/**
	 * 購入画面（確認画面）
	 */
	@GetMapping
	public String purchase(
			@RequestParam(name = "useGacha", defaultValue = "false") boolean useGacha,
			HttpSession session,
			Model model) {

		//		boolean gachaResult = useGachaService.init(session);
		//
		//		if (useGacha) {
		//			gachaResult = useGachaService.setTrue(session);
		//		}
		//		//		if (!useGacha) {
		//		//			gachaResult = useGachaService.setFalse(session);
		//		//		}

		// ログインユーザー取得
		User loginUser = (User) session.getAttribute("loginUser");

		// 未ログイン
		if (loginUser == null) {
			return "redirect:/login";
		}

		//ログインしてなかったときはsessionに値が入らない

		boolean gachaResult = useGachaService.init(session);

		if (useGacha) {
			gachaResult = useGachaService.setTrue(session);
		}

		// カート商品取得
		List<CartItem> cartItems = cartService.getCartFromDb(
				loginUser.getId());

		// カートが空
		if (cartItems == null || cartItems.isEmpty()) {
			return "redirect:/cart";
		}

		// ルーレット結果取得
		Cart cart = cartService.getCartByUserId(
				loginUser.getId());

		// 商品合計
		int totalPrice = cartItems.stream()
				.mapToInt(CartItem::getSubtotal)
				.sum();

		// ★ winなら半額
		//		if (cart != null && "win".equals(cart.getGameResult())) {
		//			totalPrice = totalPrice / 2;
		//		}

		if (cart != null && rouletteService.get(session)) {
			totalPrice = totalPrice / 2;
		}

		// ★ ガチャ利用なら最後に+100円
		if (gachaResult) {
			totalPrice += 100;
		}

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

		model.addAttribute(
				"gachaResult",
				gachaResult);

		return "purchase/checkout";
	}

	/**
	 * 購入確定
	 */
	@PostMapping("/check")
	public String confirm(
			@RequestParam(name = "customerName") String customerName,

			@RequestParam(name = "address") String address,

			@RequestParam(name = "useGacha", required = false, defaultValue = "false") boolean useGacha,

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

		// ルーレット結果リセット
		cartService.resetGameResult(
				loginUser.getId());

		useGachaService.clear(session);

		rouletteService.clear(session);

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
				// DB保存
				cartService.addItemToDb(
						loginUser.getId(),
						product,
						quantity);

			} else {
				// セッション保存
				cartService.addItemToSession(
						session,
						product,
						quantity);
			}
		}

		return "redirect:/purchase";
	}
}
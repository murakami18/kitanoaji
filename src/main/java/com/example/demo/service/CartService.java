package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;

@Service
public class CartService {

	private static final String CART_KEY = "cart";

	// DB操作用のMapperを注入します
	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private CartItemMapper cartItemMapper;

	/** セッションからカートを取得する（存在しなければ空のリストを返す） */
	@SuppressWarnings("unchecked")
	public List<CartItem> getCart(HttpSession session) {
		List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_KEY);
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute(CART_KEY, cart);
		}
		return cart;
	}

	/** カートに商品を追加する（同じ商品が既にあれば数量を増やす） */
	public void addItemToSession(HttpSession session, Product product) {
		List<CartItem> cart = getCart(session);
		for (CartItem item : cart) {
			if (item.getProductId() == product.getId()) {
				item.incrementQuantity();
				return;
			}
		}
		cart.add(new CartItem(product.getId(), product.getName(), product.getPrice()));
	}

	/**
	 * ログインユーザーのカート情報をデータベースに保存・更新します。
	 */
	@Transactional // 複数のテーブルを操作するため、トランザクション管理を行います
	public void addItemToDb(int userId, Product product) {

		// 1. ユーザーに紐づくカート(Cartsテーブル)が存在するか確認
		Cart cart = cartMapper.findByUserId(userId);

		// 2. カートが存在しない場合は新しく作成
		if (cart == null) {
			cart = new Cart();
			//cart.setUserId(userId);
			//cart.setGameResult("NORMAL"); // 初期状態を設定
			cartMapper.insert(cart);
			// ※CartMapperの@Optionsにより、自動採番されたIDがcart.getId()で取得可能になります
		}

		// 3. カート内に同じ商品(CartItemsテーブル)がすでに入っているか確認
		CartItem existingItem = cartItemMapper.findByCartIdAndProductId(cart.getId(), product.getId());

		if (existingItem != null) {
			// 4. すでに存在する場合は、数量(quantity)を +1 して更新
			int newQuantity = existingItem.getQuantity() + 1;
			//cartItemMapper.updateQuantity(cart.getId(), product.getId(), newQuantity);
		} else {
			// 5. 存在しない場合は、新しい明細として追加（数量1）
			CartItem newItem = new CartItem();
			newItem.setProductId(cart.getId()); // Cartsテーブルと紐づけるためのID[cite: 1]
			newItem.setProductId(product.getId()); // Productsテーブルと紐づけるためのID[cite: 1]
			newItem.setQuantity(1);
			cartItemMapper.insert(newItem);
		}
	}

	/** カートから商品を削除する */
	public void removeItem(HttpSession session, int productId) {
		List<CartItem> cart = getCart(session);
		cart.removeIf(item -> item.getProductId() == productId);
	}

	/** カートを空にする */
	public void clearCart(HttpSession session) {
		session.removeAttribute(CART_KEY);
	}
}
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

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private CartItemMapper cartItemMapper;

	// ==========================================
	// セッション（未ログイン）用の処理
	// ==========================================

	/** セッションからカートを取得する（存在しなければ空のリストを返す） */
	@SuppressWarnings("unchecked")
	public List<CartItem> getCartFromSession(HttpSession session) {
		List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_KEY);
		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute(CART_KEY, cart);
		}
		return cart;
	}

	/** セッションのカートに商品を追加する */
	public void addItemToSession(HttpSession session, Product product) {
		List<CartItem> cart = getCartFromSession(session);
		for (CartItem item : cart) {
			if (item.getProductId() == product.getId()) {
				item.incrementQuantity();
				return;
			}
		}
		cart.add(new CartItem(product.getId(), product.getName(), product.getPrice()));
	}

	/** セッションのカートから商品を削除する */
	public void removeItemFromSession(HttpSession session, int productId) {
		List<CartItem> cart = getCartFromSession(session);
		cart.removeIf(item -> item.getProductId() == productId);
	}

	/** セッションのカートを空にする */
	public void clearCart(HttpSession session) {
		session.removeAttribute(CART_KEY);
	}

	// ==========================================
	// データベース（ログイン中）用の処理
	// ==========================================

	/**
	 * データベースからログインユーザーのカート情報を取得します。
	 */
	public List<CartItem> getCartFromDb(int userId) {
		// 1. ユーザーのカートが存在するか確認
		Cart cart = cartMapper.findByUserId(userId);

		if (cart == null) {
			// カートがまだ作られていなければ空のリストを返す
			return new ArrayList<>();
		}

		// 2. カートIDに紐づく商品リストを取得して返す
		return cartItemMapper.findByCartId(cart.getId());
	}

	/**
	 * ログインユーザーのカート情報をデータベースに保存・更新します。
	 */
	@Transactional
	public void addItemToDb(int userId, Product product) {
		Cart cart = cartMapper.findByUserId(userId);

		if (cart == null) {
			cart = new Cart();
			cart.setUserId(userId);
			cart.setGameResult("NORMAL");
			cartMapper.insert(cart);
		}

		CartItem existingItem = cartItemMapper.findByCartIdAndProductId(cart.getId(), product.getId());

		if (existingItem != null) {
			int newQuantity = existingItem.getQuantity() + 1;
			cartItemMapper.updateQuantity(cart.getId(), product.getId(), newQuantity);
		} else {
			CartItem newItem = new CartItem();
			newItem.setCartId(cart.getId());
			newItem.setProductId(product.getId());
			newItem.setQuantity(1);
			cartItemMapper.insert(newItem);
		}
	}

	/**
	 * データベースのカートから特定の商品を削除します。
	 */
	@Transactional
	public void removeItemFromDb(int userId, int productId) {
		Cart cart = cartMapper.findByUserId(userId);
		if (cart != null) {
			// カートが存在する場合のみ、該当商品を削除
			cartItemMapper.deleteByCartIdAndProductId(cart.getId(), productId);
		}
	}
}
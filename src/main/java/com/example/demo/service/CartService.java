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

	/** セッションのカートに商品を追加する（個数指定対応） */
	public void addItemToSession(HttpSession session, Product product, int quantity) { // ★ 引数に quantity を追加
		List<CartItem> cart = getCartFromSession(session);
		for (CartItem item : cart) {
			if (item.getProductId() == product.getId()) {
				// ★ 既存の数量に、画面から送られてきた数量を足す
				item.setQuantity(item.getQuantity() + quantity);
				return;
			}
		}

		// ★ 新規追加時、引数の quantity をセットしてインスタンス化
		// (※CartItemのコンストラクタが引数にquantityを取れる必要があります。詳細は後述)
		CartItem newItem = new CartItem(product.getId(), product.getName(), product.getPrice());
		newItem.setQuantity(quantity);
		cart.add(newItem);
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
	 * 
	 * データベースからログインユーザーのユーザーIdを取得
	 * 
	 */
	public Cart getCartByUserId(int userId) {

		return cartMapper.findByUserId(userId);
	}

	/**
	 * データベースからログインユーザーのカート情報を取得します。
	 */
	public List<CartItem> getCartFromDb(int userId) {
		Cart cart = cartMapper.findByUserId(userId);

		if (cart == null) {
			return new ArrayList<>();
		}

		return cartItemMapper.findByCartId(cart.getId());
	}

	/**
	 * ログインユーザーのカート情報をデータベースに保存・更新します（個数指定対応）。
	 */
	@Transactional
	public void addItemToDb(int userId, Product product, int quantity) { // ★ 引数に quantity を追加
		Cart cart = cartMapper.findByUserId(userId);

		if (cart == null) {
			cart = new Cart();
			cart.setUserId(userId);
			cart.setGameResult(null);
			cartMapper.insert(cart);
		}

		CartItem existingItem = cartItemMapper.findByCartIdAndProductId(cart.getId(), product.getId());

		if (existingItem != null) {
			// ★ 既存の数量 ＋ 画面から送られてきた数量
			int newQuantity = existingItem.getQuantity() + quantity;
			cartItemMapper.updateQuantity(cart.getId(), product.getId(), newQuantity);
		} else {
			CartItem newItem = new CartItem();
			newItem.setCartId(cart.getId());
			newItem.setProductId(product.getId());
			// ★ 新規追加時も 1 固定ではなく quantity をセット
			newItem.setQuantity(quantity);
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
			cartItemMapper.deleteByCartIdAndProductId(cart.getId(), productId);
		}
	}

	/**
	 * データベースのカートからすべての商品を削除します。
	 */
	@Transactional
	public void removeAllItemFromDb(int userId) {
		Cart cart = cartMapper.findByUserId(userId);

		cartItemMapper.clearCartItems(cart.getId());

	}

	// ==========================================
	// カートの統合（マージ）処理
	// ==========================================

	/**
	 * セッションカートの商品をデータベースのカートにマージ（統合）します。
	 * ログイン成功直後に呼び出してください。
	 */
	@Transactional
	public void mergeSessionCartToDb(HttpSession session, int userId) {
		// 1. セッションのカートを取得
		List<CartItem> sessionCart = getCartFromSession(session);

		// 2. セッションカートが空なら何もしないで終了
		if (sessionCart == null || sessionCart.isEmpty()) {
			return;
		}

		// 3. ユーザーのDBカートを取得（存在しなければ新規作成）
		Cart cart = cartMapper.findByUserId(userId);
		if (cart == null) {
			cart = new Cart();
			cart.setUserId(userId);
			cart.setGameResult(null);
			cartMapper.insert(cart);
		}

		// 4. セッションの商品を一つずつDBへ移行
		for (CartItem sessionItem : sessionCart) {
			CartItem dbItem = cartItemMapper.findByCartIdAndProductId(cart.getId(), sessionItem.getProductId());

			if (dbItem != null) {
				// 既にDBカートに同じ商品がある場合は、セッションの数量を「加算」する
				int newQuantity = dbItem.getQuantity() + sessionItem.getQuantity();
				cartItemMapper.updateQuantity(cart.getId(), sessionItem.getProductId(), newQuantity);
			} else {
				// DBカートにない場合は新規追加
				CartItem newItem = new CartItem();
				newItem.setCartId(cart.getId());
				newItem.setProductId(sessionItem.getProductId());
				newItem.setQuantity(sessionItem.getQuantity()); // セッションに入っていた数量をセット
				cartItemMapper.insert(newItem);
			}
		}

		// 5. 移行が完了したら、セッションのカートを削除して綺麗にする
		clearCart(session);
	}
}
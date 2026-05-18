package com.example.demo.entity;

public class CartItem {
	private int cartId;

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	private int productId;
	private String name;
	private int price;

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	private int quantity;

	public CartItem(int productId, String name, int price) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.quantity = 1;
	}

	public CartItem() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public int getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void incrementQuantity() {
		this.quantity++;
	}

	public int getSubtotal() {
		return price * quantity;
	}

	public void setId(int id) {
		// TODO 自動生成されたメソッド・スタブ

	}

}

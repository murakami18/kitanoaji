package com.example.demo.entity;

public class OrderItem {
	private int id;
	private int orderId;
	private int productId;
	private int productPrice;
	private int quantity;

	public OrderItem(int id, int orderId, int productId, int productPrice, int quantity) {
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}

	public int id() {
		return id;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getProductId() {
		return productId;
	}

	public int productPrice() {
		return productPrice;
	}

	public int getQuantity() {
		return quantity;
	}
}

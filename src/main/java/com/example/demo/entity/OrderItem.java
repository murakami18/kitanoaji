package com.example.demo.entity;

public class OrderItem {
	private int id;
	private int orderId;
	private int productId;
	private int productPrice;
	private int quantity;

	//	public OrderItem(int id, int orderId, int productId, int productPrice, int quantity) {
	//		this.id = id;
	//		this.orderId = orderId;
	//		this.productId = productId;
	//		this.productPrice = productPrice;
	//		this.quantity = quantity;
	//	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}

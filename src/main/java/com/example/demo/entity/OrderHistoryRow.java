package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 購入履歴画面の各明細行を表すエンティティクラス
 */
public class OrderHistoryRow {
	private int orderId;
	private LocalDateTime createdAt;
	private String productName;
	private BigDecimal price;
	private int quantity;
	private BigDecimal subtotal;

	public OrderHistoryRow() {
	}

	public OrderHistoryRow(int orderId, LocalDateTime createdAt, String productName,
			BigDecimal price, int quantity, BigDecimal subtotal) {
		this.orderId = orderId;
		this.createdAt = createdAt;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
		this.subtotal = subtotal;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
}
package com.example.demo.entity;

import java.time.LocalDateTime;

public class Order {
	private int id;
	private int userId;
	private LocalDateTime createdAt;
	private LocalDateTime deliveredAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliverdAt) {
		this.deliveredAt = deliveredAt;
	}
}

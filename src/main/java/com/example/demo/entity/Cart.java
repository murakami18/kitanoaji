package com.example.demo.entity;

public class Cart {
	private int id;
	private int userId;
	private String game_result;

	public String getGame_result() {
		return game_result;
	}

	public void setGame_result(String game_result) {
		this.game_result = game_result;
	}

	public Cart() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Cart(int id, int userId) {
		this.id = id;
		this.userId = userId;
	}

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

}

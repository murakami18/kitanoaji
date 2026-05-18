package com.example.demo.entity;

public class Cart {
	private int id;
	private int userId;
	private String gameResult;

	public String getGameResult() {
		return gameResult;
	}

	public void setGameResult(String gameResult) {
		this.gameResult = gameResult;
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


package com.example.demo.service;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
public class UseGachaService {

	private static final String SESSION_KEY = "useGacha";

	// 初期化（なければfalse）
	public boolean init(HttpSession session) {
		Object value = session.getAttribute(SESSION_KEY);

		if (value == null) {
			session.setAttribute(SESSION_KEY, false);
			return false;
		}

		return (boolean) value;
	}

	//	// トグル（true⇄false）
	//	public boolean toggle(HttpSession session) {
	//		boolean current = init(session);
	//		boolean next = !current;
	//		session.setAttribute(SESSION_KEY, next);
	//		return next;
	//	}

	//tureのメソッドとfalseのメソッドを作る
	//useGachaがtrueの時のメソッド
	public boolean setTrue(HttpSession session) {
		session.setAttribute(SESSION_KEY, true);
		return true;
	}

	//useGachaがfalseの時のメソッド
	public boolean setFalse(HttpSession session) {
		session.setAttribute(SESSION_KEY, false);
		return false;
	}

	// 現在値取得
	public boolean get(HttpSession session) {
		return init(session);
	}

	// 明示的にセット
	public void set(HttpSession session, boolean value) {
		session.setAttribute(SESSION_KEY, value);
	}

	//ヒグマが出たときにsessionを消す下のclearメソッド

	public void clear(HttpSession session) {
		session.removeAttribute("useGacha");
	}
}

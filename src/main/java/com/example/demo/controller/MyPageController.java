package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.OrderHistoryRow;
import com.example.demo.entity.User;
import com.example.demo.mapper.OrderMapper;

@Controller
public class MyPageController {

	// 🌟 フィールド名の記述ミス、またはインポート漏れで「orderMapper を解決できません」が発生していました
	@Autowired
	private OrderMapper orderMapper;

	@GetMapping("/mypage")
	public String showMyPage(HttpSession session) {

		// 1. セッションからログインユーザー情報を取得
		//User型のloginUserを
		User loginUser = (User) session.getAttribute("loginUser");

		// 2. ログイン状態でなければ（nullなら） /login にリダイレクト
		if (loginUser == null) {
			return "redirect:/login";
		}

		return "mypage/mypage";
	}

	@GetMapping("/mypage/history")
	public String showOrderHistory(HttpSession session, Model model) {

		// 1. セッションからログインユーザー情報を取得
		//authControllerでsetAttributeしてるloginUserをgetAttributeしてUser型にキャスト
		User loginUser = (User) session.getAttribute("loginUser");

		// 2. ログイン状態でなければ（nullなら） /login にリダイレクト
		if (loginUser == null) {
			return "redirect:/login";
		}

		// 3. ログインユーザーのIDを使って、DBから本物の注文履歴を取得

		List<OrderHistoryRow> historyRows = orderMapper.findHistoryByUserId(loginUser.getId());

		// 4. HTML側の ${historyRows} に取得したデータを渡す
		model.addAttribute("historyRows", historyRows);

		return "mypage/history";

	}

	@GetMapping("/mypage/terms_of_service")
	public String showTermsOfService() {
		return "mypage/terms_of_service";
	}

	@GetMapping("/mypage/privacy_policy")
	public String showPrivacyPolicy() {
		return "mypage/privacy_policy";
	}

	@GetMapping("/mypage/opinion")
	public String opinion() {
		return "mypage/opinion";
	}

	@GetMapping("/mypage/opinion/thanks")
	public String thanks() {
		return "mypage/thanks";
	}

	@GetMapping("/mypage/question")
	public String question() {
		return "mypage/question";
	}
}
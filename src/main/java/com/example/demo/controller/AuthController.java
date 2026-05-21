package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.form.LoginForm;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.CartService;
import com.example.demo.service.UseGachaService;

@Controller
public class AuthController {

	private final UserMapper userMapper;
	private final CartService cartService;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final UseGachaService gachaService;

	public AuthController(UserMapper userMapper, CartService cartService, UseGachaService gachaService) {
		this.userMapper = userMapper;
		this.cartService = cartService;
		this.gachaService = gachaService;
	}

	/** ログイン画面を表示する */
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("form", new LoginForm());
		return "auth/login";
	}

	/** ログイン処理を行う */
	@PostMapping("/login")
	public String login(
			@Validated @ModelAttribute("form") LoginForm form,
			BindingResult bindingResult,
			HttpSession session,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "auth/login";
		}

		// DBからユーザを検索する
		User user = userMapper.findByEmail(form.getEmail());

		System.out.println("email=" + form.getEmail());
		System.out.println("user=" + user);

		if (user == null || !passwordEncoder.matches(form.getPassword(), user.getPassword())) {
			model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
			return "auth/login";
		}

		// ログイン成功：セッションにユーザ情報を保存する
		// これにより、HomeController側で「session.getAttribute("loginUser")」から安全にUser情報を取得できます
		session.setAttribute("loginUser", user);
		gachaService.setFalse(session);

		// ========================================================
		// セッションにあったカートの中身をDBにマージする
		// ========================================================
		cartService.mergeSessionCartToDb(session, user.getId());

		// ─── 【修正】URLパラメータは付与せず、シンプルにホーム画面へリダイレクト ───
		return "redirect:/home";
	}

	/** ログアウト処理を行う */
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}
}
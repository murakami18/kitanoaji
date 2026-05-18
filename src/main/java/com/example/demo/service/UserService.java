package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.form.UserForm;

public interface UserService {

	/**
	 * ユーザを登録する。
	 * @param form 登録フォームの入力値
	 * @return 
	 */
	User register(UserForm form);
}
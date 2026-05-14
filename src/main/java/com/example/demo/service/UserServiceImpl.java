package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.form.UserForm;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public void register(UserForm form) {
		// 現段階ではコンソールに出力するだけ
		System.out.println("ユーザ登録：" + form.getName() + " <" + form.getEmail() + ">");
	}
}
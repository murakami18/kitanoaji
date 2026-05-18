package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.form.UserForm;
import com.example.demo.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public User register(UserForm form) {
		if (userMapper.existsByEmail(form.getEmail())) {
			throw new IllegalArgumentException("このメールは既に使われています");
		}
		System.out.println("ユーザ登録：" + form.getName() + " <" + form.getEmail() + ">");
		User user = new User();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setCategoryId(form.getCategoryId());
		user.setRegionId(form.getRegionId());
		userMapper.insert(user);

		return user; // 修正2: 最後に作成した user を返す
	}
}

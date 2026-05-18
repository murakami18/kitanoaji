package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.mapper.OrderMapper;

@Service
public class OrderService {

	private final OrderMapper orderMapper;

	public OrderService(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	/**
	 * カートの内容を注文として保存する。
	 * @param userId    ログイン中のユーザID
	 * @param cartItems カートの商品リスト
	 * @return 保存された注文ID
	 */
	@Transactional
	public int placeOrder(int userId, List<CartItem> cartItems) {
		// 注文ヘッダを保存
		Order order = new Order();
		order.setUserId(userId);
		orderMapper.insertOrder(order);

		// 注文明細を保存
		List<OrderItem> items = cartItems.stream()
				.map(c -> new OrderItem(order.getId(), c.getUserId(),
						c.getCreatedAt(), c.getDeliveredAt()))
				.collect(Collectors.toList());
		orderMapper.insertOrderItems(items);

		return order.getId();
	}
}

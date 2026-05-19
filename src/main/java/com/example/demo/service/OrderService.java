package com.example.demo.service;

import java.time.LocalDateTime;
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
		// 1. 注文ヘッダ（Order）を作成して保存
		Order order = new Order();
		order.setUserId(userId);
		order.setCreatedAt(LocalDateTime.now()); // 注文日時をシステム時間でセット

		// 自動採番されたIDが order.id に書き戻されます
		orderMapper.insertOrder(order);

		// 2. カートの商品リスト（CartItem）を注文明細（OrderItem）のリストに詰め替える
		List<OrderItem> items = cartItems.stream()
				.map(c -> {
					OrderItem item = new OrderItem();
					// id はDBの自動採番（SERIAL等）に任せるため、Java側ではセットしない（初期値のままにする）
					item.setOrderId(order.getId()); // 採番された最新の注文ID
					item.setProductId(c.getProductId()); // 商品ID
					item.setProductPrice(c.getPrice()); // 購入時の価格
					item.setQuantity(c.getQuantity()); // 数量
					return item;
				})
				.collect(Collectors.toList());

		// 3. 注文明細を一括保存
		orderMapper.insertOrderItems(items);

		// 生成された注文IDを返す
		return order.getId();
	}
}
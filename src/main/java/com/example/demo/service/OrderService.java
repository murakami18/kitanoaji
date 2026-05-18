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
		// ※ deliveredAt（配送日時）は注文時点では未配送のため初期値のnullのままとします

		// OrderMapperの @Options により、実行後に自動採番されたIDが order.id に書き戻されます
		orderMapper.insertOrder(order);

		// 2. カートの商品リスト（CartItem）を注文明細（OrderItem）のリストに詰め替える
		List<OrderItem> items = cartItems.stream()
				.map(c -> {
					// OrderItemの引数付きコンストラクタを利用してインスタンス化
					return new OrderItem(
							0, // id (DBの自動採番に任せるため、一旦0をセット)
							order.getId(), // orderId (上記で採番された最新の注文ID)
							c.getProductId(), // productId (カート内の商品ID)
							c.getPrice(), // productPrice (CartItemが保持している購入時の価格)
							c.getQuantity() // quantity (カート内の数量)
					);
				})
				.collect(Collectors.toList());

		// 3. 注文明細を一括保存
		orderMapper.insertOrderItems(items);

		// 生成された注文IDを返す
		return order.getId();
	}
}
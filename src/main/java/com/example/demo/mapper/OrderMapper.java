package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderHistoryRow;
import com.example.demo.entity.OrderItem;

@Mapper
public interface OrderMapper {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insertOrder(Order order);

	void insertOrderItems(List<OrderItem> items);

	//	/** 指定ユーザの購入履歴を取得する */
	//	List<OrderHistoryRow> findHistoryByUserId(int userId);
	// 🌟 注文履歴をユーザーIDで取得する
	List<OrderHistoryRow> findHistoryByUserId(@Param("userId") int userId);
}

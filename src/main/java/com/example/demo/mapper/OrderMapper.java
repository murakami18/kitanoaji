package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.core.annotation.Order;

import com.example.demo.entity.OrderItem;

@Mapper
public interface OrderMapper {

	/** 注文を保存し、生成されたIDをorderオブジェクトに設定する */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insertOrder(Order order);

	/** 注文明細を一括保存する */
	void insertOrderItems(List<OrderItem> items);
}

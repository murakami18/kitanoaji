package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;

@Mapper
public interface OrderMapper {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insertOrder(Order order);

	void insertOrderItems(List<OrderItem> items);
}

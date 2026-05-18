package com.example.demo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CommonModel {

	@ModelAttribute("regionMap")
	public Map<Integer, String> regionMap() {
		return Map.ofEntries(
				Map.entry(1, "宗谷"),
				Map.entry(2, "留萌"),
				Map.entry(3, "上川"),
				Map.entry(4, "オホーツク"),
				Map.entry(5, "根室"),
				Map.entry(6, "釧路"),
				Map.entry(7, "十勝"),
				Map.entry(8, "空知"),
				Map.entry(9, "石狩"),
				Map.entry(10, "後志"),
				Map.entry(11, "胆振"),
				Map.entry(12, "日高"),
				Map.entry(13, "檜山"),
				Map.entry(14, "渡島"));
	}

	@ModelAttribute("categoryMap")
	public Map<Integer, String> categoryMap() {
		return Map.ofEntries(
				Map.entry(1, "豚肉"),
				Map.entry(2, "牛肉"),
				Map.entry(3, "鶏肉"),
				Map.entry(4, "ジンギスカン"),
				Map.entry(5, "くじら肉"),
				Map.entry(6, "加工肉（ハム、ウインナー）"),
				Map.entry(7, "魚"),
				Map.entry(8, "貝"),
				Map.entry(9, "海藻"),
				Map.entry(10, "缶詰"),
				Map.entry(11, "珍味"),
				Map.entry(12, "日本酒"),
				Map.entry(13, "ウイスキー"),
				Map.entry(14, "焼酎"),
				Map.entry(15, "チューハイ"),
				Map.entry(16, "ワイン"),
				Map.entry(17, "シードル"),
				Map.entry(18, "ビール"),
				Map.entry(19, "牛乳"),
				Map.entry(20, "チーズ"),
				Map.entry(21, "ヨーグルト"),
				Map.entry(22, "バター、乳加工品"),
				Map.entry(23, "卵"),
				Map.entry(24, "いも類、里芋、山芋"),
				Map.entry(25, "大根、人参、根菜類"),
				Map.entry(26, "ほうれん草、チンゲン菜、葉物類"),
				Map.entry(27, "玉ねぎ、アスパラ、葉茎菜類"),
				Map.entry(28, "トマト、きゅうり、果菜類"),
				Map.entry(29, "納豆、豆腐、豆類"),
				Map.entry(30, "米"));
	}

}
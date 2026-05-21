package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

	//regionId というクエリパラメータをそれぞれの地域に設定して 
	///home に渡してる
	@GetMapping("/map")
	public String showMap() {

		return "map";
	}
}
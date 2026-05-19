package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.entity.User; // ★追加：Userエンティティのインポート
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.RegionMapper;

@Controller
public class HomeController {

	private final ProductMapper productMapper;
	private final RegionMapper regionMapper;
	private final CategoryMapper categoryMapper;

	// UserMapperはセッションからUserオブジェクトが取れるため、ここではインジェクション不要になりました
	public HomeController(ProductMapper productMapper, RegionMapper regionMapper, CategoryMapper categoryMapper) {
		this.productMapper = productMapper;
		this.regionMapper = regionMapper;
		this.categoryMapper = categoryMapper;
	}

	@GetMapping("/home")
	public String homeView(
			@RequestParam(name = "regionId", required = false) Integer regionId,
			@RequestParam(name = "categoryIds", required = false) List<Integer> categoryIds,
			HttpSession session,
			Model model) {

		// ========================================================
		// 0. セッションからログインユーザー情報を取得
		// ========================================================
		// セッションに「loginUser」というキーで User オブジェクトが保存されている前提
		User loginUser = (User) session.getAttribute("loginUser");
		boolean isLoggedIn = (loginUser != null);

		// ========================================================
		// 1. おすすめ商品（上段：横スクロール）のロジック
		// ========================================================
		List<Product> recommendProducts = new ArrayList<>();

		if (isLoggedIn) {
			// ログインしている場合：ユーザーの持つカテゴリID（Integer）を取得
			Integer userCategoryId = loginUser.getCategoryId();

			if (userCategoryId != null) {
				// ProductMapperのfindByCategoryIdsはList<Integer>を期待するため、単一のIDをリストに変換して渡す
				List<Integer> searchCategoryIds = Collections.singletonList(userCategoryId);
				recommendProducts = productMapper.findByCategoryIds(searchCategoryIds);
			}

			// ユーザーにカテゴリが設定されていない、または該当する商品が0件の場合は、全件からランダムで最大10件を表示（常時表示のフォールバック）
			if (recommendProducts == null || recommendProducts.isEmpty()) {
				List<Product> allProducts = productMapper.findAll();
				if (allProducts != null && !allProducts.isEmpty()) {
					List<Product> shuffledList = new ArrayList<>(allProducts);
					Collections.shuffle(shuffledList);
					recommendProducts = shuffledList.stream().limit(10).collect(Collectors.toList());
				} else {
					recommendProducts = new ArrayList<>();
				}
			}
		} else {
			// ログインしていない場合：おすすめ商品は表示しない
			recommendProducts = null;
		}
		model.addAttribute("recommendProducts", recommendProducts);

		// ========================================================
		// 2. メイン商品一覧（下段）：カテゴリIDやリージョンIDでのフィルタリングを適用
		// ========================================================
		List<Product> products;
		boolean hasCategories = (categoryIds != null && !categoryIds.isEmpty());

		if (regionId != null || hasCategories) {
			// フィルタリング条件（地域またはカテゴリ）が指定されている場合
			if (regionId != null && hasCategories) {
				products = productMapper.findByRegionIdAndCategoryIds(regionId, categoryIds);
			} else if (regionId != null) {
				products = productMapper.findByRegionId(regionId);
			} else {
				products = productMapper.findByCategoryIds(categoryIds);
			}
		} else {
			// フィルタリング条件が何も指定されていない場合：全件表示
			products = productMapper.findAll();
		}
		model.addAttribute("products", products);

		// ========================================================
		// 3. 絞り込み中情報の処理（既存のまま維持）
		// ========================================================
		if (regionId != null) {
			Region selectedRegion = regionMapper.findById(regionId);
			model.addAttribute("selectedRegion", selectedRegion);
		}

		if (hasCategories) {
			List<Category> selectedCategories = new ArrayList<>();
			for (Integer id : categoryIds) {
				Category cat = categoryMapper.findById(id);
				if (cat != null) {
					selectedCategories.add(cat);
				}
			}
			model.addAttribute("selectedCategories", selectedCategories);
		}

		return "home";
	}
}
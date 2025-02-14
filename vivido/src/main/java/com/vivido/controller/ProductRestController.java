package com.vivido.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vivido.domain.ProductVO;
import com.vivido.service.ProductService;

import ch.qos.logback.core.model.Model;

@RestController
@RequestMapping("/management/products")
public class ProductRestController {

	@Autowired
	private ProductService productService;
	
	
	////////////////////////////상품 목록 페이지 시작////////////////////////////////
	
	// 전체 상품 조회 API
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllProducts(
			@RequestParam(value = "page", defaultValue = "1") int pageNum,
			@RequestParam(value = "size", defaultValue = "5") int pageSize) {

		// 상품 목록과 페이징 정보 가져오기
		Map<String, Object> response = productService.getProducts(pageNum, pageSize);

		return ResponseEntity.ok(response);
	}

	// 상품 삭제 API (DELETE /management/products/{productId})
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
		boolean isDeleted = productService.deleteProductById(productId);
		if (isDeleted) {
			return ResponseEntity.noContent().build(); // 204 No Content
		} else {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}
	}

	// 상품 정보 조회 API
	@GetMapping("/{productId}")
	public ResponseEntity<ProductVO> getProductInfo(@PathVariable String productId) {
		ProductVO product = productService.getProductById(productId);
		return ResponseEntity.ok(product);
	}

	// 상품 수정 API
	@PutMapping("/{productId}")
	public ResponseEntity<String> updateProduct(@PathVariable String productId, @RequestBody ProductVO product) {

		// productId를 @RequestBody에서 전달된 값에 덮어씌우는 방식
		product.setProductId(productId);

		// 상품 수정 수행
		int result = productService.updateProduct(product);

		// 상품 수정이 성공한 경우
		if (result > 0) {
			return ResponseEntity.ok("상품 수정 성공");
		} else {
			// 수정 실패 시 좀 더 구체적인 실패 이유 제공 (선택 사항)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 수정 실패");
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductVO>> searchProducts(
	    @RequestParam(value = "product_name", required = false) String productName,
	    @RequestParam(value = "product_id", required = false) String productId,
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "create_date", required = false) String createDate) {

	    // ProductVO 객체 생성하여 검색 조건 설정
	    ProductVO productVO = new ProductVO();
	    productVO.setProductName(productName);
	    productVO.setProductId(productId);
	    productVO.setProductKeyword(keyword);

	    // create_date가 유효하면 LocalDate로 변환
	    if (createDate != null && !createDate.isEmpty()) {
	        try {
	            // "yyyy-MM-dd" 형식의 문자열을 LocalDate로 변환
	            LocalDate localDate = LocalDate.parse(createDate);
	            productVO.setCreateDate(localDate); // productVO에 LocalDate로 설정
	        } catch (DateTimeParseException e) {
	            // 잘못된 날짜 형식에 대한 예외 처리
	            System.out.println("잘못된 날짜 형식: " + createDate);
	        }
	    }

	    // DB에서 검색 수행 (예시)
	    List<ProductVO> products = productService.searchProducts(productVO);  // Service 메서드 호출
	    return ResponseEntity.ok(products);  // 결과 반환
	}

	@GetMapping("/getSubcategories")
	public List<String> getSubcategories(@RequestParam String productCategory) {
		// 카테고리로 해당 물품 정보 반환
		return productService.getSubcategoriesByCategory(productCategory);
	}

	@GetMapping("/searchProducts")
	public List<ProductVO> searchProducts(@RequestParam String productCategory,
			@RequestParam String productCategoryDetails) {
		// 카테고리와 세분류에 맞는 상품 정보를 가져옴
		List<ProductVO> products = productService.getProductsByCategoryAndSubcategory(productCategory,
				productCategoryDetails);
		return products; // 상품 목록을 JSON 형태로 반환
	}

	// 렌탈 상태 데이터 반환
	@GetMapping("/rentalStatus")
	public Map<String, Integer> getRentalStatus() {
		return productService.getRentalCounts();
	}
	////////////////////////////상품 목록 페이지 끝////////////////////////////////
	
	////////////////////////////상품 등록 페이지 시작////////////////////////////////
	
	
	 @PostMapping("/register")
	    public ResponseEntity<String> registerProduct(@RequestBody ProductVO productVO) {
	        try {
	            productService.registerProduct(productVO);
	            return ResponseEntity.ok("Product registered successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("Error during product registration: " + e.getMessage());
	        }
	    }
	
	
	
	
	
	////////////////////////////상품 등록 페이지 끝////////////////////////////////
	
}

package com.vivido.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vivido.domain.ProductVO;
import com.vivido.service.ProductService;

@RestController
@RequestMapping("/management/products")
public class ProductRestController {

	@Autowired
	private ProductService productService;

	// 전체 상품 조회 API
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllProducts(
	    @RequestParam(value = "page", defaultValue = "1") int pageNum,
	    @RequestParam(value = "size", defaultValue = "10") int pageSize) {

	    // 페이징 처리된 상품 목록 조회
	    List<ProductVO> products = productService.getProducts(pageNum, pageSize);

	    // 총 페이지 수 계산
	    int totalPages = productService.getTotalPages(pageSize);

	    // 응답할 데이터 구성
	    Map<String, Object> response = new HashMap<>();
	    response.put("products", products);
	    response.put("totalPages", totalPages);
	    response.put("currentPage", pageNum);

	    return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<String> updateProduct(
            @PathVariable String productId, 
            @RequestBody ProductVO product) {

        // productId를 @RequestBody에서 전달된 값에 덮어씌우는 방식
        product.setProductId(productId); 

        // 상품 수정 수행
        int result = productService.updateProduct(product);

        // 상품 수정이 성공한 경우
        if (result > 0) {
            return ResponseEntity.ok("상품 수정 성공");
        } else {
            // 수정 실패 시 좀 더 구체적인 실패 이유 제공 (선택 사항)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 수정 실패");
        }
    }
	

}

package com.vivido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<List<ProductVO>> getAllProducts() {
        List<ProductVO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 특정 상품 조회 API
    @GetMapping("/{productId}")
    public ResponseEntity<ProductVO> getProductById(@PathVariable("productId") String productId) {
        ProductVO product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    

    // ✅ 상품 삭제 API (DELETE /management/products/{productId})
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        boolean isDeleted = productService.deleteProductById(productId);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }
	
	
}

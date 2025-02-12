package com.vivido.repository;

import java.util.List;

import com.vivido.domain.ProductVO;

public interface ProductDAO {

	public List<ProductVO> getAllProducts();

	public int deleteProductById(String productId);

	// 상품 정보 조회
	public ProductVO getProductById(String productId);

	// 상품 정보 수정
	public int updateProduct(ProductVO product);

	public List<ProductVO> selectProducts(int offset, int pageSize);
	
	public int getTotalProductCount();

}
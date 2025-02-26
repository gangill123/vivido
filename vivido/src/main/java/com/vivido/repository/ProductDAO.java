package com.vivido.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.vivido.domain.ProductVO;

public interface ProductDAO {

	public List<ProductVO> getAllProducts();

	public int deleteProductById(String productId);
	
	public int deleteProductsByIds(List<String> productIds) ;

	// 상품 정보 조회
	public ProductVO getProductById(String productId);

	// 상품 정보 수정
	public int updateProduct(ProductVO product);

	public List<ProductVO> selectProducts(int offset, int pageSize);

	public int getTotalProductCount();

	public List<ProductVO> searchProducts(ProductVO productVO);

	public List<String> getSubcategoriesByCategory(String productCategory);

	public List<ProductVO> getProductsByCategoryAndSubcategory(String productCategory, String productCategoryDetails);

	public Map<String, Integer> getDisplayCounts();
	
    List<ProductVO> getProductsByIds(Map<String, Object> paramMap);  // 기존 List<String> → Map<String, Object> 변경
    

    // 상품 등록
    void insertProduct(ProductVO productVO);

    // 이미지 등록
    void insertProductImage(ProductVO productImage);
    // 이미지 업데이트 
	void updateProductImages(ProductVO product);

	public void updateProductStatus(String productId, int status);

	

}
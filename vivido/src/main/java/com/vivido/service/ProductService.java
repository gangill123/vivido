package com.vivido.service;

import java.util.List;
import java.util.Map;

import com.vivido.domain.ProductOptionVO;
import com.vivido.domain.ProductVO;

import jakarta.servlet.http.HttpServletResponse;


public interface ProductService {

	public List<ProductVO> getAllProducts();

	public boolean deleteProductById(String productId);
	
	public boolean deleteProductsByIds(List<String> productIds);
	
	public ProductVO getProductById(String productId);

    public void updateProduct(ProductVO product);
    
    public Map<String, Object> getProducts(int pageNum, int pageSize);
    
    public int getTotalPages(int pageSize);
    
    List<ProductVO> searchProducts(ProductVO productVO);

	public List<String> getSubcategoriesByCategory(String productCategory);

	public List<ProductVO> getProductsByCategoryAndSubcategory(String productCategory, String productCategoryDetails);
	
	Map<String, Integer> getDisplayCounts();
	
	public byte[] exportProductsToExcel(List<String> productIds);
	  
	 public void changeProductStatus(List<String> productIds, int status);
	
	
	
	////////////////////////////상품 등록 페이지 시작////////////////////////////////
	public void registerProduct(ProductVO productVO, List<ProductVO> productImages, List<ProductOptionVO> productOptions);

	List<ProductVO> getProductsByIds(List<String> productIds);

	
	
	///////////////////////////////상품 등록 페이지 끝////////////////////////////////
	
}

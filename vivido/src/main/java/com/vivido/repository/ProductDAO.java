package com.vivido.repository;

import java.util.List;

import com.vivido.domain.ProductVO;




public interface ProductDAO {
	
	    public List<ProductVO> getAllProducts();
	    
	    public ProductVO getProductById(String productId);
	    
		public int deleteProductById(String productId);
	    
}
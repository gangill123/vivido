package com.vivido.service;

import java.util.List;


import com.vivido.domain.ProductVO;


public interface ProductService {

	public List<ProductVO> getAllProducts();

	public boolean deleteProductById(String productId);
	
	public ProductVO getProductById(String productId);

    public int updateProduct(ProductVO product);
    
    public List<ProductVO> getProducts(int pageNum, int pageSize);
    
    public int getTotalPages(int pageSize);

}

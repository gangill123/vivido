package com.vivido.service;

import java.util.List;


import com.vivido.domain.ProductVO;


public interface ProductService {

	public List<ProductVO> getAllProducts();

	public ProductVO getProductById(String productId);

	public boolean deleteProductById(String productId);

}

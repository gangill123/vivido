package com.vivido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vivido.domain.ProductVO;
import com.vivido.repository.ProductDAO;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<ProductVO> getAllProducts() {
		return productDAO.getAllProducts();
	}

	@Override
	public ProductVO getProductById(String productId) {
		return productDAO.getProductById(productId);
	}
	
	@Override
	// ✅ 상품 삭제 로직
	public boolean deleteProductById(String productId) {
	    return productDAO.deleteProductById(productId) > 0;
	}

}

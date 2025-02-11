package com.vivido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
	// 상품 삭제 로직
	public boolean deleteProductById(String productId) {
		return productDAO.deleteProductById(productId) > 0;
	}

	@Override
	public ProductVO getProductById(String productId) {
		return productDAO.getProductById(productId);
	}

	@Override
	public int updateProduct(ProductVO product) {
		return productDAO.updateProduct(product);
	}
	
	@Override
	public List<ProductVO> getProducts(int pageNum, int pageSize) {
		// 페이징 처리 시작
		PageHelper.startPage(pageNum, pageSize);

		// 데이터 조회
		List<ProductVO> products = productDAO.selectProducts();

		// PageInfo로 페이징 정보 반환
		PageInfo<ProductVO> pageInfo = new PageInfo<>(products);

		// PageInfo에서 실제 데이터 리스트를 반환
		return pageInfo.getList();
	}
	
	@Override
	public int getTotalPages(int pageSize) {
		// 전체 데이터 개수 조회
		int totalRecords = productDAO.getTotalProductCount();

		// 전체 페이지 수 계산
		return (int) Math.ceil((double) totalRecords / pageSize);
	}

}

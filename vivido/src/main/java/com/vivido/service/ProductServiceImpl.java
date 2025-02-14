package com.vivido.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	// 상품 목록 페이징 처리

	@Override
	public Map<String, Object> getProducts(int pageNum, int pageSize) {
		// 1. 전체 데이터 개수 계산
		int totalItems = productDAO.getTotalProductCount();

		// 2. 전체 페이지 수 계산 (올림 처리)
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		// 3. 현재 페이지 번호가 유효한지 확인
		pageNum = (pageNum > 0) ? pageNum : 1;
		pageNum = (pageNum > totalPages) ? totalPages : pageNum;

		// 4. OFFSET 계산 (현재 페이지에서 데이터를 추출)
		int offset = (pageNum - 1) * pageSize;

		// 5. 해당 페이지에 해당하는 데이터 조회
		List<ProductVO> products = productDAO.selectProducts(offset, pageSize);

		// 6. 페이징 정보를 포함한 결과 반환
		Map<String, Object> response = new HashMap<>();
		response.put("products", products); // 실제 데이터 리스트
		response.put("totalPages", totalPages); // 총 페이지 수
		response.put("currentPage", pageNum); // 현재 페이지
		response.put("pageSize", pageSize); // 페이지당 항목 수
		response.put("totalItems", totalItems); // 총 항목 수

		return response;

	}

	@Override
	public int getTotalPages(int pageSize) {
		// 전체 데이터 개수 조회 (이 부분은 ProductDAO에서 처리)
		int totalItems = productDAO.getTotalProductCount(); // 전체 데이터 개수를 조회하는 메서드 호출

		// 총 페이지 수 계산 (전체 데이터 개수를 pageSize로 나누고 올림 처리)
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		return totalPages;
	}

	// 상품 검색 조회
	@Override
	public List<ProductVO> searchProducts(ProductVO productVO) {
		return productDAO.searchProducts(productVO);
	}

	@Override
	public List<String> getSubcategoriesByCategory(String productCategory) {
		// 카테고리에 맞는 세분류를 반환하는 로직
		return productDAO.getSubcategoriesByCategory(productCategory);
	}

	@Override
	public List<ProductVO> getProductsByCategoryAndSubcategory(String productCategory, String productCategoryDetails) {
		// 카테고리와 세분류에 맞는 상품 정보를 가져오는 로직
		return productDAO.getProductsByCategoryAndSubcategory(productCategory, productCategoryDetails);
	}

	@Override
	public Map<String, Integer> getRentalCounts() {
		return productDAO.getRentalCounts();
	}

	////////////////////////////상품 등록 페이지 시작////////////////////////////////
	 // 상품 등록
	@Override
	public void registerProduct(ProductVO productVO) {
		productDAO.insertProduct(productVO);
	}
	
	
	
	
	///////////////////////////////상품 등록 페이지 끝////////////////////////////////
	 
	
	
	
	
	
}

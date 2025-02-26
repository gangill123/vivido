package com.vivido.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.vivido.domain.ProductVO;

@Repository
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private SqlSession sqlSession;

	private static final String NAMESPACE = "com.vivido.mapper.productMapper";

	@Override
	public List<ProductVO> getAllProducts() {
	    List<ProductVO> products = sqlSession.selectList(NAMESPACE + ".getAllProducts");
	    System.out.println(products);  // 결과가 정상적으로 반환되는지 확인
	    return products;  // 로그 찍은 후에 반환
	}

	// 상품 삭제 로직
	@Override
	public int deleteProductById(String productId) {
		return sqlSession.delete(NAMESPACE + ".deleteProductById", productId);
	}
	@Override
	public int deleteProductsByIds(List<String> productIds) {
	    return sqlSession.delete(NAMESPACE + ".deleteProductsByIds", productIds);
	}
	

	// 상품 정보 조회
	@Override
	public ProductVO getProductById(String productId) {
		return sqlSession.selectOne(NAMESPACE + ".getProductById", productId);
	}

	// 상품 정보 수정
	@Override
	public int updateProduct(ProductVO product) {
		return sqlSession.update(NAMESPACE + ".updateProduct", product);
	}
    @Override
    public void updateProductImages(ProductVO product) {
        sqlSession.update(NAMESPACE + ".updateProductImages", product);
    }
	
	
	
	
	@Override
	public List<ProductVO> selectProducts(int offset, int pageSize) {
		// OFFSET과 LIMIT을 사용하여 페이징 처리된 데이터 조회
		return sqlSession.selectList(NAMESPACE + ".selectProducts", new HashMap<String, Integer>() {
			{
				put("offset", offset);
				put("pageSize", pageSize);
			}
		});
	}

	// 상품 총 개수 조회
	@Override
	public int getTotalProductCount() {
		return sqlSession.selectOne(NAMESPACE + ".getTotalProductCount");
	}

	// 상품 카테고리별 검색
	@Override
	public List<ProductVO> searchProducts(ProductVO productVO) {
		return sqlSession.selectList(NAMESPACE + ".searchProducts", productVO);
	}

	// 카테고리에 맞는 세분류 목록 조회
	@Override
	public List<String> getSubcategoriesByCategory(String productCategory) {
		return sqlSession.selectList(NAMESPACE + ".getSubcategoriesByCategory", productCategory);
	}

	// 카테고리와 세분류에 맞는 상품 목록 조회
	@Override
	public List<ProductVO> getProductsByCategoryAndSubcategory(String productCategory, String productCategoryDetails) {
		return sqlSession.selectList(NAMESPACE + ".getProductsByCategoryAndSubcategory",
				new ProductVO(productCategory, productCategoryDetails));
	}
	
	@Override
	public Map<String, Integer> getDisplayCounts() {
		return sqlSession.selectOne(NAMESPACE + ".getDisplayCounts");
	}
	
	 @Override
	    public List<ProductVO> getProductsByIds(Map<String, Object> paramMap) {  // 여기서도 Map 사용
	        return sqlSession.selectList(NAMESPACE + ".getProductsByIds", paramMap);
	    }
	 
	 @Override
	 public void updateProductStatus(String productId, int status) {
	     // 파라미터를 Map에 담아서 SQL 쿼리 호출
	     Map<String, Object> paramMap = new HashMap<>();
	     paramMap.put("productId", productId);
	     paramMap.put("status", status);

	     // MyBatis에서 SQL 쿼리를 실행하여 상품 상태를 변경
	     sqlSession.update(NAMESPACE + ".updateProductStatus", paramMap);
	 }

	 
	
	
	////////////////////////////상품 등록 페이지 시작////////////////////////////////
	@Override
	public void insertProduct(ProductVO productVO) {
		sqlSession.insert(NAMESPACE + ".insertProduct", productVO);
	}
	@Override
	public void insertProductImage(ProductVO productImage) {
		sqlSession.insert(NAMESPACE + ".insertProductImage", productImage);
		
	}
		
	
	
	
	////////////////////////////상품 등록 페이지 끝////////////////////////////////

}
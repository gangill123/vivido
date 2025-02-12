package com.vivido.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vivido.domain.ProductVO;

@Repository
public class ProductDAOImpl implements ProductDAO {
	
	@Autowired
	private SqlSession sqlSession;

	private static final String NAMESPACE = "com.vivido.mapper.productMapper";

	@Override
	public List<ProductVO> getAllProducts() {
		return sqlSession.selectList(NAMESPACE + ".getAllProducts");
	}

	// 상품 삭제 로직
	@Override
	public int deleteProductById(String productId) {
		return sqlSession.delete(NAMESPACE + ".deleteProductById", productId);
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
	public List<ProductVO> selectProducts(int offset, int pageSize) {
	    // OFFSET과 LIMIT을 사용하여 페이징 처리된 데이터 조회
	    return sqlSession.selectList(NAMESPACE + ".selectProducts", 
	                                 new HashMap<String, Integer>() {{
	                                     put("offset", offset);
	                                     put("pageSize", pageSize);
	                                 }});
	}
	
	// 상품 총 개수 조회
    @Override
    public int getTotalProductCount() {
        return sqlSession.selectOne(NAMESPACE + ".getTotalProductCount");
    }

}
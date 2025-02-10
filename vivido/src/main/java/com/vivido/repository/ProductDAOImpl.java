package com.vivido.repository;

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

	@Override
	public ProductVO getProductById(String productId) {
		return sqlSession.selectOne(NAMESPACE + ".getProductById", productId);
	}
	   // ✅ 상품 삭제 로직
	@Override
	public int deleteProductById(String productId) {
	    return sqlSession.delete(NAMESPACE + ".deleteProductById", productId);
	}
}
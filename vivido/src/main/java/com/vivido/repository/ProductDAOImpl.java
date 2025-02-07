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

	    @Override
	    public ProductVO getEmployeeById(String empId) {
	        // 쿼리 ID와 파라미터를 정확하게 지정
	        return sqlSession.selectOne("EmployeeDAO.getEmployeeById", empId);
	    }

		@Override
		public List<ProductVO> getEmployeeById() {
			 return sqlSession.selectList("EmployeeDAO.getAllEmployees");
		}
 
}
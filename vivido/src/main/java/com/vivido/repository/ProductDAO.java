package com.vivido.repository;

import java.util.List;

import com.vivido.domain.ProductVO;




public interface ProductDAO {
    List<ProductVO> getEmployeeById();  // emp_id로 조회하는 메서드
    ProductVO getEmployeeById(String empId);
}
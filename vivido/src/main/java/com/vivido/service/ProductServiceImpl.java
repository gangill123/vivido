package com.vivido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vivido.domain.ProductVO;
import com.vivido.repository.ProductDAO;

public class ProductServiceImpl implements ProductService{
	

    @Autowired
    private ProductDAO employeeDAO;
    
    @Override
    public List<ProductVO> getAllEmployees() {
    	 return employeeDAO.getEmployeeById();  // DAO에서 데이터를 가져오는 메서드 호출
    }


}

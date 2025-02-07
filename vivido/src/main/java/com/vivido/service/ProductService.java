package com.vivido.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vivido.domain.ProductVO;
import com.vivido.repository.ProductDAO;

public interface ProductService {
    List<ProductVO> getAllEmployees();
}

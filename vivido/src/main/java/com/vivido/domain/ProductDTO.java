package com.vivido.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
public class ProductDTO {
	
    private String productId;
    private String productName;
    private int productPrice;
    private String productCategory;

}

package com.vivido.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@ToString           // toString() 자동 생성
public class OrderDetailVO {

	  private int orderDetailId;            // 주문상세번호
	    private String orderId;               // 주문 번호
	    private String productId;             // 상품 고유 번호
	    private int productSubprice;          // 상품가격 (합계)


}

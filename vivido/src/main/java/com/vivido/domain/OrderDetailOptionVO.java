package com.vivido.domain;

import java.security.Timestamp;

public class OrderDetailOptionVO {
	
	   private int orderDetailOptionId;          // 주문상세옵션번호
	    private int orderDetailId;                // 주문상세번호
	    private int optionPrice;                  // 옵션가격 (계산된 가격)
	    private int quantity;                     // 개수
	    private String productOption;             // 옵션정보
	    private String status;                    // 상태
	    private Timestamp createDate;             // 생성일
	    private Timestamp dispatchDate;           // 발송 처리일
	    private Timestamp cancellationRequestDate; // 취소 요청일
	    private Timestamp returnRequestDate;      // 반품 요청일
	
	
}

package com.vivido.domain;

import java.security.Timestamp;

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

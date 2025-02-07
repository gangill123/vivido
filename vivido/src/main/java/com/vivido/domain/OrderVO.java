package com.vivido.domain;

import java.sql.Timestamp;

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
public class OrderVO {
	
	   private String orderId;              // 주문 번호
	    private String memberId;             // 회원 번호
	    private String orderName;            // 주문자 성함
	    private String productId;            // 상품 고유 번호
	    private String payMethod;            // 결제 방식
	    private int totalPrice;              // 총 결제 금액
	    private String recipient;            // 받는 사람
	    private String address;              // 받는 사람 주소
	    private String detailAddress;        // 받는 사람 상세 주소
	    private String postalCode;           // 우편번호
	    private String recipientPhone;       // 받는 사람 전화번호
	    private String paymentStatus;        // 결제 여부
	    private Timestamp createDate;        // 생성 일자
	    private Timestamp updateDate;        // 수정 일자
	    private String status;               // 주문 상태
	    private String invoiceNumber;        // 송장 번호
	    private String deliveryMethod;       // 배송 방법
	    private Timestamp paymentDate;       // 결제 일자
	
}

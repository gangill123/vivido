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
public class AddressVO {
	
    private int addressId;                  // 배송지 ID
    private String memberId;                // 회원번호
    private String isDefaultAddress;        // 기본배송지 여부
    private String addressName;             // 주소지 이름
    private String postalCode;              // 우편번호
    private String roadAddress;             // 도로명 주소
    private String detailAddress;           // 상세 주소
    private String extraAddress;            // 추가 주소
    private String recipient;               // 수취인
    private String recipientPhone;          // 수취인 전화번호
    private Timestamp createDate;           // 생성일
    private Timestamp updateDate;           // 수정일

}

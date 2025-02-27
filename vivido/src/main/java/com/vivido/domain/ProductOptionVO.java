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
@ToString     
public class ProductOptionVO {
    private int optionId;
    private String productId;  // VARCHAR(50)이므로 String 타입
    private OptionType optionType;
    private String optionValue;
    private int price;
    private int optionStatus; // 0: 미진열, 1: 진열
}



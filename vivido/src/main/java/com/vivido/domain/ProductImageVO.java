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
public class ProductImageVO {
	
    private int imageId;
    private String imageUrl;
    private String thumbnailUrl;
    private boolean isPrimary;
    private Timestamp createdAt;

}

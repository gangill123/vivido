package com.vivido.domain; 

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@ToString           // toString() 자동 생성
public class ProductVO {

    private String productId;  // 상품 고유 번호
    private String productSellerId;  // 판매자 ID
    private String productCategory;  // 카테고리
    private String productCategoryDetails;  // 카테고리 세분류
    private String productKeyword;  // 키워드
    private String productName;  // 상품명
    private int productPrice;  // 렌탈가
    private Timestamp startDate;  // 렌탈 시작일자
    private Timestamp endDate;  // 렌탈 종료일자
    private int discountRate;  // 할인율
    private int productStock;  // 재고 수량
    private boolean productOptionId;  // 옵션 (true/false)
    private String productContent;  // 상품 내용
    private String brand;  // 브랜드 (기본값: 비비도)
    private String manufacturer;  // 제조사 (기본값: 비비도)
    private String productOrigin;  // 원산지 (기본값: 국내)
    private int productStatus;  // 상품 상태
    private String deliveryMethod;  // 배송 방법
    private String deliveryCompany;  // 택배사
    private int deliveryPrice;  // 배송비
    private String address;  // 출고지
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate; //등록일자
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updateDate;  // 수정일자
    private Timestamp approvalDate;  // 승인일자
    private String approvalId;  // 승인자 ID
    private String comments;  // 비고
    private int discountedPrice; // 할인된 가격

    
	
    private int imageId;
    private String imageUrl;
    private String thumbnailUrl;
    private boolean isPrimary;
    private Timestamp createdAt;
    
    
   // 1) AS displayCount,    <!-- 진열 중인 물품 -->
   // 0) AS ndisplayCount,   <!-- 미진열 물품 -->
   // 2) AS outOfStockCount,  <!-- 품절 물품 -->
   // 3) AS discontinuedCount,<!-- 판매 중지 물품 -->


    // getter and setter
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    // Getter
    public boolean IsPrimary() {
        return isPrimary;
    }

    // Setter
    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    
    // 상품 옵션 리스트 추가
    public List<ProductOptionVO> productOptions;
    
    // Getter & Setter
    public List<ProductOptionVO> getProductOptions() { return productOptions; }
    public void setProductOptions(List<ProductOptionVO> productOptions) { this.productOptions = productOptions; }
    
    
    
    
    
    // 카테고리와 세분류를 받을 수 있는 생성자 추가
    public ProductVO(String productCategory, String productCategoryDetails) {
        this.productCategory = productCategory;
        this.productCategoryDetails = productCategoryDetails;
    }
    
    
    
}
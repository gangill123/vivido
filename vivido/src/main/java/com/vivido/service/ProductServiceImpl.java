package com.vivido.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vivido.domain.ProductVO;
import com.vivido.repository.ProductDAO;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<ProductVO> getAllProducts() {
		return productDAO.getAllProducts();
	}

	@Override
	// 상품 삭제 로직
	public boolean deleteProductById(String productId) {

		return productDAO.deleteProductById(productId) > 0;
	}
	
	@Override
	// 여러 상품 삭제 로직
	public boolean deleteProductsByIds(List<String> productIds) {
	    return productDAO.deleteProductsByIds(productIds) > 0;
	}
	

	@Override
	public ProductVO getProductById(String productId) {
		return productDAO.getProductById(productId);
	}

	
    @Override
    public void updateProduct(ProductVO product) {
        productDAO.updateProduct(product);
        
        // 이미지 URL이 존재하면 업데이트 실행
        if (product.getImageUrl() != null || product.getThumbnailUrl() != null) {
            productDAO.updateProductImages(product);
        }
    }

	// 상품 목록 페이징 처리

	@Override
	public Map<String, Object> getProducts(int pageNum, int pageSize) {
		// 1. 전체 데이터 개수 계산
		int totalItems = productDAO.getTotalProductCount();

		// 2. 전체 페이지 수 계산 (올림 처리)
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		// 3. 현재 페이지 번호가 유효한지 확인
		pageNum = (pageNum > 0) ? pageNum : 1;
		pageNum = (pageNum > totalPages) ? totalPages : pageNum;

		// 4. OFFSET 계산 (현재 페이지에서 데이터를 추출)
		int offset = (pageNum - 1) * pageSize;

		// 5. 해당 페이지에 해당하는 데이터 조회
		List<ProductVO> products = productDAO.selectProducts(offset, pageSize);
		
		 // 각 상품의 썸네일 URL 확인 (디버깅)
	    for (ProductVO product : products) {
	        System.out.println("Product ID: " + product.getProductId() + ", Thumbnail URL: " + product.getThumbnailUrl());
	    }

		
		// 6. 페이징 정보를 포함한 결과 반환
		Map<String, Object> response = new HashMap<>();
		response.put("products", products); // 실제 데이터 리스트
		response.put("totalPages", totalPages); // 총 페이지 수
		response.put("currentPage", pageNum); // 현재 페이지
		response.put("pageSize", pageSize); // 페이지당 항목 수
		response.put("totalItems", totalItems); // 총 항목 수

		return response;

	}

	@Override
	public int getTotalPages(int pageSize) {
		// 전체 데이터 개수 조회 (이 부분은 ProductDAO에서 처리)
		int totalItems = productDAO.getTotalProductCount(); // 전체 데이터 개수를 조회하는 메서드 호출

		// 총 페이지 수 계산 (전체 데이터 개수를 pageSize로 나누고 올림 처리)
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		return totalPages;
	}

	// 상품 검색 조회
	@Override
	public List<ProductVO> searchProducts(ProductVO productVO) {
		return productDAO.searchProducts(productVO);
	}

	@Override
	public List<String> getSubcategoriesByCategory(String productCategory) {
		// 카테고리에 맞는 세분류를 반환하는 로직
		return productDAO.getSubcategoriesByCategory(productCategory);
	}

	@Override
	public List<ProductVO> getProductsByCategoryAndSubcategory(String productCategory, String productCategoryDetails) {
		// 카테고리와 세분류에 맞는 상품 정보를 가져오는 로직
		return productDAO.getProductsByCategoryAndSubcategory(productCategory, productCategoryDetails);
	}

	@Override
	public Map<String, Integer> getDisplayCounts() {
		return productDAO.getDisplayCounts();
	}
	
	@Override
	public byte[] exportProductsToExcel(List<String> productIds) {
	    List<ProductVO> products = getProductsByIds(productIds);

	    Workbook workbook = new XSSFWorkbook();
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    try {
	        Sheet sheet = workbook.createSheet("Products");

	        // 헤더 스타일 추가
	        CellStyle headerStyle = workbook.createCellStyle();
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true); // 글꼴을 굵게 설정
	        headerFont.setFontHeightInPoints((short) 12); // 글꼴 크기 설정 (필요시)
	        headerStyle.setFont(headerFont); // 스타일에 폰트를 적용

	        // 헤더 생성
	        Row headerRow = sheet.createRow(0);
	        String[] headers = {
	            "상품 ID", "판매자 ID", "카테고리", "세분류", "키워드", "상품명", 
	            "렌탈가", "렌탈 시작일", "렌탈 종료일", "할인율", "재고 수량", 
	            "옵션", "상품 내용", "브랜드", "제조사", "원산지", 
	            "상품 상태", "배송 방법", "택배사", "배송비", "출고지", 
	            "등록일", "수정일", "승인일", "승인자 ID", "비고"
	        };

	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle); // 스타일 적용
	        }

	        // 날짜 포맷 설정
	        CellStyle dateCellStyle = workbook.createCellStyle();
	        CreationHelper createHelper = workbook.getCreationHelper();
	        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

	        // 숫자 셀 스타일 추가
	        CellStyle numberCellStyle = workbook.createCellStyle();
	        numberCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));

	        int rowNum = 1;
	        for (ProductVO product : products) {
	            Row row = sheet.createRow(rowNum++);

	            // 상품 ID
	            row.createCell(0).setCellValue(product.getProductId() != null ? product.getProductId() : "N/A");
	            // 판매자 ID
	            row.createCell(1).setCellValue(product.getProductSellerId() != null ? product.getProductSellerId() : "N/A");
	            // 카테고리
	            row.createCell(2).setCellValue(product.getProductCategory() != null ? product.getProductCategory() : "정보 없음");
	            // 세분류
	            row.createCell(3).setCellValue(product.getProductCategoryDetails() != null ? product.getProductCategoryDetails() : "정보 없음");
	            // 키워드
	            row.createCell(4).setCellValue(product.getProductKeyword() != null ? product.getProductKeyword() : "정보 없음");
	            // 상품명
	            row.createCell(5).setCellValue(product.getProductName() != null ? product.getProductName() : "정보 없음");
	            // 렌탈가
	            Cell priceCell = row.createCell(6);
	            priceCell.setCellValue(product.getProductPrice() != 0 ? product.getProductPrice() : 0);
	            priceCell.setCellStyle(numberCellStyle); // 숫자 형식 스타일 적용
	            // 렌탈 시작일자
	            Cell startDateCell = row.createCell(7);
	            if (product.getStartDate() != null) {
	                startDateCell.setCellValue(new java.util.Date(product.getStartDate().getTime()));
	                startDateCell.setCellStyle(dateCellStyle);
	            } else {
	                startDateCell.setCellValue("정보 없음");
	            }
	            // 렌탈 종료일자
	            Cell endDateCell = row.createCell(8);
	            if (product.getEndDate() != null) {
	                endDateCell.setCellValue(new java.util.Date(product.getEndDate().getTime()));
	                endDateCell.setCellStyle(dateCellStyle);
	            } else {
	                endDateCell.setCellValue("정보 없음");
	            }
	            // 할인율
	            row.createCell(9).setCellValue(product.getDiscountRate());
	            // 재고 수량
	            Cell stockCell = row.createCell(10);
	            stockCell.setCellValue(product.getProductStock());
	            stockCell.setCellStyle(numberCellStyle); // 숫자 형식 스타일 적용
	            // 옵션 (true/false)
	            row.createCell(11).setCellValue(product.isProductOptionId() ? "옵션 있음" : "옵션 없음");
	            // 상품 내용
	            row.createCell(12).setCellValue(product.getProductContent() != null ? product.getProductContent() : "정보 없음");
	            // 브랜드
	            row.createCell(13).setCellValue(product.getBrand() != null ? product.getBrand() : "비비도");
	            // 제조사
	            row.createCell(14).setCellValue(product.getManufacturer() != null ? product.getManufacturer() : "비비도");
	            // 원산지
	            row.createCell(15).setCellValue(product.getProductOrigin() != null ? product.getProductOrigin() : "국내");
	            // 상품 상태
	            row.createCell(16).setCellValue(product.getProductStatus());
	            // 배송 방법
	            row.createCell(17).setCellValue(product.getDeliveryMethod() != null ? product.getDeliveryMethod() : "정보 없음");
	            // 택배사
	            row.createCell(18).setCellValue(product.getDeliveryCompany() != null ? product.getDeliveryCompany() : "정보 없음");
	            // 배송비
	            row.createCell(19).setCellValue(product.getDeliveryPrice());
	            // 출고지
	            row.createCell(20).setCellValue(product.getAddress() != null ? product.getAddress() : "정보 없음");
	            // 등록일자
	            if (product.getCreateDate() != null) {
	                row.createCell(21).setCellValue(product.getCreateDate().toString());
	            } else {
	                row.createCell(21).setCellValue("Invalid Date");
	            }
	            // 수정일자
	            if (product.getUpdateDate() != null) {
	                row.createCell(22).setCellValue(product.getUpdateDate().toString());
	            } else {
	                row.createCell(22).setCellValue("Invalid Date");
	            }
	            // 승인일자
	            if (product.getApprovalDate() != null) {
	                row.createCell(23).setCellValue(new java.util.Date(product.getApprovalDate().getTime()));
	                row.getCell(23).setCellStyle(dateCellStyle); // 날짜 형식 스타일 적용
	            } else {
	                row.createCell(23).setCellValue("정보 없음");
	            }
	            // 승인자 ID
	            row.createCell(24).setCellValue(product.getApprovalId() != null ? product.getApprovalId() : "정보 없음");
	            // 비고
	            row.createCell(25).setCellValue(product.getComments() != null ? product.getComments() : "정보 없음");
	        }

	        // 컬럼 너비 자동 조정
	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);

	            // 추가적으로 각 열의 너비를 약간 넓혀서 보기 좋게 만들기
	            int columnWidth = sheet.getColumnWidth(i);
	            sheet.setColumnWidth(i, columnWidth + 1024); // 자동 너비에 여유를 더함
	        }

	        // 엑셀 데이터를 바이트 배열로 반환
	        workbook.write(bos);
	        return bos.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            bos.close();
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}






	  @Override
	    public List<ProductVO> getProductsByIds(List<String> productIds) {
	        Map<String, Object> paramMap = new HashMap<>();
	        paramMap.put("productIds", productIds);
	        return productDAO.getProductsByIds(paramMap);
	    }
	
	  
	  
	  @Override
	  public void changeProductStatus(List<String> productIds, int status) {
	        for (String productId : productIds) {
	            productDAO.updateProductStatus(productId, status);
	        }
	    }




    
	
	

	////////////////////////////상품 등록 페이지 시작////////////////////////////////
	 // 상품 등록
	@Override
	   public void registerProduct(ProductVO productVO, List<ProductVO> productImages) {
        // 상품 등록
        productDAO.insertProduct(productVO);

        // 이미지 등록
        for (ProductVO productImage : productImages) {
            productDAO.insertProductImage(productImage);
        }
    
    }
	
	
	
	///////////////////////////////상품 등록 페이지 끝////////////////////////////////
	 
	
	
	
	
	
}

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public int updateProduct(ProductVO product) {
		return productDAO.updateProduct(product);
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
	public Map<String, Integer> getRentalCounts() {
		return productDAO.getRentalCounts();
	}
	
	@Override
	public byte[] exportProductsToExcel(List<String> productIds) {
	    List<ProductVO> products = productDAO.getProductsByIds(productIds);

	    // 엑셀 생성
	    try (Workbook workbook = new XSSFWorkbook(); // .xlsx 형식의 엑셀 파일을 생성
	         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

	        Sheet sheet = workbook.createSheet("Products");

	        // 헤더 작성
	        Row headerRow = sheet.createRow(0);
	        String[] headers = {"상품 ID", "상품명", "카테고리", "가격", "재고", "등록일"};
	        for (int i = 0; i < headers.length; i++) {
	            headerRow.createCell(i).setCellValue(headers[i]);
	        }

	        // 날짜 셀 스타일 생성
	        CellStyle dateCellStyle = workbook.createCellStyle();
	        CreationHelper createHelper = workbook.getCreationHelper();
	        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

	        // 상품 데이터 작성
	        int rowNum = 1;
	        for (ProductVO product : products) {
	            Row row = sheet.createRow(rowNum++);
	            row.createCell(0).setCellValue(product.getProductId());  // 상품 ID
	            row.createCell(1).setCellValue(product.getProductName());  // 상품명
	            row.createCell(2).setCellValue(product.getProductCategory());  // 카테고리
	            row.createCell(3).setCellValue(product.getProductPrice());  // 가격
	            row.createCell(4).setCellValue(product.getProductStock());  // 재고

	            // 등록일을 날짜로 처리하여 셀에 설정
	            if (product.getCreateDate() != null) {
	                // LocalDate를 java.util.Date로 변환
	                java.util.Date utilDate = Date.from(product.getCreateDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

	                // 날짜 셀 생성
	                Cell dateCell = row.createCell(5);
	                dateCell.setCellValue(utilDate);  // 날짜 값 설정
	                dateCell.setCellStyle(dateCellStyle);  // 날짜 셀 스타일 적용
	            } else {
	                row.createCell(5).setCellValue("");  // 등록일이 null인 경우 빈 셀
	            }
	        }

	        // 엑셀 컬럼 자동 크기 조정
	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        // 엑셀 파일을 ByteArrayOutputStream에 작성
	        workbook.write(bos);
	        bos.flush();  // 버퍼를 플러시하여 데이터가 완전히 기록되도록 함


	        return bos.toByteArray();  // 최종적으로 바이트 배열로 반환
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;  // 오류 발생 시 null 반환
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

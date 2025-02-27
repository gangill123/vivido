package com.vivido.controller;



import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.springframework.core.io.FileSystemResource;



import org.springframework.core.io.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.FileNotFoundException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vivido.domain.ChangeStatusRequest;
import com.vivido.domain.ProductOptionVO;
import com.vivido.domain.ProductVO;
import com.vivido.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.vivido.domain.OptionType;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/management/products")
public class ProductRestController {

	@Autowired
	private ProductService productService;

	//////////////////////////// 상품 목록 페이지 시작////////////////////////////////

	// 전체 상품 조회 API
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllProducts(
			@RequestParam(value = "page", defaultValue = "1") int pageNum,
			@RequestParam(value = "size", defaultValue = "5") int pageSize) {

		// 상품 목록과 페이징 정보 가져오기
		Map<String, Object> response = productService.getProducts(pageNum, pageSize);

		return ResponseEntity.ok(response);
	}

	// 상품 삭제 API (DELETE /management/products/{productId})
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
		boolean isDeleted = productService.deleteProductById(productId);
		if (isDeleted) {
			return ResponseEntity.noContent().build(); // 204 No Content
		} else {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteSelectedProducts(@RequestBody Map<String, List<String>> requestBody) {
		List<String> productIds = requestBody.get("productIds");

		boolean isDeleted = productService.deleteProductsByIds(productIds); // 여러 개 상품 삭제
		if (isDeleted) {
			return ResponseEntity.noContent().build(); // 204 No Content
		} else {
			return ResponseEntity.notFound().build(); // 404 Not Found
		}
	}

	// 상품 정보 조회 API
	@GetMapping("/{productId}")
	public ResponseEntity<ProductVO> getProductInfo(@PathVariable String productId) {
		ProductVO product = productService.getProductById(productId);
		return ResponseEntity.ok(product);
	}

	@PostMapping("/update")
	public ResponseEntity<Map<String, String>> updateProduct(
	        @RequestParam("productId") String productId,
	        @RequestParam("productCategory") String productCategory,
	        @RequestParam("productCategoryDetails") String productCategoryDetails,
	        @RequestParam("productKeyword") String productKeyword,
	        @RequestParam("productName") String productName,
	        @RequestParam("productPrice") int productPrice,
	        @RequestParam("discountedPrice") int discountedPrice,
	        @RequestParam("discountRate") int discountRate,
	        @RequestParam("productStock") int productStock,
	        @RequestParam("productContent") String productContent,
	        @RequestParam("brand") String brand,
	        @RequestParam("manufacturer") String manufacturer,
	        @RequestParam("productOrigin") String productOrigin,
	        @RequestParam("createDate") String createDate,
	        @RequestParam("comments") String comments,
	        @RequestParam(value = "productImages", required = false) MultipartFile[] productImages,
	        @RequestParam("deliveryMethod") String deliveryMethod,
	        @RequestParam("deliveryCompany") String deliveryCompany,
	        @RequestParam(value = "discountedPrice", defaultValue = "0") int deliveryPrice,
	        @RequestParam("address") String address) {
	    
	    Map<String, String> response = new HashMap<>();

	    // 기존 상품 정보 조회
	    ProductVO existingProduct = productService.getProductById(productId);
	    if (existingProduct == null) {
	        response.put("status", "error");
	        response.put("message", "상품이 존재하지 않습니다.");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }

	    // 새로운 값으로 업데이트
	    existingProduct.setProductCategory(productCategory);
	    existingProduct.setProductCategoryDetails(productCategoryDetails);
	    existingProduct.setProductKeyword(productKeyword);
	    existingProduct.setProductName(productName);
	    existingProduct.setProductPrice(productPrice);
	    existingProduct.setDiscountedPrice(discountedPrice);
	    existingProduct.setDiscountRate(discountRate);
	    existingProduct.setProductStock(productStock);
	    existingProduct.setProductContent(productContent);
	    existingProduct.setBrand(brand);
	    existingProduct.setManufacturer(manufacturer);
	    existingProduct.setProductOrigin(productOrigin);
	    existingProduct.setCreateDate(LocalDate.parse(createDate));
	    existingProduct.setComments(comments);
	    existingProduct.setDeliveryMethod(deliveryMethod);
	    existingProduct.setDeliveryCompany(deliveryCompany);
	    existingProduct.setDeliveryPrice(deliveryPrice);
	    existingProduct.setAddress(address);

	    // 이미지 파일 저장 및 업데이트
	    if (productImages != null && productImages.length > 0) {
	        List<String> imageUrls = new ArrayList<>();
	        String thumbnailUrl = null;

	        for (int i = 0; i < productImages.length; i++) {
	            MultipartFile imageFile = productImages[i];
	            try {
	                // 이미지 파일 저장
	                String imageFileName = saveImage(imageFile);
	                imageUrls.add(imageFileName);

	                // 첫 번째 이미지를 썸네일로 지정
	                if (i == 0) {
	                    thumbnailUrl = createThumbnail(imageFileName);
	                }
	            } catch (IOException e) {
	                response.put("status", "error");
	                response.put("message", "이미지 처리 중 오류 발생: " + e.getMessage());
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	            }
	        }

	        // 기존 이미지 정보 갱신
	        existingProduct.setImageUrl(String.join(",", imageUrls)); // 여러 개의 이미지를 ,로 구분하여 저장
	        existingProduct.setThumbnailUrl(thumbnailUrl);
	    }

	    // 상품 업데이트 실행
	    try {
	        productService.updateProduct(existingProduct);
	        response.put("status", "success");
	        response.put("message", "상품 정보가 성공적으로 업데이트되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "상품 업데이트 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}







	@GetMapping("/search")
	public ResponseEntity<List<ProductVO>> searchProducts(
			@RequestParam(value = "product_name", required = false) String productName,
			@RequestParam(value = "product_id", required = false) String productId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "create_date", required = false) String createDate) {

		// ProductVO 객체 생성하여 검색 조건 설정
		ProductVO productVO = new ProductVO();
		productVO.setProductName(productName);
		productVO.setProductId(productId);
		productVO.setProductKeyword(keyword);

		// create_date가 유효하면 LocalDate로 변환
		if (createDate != null && !createDate.isEmpty()) {
			try {
				// "yyyy-MM-dd" 형식의 문자열을 LocalDate로 변환
				LocalDate localDate = LocalDate.parse(createDate);
				productVO.setCreateDate(localDate); // productVO에 LocalDate로 설정
			} catch (DateTimeParseException e) {
				// 잘못된 날짜 형식에 대한 예외 처리
				System.out.println("잘못된 날짜 형식: " + createDate);
			}
		}

		// DB에서 검색 수행 (예시)
		List<ProductVO> products = productService.searchProducts(productVO); // Service 메서드 호출
		return ResponseEntity.ok(products); // 결과 반환
	}

	@GetMapping("/getSubcategories")
	public List<String> getSubcategories(@RequestParam String productCategory) {
		// 카테고리로 해당 물품 정보 반환
		return productService.getSubcategoriesByCategory(productCategory);
	}

	@GetMapping("/searchProducts")
	public List<ProductVO> searchProducts(@RequestParam String productCategory,
			@RequestParam String productCategoryDetails) {
		// 카테고리와 세분류에 맞는 상품 정보를 가져옴
		List<ProductVO> products = productService.getProductsByCategoryAndSubcategory(productCategory,
				productCategoryDetails);
		return products; // 상품 목록을 JSON 형태로 반환
	}

	// 렌탈 상태 데이터 반환
	@GetMapping("/displayStatus")
	public Map<String, Integer> getDisplayStatus() {
		return productService.getDisplayCounts();
	}
	
	@PostMapping("/exportProducts")
	public ResponseEntity<?> exportProductsToExcel(@RequestBody Map<String, List<String>> requestBody) {
	    // 상품 ID 목록을 받아옵니다.
	    List<String> productIds = requestBody.get("productIds");

	    // 엑셀 파일 생성
	    byte[] excelFile = productService.exportProductsToExcel(productIds);

	    // 엑셀 파일 생성에 실패한 경우
	    if (excelFile == null || excelFile.length == 0) {
	        String errorMessage = "파일 생성에 실패했습니다.";
	        // 오류 메시지 반환
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(errorMessage);  // 오류 메시지는 문자열로 반환
	    }

	    // 파일 다운로드 헤더 설정
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	    headers.setContentDisposition(ContentDisposition.attachment().filename("products.xlsx").build());
	    headers.setContentLength(excelFile.length);

	    // 엑셀 파일과 함께 응답을 반환
	    return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
	}
	
	 @PostMapping("/changeStatus")
	    public ResponseEntity<String> changeStatus(@RequestBody ChangeStatusRequest request) {
	        productService.changeProductStatus(request.getProductIds(), request.getStatus());
	        return ResponseEntity.ok("상품 상태가 변경되었습니다.");
	    }


	


////////////////////////////상품 목록 페이지 끝////////////////////////////////

//////////////////////////// 상품 등록 페이지 시작////////////////////////////////
	 
	 private void parseOptions(List<ProductOptionVO> options, List<String> optionList, String productId, OptionType optionType) {
		    for (String option : optionList) {
		        try {
		            String[] data = option.split(" - ");
		            if (data.length == 3) {
		                String name = data[0];
		                int status = "진열".equals(data[1]) ? 1 : 0;
		                int price = Integer.parseInt(data[2].replace("원", "").trim());
		                options.add(new ProductOptionVO(0, productId, optionType, name, status, price));
		            }
		        } catch (Exception e) {
		            System.out.println("옵션 데이터 파싱 실패: " + option);
		        }
		    }
		}
	 
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerProduct(
	        @RequestParam("productId") String productId,
	        @RequestParam("productCategory") String productCategory,
	        @RequestParam("productCategoryDetails") String productCategoryDetails,
	        @RequestParam("productKeyword") String productKeyword, 
	        @RequestParam("productName") String productName,
	        @RequestParam("productPrice") int productPrice, 
	        @RequestParam("discountedPrice") int discountedPrice, 
	        @RequestParam("discountRate") int discountRate,
	        @RequestParam("productStock") int productStock, 
	        @RequestParam("productContent") String productContent,
	        @RequestParam("brand") String brand, 
	        @RequestParam("manufacturer") String manufacturer,
	        @RequestParam("productOrigin") String productOrigin, 
	        @RequestParam("createDate") String createDate,
	        @RequestParam("comments") String comments, 
	        @RequestParam(value = "productImages", required = false) MultipartFile[] productImages,
	        @RequestParam("deliveryMethod") String deliveryMethod,
	        @RequestParam("deliveryCompany") String deliveryCompany,
	        @RequestParam("deliveryPrice") int deliveryPrice,
	        @RequestParam("address") String address,
	        @RequestParam(value = "productColor", required = false) String productColor,
	        @RequestParam(value = "productSize", required = false) String productSize,
	        @RequestParam(value = "additionalProduct", required = false) String additionalProduct,

	        @RequestParam(value = "optionValues", required = false) String[] optionValues

	        
	) {
	    Map<String, String> response = new HashMap<>();

	    ProductVO productVO = new ProductVO();
	    productVO.setProductId(productId);
	    productVO.setProductCategory(productCategory);
	    productVO.setProductCategoryDetails(productCategoryDetails);
	    productVO.setProductKeyword(productKeyword);
	    productVO.setProductName(productName);
	    productVO.setProductPrice(productPrice);
	    productVO.setDiscountedPrice(discountedPrice);
	    productVO.setDiscountRate(discountRate);
	    productVO.setProductStock(productStock);
	    productVO.setProductContent(productContent);
	    productVO.setBrand(brand);
	    productVO.setManufacturer(manufacturer);
	    productVO.setProductOrigin(productOrigin);
	    productVO.setCreateDate(LocalDate.parse(createDate));
	    productVO.setComments(comments);

	    // 배송 관련 정보 추가
	    productVO.setDeliveryMethod(deliveryMethod);
	    productVO.setDeliveryCompany(deliveryCompany);
	    productVO.setDeliveryPrice(deliveryPrice);
	    productVO.setAddress(address);
	    
	    
	    // 색상, 사이즈, 추가 상품 옵션 리스트 초기화
        List<String> colorList = productColor != null ? Arrays.asList(productColor.split("\\|")) : new ArrayList<>();
        List<String> sizeList = productSize != null ? Arrays.asList(productSize.split("\\|")) : new ArrayList<>();
        List<String> additionalList = additionalProduct != null ? Arrays.asList(additionalProduct.split("\\|")) : new ArrayList<>();

        // 옵션 정보 추가
        List<ProductOptionVO> productOptions = new ArrayList<>();
     // 색상 옵션 추가 전
        for (String color : colorList) {
            String[] data = color.trim().split(" - ");
            if (data.length < 3) continue;
            try {
                String name = data[0].trim();
                int status = "진열".equals(data[1].trim()) ? 1 : 0;
                int price = Integer.parseInt(data[2].trim().replace("원", "").replace(",", ""));
                productOptions.add(new ProductOptionVO(0, productId, OptionType.COLOR, name, price, status));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format for color: " + color);
            }
        }

        for (String size : sizeList) {
            String[] data = size.trim().split(" - ");
            if (data.length < 3) continue;
            try {
                String name = data[0].trim();
                int status = "진열".equals(data[1].trim()) ? 1 : 0;
                int price = Integer.parseInt(data[2].trim().replace("원", "").replace(",", ""));
                productOptions.add(new ProductOptionVO(0, productId, OptionType.SIZE, name, price, status));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format for size: " + size);
            }
        }

        for (String additional : additionalList) {
            String[] data = additional.trim().split(" - ");
            if (data.length < 3) continue;
            try {
                String name = data[0].trim();
                int status = "진열".equals(data[1].trim()) ? 1 : 0;
                int price = Integer.parseInt(data[2].trim().replace("원", "").replace(",", ""));
                productOptions.add(new ProductOptionVO(0, productId, OptionType.ADDITIONAL, name, price, status));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format for additional product: " + additional);
            }
        }

        // 중복 제거 후 추가
        Set<String> uniqueOptions = new HashSet<>();
        if (optionValues != null) {
            for (String optionValue : optionValues) {
                if (uniqueOptions.add(optionValue.trim())) {
                    productOptions.add(new ProductOptionVO(0, productId, OptionType.ADDITIONAL, optionValue.trim(), 0, 1));
                }
            }
        }


		// 이미지 파일 저장 및 썸네일 생성
		List<ProductVO> productImageList = new ArrayList<>();
		if (productImages != null) {
		    for (int i = 0; i < productImages.length; i++) {
		        MultipartFile imageFile = productImages[i];
		        try {
		            // 이미지 파일 저장 (파일명만 반환됨)
		            String imageFileName = saveImage(imageFile);

		            // 썸네일 파일 생성
		            String thumbnailFileName = null;
		            boolean isPrimary = false; // 기본적으로 썸네일이 아니도록 설정

		            // 첫 번째 이미지가 썸네일로 지정된 경우 (원하는 조건에 맞게 수정 가능)
		            if (i == 0) {
		                thumbnailFileName = createThumbnail(imageFileName); // 썸네일 파일 생성
		                isPrimary = true; // 첫 번째 이미지는 썸네일로 설정
		            }

		            // ProductVO에 이미지 정보 설정
		            ProductVO productImageVO = new ProductVO();
		            productImageVO.setProductId(productId);
		            productImageVO.setImageUrl(imageFileName); // 원본 파일명만 저장
		            productImageVO.setThumbnailUrl(thumbnailFileName); // 썸네일 파일명 저장 (없으면 null)
		            productImageVO.setIsPrimary(isPrimary); // 썸네일 여부 설정
		            productImageVO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		            productImageList.add(productImageVO);
		        } catch (IOException e) {
		            response.put("status", "error");
		            response.put("message", "이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		        }
		    }
		}

		try {
			// 서비스로 상품과 이미지 등록
			productService.registerProduct(productVO, productImageList, productOptions);
			response.put("status", "success");
			response.put("message", "상품 등록 성공");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "상품 등록 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private String saveImage(MultipartFile file) throws IOException {
		String directory = "C:/uploads/"; // 파일 저장 경로

		// UUID를 사용하여 중복 방지
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		// 디렉터리 생성
		File uploadDir = new File(directory);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// 파일 저장
		File dest = new File(uploadDir, fileName);
		file.transferTo(dest);

		// **파일명만 반환 (경로 제외)**
		return fileName;
	}

	private String createThumbnail(String imageFileName) throws IOException {
		String directory = "C:/uploads/"; // 원본 이미지 파일이 저장된 경로
		String thumbnailDir = "C:/uploads/thumbnails/"; // 썸네일 저장 경로

		File originalImageFile = new File(directory + imageFileName);
		File thumbnailDirectory = new File(thumbnailDir);

		if (!thumbnailDirectory.exists()) {
			thumbnailDirectory.mkdirs();
		}

		// 썸네일 파일 이름 생성
		String thumbnailFileName = imageFileName.replace(".", "_thumb.");
		File thumbnailFile = new File(thumbnailDirectory, thumbnailFileName);

		// 원본 이미지 읽기 및 썸네일 생성
		BufferedImage originalImage = ImageIO.read(originalImageFile);
		int thumbnailWidth = 150;
		int thumbnailHeight = 150;
		BufferedImage thumbnailImage = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
		thumbnailImage.getGraphics().drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);

		// 썸네일 저장
		ImageIO.write(thumbnailImage, "jpg", thumbnailFile);

		// **썸네일 파일명만 반환**
		return thumbnailFileName;
	}

	private static final String UPLOAD_DIR = "C:/uploads/summer"; // 업로드 폴더 경로

	 @PostMapping("/uploadImage")
	    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
	        Map<String, String> response = new HashMap<>();

	        try {
	            // 파일 저장 경로 생성
	            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	            File saveFile = new File(UPLOAD_DIR, fileName);
	            file.transferTo(saveFile); // 파일 저장

	            // 저장된 이미지의 URL 생성
	            String fileUrl = "/uploads/summer/" + fileName;  // 클라이언트가 접근할 URL
	            response.put("fileUrl", fileUrl);

	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }

	//////////////////////////// 상품 등록 페이지 끝////////////////////////////////

}

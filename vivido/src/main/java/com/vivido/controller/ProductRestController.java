package com.vivido.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vivido.domain.ProductVO;
import com.vivido.service.ProductService;

import ch.qos.logback.core.model.Model;

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

	// 상품 정보 조회 API
	@GetMapping("/{productId}")
	public ResponseEntity<ProductVO> getProductInfo(@PathVariable String productId) {
		ProductVO product = productService.getProductById(productId);
		return ResponseEntity.ok(product);
	}

	// 상품 수정 API
	@PutMapping("/{productId}")
	public ResponseEntity<String> updateProduct(@PathVariable String productId, @RequestBody ProductVO product) {

		// productId를 @RequestBody에서 전달된 값에 덮어씌우는 방식
		product.setProductId(productId);

		// 상품 수정 수행
		int result = productService.updateProduct(product);

		// 상품 수정이 성공한 경우
		if (result > 0) {
			return ResponseEntity.ok("상품 수정 성공");
		} else {
			// 수정 실패 시 좀 더 구체적인 실패 이유 제공 (선택 사항)
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 수정 실패");
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
	@GetMapping("/rentalStatus")
	public Map<String, Integer> getRentalStatus() {
		return productService.getRentalCounts();
	}
	//////////////////////////// 상품 목록 페이지 끝////////////////////////////////

	//////////////////////////// 상품 등록 페이지 시작////////////////////////////////

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerProduct(@RequestParam("productId") String productId,
			@RequestParam("productCategory") String productCategory,
			@RequestParam("productCategoryDetails") String productCategoryDetails,
			@RequestParam("productKeyword") String productKeyword, @RequestParam("productName") String productName,
			@RequestParam("productPrice") int productPrice, @RequestParam("discountRate") int discountRate,
			@RequestParam("productStock") int productStock, @RequestParam("productContent") String productContent,
			@RequestParam("brand") String brand, @RequestParam("manufacturer") String manufacturer,
			@RequestParam("productOrigin") String productOrigin, @RequestParam("createDate") String createDate,
			@RequestParam("comments") String comments, @RequestParam("productImages") MultipartFile[] productImages) {
		Map<String, String> response = new HashMap<>();

		ProductVO productVO = new ProductVO();
		productVO.setProductId(productId);
		productVO.setProductCategory(productCategory);
		productVO.setProductCategoryDetails(productCategoryDetails);
		productVO.setProductKeyword(productKeyword);
		productVO.setProductName(productName);
		productVO.setProductPrice(productPrice);
		productVO.setDiscountRate(discountRate);
		productVO.setProductStock(productStock);
		productVO.setProductContent(productContent);
		productVO.setBrand(brand);
		productVO.setManufacturer(manufacturer);
		productVO.setProductOrigin(productOrigin);
		productVO.setCreateDate(LocalDate.parse(createDate));
		productVO.setComments(comments);

		// 이미지 파일 저장 및 썸네일 생성
		List<ProductVO> productImageList = new ArrayList<>();
		if (productImages != null) {
		    for (MultipartFile imageFile : productImages) {
		        try {
		            // 이미지 파일 저장 (경로 없이 파일명만 반환)
		            String imageFileName = saveImage(imageFile);

		            // 원본 이미지 파일 객체 생성 (저장된 디렉터리 경로를 추가)
		            // C:/uploads/ -> src/main/resources/static/uploads/
		            File originalImageFile = new File("src/main/resources/static/uploads/" + imageFileName);

		            // 썸네일 생성 (파일명만 반환)
		            String thumbnailFileName = createThumbnail(originalImageFile);

		            // ProductVO에 이미지 정보 설정
		            ProductVO productImageVO = new ProductVO();
		            productImageVO.setProductId(productId);
		            productImageVO.setImageUrl("/uploads/" + imageFileName); // 상대 URL로 반환 (웹에서 접근 가능)
		            productImageVO.setThumbnailUrl("/uploads/thumbnails/" + thumbnailFileName); // 상대 URL로 썸네일 경로 설정
		            productImageVO.setIsPrimary(false); // 기본 이미지는 false로 설정 (필요에 따라 true로 변경)
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
			productService.registerProduct(productVO, productImageList);
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
	    // 실제 업로드할 디렉터리 경로 (운영 환경에서 접근 가능한 경로)
	    String directory = "src/main/resources/static/uploads/"; // 상대 경로로 변경
	    
	    // 파일 이름을 UUID로 변경하여 중복 방지
	    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

	    // 저장할 디렉터리 경로를 지정
	    File uploadDir = new File(directory);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdirs(); // 디렉터리가 없으면 생성
	    }

	    // 파일을 해당 경로에 저장
	    File dest = new File(uploadDir, fileName);
	    
	    if (!dest.exists()) {
	        dest.createNewFile(); // 파일이 없으면 새로운 파일 생성
	    }
	    
	    file.transferTo(dest); // IOException 발생 가능

	    // 저장된 이미지 URL 반환 (웹에서 접근할 수 있도록)
	    return "/uploads/" + fileName;

	}

	private String createThumbnail(File originalImageFile) throws IOException {
	    // 프로젝트의 상대 경로로 설정 (예: static/thumbnails 디렉토리)
	    String thumbnailDir = "src/main/resources/static/uploads/thumbnails/";  // 상대 경로

	    File thumbnailDirectory = new File(thumbnailDir);
	    if (!thumbnailDirectory.exists()) {
	        thumbnailDirectory.mkdirs(); // 디렉터리가 없다면 생성
	    }

	    // 썸네일 파일 이름 생성
	    String thumbnailFileName = originalImageFile.getName().replace(".", "_thumb.");
	    File thumbnailFile = new File(thumbnailDirectory, thumbnailFileName);

	    // 썸네일 이미지 생성 (예시: 150x150 크기로 리사이즈)
	    BufferedImage originalImage = ImageIO.read(originalImageFile);
	    int thumbnailWidth = 150;
	    int thumbnailHeight = 150;
	    BufferedImage thumbnailImage = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
	    thumbnailImage.getGraphics().drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);

	    // 썸네일 파일 저장
	    ImageIO.write(thumbnailImage, "jpg", thumbnailFile); // 파일 형식은 필요에 따라 변경

	    // 썸네일 URL 반환
	    return "/uploads/thumbnails/" + thumbnailFileName; // 웹에서 접근할 수 있는 경로
	}
	
	   private static final String UPLOAD_DIR = "C:/uploads/summer"; // 업로드 폴더 경로

	 @PostMapping("/uploadImage")
	    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
	        Map<String, String> response = new HashMap<>();

	        try {
	            // 업로드 디렉토리 경로
	            Path uploadPath = Paths.get(UPLOAD_DIR);
	            
	            // 디렉토리가 존재하지 않으면 폴더 생성
	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);  // 폴더 생성
	            }

	            // 파일 이름 설정 (UUID 사용 등)
	            String fileName = UUID.randomUUID().toString() + ".jpg"; 
	            Path filePath = uploadPath.resolve(fileName);  // 파일 경로 생성

	            // 파일을 업로드 디렉토리에 저장
	            file.transferTo(filePath);

	            // 업로드된 파일의 URL 반환
	            response.put("fileUrl", "/uploads/" + fileName);

	            return ResponseEntity.ok(response);
	        } catch (IOException e) {
	            response.put("error", "파일 업로드 실패");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }


	//////////////////////////// 상품 등록 페이지 끝////////////////////////////////

}

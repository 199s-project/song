package com.example.demo.Controller;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.AvgMaterialpriceVO;
import com.example.demo.dto.CompanyVO;
import com.example.demo.dto.FileVO;
import com.example.demo.dto.InventoryAppropriateAmountVO;
import com.example.demo.dto.InventoryProductAppropriateAmountVO;
import com.example.demo.dto.InventoryVO;
import com.example.demo.dto.MaterialVO;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.OrderformDetailVO;
import com.example.demo.dto.OrderformVO;
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.ProductionDetailVO;
import com.example.demo.dto.ProductionVO;
import com.example.demo.dto.QcDetailVO;
import com.example.demo.dto.QcVO;
import com.example.demo.dto.QuotationDetailVO;
import com.example.demo.dto.QuotationVO;
import com.example.demo.dto.RecipeDetailVO;
import com.example.demo.dto.RecipeVO;
import com.example.demo.service.ProjectService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProjectController {

	private ModelAndView mv;
	
	@Autowired
	private ProjectService projectService;

	// 홈 화면 (index.html) 이동
	@GetMapping("index")
	public String index(){
	    log.info("index");
	    return "redirect:/";
	}
	
	// 로그인화면 이동
	@GetMapping("login")
	public ModelAndView getLogin() {
		mv = projectService.getLogin();
		return mv;
	}
   
	// 로그인
	@PostMapping("login")
	public ModelAndView postLogin(@RequestParam Map<String,Object> map,HttpSession session) {
		mv = projectService.postLogin(map, session);
		return mv;
	}
   
	// 로그아웃
	@GetMapping("logout")
	public String getLogout(HttpSession session) {
		mv = projectService.getLogout(session);
		return "login";
	}
	
	// 회원가입 화면 이동
	@GetMapping("register")
	public String getRegister(){
		log.info("register");
		return "register";
	}
   
	// 회원가입
	@PostMapping("register")
	public ModelAndView postRegister(@RequestParam Map<String,Object> map) {
		mv = projectService.postRegister(map);
		System.out.println(map);
		return mv;
	}
   
	
	// 아이디 중복검사
	@GetMapping("/check-member_id")
	public ResponseEntity<String> checkMember_id(@RequestParam("member_id") String member_id) {
		boolean isTaken = projectService.isMember_idTaken(member_id);
   	
		if (isTaken) {
			return ResponseEntity.ok("이미 사용중인 아이디입니다.");
		} else {
			return ResponseEntity.ok("사용 가능한 아이디입니다.");
		}
	}
   
   
   
	
	

	

	
	
    // 비어있는 페이지
	@GetMapping("notepad")
	public String notepad(){
	    log.info("notepad");
	    return "notepad";
	}	
	
	// 구매계약서 상세 화면 이동
	@GetMapping("quotationDetail")
	public String quotationDetail(){
	    log.info("quotationDetail");
	    
	    return "quotationDetail";
	}	
	

	
	// company 등록
	@PostMapping("addCompany")
	public String addCompany(@RequestBody CompanyVO companyVO){
	    String name = companyVO.getCompany_name();
	    int r = projectService.addCompany(companyVO);
	    return "index";
	}
	

	
	// 제품 등록
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductVO productVO) {
        try {
            // 제품 정보 저장 로직 (예: DB 저장)
            // productService.save(request);
            int r = projectService.addProduct(productVO);
            // inventory에 재고0으로 열만 추가하는 코드
            InventoryVO inventoryVO = new InventoryVO();
            inventoryVO.setInven_item_num(projectService.findMaxProductNum());
            inventoryVO.setInven_name(productVO.getProduct_name());
            int r2 = projectService.addProductInventory(inventoryVO);
            
            // 성공 메시지 반환
            return ResponseEntity.ok(Map.of("message", "Product added successfully"));
        } catch (Exception e) {
            // 오류 발생 시 JSON 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to add product"));
        }
    }

	

	// 파일(이미지) 업로드를 위한 경로지정
	@Value("${org.zerock.upload.path}")
	private String uploadPath;
	
	// 파일 업로드
    @PostMapping(value = "imageUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> imageUpload(@RequestParam(value="fileVO") MultipartFile[] files) {
    	
        try {
        	// 제품의 pk값이 auto-increment로 되어있기 때문에 여기에 1를 더해서 지금 등록한 제품의 pk값을 찾아오려는 과정
        	String maxnum = ""+projectService.findMaxProductNum();
        	
        	// 멀티업로드된 파일 리스트에서 각각의 파일에 대해 데이터베이스에 등록하는 과정
        	for (MultipartFile file : files) {
        		
        		String originalName = file.getOriginalFilename();
        		String uuid = UUID.randomUUID().toString();
        		String uploadName = uuid + "_" + originalName;
        		
        		Path savePath = Paths.get(uploadPath, uploadName);
        		file.transferTo(savePath);
  			
        		FileVO VO = new FileVO();
  			
        		VO.setFile_name(uploadName);
        		VO.setFile_path("images/" + uploadName);

        		VO.setFile_subject("product");
        		VO.setFile_pk(maxnum);
  			
        		int r = projectService.fileUpload(VO);
        	}
            // 성공 메시지 반환
            return ResponseEntity.ok(Map.of("message", "File added successfully"));
            
        } catch (Exception e) {
            // 오류 발생 시 JSON 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to add file"));
        }
        
    }
    
    // 제품 등록 시 제품코드 중복확인
    @ResponseBody
	@PostMapping("productCodeCheck")
	public int productCodeCheck(@RequestParam("product_code") String product_code) {
	    int cnt = projectService.productCodeCheck(product_code);
	    return cnt;
	}
    
    // 제품 등록 시 입력한 것과 가장 일치하는 협력사 이름을 찾는 과정
    @ResponseBody
	@PostMapping("companyNameCheck")
	public String companyNameCheck(@RequestParam("company_name") String company_name) {
    	company_name = company_name + "%";
	    String company = projectService.companyNameCheck(company_name);
	    return company;
	}
	
    // 구매계약서 등록 화면 이동
    @GetMapping("getOrderformRegister")
    public ModelAndView getOrderformRegister() throws Exception {
    	mv = projectService.getOrderformRegister();
        return mv;
    }
    
    // 구매계약서 등록
    @PostMapping("postOrderformRegister")
    public ModelAndView postOrderformRegister(@RequestParam Map<String,Object> map) throws Exception {
    	
    	
    	mv = projectService.postOrderformRegister(map);
    	List<OrderformVO> list = projectService.orderList();
    	mv.addObject("orderList", list);
        return mv;
    }
    
    // 판매계약서 등록 화면 이동
    @GetMapping("getQuotationRegister")
    public ModelAndView getQuotationRegister() throws Exception {
    	mv = projectService.getQuotationRegister();
        return mv;
    }
    
    
    
    // 판매계약서 등록
    @PostMapping("postQuotationRegister")
    public String postQuotationRegister(@RequestParam Map<String,Object> map, @RequestParam("count") int count) throws Exception {
    	
    	CompanyVO company1 = projectService.getCompanyByCompanyName((String)map.get("company1"));
    	CompanyVO company2 = projectService.getCompanyByCompanyName((String)map.get("company2"));
    	
    	QuotationVO quotationVO = new QuotationVO();
    	quotationVO.setQuot_name((String)map.get("quot_name"));
    	quotationVO.setQuot_stat((String)map.get("quot_stat"));
    	quotationVO.setCompany_num(company1.getCompany_num());
    	quotationVO.setCompany_num2(company2.getCompany_num());
    	quotationVO.setQuot_content((String)map.get("content"));
    	quotationVO.setQuot_startdate((String)map.get("start_date"));
    	quotationVO.setQuot_enddate((String)map.get("end_date"));
    	
    	int r = projectService.insertQuotation(quotationVO);
    	
    	int quot_num = projectService.getLastQuotationNum();
    	QuotationVO updatedQuotation = projectService.getQuotationByQuotnum(quot_num);
    	
    	String code1 = updatedQuotation.getQuot_regdate().substring(0,10).replaceAll("-", "");
    	String code2 = String.format("%04d", quot_num % 1000);
    	String code3 = String.format("%04d", updatedQuotation.getCompany_num2() % 1000);
    	String code = code1 + code2 + code3;
    	
    	int s = projectService.insertQuotationCode(quot_num, code);
    	
    	for (int i = 1; i <= count; i++) {
    		String a = "item" + i;
    		String b = "unit_price" + i;
    		String c = "quantity" + i;
    		String d = "total_price" + i;
    		
    		QuotationDetailVO quotationDetailVO = new QuotationDetailVO();
    		
    		
			String product_name = (String) (map.get(a));
			String product_price = (String) (map.get(b));
			
			log.info(product_name);
			if (product_name == "") {
				continue;
			}
			if (product_price == "") {
				continue;
			}
			
			int product_num = projectService.getProductByProductName(product_name).getProduct_num();
			int quotdetail_amount = Integer.parseInt((String)map.get(c));
			int quotdetail_price = Integer.parseInt((String)map.get(d));
			
			quotationDetailVO.setQuot_num(quot_num);
    		quotationDetailVO.setProduct_num(product_num);
    		quotationDetailVO.setQuotdetail_amount(quotdetail_amount);
    		quotationDetailVO.setQuotdetail_price(quotdetail_price);
    		
    		int result = projectService.insertQuotationDetail(quotationDetailVO);
    		
    	}
    	
    	return "redirect:salesContract";
    }
    
    
    //판매계약서 상세보기로 이동
    @GetMapping("getQuotationDetail")
    public String getQuotationDetail(
    		@RequestParam("quot_num") int quot_num, Model model
    		) throws Exception {
    	
    	QuotationVO quotationVO = projectService.getQuotationByQuotnum(quot_num);
    	List<QuotationDetailVO> quotationDetailListVO = projectService.getQuotationDetailListByQuotnum(quot_num);
    	
    	int payment = 0;
    	for (QuotationDetailVO quotationDetail : quotationDetailListVO) {
    		payment += quotationDetail.getQuotdetail_price();
    	}
    	model.addAttribute("payment",payment);
    	
		CompanyVO company1VO = projectService.getCompanyByCompanynum(quotationVO.getCompany_num());
		CompanyVO company2VO = projectService.getCompanyByCompanynum(quotationVO.getCompany_num2());
    	
    	model.addAttribute("quotationDetailListVO",quotationDetailListVO);
		model.addAttribute("company1VO",company1VO);
		model.addAttribute("company2VO",company2VO);
		model.addAttribute("quotationVO",quotationVO);
		
        return "quotationDetail";
    }
    
    
    
    
    
    // 입력한 회사명으로 해당 회사의 정보들을 불러오는 과정
    @GetMapping("/getCompanyByCompanyName")
    public ResponseEntity<CompanyVO> getCompanyByCompanyName(
          @RequestParam("company_name") String company_name
          ) {
       CompanyVO company = projectService.getCompanyByCompanyName(company_name);
       
       return ResponseEntity.ok(company);
    }
    
    // 입력한 제품명으로 해당 제품의 정보들을 불러오는 과정
    @GetMapping("/getProductByProductName")
    public ResponseEntity<ProductVO> getProductByProductName(
          @RequestParam("product_name") String product_name
          ) {
       ProductVO product = projectService.getProductByProductName(product_name);
       
       return ResponseEntity.ok(product);
    }
    
    // ----------------------------------------------------------------------------------------
    
    // 협력사 정보 등록 시 협력사 이름 중복 확인
    @ResponseBody
	@PostMapping("companyNameValidation")
	public int companyNameValidation(@RequestParam("company_name") String company_name) {
	    int cnt = projectService.companyNameValidation(company_name);
	    return cnt;
	}
    
	// 협력사 정보 등록 시 협력사 등록번호 중복 확인
    @ResponseBody
	@PostMapping("companyCodeValidation")
	public int companyCodeValidation(@RequestParam("company_code") String company_code) {
	    int cnt = projectService.companyCodeValidation(company_code);
	    return cnt;
	}
    
    
    
	public static int productPageSIZE = 9; // 한 페이지에 담을 게시글의 개수
	public static int productTotalRecord = 0;
	public static int productTotalPage = 1;

	public static int productStartPage = 1;
	public static int productEndPage = 10;
	public static int productPageListSIZE = 10;
    
    // 제품 리스트 화면 이동
    @GetMapping("product")
    public ModelAndView product(@RequestParam(value = "pageNUM", defaultValue = "1") int pageNUM,
			@RequestParam(value = "pageListNUM", defaultValue = "1") int pageListNUM) {
  	   
    	log.info(pageNUM+"");
    	
		productTotalRecord = projectService.product_totalRecord(); // del=0인 게시글의 총 개수
		productTotalPage = productTotalRecord / productPageSIZE;
		if (productTotalRecord % productPageSIZE != 0) {
			productTotalPage++;
		}

		int start = (pageNUM - 1) * productPageSIZE;

		productStartPage = (pageListNUM - 1) * productPageListSIZE + 1;
		productEndPage = productStartPage + productPageListSIZE - 1;
		if (productEndPage > productTotalPage) {
			productEndPage = productTotalPage;
		}
    	
		ModelAndView mav = new ModelAndView();
		
    	
		List<ProductVO> productList = projectService.productListForPaging(start, productPageSIZE);
  	   
		for (ProductVO product : productList) {
			int product_num = product.getProduct_num();
  		   
			int amount = projectService.fileAmount(product_num);
  		   
			if (amount == 0) {
				product.setFile_name("No Image");
				product.setFile_path("vendors/images/no-image-alert.jpg");
			} else {
				FileVO file = projectService.findFirstImage(product_num);
				product.setFile_amount(amount);
				product.setFile_name(file.getFile_name());
				product.setFile_path(file.getFile_path());
			}
		}
  	   
		mav.addObject("list", productList);
		
		mav.addObject("totalPage", productTotalPage);
		mav.addObject("startPage", productStartPage);
		mav.addObject("pageListNUM", pageListNUM);
		mav.addObject("endPage", productEndPage);
		
		mav.addObject("pageNUM", pageNUM);
		
		mav.setViewName("product");
  	   
		return mav;
	}
    
    
    
    
    
    
    @GetMapping("/getProductDetail")
    public ResponseEntity<?> getProductDetail(@RequestParam("product_num") int product_num, Model model) {
        ProductVO product = projectService.getProductDetail(product_num);
        List<FileVO> files = projectService.getProductImages(product_num);
        int amount = projectService.fileAmount(product_num);
        
        model.addAttribute("product",product);
        
        String[] imagePathArr = new String[amount];
        int cnt = 0;
        
        for (FileVO file : files) {
        	imagePathArr[cnt] = file.getFile_path();
        	cnt++;
        }
        model.addAttribute("imagePathArr",imagePathArr);
        
        if (product != null) {
            return ResponseEntity.ok(model);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @ResponseBody
    @GetMapping("/getCompanyNameList")
    public String[] getCompanyNameList(Model model) {
    	List<CompanyVO> companyList = projectService.getCompanyList();
    	
    	String[] companyNameList = new String[companyList.size()];
    	int cnt = 0;
    	for (CompanyVO company : companyList) {
    		companyNameList[cnt] = company.getCompany_name();
    		cnt++;
    	}
    	model.addAttribute("companyNameList",companyNameList);
    	
        return companyNameList;
    }
	
    
    @ResponseBody
    @GetMapping("/getProductNameList")
    public String[] getProductNameList(Model model) {
    	log.info("controller access");
    	List<ProductVO> productList = projectService.getProductList();
    	
    	String[] productNameList = new String[productList.size()];
    	int cnt = 0;
    	for (ProductVO product : productList) {
    		productNameList[cnt] = product.getProduct_name();
    		cnt++;
    	}
    	model.addAttribute("productNameList",productNameList);
    	
        return productNameList;
    }
 
    
	// 재고관리 화면 이동
	@GetMapping("inventory")
	public String inventory(Model model){
		
		List<InventoryVO> inventoryList = projectService.getInventoryList();
		model.addAttribute("inventoryList",inventoryList);
		
		List<InventoryVO> inventoryMaterialList = projectService.getInventoryMaterialList();
		model.addAttribute("inventoryMaterialList",inventoryMaterialList);
		
		List<InventoryVO> inventoryProductList = projectService.getInventoryProductList();
		model.addAttribute("inventoryProductList",inventoryProductList);
		
		return "inventory";
	}
    
    
 // 윤호 자리@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       
    
    
    
    
    // 협력사 목록 화면 이동
    @GetMapping("company")
    public String company(Model model) {
 	   
 	   List<CompanyVO> list = projectService.getCompanyList();
 	   
// 	   for (CompanyVO company : list) {
// 		   company.setCompany_address(simpleAddress(company.getCompany_address()));
// 	   }
 	   
        model.addAttribute("companyList", list);
 	   return "company";
    }
    
    public static String simpleAddress(String address) {
    	if (address.length() < 8) {
    		return "-";
    	}
        String trimmedAddress = address.substring(8);
        String[] words = trimmedAddress.split(" ");

        if (words.length >= 2) {
            return words[0] + " " + words[1];
        }
        return "-";
    }
    
    
	@PostMapping("updateCompany")
	public String updateCompany(@RequestBody CompanyVO companyVO){
	    String name = companyVO.getCompany_name();
	    log.info("Company's name is : "+ name);
	    int r = projectService.updateCompany(companyVO);
	    return "company";
	}
    
	@ResponseBody
	@PostMapping("quotationProceed")
	public int quotationProceed(@RequestParam("quot_num") int quot_num){
	    log.info("Quotation number is : "+ quot_num);
	    List<QuotationDetailVO> quotdetailList = projectService.getQuotationDetailList(quot_num);
	    
	    for (QuotationDetailVO quotdetail : quotdetailList) {
	    	
	    	int product_num = quotdetail.getProduct_num();
	    	int amount = quotdetail.getQuotdetail_amount();
	    	
	    	InventoryVO inventory = projectService.getInventory(product_num);
	    	if (inventory == null) {
	    		return -1;
	    	}
	    	if (amount > inventory.getInven_amount()) {
	    		return -2;
	    	}
	    }
	    
	    for (QuotationDetailVO quotdetail : quotdetailList) {
	    	
	    	int product_num = quotdetail.getProduct_num();
	    	int amount = quotdetail.getQuotdetail_amount();
	    	
	    	int r = projectService.updateInventoryAmount(product_num, amount);
	    }
	    
	    int s = projectService.updateQuotationStat(quot_num);
	    int t = projectService.uploadReleaseDate(quot_num);
	    
	    return 1;
	}
	
	
	
	
	// 김윤호 25/01/27 부터 새로 작성	

	
	
	
	
	
	
	
	
    
    
// 윤호 자리@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    
    
    
    

// -------------------------new 작업공간(이의재) -------------------------
    

    
    //구매계약서 상세보기로 이동
    @GetMapping("getOrderformDetail")
    public ModelAndView getOrderformDetail(
    		@RequestParam("orderform_num") int orderform_num
    		) {
    	mv = projectService.getOrderformDetail(orderform_num);
    	return mv;
    }
    
    
    //계약서 List에서 구매,판매 상세보기로 이동
    @GetMapping("getAllFormDetail")
    public String getAllFormDetail(
    		@RequestParam("this_num") String this_num
    		) {
    	if (this_num.contains("quot")) {
    		int quot_num = Integer.parseInt(this_num.replaceAll("quot", ""));
    		return "redirect:getQuotationDetail?quot_num="+quot_num;
    	} else {
    		int orderform_num = Integer.parseInt(this_num.replaceAll("order", ""));
    		return "redirect:getOrderformDetail?orderform_num="+orderform_num;
    	}
    }
    
	// 파일 업로드
    @PostMapping(value = "materialImageUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> materialImageUpload(@RequestParam(value="fileVO") MultipartFile[] files) {
    	
        try {
        	// 제품의 pk값이 auto-increment로 되어있기 때문에 여기에 1를 더해서 지금 등록한 제품의 pk값을 찾아오려는 과정
        	String maxnum = ""+projectService.findMaxMaterialNum();
        	
        	// 멀티업로드된 파일 리스트에서 각각의 파일에 대해 데이터베이스에 등록하는 과정
        	for (MultipartFile file : files) {
        		
        		String originalName = file.getOriginalFilename();
        		String uuid = UUID.randomUUID().toString();
        		String uploadName = uuid + "_" + originalName;
        		
        		Path savePath = Paths.get(uploadPath, uploadName);
        		file.transferTo(savePath);
  			
        		FileVO VO = new FileVO();
  			
        		VO.setFile_name(uploadName);
        		VO.setFile_path("images/" + uploadName);

        		VO.setFile_subject("material");
        		VO.setFile_pk(maxnum);
  			
        		int r = projectService.fileUpload(VO);
        	}
            // 성공 메시지 반환
            return ResponseEntity.ok(Map.of("message", "File added successfully"));
            
        } catch (Exception e) {
            // 오류 발생 시 JSON 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to add file"));
        }
        
    }
    
	// 원자재 등록
    @PostMapping("/addMaterial")
    public ResponseEntity<?> addMaterial(@RequestBody MaterialVO materialVO) {
        try {
            // 제품 정보 저장 로직 (예: DB 저장)
            // productService.save(request);
            int r = projectService.addMaterial(materialVO);
            // inventory에 재고0으로 열만 추가하는 코드
            InventoryVO inventoryVO = new InventoryVO();
            inventoryVO.setInven_item_num(projectService.findMaxMaterialNum());
            inventoryVO.setInven_name(materialVO.getMaterial_name());
            int r2 = projectService.addMaterialInventory(inventoryVO);
            // 성공 메시지 반환
            return ResponseEntity.ok(Map.of("message", "Product added successfully"));
        } catch (Exception e) {
            // 오류 발생 시 JSON 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Failed to add product"));
        }
    }

    // 제품 리스트 화면 이동
    @GetMapping("Material")
    public String Material(Model model) {
  	   
  	   List<MaterialVO> MaterialList = projectService.getMaterialList();
  	   
  	   for (MaterialVO material : MaterialList) {
  		   int material_num = material.getMaterial_num();
  		   
  		   int amount = projectService.materialFileAmount(material_num);
  		   
  		   if (amount == 0) {
  			   material.setFile_name("No Image");
  			   material.setFile_path("vendors/images/product-img1.jpg");
  		   } else {
  			   FileVO file = projectService.materialFindFirstImage(material_num);
  			   material.setFile_amount(amount);
  			   material.setFile_name(file.getFile_name());
  			   material.setFile_path(file.getFile_path());
  		   }
  		   
  	   }
  	   
  	   
  	   model.addAttribute("MaterialList",MaterialList);
  	   
  	   return "material";
	}
    
    @GetMapping("/getMaterialDetail")
    public ResponseEntity<?> getMaterialDetail(@RequestParam("material_num") int material_num, Model model) {
        MaterialVO material = projectService.getMaterialDetail(material_num);
        List<FileVO> files = projectService.getMaterialImages(material_num);
        int amount = projectService.materialFileAmount(material_num);
        
        model.addAttribute("material",material);
        
        String[] imagePathArr = new String[amount];
        int cnt = 0;
        
        for (FileVO file : files) {
        	imagePathArr[cnt] = file.getFile_path();
        	cnt++;
        }
        model.addAttribute("imagePathArr",imagePathArr);
        
        if (material != null) {
            return ResponseEntity.ok(model);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @ResponseBody
    @GetMapping("/getMaterialNameList")
    public String[] getMaterialNameList(Model model) {
    	
    	List<MaterialVO> materialList = projectService.getMaterialList();
    	
    	String[] productNameList = new String[materialList.size()];
    	int cnt = 0;
    	for (MaterialVO product : materialList) {
    		productNameList[cnt] = product.getMaterial_name();
    		cnt++;
    	}
    	model.addAttribute("productNameList",productNameList);
    	
        return productNameList;
    }
    
    @ResponseBody
    @PostMapping("/getMaterialNameList")
    public String[] getPostMaterialNameList(Model model) {
    	List<MaterialVO> materialList = projectService.getMaterialList();
    	String[] materialNameList = new String[materialList.size()];
    	int cnt = 0;
    	for (MaterialVO material : materialList) {
    		materialNameList[cnt] = material.getMaterial_name();
    		cnt++;
    	}
    	model.addAttribute("materialNameList", materialNameList);
    	return materialNameList;
    }
    
    // 입력한 원자재명으로 해당 원자재의 정보들을 불러오는 과정
    @GetMapping("/getMaterialByMaterialName")
    public ResponseEntity<MaterialVO> getMaterialByMaterialName(
          @RequestParam("product_name") String product_name
          ) {
       MaterialVO product = projectService.getMaterialByMaterialName(product_name);
       
       return ResponseEntity.ok(product);
    }
    
    
    //원자재 등록 페이지로 이동
    @GetMapping("getMaterialRegister")
    public ModelAndView getMaterialRegister(
    		
    		) {
    	mv = projectService.getMaterialRegister();
    	return mv;
    }
    
    // 원자재 등록 시 원자재 코드 중복확인
    @ResponseBody
	@PostMapping("materialCodeCheck")
	public int materialCodeCheck(@RequestParam("material_code") String material_code) {
	    int cnt = projectService.materialCodeCheck(material_code);
	    return cnt;
	}
    
    //멤버 리스트로 이동
    
    @GetMapping("member")
    public ModelAndView member(
    		
    		) {
    	mv = projectService.member();
    	return mv;
    }
    
    @GetMapping("/getMemberByMemberId")
    public ResponseEntity<MemberVO> getMemberByMemberID (
    		@RequestParam("member_id") String member_id
    		) {
    	MemberVO member = projectService.getMemberByMemberId(member_id);
    	
    	return ResponseEntity.ok(member);
    }
    
    // 멤버 수정시 아이디 중복 확인
    @ResponseBody
	@PostMapping("memberIdValidation")
	public int memberIdValidation(
			@RequestParam Map<String,Object> map
			) {
	    int cnt = projectService.memberIdValidation(map);
	    return cnt;
	}
    
    @PostMapping("/updateMember")
    public String updateMember(@RequestBody MemberVO memberVO) {
    	int result = projectService.updateMember(memberVO);
    	return "index";
    }
    
    @GetMapping("getRecipeRegister")
    public String getRecipeRegister() {
    	return "recipeRegister";
    }
    
    // 레시피 등록시 제품이름 중복확인
    @ResponseBody
	@PostMapping("/productNameCheck")
	public int productNameCheck(@RequestParam("product_name") String product_name) {
	    int cnt = projectService.productNameCheck(product_name);
	    return cnt;
	}
    
    @ResponseBody
    @GetMapping("/getProductCodeAndNameListConcat")
    public String[] getProductCodeAndNameListConcat(Model model) {
    	String[] productCodeAndNameListConcat = projectService.getProductCodeAndNameListConcat();
    	return productCodeAndNameListConcat;
    }
    
    @ResponseBody
    @PostMapping("/addRecipe")
    public ResponseEntity<?> addRecipe (@RequestBody Map<String,Object> map) {
    	try {
    		int r = projectService.addRecipe(map);
    		return ResponseEntity.ok(Map.of("message", "저장 성공"));
    	} catch (Exception e) {
    		return ResponseEntity.ok(Map.of("message", "저장 성공"));
    	}
    }
    
    @ResponseBody
    @GetMapping("/recipeProductCodeCheck")
    public int recipeProductCodeCheck(@RequestParam("product_code") String product_code) {
    	int cnt = projectService.recipeProductCodeCheck(product_code);
    	return cnt;
    }
    
    @GetMapping("recipe")
    public ModelAndView getRecipe() {
    	mv = new ModelAndView();
    	mv = projectService.getRecipe();
    	return mv;
    }
    
    @GetMapping("/getRecipeDetailByProductcode")
    public ResponseEntity<List<RecipeDetailVO>> getRecipeDetailByProductcode(
    		@RequestParam("product_code") String product_code
    		) {
    	
    	List<RecipeDetailVO> recipeDetailListVO = projectService.getRecipeDetailByProductcode(product_code);
    	
    	return ResponseEntity.ok(recipeDetailListVO);
    	
    }
    
    @GetMapping("/getRecipeByRecipeNum")
    public ResponseEntity<RecipeVO> getRecipeByRecipeNum(
    		@RequestParam("recipe_num") int recipe_num
    		) {
    	
    	RecipeVO recipeVO = projectService.getRecipeByRecipeNum(recipe_num);
    	
    	return ResponseEntity.ok(recipeVO);
    }
    
    @GetMapping("/getRecipeDetailListByRecipeNum")
    public ResponseEntity<List<RecipeDetailVO>> getRecipeDetailListByRecipeNum(
    		@RequestParam("recipe_num") int recipe_num
    		) {
    	List<RecipeDetailVO> RecipeDetailVOList = projectService.getRecipeDetailListByRecipeNum(recipe_num);
    	return ResponseEntity.ok(RecipeDetailVOList);
    }
    
    @PostMapping("/updateRecipe")
    public ResponseEntity<?> updateRecipe(@RequestBody Map<String,Object> map) {
    	
    	try {
    		int r = projectService.updateRecipe(map);
    		System.out.println(map);
    		return ResponseEntity.ok(Map.of("message", "수정 성공"));
    	} catch (Exception e) {
    		return ResponseEntity.ok(Map.of("message", "수정 실패"));
    	}
    }
    
	@ResponseBody
	@PostMapping("/orderformProceed")
	public int orderformProceed(@RequestParam("orderform_num") int orderform_num) throws Exception{
	    List<OrderformDetailVO> orderformDetailList = projectService.getOrderformDetailListByOrderformnum(orderform_num);
	    List<AvgMaterialpriceVO> AvgMaterialpriceList = projectService.getAllAvgMaterialprice();
	    
	    for (OrderformDetailVO orderformDetail : orderformDetailList) {
		
			QcVO qcVO = new QcVO();
			
	    	qcVO.setQc_type("order");
	    	qcVO.setPaper_num(orderform_num);
	    	qcVO.setQc_item_num(orderformDetail.getProduct_num());
	    	qcVO.setQc_quan(orderformDetail.getOrderdetail_amount());
	    	
	    	int result = projectService.insertqc(qcVO);
	    }
	    
	    for (AvgMaterialpriceVO avgMaterialprice : AvgMaterialpriceList) {
	    	int r = projectService.updateMaterialInvenPrice(avgMaterialprice);
	    }
	    
	    int result2 = projectService.updateOrderformFinish(orderform_num);
	    
		return result2;
	}
	
	@GetMapping("getInventoryAppropriateAmountRegister")
	public String getInventoryAppropriateAmountRegister() {
		mv = new ModelAndView();
		
		return "getInventoryAppropriateAmountRegister";
	}
	
    @ResponseBody
    @PostMapping("/getInventoryAppropriateAmount")
    public ResponseEntity<List<InventoryAppropriateAmountVO>> getInventoryAppropriateAmount() throws Exception {
    	List<InventoryAppropriateAmountVO> inventoryAppropriateAmountListVO = projectService.getInventoryAppropriateAmount();
    	return ResponseEntity.ok(inventoryAppropriateAmountListVO);
    }
    
    @ResponseBody
    @PostMapping("/addAppropriateAmount")
    public ResponseEntity<?> addAppropriateAmount (@RequestBody Map<String,Object> map) throws Exception {
		System.out.println(map);
		try {
			int r = projectService.addAppropriateAmount(map);
			return ResponseEntity.ok(Map.of("message", "저장 성공"));
		} catch (Exception e) {
			return ResponseEntity.ok(Map.of("message", "저장 성공"));
		}
	}
    
    @ResponseBody
	@PostMapping("/materialNameCheck")
	public int materialNameCheck(@RequestParam("material_name") String material_name) {
	    int cnt = projectService.materialNameCheck(material_name);
	    return cnt;
	}
    
    
    @ResponseBody
    @PostMapping("/getInventoryProductAppropriateAmount")
    public ResponseEntity<List<InventoryProductAppropriateAmountVO>> getInventoryProductAppropriateAmount() throws Exception {
    	List<InventoryProductAppropriateAmountVO> inventoryProductAppropriateAmountListVO = projectService.getInventoryProductAppropriateAmount();
    	return ResponseEntity.ok(inventoryProductAppropriateAmountListVO);
    }
    
    
    
    
 // -------------------------new 작업공간(이의재) -------------------------    
    
    
    // 박나현 시작. ------------------------------
    
   	// QC 리스트 페이지로 이동
   	@GetMapping("qc")
   	public String qc(Model model) {
   		List<QcVO> QcList = projectService.getQcList();
   		List<QcVO> QcList0 = projectService.getQcList0();
   		List<QcVO> QcList1 = projectService.getQcList1();
   		List<QcVO> QcList2 = projectService.getQcList2();
   		model.addAttribute("QcList", QcList);
   		model.addAttribute("QcList0", QcList0);
   		model.addAttribute("QcList1", QcList1);
   		model.addAttribute("QcList2", QcList2);
   		log.info("qc 이동");
   		return "qc";
   	}

   	// QC 상세 페이지로 이동
   	@GetMapping("qcDetail")
   	public String qcDetail(@RequestParam("qc_num") int qc_num, Model model) {

   		log.info("qcDetail 이동");
   		log.info("qc_num = " + qc_num);

   		QcVO qc = projectService.getOneQc(qc_num);

   		// 타입 저장, 이름 담아줄 변수 생성
   		String qctype = qc.getQc_type(); // 타입을 게또
   		String item_name = null; // name은 qc에 들어가지 않고 직접 뿌려줍니다

   		// 원자재 또는 상품 ==> 이름 찾아옴
   		if ("order".equals(qctype)) {
   			item_name = projectService.getQcMName(qc_num);
   			log.info("order로 넘어왔음, item_name == " + item_name);
   		} else if ("plan".equals(qctype)) {
   			item_name = projectService.getQcPName(qc_num);
   			log.info("plan로 넘어왔음, item_name == " + item_name);
   		} else {
   			log.info("통과했슈");
   		}

   		// 상세 내역 불러오기
   		List<QcVO> QcDetailList = projectService.getOneQcDetail(qc_num);
   		int totalQC = qc.getQc_quan();
   		int totalFail = projectService.getTotalFail(qc_num);
   		int totalPass = totalQC - totalFail;
   		double failRate = (double) totalFail / totalQC * 100;

   		model.addAttribute("qc", qc);
   		model.addAttribute("item_name", item_name);
   		model.addAttribute("qc_num", qc_num);
   		model.addAttribute("QcDetailList", QcDetailList);
   		model.addAttribute("totalQC", totalQC);
   		model.addAttribute("totalFail", totalFail);
   		model.addAttribute("totalPass", totalPass);
   		model.addAttribute("failRate", failRate);

   		return "qcDetail";
   	}

   	// QC 수정 페이지 이동
   	@GetMapping("qcTest")
   	public String qcTest(@RequestParam("qc_num") int qc_num, Model model, HttpSession session) {

   		log.info("qcTest 이동");
   		log.info("qc_num = " + qc_num);

   		// qc_num으로 qc 기본 정보 불러오기
   		QcVO qc = projectService.getOneQc(qc_num);

   		// 타입 저장, 이름 담아줄 변수 생성
   		String qctype = qc.getQc_type();
   		String item_name = null; // name은 qc에 들어가지 않고 직접 출력해줍니다.

   		// 타입 조건문 ==> 이름 찾아옴
   		if ("order".equals(qctype)) {
   			item_name = projectService.getQcMName(qc_num);
   			log.info("order로 넘어왔음, item_name == " + item_name);
   		} else if ("plan".equals(qctype)) {
   			item_name = projectService.getQcPName(qc_num);
   			log.info("plan로 넘어왔음, item_name == " + item_name);
   		} else {
   			log.info("통과했슈");
   		}

   		// qc_num으로 상세 응답 정보 불러오기
   		List<QcVO> QcDetailList = projectService.getOneQcDetail(qc_num);

   		int totalQC = qc.getQc_quan(); // 검사하는 수
   		int totalFail = projectService.getTotalFail(qc_num); // 총 부적격 수
   		int totalPass = totalQC - totalFail; // 총 통과 물품 수
   		double failRate = (double) totalFail / totalQC * 100; // 부적격률 계산

   		model.addAttribute("qc", qc);
   		model.addAttribute("item_name", item_name);
   		model.addAttribute("qc_num", qc_num);
   		model.addAttribute("QcDetailList", QcDetailList);
   		model.addAttribute("totalQC", totalQC);
   		model.addAttribute("totalFail", totalFail);
   		model.addAttribute("totalPass", totalPass);
   		model.addAttribute("failRate", failRate);

   		MemberVO user = (MemberVO) session.getAttribute("user");

   		if (user == null) {
   			return "redirect:/login";
   		}

   		model.addAttribute("member_name", user.getMember_name());

   		return "qcTest";
   	}

   	// QC Test 부적격 수량 저장
   	@ResponseBody
   	@PostMapping("updateQcDetail")
   	public ResponseEntity<String> updateQcDetail(@RequestBody List<QcDetailVO> qcDetails) {

   		try {
   			for (QcDetailVO detail : qcDetails) {

   				int qc_num = detail.getQc_num();
   				String qc_tester = detail.getQc_tester();

   				log.info("QC 문항 번호: " + detail.getQcq_num());
   				log.info("부적격 수량: " + detail.getQc_fail_quan());
   				log.info("검사자: " + detail.getQc_tester());

   				QcVO qc = new QcVO();

   				int isQcDetail = projectService.isQcDetail(detail); // 값 존재하는지 확인

   				if (isQcDetail == 0) {
   					projectService.insertQcDetail(detail);
   				} else if (isQcDetail == 1) {
   					projectService.updateQcDetail(detail);
   				}
   				projectService.updateQcStat1(detail.getQc_num()); // 상태를 '작성중'으로 변경

   				qc.setQc_num(qc_num);
   				qc.setQc_tester(qc_tester);

   				projectService.updateQcTester(qc);
   			}
   			return ResponseEntity.ok("Success");
   		} catch (Exception e) {
   			log.error("Error 발생 : ", e); // 에러 로그
   			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
   		}
   	}

   	// QC Test POST (1. 인벤토리 업데이트 2. qc 상태 업데이트 3. 필요 시 계약서 및 계획서 자동 작성 4. 완료 시 qcList로 이동)
   		@PostMapping("qcTest")
   		public String qcTest(@RequestParam Map<String, String> map, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

   			// 인벤토리 업데이트 & qc 상태 업데이트
   			// 가져온 값 확인
   			System.out.println(map);
   			
   			// 인벤토리에 넣어줄 값 설정
   			String inven_name = map.get("item_name");
   			String qc_type = map.get("qc_type");
   			int inven_item_num = Integer.parseInt(map.get("qc_item_num"));
   			int inven_amount = Integer.parseInt(map.get("totalPass"));

//   			System.out.println("########################## qc_type: " + qc_type);
//   			System.out.println("########################## inven_item_num: " + inven_item_num);
//   			System.out.println("########################## inven_amount: " + inven_amount);
//   			System.out.println("########################## qc_num: " + qc_num);

   			// 값을 넣어줄 객체 생성
   			InventoryVO inven = new InventoryVO();

   			// 객체에 값 set
   			
   			inven.setInven_name(inven_name);
   			// 타입
   				int inven_type = 0;
   				if (qc_type.equals("order")) {
   					inven_type = 0;
   				} else if (qc_type.equals("plan")) {
   					inven_type = 1;
   				}
   			inven.setInven_type(inven_type);
   			inven.setInven_item_num(inven_item_num);
   			inven.setInven_amount(inven_amount);

   			
   			// update 실행 (인벤토리 추가, qc 상태 업데이트)
   			int result1 = projectService.updateInven(inven);		
   				int qc_num = Integer.parseInt(map.get("qc_num"));
   			int result2 = projectService.updateQcStat2(qc_num);
   			
   			
   			// 부적격 재고 발생 시, [원자재 구매 계약서] 및 [제품 생산 계획서] 작성
   			
   			System.out.println("####################################### 인벤토리 업데이트 완료, 부적격 재고 처리 실행");
   			
   			// 필요 값 설정	
   			int paper_num = Integer.parseInt(map.get("paper_num"));
   			int totalFail = Integer.parseInt(map.get("totalFail"));
   			
   			MemberVO user = (MemberVO) session.getAttribute("user");
   					
   			System.out.println("####################################### paper_num 출력 ####### " + paper_num);
   			System.out.println("####################################### 총 부적격 수량 totalFail 출력 ####### " + totalFail);
   			System.out.println("####################################### type 출력 ####### " + inven_type);
   			
   			if (totalFail > 0) {
   				if (inven_type == 0) {
   					
   					OrderformVO of = projectService.getOrderformByPapernum(paper_num);
   					
   					int of_num = projectService.getLastOrderformNum() + 1;
   					String of_name = "[재신청] " + of.getOrderform_name();
   					String of_content = "[재신청] " + of.getOrderform_content();
   					
   					of.setOrderform_num(of_num);
   					of.setOrderform_name(of_name);
   					of.setOrderform_stat("진행중");
   					of.setOrderform_content(of_content);
   					
   					System.out.println("####################################### of 출력 #######" + of.toString());
   					
   					projectService.insertOrderform(of);
   					
   					OrderformDetailVO ofd = new OrderformDetailVO();
   					
   					ofd.setOrderform_num(of_num);
   					ofd.setProduct_num(inven_item_num);
   					
   					int amount = Integer.parseInt(map.get("totalFail"));
   					ofd.setOrderdetail_amount(amount);
   					
   					int price = projectService.getMaterialPrice(inven_item_num);
   					ofd.setOrderdetail_price(price * amount);
   					
   					
   					System.out.println("####################################### ofd 출력 #######" + ofd.toString());
   					
   					projectService.insertOrderformDetail(ofd);
   					
   				}
   				else if (inven_type == 1) {
   					
   					// 객체 생성 + 기존 정보 가져오기
   					ProductionVO pd = projectService.getProductionByPapernum(paper_num);
   					
   					int pd_num = projectService.getfindLastProductionNumber() + 1;
   					String pd_writer = user.getMember_name();
   					String pd_dept = user.getMember_dept();
   					String pd_name = "[재신청] " + pd.getPd_name();
   					String pd_content = "[재신청] " +  pd.getPd_content();		
   					
   					pd.setPd_num(pd_num);
   					pd.setPd_writedate(null);
   					pd.setPd_writer(pd_writer);
   					pd.setPd_dept(pd_dept);
   					pd.setPd_name(pd_name);
   					pd.setPd_content(pd_content);
   					
   					System.out.println("####################################### pd 출력 #######" + pd.toString());
   					// 위 코드를 출력하고 싶으면 DTO에서 @ToString 추가해주면 나옴 
   					projectService.insertProduction(pd);
   					
   					// detail은 새로운 객체 생성
   					ProductionDetailVO pdd = new ProductionDetailVO();
   					int amount = Integer.parseInt(map.get("totalFail"));
   					
   					pdd.setPd_num(pd_num);
   					pdd.setProduct_name(inven_name);
   					pdd.setProductiondetail_amount(amount);
   					
   					System.out.println("####################################### pdd 출력 #######" + pdd.toString());
   					// 위 코드를 출력하고 싶으면 DTO에서 @ToString 추가해주면 나옴 
   					projectService.insertProductionDetail(pdd);
   					
   					
   				}
   			};
   			
   			
   			// QcList로 이동
   			List<QcVO> QcList = projectService.getQcList();
   			redirectAttributes.addFlashAttribute("QcList", QcList);
   			redirectAttributes.addFlashAttribute("msg", "품질 검사서 제출 완료!");
   			log.info("qc 이동");

   			return "redirect:qc";
   		}

   	// QC 유형 등록 페이지 이동
   	@GetMapping("qcTypeReg")
   	public String qcTypeReg() {
   		log.info("qcTypeReg 이동");
   		return "qcTypeReg";
   	}

   	@GetMapping("ssong")
   	public String testssong() {
   		return "ssong";
   	}
       
   	// index QC Top 5 (7일간)

   	
   	
   	
   	
    //================ 김민성 ============================================================================
    
 // 생산계획서 폼 upload
 	@PostMapping("postProductionForm")
 	public String postProductionForm(@RequestParam Map<String, Object> formData) {
 		
 		
 		log.info("formData", formData);
 		ProductionVO productionVO = new ProductionVO();
 		productionVO.setPd_writer((String) formData.get("pd_writer"));
 		productionVO.setPd_dept((String) formData.get("pd_dept"));
 		productionVO.setPd_startdate((String) formData.get("pd_startdate"));
 		productionVO.setPd_enddate((String) formData.get("pd_enddate"));
 		productionVO.setPd_name((String) formData.get("pd_name"));
 		productionVO.setPd_content((String) formData.get("pd_content"));

 		projectService.insertProduction(productionVO);

 		int num = (int) projectService.getfindLastProductionNumber();
 		// num 은 pd_num 생성되자마자 넣음
 		
 		// maxCount 추출
 		int maxCount = Integer.parseInt((String) formData.get("maxCount"));
 		// num = pd_num
 		System.out.println(maxCount);

 		List<ProductionDetailVO> list = new ArrayList<>();

 		for (int i = 1; i <= maxCount; i++) {
 			String itemNameKey = "item_name" + i;
 			String quantityKey = "quantity" + i;
 			log.info(itemNameKey);
 			log.info(quantityKey);

 			if (formData.containsKey(itemNameKey) && formData.containsKey(quantityKey)) {
 				String itemName = (String) formData.get(itemNameKey);
 				int quantity = Integer.parseInt((String) formData.get(quantityKey));

 				ProductionDetailVO planVO = new ProductionDetailVO();
 				planVO.setPd_num(num);
 				planVO.setProduct_name(itemName);
 				planVO.setProductiondetail_amount(quantity);

 				list.add(planVO);
 				
 			}
 		}

 		list.forEach(planVO -> log.info("list -> Pd num: {}, Item Name: {},Quantity: {}", planVO.getPd_num(),
 				planVO.getProduct_name(), planVO.getProductiondetail_amount()));
 		
 		int r = projectService.setproductionForm(list);
 		
 		return "redirect:productionPlan";
 	}

 	
 	
 	
 	// 구매계약서 목록 화면 이동
 	@GetMapping("purchaseContract")
 	public String purchaseContract(Model model) {

 		List<OrderformVO> list = projectService.orderList();
 		model.addAttribute("orderList", list);
 		log.info("list", list);
 		return "purchaseContract";

 	}

 	// 판매계약서 목록 화면 이동
 	@GetMapping("salesContract")
 	public String salesContractList(Model model) {

 		List<QuotationVO> list = projectService.quotationList();
 		model.addAttribute("quotationList", list);
 		log.info("list", list);
 		return "salesContract";
 	}

 	// 모든 계약서들의 목록을 볼 수 있는 화면 이동
 	@GetMapping("allForm")
 	public String allFormList(Model model) {
 		List<QuotationVO> list = projectService.allFormList();
 		model.addAttribute("AllFormList", list);
 		log.info("allFormList", list);
 		return "allForm";
 	}


 	//0204 김민성
 	// 생산계획서 폼 이동
 	@GetMapping("productionForm")
 	public String productionForm(HttpSession session,Model model) {
 		//추가된 부분 
 		MemberVO member = (MemberVO)session.getAttribute("user");
 		if(member==null) {
 			return "login";
 		}
 		if("생산".equals(member.getMember_dept())) {
 			List<ProductVO> products = projectService.getProductList();
 			model.addAttribute("product",products);
 			model.addAttribute("member",member);
 			return "productionForm";
 		}
 		else{
 			
 		}
 		List<ProductVO> products = projectService.getProductList();
 		model.addAttribute("product",products);
 		model.addAttribute("member",member);
 		log.info("productionForm()");
 		return "productionForm";
 	}

 	// facotry.html 다시 옮길것
 	//0204김민성
 	@GetMapping("factoryPlan")
 	public String factoryPlan(Model model,HttpSession session) {
 		List<ProductionVO> list = projectService.getFatoryWorkList();
 		MemberVO member = (MemberVO)session.getAttribute("user");
 		model.addAttribute("getFatoryWorkList", list);
 		/* model.addAttribute(member); */
 		log.info("factoryPlan", list);
 		return "factoryPlan";
 		
 	}
 	//0204 김민성
 	@GetMapping("feedBackList")
 	public String feedBackList(Model model,HttpSession session) {
 		List<ProductionVO> list = projectService.getFatoryWorkList1();
 		MemberVO member = (MemberVO)session.getAttribute("user");
 		model.addAttribute("getFatoryWorkList1", list);
 		model.addAttribute("member",member);
 		log.info("feedBackList", list);
 		return "feedBackList";
 	}
 	
 	//0204 김민성
 		@GetMapping("productionDeletePage")
 		public String productionDeletePage(Model model,HttpSession session) {
 			List<ProductionVO> list = projectService.getFatoryWorkList2();
 			MemberVO member = (MemberVO)session.getAttribute("user");
 			
 			model.addAttribute("getFatoryWorkList2", list);
 			model.addAttribute("member",member);
 			
 			log.info("productionDeletePage", list);
 			return "productionDeletePage";
 		}
 	
 	//0204김민성
 	@GetMapping("getFactoryDetail")
 	public ModelAndView getFactoryDetail(@RequestParam("pd_num") int pd_num,HttpSession session) {
 		ModelAndView mv = new ModelAndView();
 		//추가된 부분 
 		
 		MemberVO member = (MemberVO)session.getAttribute("user");
 		

 	
 			
 			
 			ProductionVO productionVO = new ProductionVO();
 			productionVO = projectService.getFactoryDetail(pd_num);
 			List<ProductionVO> productionListVO = projectService.getFactoryDetailList(pd_num);
 			
 			mv = new ModelAndView();
 			mv.addObject("productionVO", productionVO);
 			mv.addObject("productionList", productionListVO);
 			mv.addObject("pd_num",pd_num);
 			mv.setViewName("factoryDetail");
 			mv.addObject("member",member);
 			return mv;
 		
 		
 	}

 	
 	
 	// 생산계획서 목록 화면 이동
 	@GetMapping("productionPlan")
 	public String getproductionPlanList(Model model) {
 		//추가된 부분 
 		
 		List<ProductionVO> list = projectService.getProductionList();
 		model.addAttribute("getProductionPlanList", list);
 		log.info("getProductionPlanList", list);
 		return "productionPlan";
 		
 	
 	}
 	//0204 김민성
 	@ResponseBody
 	@PostMapping("postFactoryDetail")
 	public int postFactoryDetail(@RequestParam("pd_num")int pd_num,HttpSession session)throws Exception  {
 		log.info("pd_num: {}", pd_num);
 		MemberVO member = (MemberVO)session.getAttribute("user");//session
 		
 		QcVO qc = new QcVO();
 		ProductVO productVO = new ProductVO();
 		ProductionVO productionVO = new ProductionVO(); 
 		
 		
 		List<InventoryVO> FinalInven = new ArrayList<>();
 		InventoryVO inven1 = new InventoryVO();
 		List<ProductionDetailVO> ProductionList = projectService.getProductionListByFactoryDetail(pd_num);
 		int ProductionListSize = ProductionList.size();
 		
 		System.out.println("ProductionListSize"+ProductionListSize);
 		for(int i = 0; i< ProductionListSize;i++) {
 			String product_name = ProductionList.get(i).getProduct_name(); 
 			int productiondetail_amount = ProductionList.get(i).getProductiondetail_amount();
 			int recipe_num = projectService.getFindRecipeNum(product_name);
 			
 			List<InventoryVO> RecipeTotalList = new ArrayList<>();
 			System.out.println("recipe_num"+recipe_num);
 			List<RecipeDetailVO> RecipeList = projectService.getRecipeDetailListByRecipeNum(recipe_num);
 			int RecipeListSize = RecipeList.size();
 			log.info("RecipeList size for product {}: {}", product_name, RecipeListSize);
 			
 			
 			for(int l =0; l < RecipeListSize;l++) {
 				int Material_amount = RecipeList.get(l).getMaterial_amount();
 				String Material_name = RecipeList.get(l).getMaterial_name();
 				
 				int TotalAmount = Material_amount * productiondetail_amount;
 				InventoryVO inven = new InventoryVO();
 				
 				inven.setInven_amount(TotalAmount);
 				inven.setInven_name(Material_name);
 				RecipeTotalList.add(inven);
 			}	
 				List<InventoryVO> list = projectService.getFindInvenList();
 				log.info(product_name);
 				int listSize = list.size();
 				for(int k =0; k < listSize; k++) {
 					int Inven_amount =list.get(k).getInven_amount();
 					String Inven_name = list.get(k).getInven_name();
 					inven1.setInven_amount(Inven_amount);
 						
 						for(int j = 0; j<RecipeListSize; j++ ) {
 							String Mname = RecipeTotalList.get(j).getInven_name();
 							int totalamount = RecipeTotalList.get(j).getInven_amount();
 							System.out.println("3");
 							if(Mname.equals(Inven_name)) {
 								
 								int Total = Inven_amount - totalamount;
 								
 								System.out.println("4");
 								if( Total < 0) {
 									return 1; //재고 부족
 								}else {
 									
 									inven1.setInven_amount(Total);
 									inven1.setInven_name(Inven_name);
 									System.out.println("5");
 									FinalInven.add(inven1);
 								}
 						}
 				}
 			}// for 끝나는 부분 원자재 수 기준
 		}//for 끝나는부분 제품 수 기준
 		List<InventoryVO> exam = projectService.getFindInvenList();
 		for (InventoryVO lists : exam) { 
 		    for (InventoryVO finalInven : FinalInven) { 
 		        // 두 배열의 inven_name이 같은 경우 처리
 		        if (finalInven.getInven_name().equals(lists.getInven_name())) { 
 		        	
 		        	
 		            // 최신화: exam의 inven_amount에서 FinalInven의 inven_amount를 빼기
 		            int updatedAmount = lists.getInven_amount() - finalInven.getInven_amount(); 
 		            // 음수 방지
 		            if (updatedAmount < 0) {
 		                System.out.println("Error: Insufficient inventory for " + finalInven.getInven_name());
 		                return 1;
 		            } 
 		            // exam 리스트의 inven_amount를 최신화
 		            lists.setInven_amount(updatedAmount);

 		            // 최종 결과 확인 (디버깅용 로그)
 		            System.out.println("Updated " + lists.getInven_name() + " Amount: " + updatedAmount);
 		        }
 		    }
 		} 
 		for (InventoryVO lists : exam) { 
 			int r = projectService.reduceInventoryAmount(lists);
 			if (r != 1) {
 		        System.out.println("실패 " );
 		        return 1; // 실패 시 반환
 		        
 		    }
 		}
 		//qc set 을 해주기 위한 리스트
 		List<ProductionDetailVO> ProductbyQcSet = projectService.getProductionListByFactoryDetail(pd_num);
 		int ProductbyQcSetSize = ProductbyQcSet.size();
 		for(int c =0; c < ProductbyQcSetSize;c++) {
 			String product_name = ProductbyQcSet.get(c).getProduct_name(); 
 			int product_amount = ProductbyQcSet.get(c).getProductiondetail_amount();
 			
 			  productVO.setProduct_name(product_name);
 			  projectService.getfindProductNum(productVO);
 			
 			  ProductVO product_num = projectService.getfindProductNum(productVO);
 			  qc.setQc_item_num(product_num.getProduct_num()); 
 			  qc.setQc_quan(product_amount);
 			  qc.setQc_type("plan"); 
 			  qc.setPaper_num(pd_num);
 			  int result2 = projectService.insertqc(qc);
 		}
 		
 		productionVO.setPd_num(pd_num);
 		
 		 
 		int pd_check = projectService.setPdCheckUpdate(productionVO);
 		System.out.println("생산 성공");
 		return 2;
 	}
 	
 	//0204 김민성
 	@ResponseBody
 	@PostMapping("postFeedback")
 	public int postFeedback(@RequestParam("pd_num")int pd_num)throws Exception  {
 	ProductionVO productionVO = new ProductionVO();
 	log.info("pd_num {}",pd_num);
 	productionVO.setPd_num(pd_num);
 	
 	int pd_check = projectService.setPdCheckUpdate2(productionVO);
 	
 	return 1;	
 	}
 	
 	//0204 김민성
 	@ResponseBody
 	@PostMapping("postDeleteProduction")
 	public int postDeleteProduction(@RequestParam("pd_num")int pd_num)throws Exception  {
 	ProductionVO productionVO = new ProductionVO();
 	log.info("pd_num {}",pd_num);
 	
 	productionVO.setPd_num(pd_num);
 		
 	int pd_check = projectService.setPdCheckUpdate3(productionVO);
 	//pd_check == 99 가 delete 예정
 	return 1;	
 	}
 	
 	//0204 김민성
 	@ResponseBody
 	@PostMapping("postDeleteProductionBack")
 	public int postDeleteProductionBack(@RequestParam("pd_num")int pd_num)throws Exception  {
 	ProductionVO productionVO = new ProductionVO();
 	log.info("pd_num {}",pd_num);
 		
 	productionVO.setPd_num(pd_num);
 			
 	int pd_check = projectService.setPdCheckUpdate4(productionVO);
 		//pd_check == 0 으로 되돌림
 	return 1;	
 	}
 	
 	//0204 김민성
 	@ResponseBody
 	@PostMapping("postRealDelete")
 	public int postRealDelete(@RequestParam("pd_num")int pd_num)throws Exception  {
 	ProductionVO productionVO = new ProductionVO();
 	ProductionVO productiondetailVO = new ProductionVO();
 	
 	log.info("pd_num {}",pd_num);
 			
 	productionVO.setPd_num(pd_num);
 	productiondetailVO.setPd_num(pd_num);
 	
 	int pd_check2 = projectService.setDeleteProduction2(productiondetailVO); 
 	
 	int pd_check = projectService.setDeleteProduction(productionVO); 
 	
 			//pd_check == 0 으로 되돌림
 	return 1;	
 	}
 	
 	//0204 김민성
 	@ResponseBody
 	@GetMapping("getProductList")
 	public String[] getProductList(Model model) {
 		log.info("controller access");
 		List<ProductVO> ProductList = projectService.getProductList();

 		String[] ProductNameList = new String[ProductList.size()];
 		int cnt = 0;
 		for (ProductVO product : ProductList) {
 			ProductNameList[cnt] = product.getProduct_name();
 			cnt++;
 		}
 		model.addAttribute("ProductNameList", ProductNameList);

 		return ProductNameList;
 	}
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	//0203 김민성
 	 @GetMapping("/getInventoryByProductName")
 	    public ResponseEntity<List<RecipeVO>> getInventoryByProductName(@RequestParam("product_name") String product_name,
 	    														  @RequestParam("amount") String amount) {
 		
 		List<RecipeDetailVO> list = projectService.getTotalAmount(product_name);
 		
 		int listSize = list.size();
 		
 		List<RecipeVO> recipes = new ArrayList<>();
 		
 		for (int k = 1; k <= listSize; k++) {
 			int Mamount = list.get(k - 1).getMaterial_amount();
 			String Mname = list.get(k - 1).getMaterial_name();
 			String inven_name = Mname;
 			System.out.println(inven_name);
 			InventoryVO inventoryVO = projectService.getInvenAmount(inven_name);
 			
 			System.out.println(inventoryVO);
 			System.out.println(inventoryVO.getInven_amount());
 			
 			
 			
 			int invenAmount = inventoryVO.getInven_amount();
 			System.out.println(invenAmount);
 			int totalMamount = (Mamount * Integer.parseInt(amount));
 			System.out.println(totalMamount);
 			int Amount = invenAmount - totalMamount;
 			System.out.println(Amount);
 			
 			RecipeVO recipe = new RecipeVO();
 	        recipe.setMaterial_amount(Amount);
 	        recipe.setMaterial_name(Mname);
 	        recipes.add(recipe); // 결과를 리스트에 추가
 			}
 		return ResponseEntity.ok(recipes);
 	       
 	    }
    
    
    
    
    
}

package com.example.demo.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.example.demo.dto.CompanyVO;
import com.example.demo.dto.FileVO;
import com.example.demo.dto.OrderformVO;
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.QcVO;
import com.example.demo.dto.QuotationVO;
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
	    return "index";
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
	
	// company(협력사) 등록 화면 이동
	@GetMapping("companyRegister")
	public String companyRegister(){
	    log.info("companyRegister");
	    
	    return "companyRegister";
	}
	
	// company 등록
	@PostMapping("addCompany")
	public String addCompany(@RequestBody CompanyVO companyVO){
	    String name = companyVO.getCompany_name();
	    log.info("Company's name is : "+ name);
	    int r = projectService.addCompany(companyVO);
	    return "index";
	}
	
	// 제품 등록 화면 이동
	@GetMapping("productRegister")
	public String productRegister(){
	    log.info("productRegister");
	    
	    return "productRegister";
	}
	
	// 제품 등록
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductVO productVO) {
        try {
            // 제품 정보 저장 로직 (예: DB 저장)
            // productService.save(request);
            int r = projectService.addProduct(productVO);
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
	
	
	
	
	// 생산계획서 화면 이동
    @GetMapping("productionPlan")
    public String productionPlan() {
 	   log.info("productionPlan()");
 	   return "productionPlan";
    }
    
    // 구매계약서 목록 화면 이동
    @GetMapping("purchaseContract")
    public String purchaseContract(Model model) {
 	   
    	List<OrderformVO> list = projectService.orderList();
        model.addAttribute("orderList", list);
        log.info("list",list);
        return "purchaseContract";
        
    }
    
    // 판매계약서 목록 화면 이동
    @GetMapping("salesContract")
    public String salesContractList(Model model) {
 	   
 	   List<QuotationVO> list = projectService.quotationList();
        model.addAttribute("quotationList", list);
        log.info("list",list);
 	   return "salesContract";
    }
    
    // 모든 계약서들의 목록을 볼 수 있는 화면 이동
    @GetMapping("allForm")
    public String allFormList(Model model) {
    	List<QuotationVO> list = projectService.allFormList();
    	model.addAttribute("AllFormList", list);
    	log.info("allFormList",list);
        return "allForm";
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
    public ModelAndView postQuotationRegister(
 		   @RequestParam Map<String,Object> map
 		   ) throws Exception {
 	   mv = projectService.postQuotationRegister(map);
 	   return mv;
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
    
    // 제품 리스트 화면 이동
    @GetMapping("product")
    public String product(Model model) {
  	   log.info("product");
  	   
  	   List<ProductVO> productList = projectService.productList();
  	   
  	   for (ProductVO product : productList) {
  		   int product_num = product.getProduct_num();
  		   
  		   int amount = projectService.fileAmount(product_num);
  		   
  		   if (amount == 0) {
  			   product.setFile_name("No Image");
  			   product.setFile_path("vendors/images/product-img1.jpg");
  		   } else {
  			   FileVO file = projectService.findFirstImage(product_num);
  			   product.setFile_amount(amount);
  			   product.setFile_name(file.getFile_name());
  			   product.setFile_path(file.getFile_path());
  		   }
  		   
  	   }
  	   
  	   
  	   model.addAttribute("productList",productList);
  	   
  	   return "product";
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
    	log.info("controller access");
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
    
    
    

// -------------------------new 작업공간 -------------------------
    
    @GetMapping("getQuotationDetail")
    public ModelAndView getQuotationDetail(
    		@RequestParam("quot_num") int quot_num
    		) throws Exception {
    	mv = projectService.getQuotationDetail(quot_num);
        return mv;
    }
    
    @GetMapping("getOrderformDetail")
    public ModelAndView getOrderformDetail(
    		@RequestParam("orderform_num") int orderform_num
    		) {
    	mv = projectService.getOrderformDetail(orderform_num);
    	return mv;
    }
    
    @GetMapping("getAllFormDetail")
    public ModelAndView getAllFormDetail(
    		@RequestParam("this_num") String this_num
    		) {
    	if (this_num.contains("quot")) {
    		int quot_num = Integer.parseInt(this_num.replaceAll("quot", ""));
    		mv = projectService.getQuotationDetail(quot_num);
    		return mv;
    	} else {
    		int orderform_num = Integer.parseInt(this_num.replaceAll("order", ""));
    		mv = projectService.getOrderformDetail(orderform_num);
    		return mv;
    	}
    	
    	
    }
    
 // 박나현 시작. ------------------------------
    
    // QC 리스트 페이지로 이동
    @GetMapping("qc")
    public String qc(Model model) {
    	List<QcVO> QcList = projectService.getQcList();
    	model.addAttribute("QcList", QcList);
    	log.info("qc 이동");
        return "qc";
    }

    // QC 상세 페이지로 이동
    @GetMapping("qcDetail")
    public String qcDetail(@RequestParam("qc_num") int qc_num, Model model) {
  
    	log.info("qcDetail 이동");
    	log.info("qc_num = "+qc_num);

    	QcVO qc = projectService.getOneQc(qc_num); 	   
    	List<QcVO> QcDetailList = projectService.getOneQcDetail(qc_num);
    	int totalQC = qc.getQc_quan();
    	int totalFail = projectService.getTotalFail(qc_num);
    	int totalPass = totalQC - totalFail;
    	double failRate = (double)totalFail/totalQC*100;
    	
    	model.addAttribute("qc", qc);
    	model.addAttribute("qc_num", qc_num);
    	model.addAttribute("QcDetailList", QcDetailList);
    	model.addAttribute("totalQC", totalQC);
    	model.addAttribute("totalFail", totalFail);
    	model.addAttribute("totalPass", totalPass);
    	model.addAttribute("failRate", failRate);
    	
        return "qcDetail";
    }


    // QC 유형 등록 페이지 이동
    @GetMapping("qcTest")
    public String qcTest(@RequestParam("qc_num") int qc_num, Model model) {
    	log.info("qcTest 이동");
    	log.info("qc_num = "+qc_num);

    	QcVO qc = projectService.getOneQc(qc_num); 	   
    	List<QcVO> QcDetailList = projectService.getOneQcDetail(qc_num);
    	int totalQC = qc.getQc_quan();
    	int totalFail = projectService.getTotalFail(qc_num);
    	int totalPass = totalQC - totalFail;
    	double failRate = (double)totalFail/totalQC*100;
    	
    	model.addAttribute("qc", qc);
    	model.addAttribute("qc_num", qc_num);
    	model.addAttribute("QcDetailList", QcDetailList);
    	model.addAttribute("totalQC", totalQC);
    	model.addAttribute("totalFail", totalFail);
    	model.addAttribute("totalPass", totalPass);
    	model.addAttribute("failRate", failRate);
        return "qcTest";
    }
    
    // QC 유형 등록 페이지 이동
    @GetMapping("qcTypeReg")
    public String qcTypeReg() {
    	log.info("qcTypeReg 이동");
        return "qcTypeReg";
    }
    


// 박나현. 끝. ------------------------------
    
}

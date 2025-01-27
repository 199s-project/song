package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.ProjectDAO;
import com.example.demo.dto.CompanyVO;
import com.example.demo.dto.FileVO;
import com.example.demo.dto.InventoryVO;
import com.example.demo.dto.MaterialVO;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.OrderformDetailVO;
import com.example.demo.dto.OrderformVO;
import com.example.demo.dto.PaymentMaterialVO;
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.ProductionDetailVO;
import com.example.demo.dto.ProductionVO;
import com.example.demo.dto.QcDetailVO;
import com.example.demo.dto.QcVO;
import com.example.demo.dto.QuotationDetailVO;
import com.example.demo.dto.QuotationVO;
import com.example.demo.dto.RecipeDetailVO;
import com.example.demo.dto.RecipeVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectService {
	
	private ModelAndView mv;

	@Autowired
	private ProjectDAO projectDAO;
	

	public ModelAndView getLogin() {
	       mv = new ModelAndView();
	       mv.setViewName("login");
	       return mv;
	   }
	
	// 로그인
    public ModelAndView postLogin(@RequestParam Map<String,Object> map,HttpSession session) {
    	mv = new ModelAndView();
    	MemberVO member = projectDAO.getMember(map);
    	
    	if (member == null) {
    		mv.addObject("msg", "로그인에 실패하였습니다.");
    		mv.setViewName("login");
    		return mv;
    	}
    	 
    	session.setAttribute("user", member); 
        mv.setViewName("redirect:/");
    	
    	return mv;
    }
    
    
    public ModelAndView getLogout(HttpSession session) {
    	mv = new ModelAndView();
    	session.invalidate();
    	mv.setViewName("login");
    	return mv;
    }
	
    // 회원가입
    public ModelAndView postRegister(Map<String,Object> map) {
    	mv = new ModelAndView();
    	int r = projectDAO.postRegister(map);
    	
    	if (r == 1) {
    		mv.addObject("msg", "회원가입 성공");
    		mv.setViewName("index");
    		return mv;
    	} 
    	
    	mv.addObject("msg", "회원가입 실패. 다시 진행해주세요.");
    	mv.setViewName("register");
    	return mv;
    }
    
    public boolean isMember_idTaken(String member_id) {
    	return projectDAO.checkMember_id(member_id) > 0;
    }
    
	public int addCompany(CompanyVO companyVO) {
		return projectDAO.addCompany(companyVO);
	}
	public int addProduct(ProductVO productVO) {
		return projectDAO.addProduct(productVO);
	}
	public int findMaxProductNum() {
		return projectDAO.findMaxProductNum();
	}
	public int fileUpload(FileVO fileVO) {
		return projectDAO.fileUpload(fileVO);
	}
	public int productCodeCheck(String product_code) {
		return projectDAO.productCodeCheck(product_code);
	}
	public String companyNameCheck(String company_name) {
		return projectDAO.companyNameCheck(company_name);
	}
	
	
	public List<OrderformVO> orderList(){
		log.info("orderList()");
		return projectDAO.orderList();
	}
	public List<QuotationVO> quotationList(){
		log.info("quotationList()");
		return projectDAO.quotationList();
	}
	public List<QuotationVO> allFormList(){
		log.info("allFormList()");
		return projectDAO.allFormList();
	}
	
	
	// 구매계약서 등록 화면 이동
	public ModelAndView getOrderformRegister(HttpSession session) throws Exception {
    	mv = new ModelAndView();
    	MemberVO user = (MemberVO)session.getAttribute("user");
    	List<CompanyVO> companies = projectDAO.getCompanyList();
    	List<MaterialVO> products = projectDAO.getMaterialList();
    	String productsJson = new ObjectMapper().writeValueAsString(products);
    	mv.addObject("products", products);
    	mv.addObject("user", user);
    	mv.addObject("productsJson", productsJson);
    	mv.addObject("companies", companies);
    	mv.setViewName("orderformRegister");
    	
    	return mv;	
    }
    
    // 구매계약서 등록
	public ModelAndView postOrderformRegister(Map<String,Object> map) throws Exception {
    	mv = new ModelAndView();
    	CompanyVO company1 = projectDAO.getCompanyByCompanyName((String)map.get("company1"));
    	CompanyVO company2 = projectDAO.getCompanyByCompanyName((String)map.get("company2"));
    	
    	OrderformVO orderformVO = new OrderformVO();
    	orderformVO.setOrderform_name((String)map.get("orderform_name"));
    	orderformVO.setOrderform_stat((String)map.get("orderform_stat"));
    	orderformVO.setCompany_num(company1.getCompany_num());
    	orderformVO.setCompany_num2(company2.getCompany_num());
    	orderformVO.setOrderform_writer((String)map.get("orderform_writer"));
    	orderformVO.setOrderform_content((String)map.get("content"));
    	orderformVO.setOrderform_startDate((String)map.get("start_date"));
    	orderformVO.setOrderform_endDate((String)map.get("end_date"));
    	
    	int r = projectDAO.insertOrderform(orderformVO);
    	
    	int OrderformLastNum = projectDAO.getLastOrderformNum();

    	OrderformVO updatedOrderform = projectDAO.getOrderformByOrderformnum(OrderformLastNum);
    	
    	String code1 = updatedOrderform.getOrderform_regdate().substring(0,10).replaceAll("-", "");
    	String code2 = String.format("%04d", OrderformLastNum % 1000);
    	String code3 = String.format("%04d", updatedOrderform.getCompany_num2() % 1000);
    	String code = code1 + code2 + code3;
    	
    	int s = projectDAO.insertOrderformCode(OrderformLastNum, code);
    	
    	Map<String,Object> itemData = map.entrySet()
    			.stream()
    			.filter(entry -> entry.getKey().contains("item"))
    			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    	
    	Optional<Integer> itemMaxNumber = itemData.keySet()
    			.stream()
    			.filter(key -> key.startsWith("item"))
    			.map(key -> Integer.parseInt(key.replace("item", "")))
    			.max(Integer::compareTo);
    	
    	int itemMaxNumberint = itemMaxNumber.orElse(0);
    	
    	OrderformDetailVO orderformDetailVO = new OrderformDetailVO();
    	PaymentMaterialVO inventoryMaterialVO = new PaymentMaterialVO();
    	QcVO qcVO = new QcVO();
    	
    	for(int i=1; i <= itemMaxNumberint; i++) {
    		String itemkey = "item" + i;
    		String itemvalue = (String)map.get(itemkey);
    		MaterialVO materialVO = projectDAO.getMaterialByMaterialName(itemvalue);
    		int material_num = materialVO.getMaterial_num();
    		
    		String amountkey = "quantity" + i;
    		String amountvalue = (String)map.get(amountkey);
    		int amountvalueInt = Integer.parseInt(amountvalue);
    		
    		String pricekey = "total_price" + i;
    		String pricevalue = (String)map.get(pricekey);
    		int pricevalueInt = Integer.parseInt(pricevalue);
    		orderformDetailVO.setOrderform_num(OrderformLastNum);
	    	orderformDetailVO.setProduct_num(material_num);
	    	orderformDetailVO.setOrderdetail_amount(amountvalueInt);
	    	orderformDetailVO.setOrderdetail_price(pricevalueInt);
	    	
	    	int result = projectDAO.insertOrderformDetail(orderformDetailVO);
	    	
	    	qcVO.setQc_type("order");
	    	qcVO.setPaper_num(OrderformLastNum);
	    	qcVO.setQc_writer("test");
	    	qcVO.setQc_item_num(materialVO.getMaterial_num());
	    	qcVO.setQc_quan(Integer.parseInt(amountvalue));
	    	
	    	int result2 = projectDAO.insertqc(qcVO);
	    	
    	}
    	mv.addObject("msg", "계약서 등록 완료");
    	mv.setViewName("purchaseContract");
    	return mv;
    }
    
    
    // 판매계약서 등록 화면 이동
    public ModelAndView getQuotationRegister() throws Exception {
    	
    	mv = new ModelAndView();
    	List<CompanyVO> companies = projectDAO.getCompanyList();
    	List<ProductVO> products = projectDAO.getProductList();
    	String productsJson = new ObjectMapper().writeValueAsString(products);
    	mv.addObject("products", products);
    	mv.addObject("productsJson", productsJson);
    	mv.addObject("companies", companies);
    	mv.setViewName("quotationRegister");
    	return mv;
    }
    
	// 판매계약서 등록
//	public ModelAndView postQuotationRegister(Map<String,Object> map) throws Exception {
//    	mv = new ModelAndView();
//    	System.out.println(map);
//    	CompanyVO company1 = projectDAO.getCompanyByCompanyName((String)map.get("company1"));
//    	CompanyVO company2 = projectDAO.getCompanyByCompanyName((String)map.get("company2"));
//    	
//    	QuotationVO quotationVO = new QuotationVO();
//    	quotationVO.setQuot_name((String)map.get("quot_name"));
//    	quotationVO.setQuot_stat((String)map.get("quot_stat"));
//    	quotationVO.setCompany_num(company1.getCompany_num());
//    	quotationVO.setCompany_num2(company2.getCompany_num());
//    	quotationVO.setQuot_content((String)map.get("content"));
//    	quotationVO.setQuot_startdate((String)map.get("start_date"));
//    	quotationVO.setQuot_enddate((String)map.get("end_date"));
//    	
//    	int r = projectDAO.insertQuotation(quotationVO);
//    	
//    	int QuotationLastNum = projectDAO.getLastQuotationNum();
//    	
//    	Map<String,Object> itemData = map.entrySet()
//    			.stream()
//    			.filter(entry -> entry.getKey().contains("item"))
//    			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    	
//    	Optional<Integer> itemMaxNumber = itemData.keySet()
//    			.stream()
//    			.filter(key -> key.startsWith("item"))
//    			.map(key -> Integer.parseInt(key.replace("item", "")))
//    			.max(Integer::compareTo);
//    	
//    	int itemMaxNumberint = itemMaxNumber.orElse(0);
//    	
//    	QuotationDetailVO quotationDetailVO = new QuotationDetailVO();
//    	QcVO qcVO = new QcVO();
//    	
//    	for (int i=1; i<= itemMaxNumberint; i++) {
//    		String itemkey = "item" + i;
//    		String itemvalue = (String)map.get(itemkey);
//    		ProductVO productVO = projectDAO.getProductByProductName(itemvalue);
//    		int product_num = productVO.getProduct_num();
//    		
//    		String amountkey = "quantity" + i;
//    		String amountvalue = (String)map.get(amountkey);
//    		int amountvalueInt = Integer.parseInt(amountvalue);
//    		
//    		String pricekey = "total_price" + i;
//    		String pricevalue = (String)map.get(pricekey);
//    		int pricevalueInt = Integer.parseInt(pricevalue);
//    		quotationDetailVO.setQuot_num(QuotationLastNum);
//    		quotationDetailVO.setProduct_num(product_num);
//    		quotationDetailVO.setQuotdetail_amount(amountvalueInt);
//    		quotationDetailVO.setQuotdetail_price(pricevalueInt);
//    		
//    		
//    		int result = projectDAO.insertQuotationDetail(quotationDetailVO);
//    	}
//    	
//    	mv.setViewName("quotationDetail");
//    	return mv;
//    }
        
	
	public int insertQuotation(QuotationVO quotationVO) {
		return projectDAO.insertQuotation(quotationVO);
	}
	
	public int getLastQuotationNum() {
		return projectDAO.getLastQuotationNum();
	}
	
	public int insertQuotationDetail(QuotationDetailVO quotationDetailVO) {
		return projectDAO.insertQuotationDetail(quotationDetailVO);
	}
	
	
	
	
	
        
	public CompanyVO getCompanyByCompanyName(String company_name) {
		return projectDAO.getCompanyByCompanyName(company_name);
	}
  
	public ProductVO getProductByProductName(String product_name) {
		return projectDAO.getProductByProductName(product_name);
	}

	// 나현. 시작.
		// QC

		// QC 전체 리스트
		public List<QcVO> getQcList() {
			return projectDAO.getQcList();
		}
		
		// QC 리스트 : 검사 전 0
		public List<QcVO> getQcList0() {
			return projectDAO.getQcList0();
		}
		
		// QC 리스트 : 작성 중 1
		public List<QcVO> getQcList1() {
			return projectDAO.getQcList1();
		}
		
		// QC 리스트 : 검사 완료 2
		public List<QcVO> getQcList2() {
			return projectDAO.getQcList2();
		}
		
		// QC 1건 정보
		public QcVO getOneQc(int qc_num) {
			log.info("getOneQc - Service");
			return projectDAO.getOneQc(qc_num);
		}
		
		// QC 원자재 이름
		public String getQcMName(int qc_num) {
			log.info("getQcMName - Service");
			return projectDAO.getQcMName(qc_num);
		}
		
		// QC 제품 이름
		public String getQcPName(int qc_num) {
			log.info("getQcPName - Service");
			return projectDAO.getQcPName(qc_num);
		}
		
		// QC 1건 질문 - 응답 정보
		public List<QcVO> getOneQcDetail(int qc_num) {
			log.info("getOneQcDetail - Service");
			return projectDAO.getOneQcDetail(qc_num);
		}
	
		// QC 총 부적격 수량
		public int getTotalFail(int qc_num) {
			return projectDAO.getTotalFail(qc_num);
		}
		
		// Qc Detail 기존 값 존재 확인
		public int isQcDetail(QcDetailVO qcDetail) {
			return projectDAO.isQcDetail(qcDetail);
		}
		
		// (기존 값 없음) QC Detail 추가
		public int insertQcDetail(QcDetailVO qcDetails) {
			return projectDAO.insertQcDetail(qcDetails);
		}
		
		// (기존 값 있음) QC Detail 업데이트
		public int updateQcDetail(QcDetailVO qcDetails) {
			return projectDAO.updateQcDetail(qcDetails);
		}
		
		// QC 저장 버튼, 상태 : 검사중 (1)
		public int updateQcStat1(int qc_num) {
			return projectDAO.updateQcStat1(qc_num);
		}
		
		// QC 제출 버튼, 상태 : 검사 완료 (2)
		public int updateQcStat2(int qc_num) {
			return projectDAO.updateQcStat2(qc_num);
		}
					
		// QC 제출 버튼, inventory 수량 증가
		public int updateInven(InventoryVO inven) {
			return projectDAO.updateInven(inven);
		}
		
		// QC 제출 버튼, 제출자 session
		public int updateQcTester(QcVO qc) {
			return projectDAO.updateQcTester(qc);
		}
		
		
		//
		public OrderformVO getOrderformByPapernum(int paper_num) {
			return projectDAO.getOrderformByPapernum(paper_num);
		}
		
		public int getLastOrderformNum() {
			return projectDAO.getLastOrderformNum();
		}
		
		public int insertOrderform(OrderformVO of) {
			return projectDAO.insertOrderform(of);
		}
		
		public int insertOrderformDetail(OrderformDetailVO ofd) {
			return projectDAO.insertOrderformDetail(ofd);
		}
		
		public int getMaterialPrice(int inven_item_num) {
			return projectDAO.getMaterialPrice(inven_item_num);
		};
		
		public ProductionVO getProductionByPapernum(int paper_num) {
			return projectDAO.getProductionByPapernum(paper_num);
		}
		
		public int insertProductionDetail(ProductionDetailVO pdd) {
			return projectDAO.insertProductionDetail(pdd);
		}
			
		// 나현. 끝.



	// -----------------------------------------------------------------------------

	public int companyNameValidation(String company_name) {
		return projectDAO.companyNameValidation(company_name);
	}
	public int companyCodeValidation(String company_code) {
		return projectDAO.companyCodeValidation(company_code);
	}

	public List<ProductVO> productList() {
		return projectDAO.productList();
	}
	
	
	public int fileAmount(int product_num) {
		return projectDAO.fileAmount(product_num);
	}
	
	public FileVO findFirstImage(int product_num) {
		return projectDAO.findFirstImage(product_num);
	}
	
	public ProductVO getProductDetail (int product_num) {
		return projectDAO.getProductDetail(product_num);
	}

	public List<FileVO> getProductImages(int product_num) {
		return projectDAO.getProductImages(product_num);
	}
	
	public List<CompanyVO> getCompanyList() {
		return projectDAO.getCompanyList();
	}
	
	public List<ProductVO> getProductList() {
		return projectDAO.getProductList();
	}
	
// NEW 작업 윤호자리 @@@@@@@@@@@@@@윤호윤호@@@@@@@@@@  @@@@@@@@@@@@@@윤호윤호@@@@@@@@@@
	
	public int updateCompany(CompanyVO companyVO) {
		return projectDAO.updateCompany(companyVO);
	}
	
	public List<QuotationDetailVO> getQuotationDetailList(int quot_num) {
		return projectDAO.getQuotationDetailList(quot_num);
	}
	
	public InventoryVO getInventory(int product_num) {
		return projectDAO.getInventory(product_num);
	}
	
	public int updateInventoryAmount(int product_num, int amount) {
		return projectDAO.updateInventoryAmount(product_num, amount);
	}
	
	public int updateQuotationStat(int quot_num) {
		return projectDAO.updateQuotationStat(quot_num);
	}
	
	public QuotationVO getQuotationByQuotnum(int quot_num) {
		return projectDAO.getQuotationByQuotnum(quot_num);
	}
	
	public int insertQuotationCode(int quot_num, String code) {
		return projectDAO.insertQuotationCode(quot_num, code);
	}
	
	public List<QuotationDetailVO> getQuotationDetailListByQuotnum(int quot_num) {
		return projectDAO.getQuotationDetailListByQuotnum(quot_num);
	}
	
	public CompanyVO getCompanyByCompanynum(int company_num) {
		return projectDAO.getCompanyByCompanynum(company_num);
	}
	
	public List<InventoryVO> getInventoryMaterialList() {
		return projectDAO.getInventoryMaterialList();
	}
	
	public List<InventoryVO> getInventoryProductList() {
		return projectDAO.getInventoryProductList();
	}
	
	public int uploadReleaseDate(int quot_num) {
		return projectDAO.uploadReleaseDate(quot_num);
	}
	
	public List<ProductVO> getProductListWithSales(int day) {
		return projectDAO.getProductListWithSales(day);
	}
	
	public List<CompanyVO> getCompanyListWithSales(int day) {
		return projectDAO.getCompanyListWithSales(day);
	}
	
	
	
// 윤호자리 @@@@@@@@@@@@@@윤호윤호@@@@@@@@@@  @@@@@@@@@@@@@@윤호윤호@@@@@@@@@@
	
	

// ---------------new 작업공간 (이의재) ----------------------------
	
//	public ModelAndView getQuotationDetail(
//			@RequestParam("quot_num") int quot_num
//			) {
//		
//		QuotationVO quotationVO = new QuotationVO();
//		quotationVO = projectDAO.getQuotationByQuotnum(quot_num);
//		List<QuotationDetailVO> quotationDetailListVO = projectDAO.getQuotationDetailListByQuotnum(quot_num);
//		
//		CompanyVO company1VO = projectDAO.getCompanyByCompanynum(quotationVO.getCompany_num());
//		CompanyVO company2VO = projectDAO.getCompanyByCompanynum(quotationVO.getCompany_num2());
//		
//		String code = updateQuotationCode(quotationVO);
//		
//		
//		mv = new ModelAndView();
//		mv.addObject("code", code);
//		mv.addObject("quotationDetailListVO", quotationDetailListVO);
//		mv.addObject("company1VO", company1VO);
//		mv.addObject("company2VO", company2VO);
//		mv.addObject("quotationVO", quotationVO);
//		mv.setViewName("quotationDetail");
//		return mv;
//	}
	
	public ModelAndView getOrderformDetail(
			@RequestParam("orderform_num") int orderform_num
			) {
		OrderformVO orderformVO = new OrderformVO();
		orderformVO = projectDAO.getOrderformByOrderformnum(orderform_num);
		List<OrderformDetailVO> orderformDetailListVO = projectDAO.getOrderformDetailListByOrderformnum(orderform_num);
		
		CompanyVO company1VO = projectDAO.getCompanyByCompanynum(orderformVO.getCompany_num());
		CompanyVO company2VO = projectDAO.getCompanyByCompanynum(orderformVO.getCompany_num2());
		
		String code = updateOrderformCode(orderformVO);
		
		mv = new ModelAndView();
		mv.addObject("code", code);
		mv.addObject("orderformDetailListVO", orderformDetailListVO);
		mv.addObject("company1VO", company1VO);
		mv.addObject("company2VO", company2VO);
		mv.addObject("orderformVO", orderformVO);
		mv.setViewName("orderformDetail");
		return mv;
	}

//	public String updateQuotationCode(QuotationVO quotationVO) {
//		String code1 = quotationVO.getQuot_regdate().substring(0,10).replaceAll("-", "");
//		String code2 = Integer.toString(quotationVO.getCompany_num());
//		String code3 = Integer.toString(quotationVO.getCompany_num2());
//		String code4 = Integer.toString(quotationVO.getQuot_num());
//		String code = code1 + code2 + code3 + code4;
//		return code;
//	}
	
	public String updateOrderformCode(OrderformVO orderformVO) {
		String code1 = orderformVO.getOrderform_regdate().substring(0,10).replaceAll("-", "");
		String code2 = Integer.toString(orderformVO.getCompany_num());
		String code3 = Integer.toString(orderformVO.getCompany_num2());
		String code4 = Integer.toString(orderformVO.getOrderform_num());
		String code = code1 + code2 + code3 + code4;
		return code;
	}
	
	public ModelAndView getAllFormDetail(
			@RequestParam("this_num") String this_num
			) {
		mv = new ModelAndView();
		if (this_num.contains("quot")) {
			int quot_num = Integer.parseInt(this_num.replaceAll("quot", ""));
			mv.addObject("quot_num", quot_num);
			mv.setViewName("getQuotationDetail");
			return mv;
		}
		return mv;
	}
	
	public ModelAndView getMaterialRegister() {
		mv = new ModelAndView();
		mv.setViewName("materialRegister");
		return mv;
	}
	
	public int materialCodeCheck(String material_code) {
		return projectDAO.materialCodeCheck(material_code);
	}
	
	public int findMaxMaterialNum() {
		return projectDAO.findMaxMaterialNum();
	}
	
	public int addMaterial(MaterialVO materialVO) {
		return projectDAO.addMaterial(materialVO);
	}
	
	public List<MaterialVO> getMaterialList() {
		return projectDAO.getMaterialList();
	}
	
	public int materialFileAmount(int material_num) {
		return projectDAO.materialFileAmount(material_num);
	}
	
	public FileVO materialFindFirstImage(int material_num) {
		return projectDAO.materialFindFirstImage(material_num);
	}

	public List<FileVO> getMaterialImages(int material_num) {
		return projectDAO.getMaterialImages(material_num);
	}
	
	public MaterialVO getMaterialDetail (int material_num) {
		return projectDAO.getMaterialDetail(material_num);
	}
	
	public MaterialVO getMaterialByMaterialName(String product_name) {
		return projectDAO.getMaterialByMaterialName(product_name);
	}
	
	public ModelAndView member(
			
			) {
		mv = new ModelAndView();
		
		List<MemberVO> memberListVO = projectDAO.getMemberList();
		
		mv.addObject("memberListVO", memberListVO);
		mv.setViewName("member");
		return mv;
	}
	
	public MemberVO getMemberByMemberId(String member_id) {
		return projectDAO.getMemberByMemberId(member_id);
	}
	
	public int memberIdValidation(Map<String,Object> map) {
		return projectDAO.memberIdValidation(map);
	}
	
	public int updateMember(MemberVO memberVO) {
		return projectDAO.updateMember(memberVO);
	}
	
	public int addMaterialInventory(InventoryVO inventoryVO) {
		return projectDAO.addMaterialInventory(inventoryVO);
	}
	
	public int addProductInventory(InventoryVO inventoryVO) {
		return projectDAO.addProductInventory(inventoryVO);
	}
	
	public int productNameCheck(String product_name) {
		return projectDAO.productNameCheck(product_name);
	}
	
	public String[] getProductCodeAndNameListConcat() {
		return projectDAO.getProductCodeAndNameListConcat();
	}
	
	public int addRecipe(@RequestParam Map<String,Object> map) throws Exception {
    	
    	String product_code = (String)map.get("recipe-input1");
    	String product_code_replaced = product_code.replaceAll("\\(.*\\)$", ""); // ex) aaaa01(product01)을 aaaa01로 바꾸는 코드
    	RecipeVO recipeVO = new RecipeVO();
    	recipeVO.setProduct_name((String)map.get("recipe-input2"));
    	recipeVO.setProduct_code(product_code_replaced);
    	recipeVO.setRecipe_price(Integer.parseInt((String) map.get("recipe-input3")));
    	
    	int result = projectDAO.insertRecipe(recipeVO);
    	
    	int product_num = projectDAO.getProductNumByProductCode(product_code_replaced);
    	int LastRecipeNum = projectDAO.getLastRecipeNum();
    	int i = 1;
    	
    	for (;;) {
    		String a = "material-name-input" + i;
    		String b = "material-amount-input" + i;
    		i += 1;
    		RecipeDetailVO recipeDetailVO = new RecipeDetailVO();
    		String material_name = (String)(map.get(a));
    		int material_amount = Integer.parseInt((String)(map.get(b)));
    		
    		if(material_name == "") {
    			continue;
    		}
    		if(material_amount == 0) {
    			continue;
    		}
    		
    		recipeDetailVO.setRecipe_num(LastRecipeNum);
    		recipeDetailVO.setProduct_num(product_num);
    		recipeDetailVO.setMaterial_name(material_name);
    		recipeDetailVO.setMaterial_amount(material_amount);
    		
    		int result2 = projectDAO.insertRecipeDetail(recipeDetailVO);
    	}
    }
	
	public int recipeProductCodeCheck(String product_code) {
		return projectDAO.recipeProductCodeCheck(product_code);
	}
	
	public ModelAndView getRecipe() {
		mv = new ModelAndView();
		List<RecipeVO> recipeVOList = projectDAO.getRecipeList();
		
		mv.addObject("recipeVOList",recipeVOList);
		mv.setViewName("recipe");
		return mv;
	}
	
	public List<RecipeDetailVO> getRecipeDetailByProductcode(
			@RequestParam("product_code") String product_code) {
		
		int product_num = projectDAO.getProductNumByProductCode(product_code);
		List<RecipeDetailVO> recipeDetailListVO = projectDAO.getRecipeDetailByProductNum(product_num);
		
		return recipeDetailListVO;
	}
	
	public RecipeVO getRecipeByRecipeNum(
			@RequestParam("recipe_num") int recipe_num
			) {
		RecipeVO recipeVO = projectDAO.getRecipeByRecipeNum(recipe_num);
		return recipeVO;
	}
	
	public int updateRecipe(@RequestParam Map<String,Object> map) throws Exception {
		int recipe_num = Integer.parseInt((String)map.get("recipe-input4"));
		String product_code = (String)map.get("recipe-input1");
		String product_name = (String)map.get("recipe-input2");
		int recipe_price = Integer.parseInt((String)map.get("recipe-input3"));
		
		RecipeVO recipeVO = new RecipeVO();
		recipeVO.setRecipe_num(recipe_num);
		recipeVO.setProduct_code(product_code);
		recipeVO.setProduct_name(product_name);
		recipeVO.setRecipe_price(recipe_price);
		
		int result = projectDAO.updateRecipe(recipeVO);
		int i = 1;
		for(;;) {
			String a = "material-name-input" + i;
			String b = "material-amount-input" + i;
			String c = "recipedetail-num" + i;
			i += 1;
			RecipeDetailVO recipeDetailVO = new RecipeDetailVO();
			String material_name = (String)(map.get(a));
			int material_amount = Integer.parseInt((String)(map.get(b)));
			int rd_num = Integer.parseInt((String)map.get(c));
			
			if (material_name == "") {
				continue;
			}
			if (material_amount == 0) {
				continue;
			}
			
			recipeDetailVO.setRd_num(rd_num);
			recipeDetailVO.setMaterial_name(material_name);
			recipeDetailVO.setMaterial_amount(material_amount);
			int result2 = projectDAO.updateRecipeDetail(recipeDetailVO);
			
		}
		
	}
	
	public List<OrderformDetailVO> getOrderformDetailListByOrderformnum (int orderform_num) {
		return projectDAO.getOrderformDetailListByOrderformnum(orderform_num);
	}
	
	public int updateOrderformFinish(int orderform_num) {
		return projectDAO.updateOrderformFinish(orderform_num);
	}
	
	// ---------------new 작업공간 (이의재) ----------------------------
	
	
// ---------------------김민성---------------------------------
	
	public List<ProductionVO> getProductionList() {
		// TODO Auto-generated method stub
		log.info("getProductionList()");
		return projectDAO.getProductionList();
	}
	
	public int setproductionForm(List<ProductionDetailVO> list) {
		// TODO Auto-generated method stub
		return projectDAO.insertproductiondetail(list);
	}
	
	public int insertProduction(ProductionVO productionVO) {
		// TODO Auto-generated method stub
		return projectDAO.insertProduction(productionVO);
	}
	
	public int getfindLastProductionNumber() {
		// TODO Auto-generated method stub
		return projectDAO.getfindLastProductionNumber();
	}
	
	public List<ProductionVO> getFactoryDetailList(int pd_num) {
		// TODO Auto-generated method stub
		return projectDAO.getFactoryDetailList(pd_num);
	}
	
	public ProductionVO getFactoryDetail(int pd_num) {
		// TODO Auto-generated method stub
		return projectDAO.getFactoryDetail(pd_num);
	}
	
	public int getRecipeNumByProductName(String product_name) {
		return projectDAO.getRecipeNumByProductName(product_name);
	}
	
	public List<RecipeDetailVO> getRecipeDetailListByRecipeNum(int recipe_num) {
		return projectDAO.getRecipeDetailListByRecipeNum(recipe_num);
	}
	
	public int reduceInventoryAmount(InventoryVO inventoryVO) {
		
		return projectDAO.reduceInventoryAmount(inventoryVO);
	}

	public int setPdCheckUpdate(ProductionVO productionVO) {
		// TODO Auto-generated method stub
		return projectDAO.setPdCheckUpdate(productionVO);
	}

	public int insertqc(QcVO qcVO) {
		// TODO Auto-generated method stub
		return projectDAO.insertqc(qcVO);
	}

	public List<ProductionVO> getFatoryWorkList() {
		// TODO Auto-generated method stub
		return projectDAO.getFatoryWorkList();
	}

	public ProductVO getfindProductNum(ProductVO productVO) {
		// TODO Auto-generated method stub
		return projectDAO.getfindProductNum(productVO);
	}

	public InventoryVO getInventoryByProductName(String product_name) {
		// TODO Auto-generated method stub
		return projectDAO.getInventoryByProductName(product_name);
	}

	public List<RecipeDetailVO> getTotalAmount(String product_name) {
		// TODO Auto-generated method stub
		return projectDAO.getTotalAmount(product_name);
	}

	public List<InventoryVO> getInventoryList() {
		// TODO Auto-generated method stub
		return projectDAO.getInventoryList();
	}

	public List<ProductionDetailVO> getProductionDetail(int pd_num) {
		// TODO Auto-generated method stub
		return projectDAO.getProductionDetail(pd_num);
	}

	public List<RecipeDetailVO> getRecipeDetailList() {
		// TODO Auto-generated method stub
		return projectDAO.getRecipeDetailList();
	}

	public  InventoryVO getInvenAmount(String Mname) {
		// TODO Auto-generated method stub
		return projectDAO.getInvenAmount(Mname);
	}

	public List<ProductionDetailVO> getProductionListByFactoryDetail(int pd_num) {
		// TODO Auto-generated method stub
		return projectDAO.getProductionListByFactoryDetail(pd_num);
	}

	public int getFindRecipeNum(String product_name) {
		// TODO Auto-generated method stub
		return projectDAO.getFindRecipeNum(product_name);
	}

	public List<InventoryVO> getFindInvenList(String product_name) {
		// TODO Auto-generated method stub
		return projectDAO.getFindInvenList(product_name);
	}
		
		
		
// ---------------------김민성---------------------------------	
	
	
	
}

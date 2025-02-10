package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.CompanyVO;
import com.example.demo.dto.FileVO;
import com.example.demo.dto.InventoryVO;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.ProductionVO;
import com.example.demo.dto.QcDashVO;
import com.example.demo.dto.QuotationVO;
import com.example.demo.dto.RecentSalesVO;
import com.example.demo.service.ProjectService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/")
    public String Home(Model model, HttpSession session){
		
	// 김윤호의 대시보드 시작
		
		// 이번주 인기상품
        int day = 7;
        
        List<ProductVO> productList = projectService.getProductListWithSales(day); // 최근 며칠동안(day)의 판매량을 가져옴
        
        for (ProductVO product : productList) {
        	int product_num = product.getProduct_num();
  		   
        	int amount = projectService.fileAmount(product_num);
  		   
        	if (amount == 0) {
        		product.setProduct_img("vendors/images/no-image-alert.jpg");
        	} else {
        		FileVO file = projectService.findFirstImage(product_num);
        		product.setProduct_img(file.getFile_path());
        	}
        }
        model.addAttribute("productSales",productList);
        
        
        // 판매계약서
        List<QuotationVO> quotationList = projectService.getUnreleasedQuotationList();
        model.addAttribute("quotationList",quotationList);
        
	// 김윤호의 대시보드 끝
        
        
        
    // 김민성의 대시보드 시작
        
        MemberVO member = (MemberVO)session.getAttribute("user");
        if(member != null) {
        	model.addAttribute(member);
        }
        
    	List<ProductionVO> list = projectService.getLastProduction();
    	model.addAttribute("list", list); 
        
        
    // 김민성의 대시보드 끝
    
        
        
        
        
    // 박나현의 대시보드 시작
        
        
    // 박나현의 대시보드 끝
        
        
        
    // 이의재의 대시보드 시작
        
		List<InventoryVO> inventoryListVO = projectService.getInventoryMaterialList();
		List<InventoryVO> productListVO = projectService.getInventoryProductList();
		
		model.addAttribute("inventoryListVO", inventoryListVO);
		model.addAttribute("productListVO", productListVO);
		
    // 이의재의 대시보드 끝
       
        
        
        
        
        
        
        return "index";
    }
	
	
	
	
	
	
	// =================================  김윤호의 Home Controller 시작  ==========================================
	
    @ResponseBody
    @GetMapping("/getCompanyListWithSales")
    public Map<String,Object> getCompanyListWithSales() {
    	
    	// 데이터 없을 땐 day 의 값을 늘리기
    	int day = 7;
    	int pieChartSize = 5;
    	
    	List<CompanyVO> companyList = projectService.getCompanyListWithSales(day);
    	
    	Map<String,Object> map = new HashMap<>();
    	
    	if (companyList.size()<pieChartSize) {
    		pieChartSize = companyList.size();
    	}
    	
    	String[] companyName = new String[pieChartSize];
    	int[] companyTotalSales = new int[pieChartSize];
    	
    	int cnt = 0;
    	for (CompanyVO company : companyList) {
    		companyName[cnt] = company.getCompany_name();
    		companyTotalSales[cnt] = company.getTotalsales();
    		cnt++;
    	}
    	
    	map.put("companyName",companyName);
    	map.put("companyTotalSales",companyTotalSales);
    	
        return map;
    }
	
    
    
    @ResponseBody
    @GetMapping("/getRecentSalesInformations")
    public Map<String,Object> getRecentSalesInformations() {
    	
    	int day = 7;
    	List<RecentSalesVO> list = projectService.getRecentSalesInformations(day);
    	
    	Map<String,Object> map = new HashMap<>();
    	
    	String[] dayOfWeek = {"월","화","수","목","금","토","일"};
    	String[] dayname = new String[day];
    	int[] recentTotalAmount = new int[day];
    	int[] recentTotalSales = new int[day];
    	
    	for (int i=0; i<day; i++) {
    		recentTotalAmount[i] = 0;
    		recentTotalSales[i] = 0;
    		dayname[i] = "before";
    	}
    	
    	int count = 0;
    	
    	for (RecentSalesVO recentSales : list) {
    		recentTotalAmount[6-recentSales.getDiffdate()] = recentSales.getTotalamount();
    		recentTotalSales[6-recentSales.getDiffdate()] = recentSales.getTotalsales();
    		if (count==0) {
    			int index = 0;
    			for (int i=0; i<day; i++) {
    				if (recentSales.getDayname().equals(dayOfWeek[i])) {
    					index = i;
    				}
    			}
    			for (int i=0; i<day; i++) {
    				dayname[i] = dayOfWeek[(index+recentSales.getDiffdate()+1+i)%7];
    			}
    		}
    		count++;
    	}
    	
//    	map.put("dayname",dayname);
//    	map.put("recentTotalAmount",recentTotalAmount);
//    	map.put("recentTotalSales",recentTotalSales);
    	
    	
    	// 판매 데이터 없을 때 연습용, 위에 map.put 2줄 주석처리하고 아래 내용 주석 해제해서 적용하기
    	
    	String[] daynameTest = {"월","화","수","목","금","토","일"};
    	int[] recentTotalAmountTest = {105, 34, 58, 95, 132, 116, 125};
    	int[] recentTotalSalesTest = {2130000, 4350000, 3740000, 6730000, 5840000, 7040000, 6843520};
    	
    	map.put("dayname",daynameTest);
    	map.put("recentTotalAmount",recentTotalAmountTest);    	
    	map.put("recentTotalSales",recentTotalSalesTest);
    	
    	
    	return map;
    	
    }
    
    @ResponseBody
    @GetMapping("/getRecentProduceInformations")
    public Map<String,Object> getRecentProduceInformations() {
    	
    	Map<String,Object> map = new HashMap<>();
    	
    	int day = 7;
    	List<RecentSalesVO> list = projectService.getRecentProduceInformations(day);
    	
    	String[] dayOfWeek = {"월","화","수","목","금","토","일"};
    	String[] dayname = new String[day];
    	int[] recentTotalProduce = new int[day];
    	
    	for (int i=0; i<day; i++) {
    		recentTotalProduce[i] = 0;
    		dayname[i] = "before";
    	}
    	
    	int count = 0;
    	
    	for (RecentSalesVO recentSales : list) {
    		recentTotalProduce[6-recentSales.getDiffdate()] = recentSales.getTotalproduce();
    		if (count==0) {
    			int index = 0;
    			for (int i=0; i<day; i++) {
    				if (recentSales.getDayname().equals(dayOfWeek[i])) {
    					index = i;
    				}
    			}
    			for (int i=0; i<day; i++) {
    				dayname[i] = dayOfWeek[(index+recentSales.getDiffdate()+1+i)%7];
    			}
    		}
    		count++;
    	}
    	
//    	map.put("dayname",dayname);
//    	map.put("recentTotalProduce",recentTotalProduce);
    	
    	
    	// 판매 데이터 없을 때 연습용, 위에 map.put 2줄 주석처리하고 아래 내용 주석 해제해서 적용하기
    	
    	String[] daynameTest = {"월","화","수","목","금","토","일"};
    	int[] recentTotalProduceTest = {105, 34, 58, 95, 132, 102, 146};
    	
    	map.put("dayname",daynameTest);
    	map.put("recentTotalProduce",recentTotalProduceTest);    	
    	
    	
    	return map;
    	
    }
    
    
    // =================================  김윤호의 Home Controller 끝  ===========================================
    
    
    
    
	// =================================  김민성의 Home Controller 시작  ==========================================
    
//	@GetMapping("/")
//    public String Home(Model model, HttpSession session){
//		MemberVO member = (MemberVO)session.getAttribute("user");
//        log.info("Home");
//        if(member != null) {
//        	List<ProductionVO> list = projectService.getLastProduction();
//        	model.addAttribute("list", list); 
//        	model.addAttribute(member);
//        	return "index";
//        }else {
//        	List<ProductionVO> list = projectService.getLastProduction();
//        	model.addAttribute("list", list); 
//        	return "index";
//        }
//    }
    
    
	@ResponseBody
    @GetMapping("/chartdata")
    public Map<String,Object> getChartData() {
    	
    	 int black = projectService.getPdCheckCount1();
    	 int white = projectService.getPdCheckCount2();
    	 
    	 System.out.println(black);
    	 System.out.println(white);
    	 
         
    	Map<String,Object> chartdata = new HashMap<>();
    	chartdata.put("black",black);
    	chartdata.put("white",white);
    	
        return chartdata;
    }
    
	// =================================  김민성의 Home Controller 끝  ===========================================
    
    
    
    
	// =================================  박나현의 Home Controller 시작  ==========================================
    
    @ResponseBody
    @GetMapping("/getQcTopMaterial")
    public Map<String,Object> getQcTopMaterial() {
    	
    	int barSize = 5;
    	
    	List<QcDashVO> QcDashList = projectService.QcMDashTop5();
//    	System.out.println("Qc 대시보드 테스트 :::::::::::::" + QcDashList);
    	Map<String,Object> map = new HashMap<>();
    	
    	if(QcDashList.size() < barSize) {
    		barSize = QcDashList.size();
    	}
    	
    	String[] itemName = new String[barSize];
    	float[] itemQcPer = new float[barSize];
    	
    	int i = 0;
    	for (QcDashVO qc:QcDashList) {
    		itemName[i] = qc.getItem_name();
    		itemQcPer[i] = qc.getFail_per();
    		i++;
    	}

    	map.put("itemName", itemName);
    	map.put("itemQcPer", itemQcPer);
    	
    	return map;
    }
    
    
    @ResponseBody
    @GetMapping("/getQcTopProduct")
    public Map<String,Object> getQcTopProduct() {
    	
    	int barSize = 5;
    	
    	List<QcDashVO> QcDashList = projectService.QcPDashTop5();
//    	System.out.println("Qc 대시보드 테스트 :::::::::::::" + QcDashList);
    	Map<String,Object> map = new HashMap<>();
    	
    	if(QcDashList.size() < barSize) {
    		barSize = QcDashList.size();
    	}
    	
    	String[] itemName = new String[barSize];
    	float[] itemQcPer = new float[barSize];
    	
    	int i = 0;
    	for (QcDashVO qc:QcDashList) {
    		itemName[i] = qc.getItem_name();
    		itemQcPer[i] = qc.getFail_per();
    		i++;
    	}

    	map.put("itemName", itemName);
    	map.put("itemQcPer", itemQcPer);
    	
    	return map;
    }
    
	// =================================  박나현의 Home Controller 끝  ===========================================    
    
    
    
    
    
	// =================================  이의재의 Home Controller 시작  ==========================================
    
    
    
	// =================================  이의재의 Home Controller 끝  ===========================================
    
}

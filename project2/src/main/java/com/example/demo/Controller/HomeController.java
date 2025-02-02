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
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.QuotationVO;
import com.example.demo.dto.RecentSalesVO;
import com.example.demo.dto.dashQcVO;
import com.example.demo.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/")
    public String Home(Model model){
		
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
        
        return "index";
    }
	
	
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
	
    
    
	// 김윤호 25/01/27 부터 새로 작성
	
    @ResponseBody
    @GetMapping("/getRecentSalesInformations")
    public Map<String,Object> getRecentSalesInformations() {
    	
    	int day = 7;
    	List<RecentSalesVO> list = projectService.getRecentSalesInformations(day);
    	
    	Map<String,Object> map = new HashMap<>();
    	
    	String[] dayOfWeek = {"월","화","수","목","금","토","일"};
    	String[] dayname = new String[day];
    	int[] recentTotalAmount = new int[day];
    	
    	for (int i=0; i<day; i++) {
    		recentTotalAmount[i] = 0;
    		dayname[i] = "before";
    	}
    	
    	int count = 0;
    	
    	for (RecentSalesVO recentSales : list) {
    		recentTotalAmount[6-recentSales.getDiffdate()] = recentSales.getTotalamount();
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
    	
    	
    	// 판매 데이터 없을 때 연습용, 위에 map.put 2줄 주석처리하고 아래 내용 주석 해제해서 적용하기
    	
    	String[] daynameTest = {"월","화","수","목","금","토","일"};
    	int[] recentTotalAmountTest = {105, 34, 58, 95, 132, 116, 125};
    	
    	map.put("dayname",daynameTest);
    	map.put("recentTotalAmount",recentTotalAmountTest);    	
    	
    	return map;
    	
    }
    
    
// 나현. 시작. --------------------------------------------------------------------

    @ResponseBody
    @GetMapping("/getQcTopMaterial")
    public Map<String,Object> getQcTopMaterial() {
    	
    	int barSize = 5;
    	
    	List<dashQcVO> dashQcList = projectService.dashQcTop5();
    	Map<String,Object> map = new HashMap<>();
    	
    	if(dashQcList.size() < barSize) {
    		barSize = dashQcList.size();
    	}
    	
    	String[] itemName = new String[barSize];
    	float[] itemQcPer = new float[barSize];
    	
    	int i = 0;
    	for (dashQcVO qc:dashQcList) {
    		itemName[i] = qc.getItem_name();
    		itemQcPer[i] = qc.getTotal_fail_per();
    		i++;
    	}

    	map.put("itemName", itemName);
    	map.put("itemQcPer", itemQcPer);
    	
    	return map;
    }
    
    
// 나현. 끝. --------------------------------------------------------------------
    
    
    
    
	
}
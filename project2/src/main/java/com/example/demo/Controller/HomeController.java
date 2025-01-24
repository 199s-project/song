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
import com.example.demo.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/")
    public String Home(Model model){
		
        log.info("Home");
        
        List<ProductVO> productList = projectService.getProductListWithSales(7);
        
        
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
        
        
        
        return "index";
    }
	
	
    @ResponseBody
    @GetMapping("/getCompanyListWithSales")
    public Map<String,Object> getCompanyListWithSales() {
    	
    	List<CompanyVO> companyList = projectService.getCompanyListWithSales(7);
    	
    	Map<String,Object> map = new HashMap<>();
    	
    	int pieChartSize = 5;
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
	
	
}

package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.CompanyVO;
import com.example.demo.dto.FileVO;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.OrderformDetailVO;
import com.example.demo.dto.OrderformVO;
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.QuotationDetailVO;
import com.example.demo.dto.QuotationVO;

@Mapper
public interface ProjectDAO {

	
	MemberVO getMember(Map<String,Object> map);
	
	int postRegister(Map<String,Object> map);
	   
	int checkMember_id(String member_id);
	
	
	List<OrderformVO> orderformList();
	
	int addCompany(CompanyVO companyVO);
	
	int addProduct(ProductVO productVO);
	
	int findMaxProductNum();
	
	int fileUpload(FileVO fileVO);
	
	int productCodeCheck(String product_code);
	
	String companyNameCheck(String company_name);

	//물품 구매 계약서
	List<OrderformVO> orderList();
	
	//물품 판매 계약서
	List<QuotationVO> quotationList();
	
	//All 계약서
	List<QuotationVO> allFormList();
	
	List<CompanyVO> getCompanyList();

	List<ProductVO> getProductList();
		
	CompanyVO getCompanyByCompanyName(String company_name);

	int insertOrderform(OrderformVO orderformVO);

	int getLastOrderformNum();
		
	ProductVO getProductByProductName(String product_name);	

	int insertOrderformDetail(OrderformDetailVO orderformDetailVO);

	int insertQuotation(QuotationVO quotationVO);

	int getLastQuotationNum();

	int insertQuotationDetail(QuotationDetailVO quotationDetailVO);
	
	
	
	
	
}

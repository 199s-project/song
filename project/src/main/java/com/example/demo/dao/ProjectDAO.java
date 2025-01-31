package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.CompanyVO;
import com.example.demo.dto.FileVO;
import com.example.demo.dto.InventoryVO;
import com.example.demo.dto.MemberVO;
import com.example.demo.dto.OrderformDetailVO;
import com.example.demo.dto.OrderformVO;
import com.example.demo.dto.PlandetailVO;
import com.example.demo.dto.ProductVO;
import com.example.demo.dto.ProductionPlanVO;
import com.example.demo.dto.QcDetailVO;
import com.example.demo.dto.QcVO;
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
	
	
	// 나현. 시작.
	// QC
	
	// QC 전체 리스트
	List<QcVO> getQcList();
	
	// QC 1건 기본 정보
	QcVO getOneQc(int qc_num);
	
	// QC 원자재 이름
	String getQcMName(int qc_num);
	
	// QC 제품 이름
	String getQcPName(int qc_num);
	
	// QC 1건 질문-응답 정보 (질문 번호, 질문 내용, 부적격 수량)
	List<QcVO> getOneQcDetail(int qc_num);
	
	// QC 1건 총 부적격 수량
	int getTotalFail(int qc_num);
	
	// QC Detail 기존 값 존재 확인
	int isQcDetail(QcDetailVO detail);
	
	// (기존 값 없음) QC Detail 추가
	int insertQcDetail(QcDetailVO detail);
	
	// (기존 값 있음) QC Detail 업데이트
	int updateQcDetail(QcDetailVO detail);
	
	// QC 저장 버튼, 상태 : 검사중 (1)
	int updateQcStat1(int qc_num);
	
	// QC 제출 버튼, 상태 : 검사 완료 (2)
	int updateQcStat2(int qc_num);
	
	// QC 제출 버튼, inventory 수량 증가
	int updateInven(InventoryVO inven);
	
	// 나현. 끝.

	List<ProductionPlanVO> getProductionPlanList();


	int insertPlandetail(List<PlandetailVO> list);


	int insertProduction(ProductionPlanVO productionPlanVO);

	int getfindLastProductionNumber();
	

	// ------------------------------------------------------
	
	int companyNameValidation(String company_name);
	
	int companyCodeValidation(String company_code);
	
	List<ProductVO> productList();
	
	int fileAmount(int product_num);
	
	FileVO findFirstImage(int product_num);
	
	ProductVO getProductDetail(int product_num);
	
	List<FileVO> getProductImages(int product_num);

// new 작업공간 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	QuotationVO getQuotationByQuotnum(int quot_num);
	
	CompanyVO getCompanyByCompanynum(int company_num);
	
	List<QuotationDetailVO> getQuotationDetailListByQuotnum(int quot_num);
	
	OrderformVO getOrderformByOrderformnum(int orderform_num);
	
	List<OrderformDetailVO> getOrderformDetailListByOrderformnum(int orderform_num);

}

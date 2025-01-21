package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QuotationVO")
public class QuotationVO {
	private int quot_num;
	private String quot_name;
	private String quot_stat;
	private int company_num;
	private int company_num2;
	private String quot_content;
	private String quot_regdate;
	private String quot_startdate;
	private String quot_enddate;
	
	
//quotaion detail vo
	private int quotdetail_num;
	private int product_num;
	private int quotdetail_amount;
	private int quotdetail_price;
	private int total_price;
	private String this_num;
	
	//orderform vo
	private int orderform_num;
	private String orderform_stat;
	private String orderform_content;
	private String orderform_regdate;
	private String orderform_startDate;
	private String orderform_endDate;
	
	
	//orderformDetailVO
	private int orderdetail_num;
	private int orderdetail_amount;
	private int orderdetail_price;
}

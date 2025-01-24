package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("OrderformVO")
public class OrderformVO {
	private int orderform_num;
	private String orderform_name;
	private String orderform_stat;
	private int company_num;
	private int company_num2;
	private String orderform_content;
	private String orderform_regdate;
	private String orderform_startDate;
	private String orderform_endDate;
	
	// 25-01-24 추가
	private String orderform_code;	
	private String company_name1;
	private String company_name2;
	
	//orderformDetailVO
	private int orderdetail_num;
	private int product_num;
	private int orderdetail_amount;
	private int orderdetail_price;
	private int total_price;
}

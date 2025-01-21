package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QuotationDetailVO")
public class QuotationDetailVO {
	private int quotdetail_num;
	private int quot_num;
	private int product_num;
	private int quotdetail_amount;
	private int quotdetail_price;
}

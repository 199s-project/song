package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("OrderformDetailVO")
public class OrderformDetailVO {
	private int orderdetail_num;
	private int orderform_num;
	private int product_num;
	private int orderdetail_amount;
	private int orderdetail_price;
}

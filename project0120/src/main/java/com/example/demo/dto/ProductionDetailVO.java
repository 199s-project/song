package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("ProductiondetailVO")
public class ProductionDetailVO {
	private int productiondetail_num;
	private int pd_num;
	private String product_name;
	private int productiondetail_amount;


	
	
}

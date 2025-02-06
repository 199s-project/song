package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("ProductionVO")
public class ProductionVO {
	private int pd_num;
	private String pd_writedate;
	private String pd_writer;
	private String pd_dept;
	private String pd_startdate;
	private String pd_enddate;
	private String pd_name;
	private String pd_content;
	private int pd_check;
	
	//plandetailVO
	private int productiondetail_num;
	private String product_name;
	private int productiondetail_amount;
	
	private int black;
	private int white;
}

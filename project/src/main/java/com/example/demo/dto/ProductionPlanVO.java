package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("ProductionPlanVO")
public class ProductionPlanVO {
	private int pd_num;
	private String pd_writedate;
	private String pd_writer;
	private String pd_dept;
	private String pd_startdate;
	private String pd_enddate;
	private String pd_name;
	private String pd_content;
	
	//plandetailVO
	private int plandetail_num;
	private String plandetail_name;
	private String plandetail_amount;
}

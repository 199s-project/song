package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("PlandetailVO")
public class PlandetailVO {
	private int plandetail_num;
	private int pd_num;
	private String plandetail_name;
	private int plandetail_amount;


	
	
}

package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("RecentSalesVO")
public class RecentSalesVO {
	
	private int totalamount;
	private int totalsales;
	private int diffdate;
	private String regdate;
	private String dayname;
	
	private int totalproduce;
}

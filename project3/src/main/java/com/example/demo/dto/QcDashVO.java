package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("QcDashVO")
public class QcDashVO {

	private int item_num;		// 아이템 번호
	private String item_name;	// 아이템 이름
	private float fail_per;		// 불량률
	private int total;			// 전체 검사 수량
	private int total_fail;		// 전체 불량 수량
	
}
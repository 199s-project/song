package com.example.demo.dto;

import org.apache.ibatis.type.Alias;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QcVO")
public class QcVO {

	private int qc_num;		// qc 검사 번호
	private String qc_type;	// qc 타입
	private int paper_num;	// 요청한 문서 번호 
	private String qc_reg;	// qc 요청일
	private String qc_writer;	// qc 등록자
	private String qc_date;		// qc 검사일
	private String qc_tester;	// qc 검사자
	private int qc_item_num;	// 검사 대상 (원자재, 상품)
	private int qc_quan;	// 검사 수량
	private int qc_stat;	// 검사 상태 (진행중, 완료)
}
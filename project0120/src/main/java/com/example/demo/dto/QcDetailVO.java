package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QcDetailVO")
public class QcDetailVO {
	private int qcd_num;	// qc 상세 번호
	private int qc_num;		// qc 번호
	private int qcq_num;	// 질문 번호
	private int qc_fail_quan;	// 불량 건수
	
	private String qc_tester; // (01-20 추가) 검수자
}

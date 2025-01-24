package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QcQuestVO")
public class QcQuestVO {
	private String qc_type;	// 검사 종류
	private int qcq_num;	// 질문 번호
	private String qcquest;	// 질문 내용
}
package com.example.demo.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
	
	// QcDetailVO
	private int qcd_num;	// qc 상세 번호
// 	private int qc_num;		// qc 번호 (중복)
//	private String qcq_num;	// 질문 번호
	private int qc_fail_quan;	// 불량 건수
	
	// QcQuestVO
//	private String qc_type;	// 검사 종류 (중복)
	private int qcq_num;	// 질문 번호
	private String qcquest;	// 질문 내용 (중복)
	
	private String qc_item_name; // 상품명 (추가함)
	
	private List<QcVO> QcVoList;
	private int totalPass;
}
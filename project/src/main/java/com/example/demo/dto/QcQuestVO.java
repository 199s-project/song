package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QcQuestVO")
public class QcQuestVO {
	private String qc_type;	
	private int qcq_num;
	private String qcquest;
}

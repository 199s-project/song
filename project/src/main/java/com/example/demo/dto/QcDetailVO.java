package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QcDetailVO")
public class QcDetailVO {
	private int qcd_num;
	private int qc_num;
	private int qcquest;
	private int qc_fail_quan;
}

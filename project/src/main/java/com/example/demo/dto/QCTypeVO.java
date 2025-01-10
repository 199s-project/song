package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("QCTypeVO")
public class QCTypeVO {
	private int qctype_num;
	private String qctype_name;	
}

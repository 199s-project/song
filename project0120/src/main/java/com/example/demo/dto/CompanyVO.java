package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("CompanyVO")
public class CompanyVO {
	private int company_num;
	private String company_name;
	private String company_busnum;
	private String company_ceo;
	private String company_phone;
	private String company_email;
	private String company_address;
}

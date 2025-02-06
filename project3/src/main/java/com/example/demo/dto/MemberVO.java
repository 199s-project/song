package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("MemberVO")
public class MemberVO {
	private int member_num;
	private String member_id;
	private String member_pwd;
	private String member_name;
	private String member_email;
	private String member_phone;
	private String member_grade;
	private String member_dept;
}

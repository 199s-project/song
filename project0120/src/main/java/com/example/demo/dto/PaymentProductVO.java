package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("PaymentProductVO")
public class PaymentProductVO {
	private int paypro_num;
	private String paypro_name;
	private int paypro_price;
	private int paypro_amount;
	private int paypro_regdate;
}

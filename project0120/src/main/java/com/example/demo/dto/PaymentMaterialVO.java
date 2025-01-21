package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("PaymentMaterialVO")
public class PaymentMaterialVO {
	private int paymat_num;
	private String paymat_name;
	private int paymat_price;
	private int paymat_amount;
	private int paymat_regdate;
}

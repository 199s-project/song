package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("InventoryVO")
public class InventoryVO {
	private int inventory_num;
	private String inventory_name;
	private int inventory_price;
	private int inventory_amount;
	private int inventory_regdate;
}

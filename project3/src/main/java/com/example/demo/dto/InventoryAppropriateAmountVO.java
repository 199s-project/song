package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("InventoryAppropriateAmountVO")
public class InventoryAppropriateAmountVO {
	private String material_name;
	private int total_material_amount;
	private int appropriate_amount;
}
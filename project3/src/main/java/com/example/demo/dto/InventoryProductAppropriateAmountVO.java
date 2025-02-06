package com.example.demo.dto;
import org.apache.ibatis.type.Alias;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Alias("InventoryProductAppropriateAmountVO")
public class InventoryProductAppropriateAmountVO {
	private String product_name;
	private int total_amount;
	private int appropriate_amount;
}
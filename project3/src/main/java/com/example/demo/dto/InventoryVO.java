package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("InventoryVO")
public class InventoryVO {
	private int inven_num;
	private int inven_item_num;
	private String inven_name;
	private int inven_type;
	private int inven_price;
	private int inven_amount;
	private int inven_appropriate_amount;
}

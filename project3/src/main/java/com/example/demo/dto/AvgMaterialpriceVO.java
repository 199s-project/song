package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("AvgMaterialpriceVO")
public class AvgMaterialpriceVO {
	private int product_num;
	private int average_material_price;
}
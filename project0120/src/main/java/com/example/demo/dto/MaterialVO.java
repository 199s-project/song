package com.example.demo.dto;



import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("MaterialVO")
public class MaterialVO {

	private int material_num;
	private String material_name;
	private String material_code;
	private String material_brand;
	private String material_category;
	private String material_subcategory;
	private String material_stat;
	private int material_price;
	private String material_img;
	// ------------------------------
	
	private int file_amount;
	private String file_name;
	private String file_path;
}

package com.example.demo.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("RecipeVO")
public class RecipeVO {
	private int recipe_num;
	private String recipe_name;
	private String product_code;
	private int recipe_price;
	
	//recipedetail
	private int rd_num;
	private String product_name;
	private int material_amount;
	private int meterial_price;
	private String material_name;
	
	private int total_material_price;
	private int total_material_amount;
	private int total_recipe_price;
	
	private int inven_amount;
	
}

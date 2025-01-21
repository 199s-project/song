package com.example.demo.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("FileVO")
public class FileVO {
	private int file_num;
	private String file_name;
	private String file_path;
	private String file_subject;
	private String file_pk;
	private List<MultipartFile> files;
}

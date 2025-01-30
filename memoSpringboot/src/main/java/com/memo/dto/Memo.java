package com.memo.dto;

import java.util.List;

import lombok.Data;

@Data
public class Memo {
	private int id;
	private String title;
	private String content;
	private List<String> imageUrls;
	private String createdDate;
	private String updatedDate;
	private int priority;
}

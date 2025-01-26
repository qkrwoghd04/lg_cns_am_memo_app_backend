package com.memo.dto;

import lombok.Data;

@Data
public class Memo {
	private int id;
	private String content;
	private String title;
	private String createdDate;
	private String updatedDate;
	private int priority;
}

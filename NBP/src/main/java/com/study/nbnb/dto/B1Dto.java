package com.study.nbnb.dto;

import java.sql.Timestamp;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class B1Dto {
	private int b1_number;
	private String writer;
	@NotNull(message="title is null.")
	@NotEmpty(message="제목을 쓰세요.")
	private String title;
	
	@NotNull(message="content is null.")
	@NotEmpty(message="내용을 쓰세요.")
	private String content;
	
	@Nullable
	private String imageurl1;
	@Nullable
	private String imageurl2;
	@Nullable
	private String imageurl3;
	private int b_like;
	private int b_dislike;
	private Timestamp time;
	private int m_number;
	
	
	public int getB1_number() {
		return b1_number;
	}
	public void setB1_number(int b1_number) {
		this.b1_number = b1_number;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageurl1() {
		return imageurl1;
	}
	public void setImageurl1(String imageurl1) {
		this.imageurl1 = imageurl1;
	}
	public String getImageurl2() {
		return imageurl2;
	}
	public void setImageurl2(String imageurl2) {
		this.imageurl2 = imageurl2;
	}
	public String getImageurl3() {
		return imageurl3;
	}
	public void setImageurl3(String imageurl3) {
		this.imageurl3 = imageurl3;
	}
	public int getB_like() {
		return b_like;
	}
	public void setB_like(int b_like) {
		this.b_like = b_like;
	}
	public int getB_dislike() {
		return b_dislike;
	}
	public void setB_dislike(int b_dislike) {
		this.b_dislike = b_dislike;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getM_number() {
		return m_number;
	}
	public void setM_number(int m_number) {
		this.m_number = m_number;
	}
	
	

}

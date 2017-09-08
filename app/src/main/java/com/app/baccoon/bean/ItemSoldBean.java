package com.app.baccoon.bean;

public class ItemSoldBean {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	private String title;
	private String image;
	private String description;
	private String mobile;
	private String buyer;
	private String price;


	public ItemSoldBean(String id, String title, String image, String description, String mobile, String buyer, String price) {
		this.id = id;
		this.title = title;
		this.image = image;
		this.description = description;
		this.mobile = mobile;
		this.buyer = buyer;
		this.price = price;
	}






}

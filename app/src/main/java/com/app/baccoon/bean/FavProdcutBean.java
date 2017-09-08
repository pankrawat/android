package com.app.baccoon.bean;

public class FavProdcutBean {

	private String pid;
	private String productName;
	private String productPrice;
	private String prod_category;
	private String productImageUrl;
	private String prod_desc;

	public FavProdcutBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc) {
		this.pid = pid;
		this.productName = productName;
		this.productPrice = productPrice;
		this.prod_category = prod_category;
		this.productImageUrl = productImageUrl;
		this.prod_fav = prod_fav;
		this.prod_location = prod_location;
		this.prod_desc = prod_desc;

	}

	private String prod_fav;
	private String prod_location;

	public String getProd_desc() {
		return prod_desc;
	}

	public void setProd_desc(String prod_desc) {
		this.prod_desc = prod_desc;
	}

	public String getProd_location() {
		return prod_location;
	}

	public void setProd_location(String prod_location) {
		this.prod_location = prod_location;
	}

	public String getProd_fav() {
		return prod_fav;
	}

	public void setProd_fav(String prod_fav) {
		this.prod_fav = prod_fav;
	}

	public String getProd_category() {
		return prod_category;
	}

	public void setProd_category(String prod_category) {
		this.prod_category = prod_category;
	}









	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}




	}
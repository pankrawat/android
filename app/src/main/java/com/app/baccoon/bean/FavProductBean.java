package com.app.baccoon.bean;

import java.io.Serializable;

public class FavProductBean implements Serializable {



	private String pid;
	private String productName;
	private String productPrice;
	private String prod_category;
	private String productImageUrl;
	private String prod_desc;

	private String prod_location;


	public String getFav_id() {
		return fav_id;
	}

	public void setFav_id(String fav_id) {
		this.fav_id = fav_id;
	}

	private String fav_id;

//	String isSold;





//	"fav_id": "1",
//
//
//			"prod_name": "zara jackect",
//			"prod_desc": "abc",
//			"prod_category": "jacket",
//			"prod_image": "abc.png",
//			"prod_price": "10000",
//			"prod_currency": "",
//
//			"prod_location": "Hauz Khas",
//
//			"isSold": "1",
//			"put_by_buyer": "0",
//			"by_dhl": "0",
//			"by_postoffice": "0"

	public FavProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String fav_id, String prod_location, String prod_desc) {
		this.pid = pid;
		this.productName = productName;
		this.productPrice = productPrice;
		this.prod_category = prod_category;
		this.productImageUrl = productImageUrl;
		this.fav_id = fav_id;
		this.prod_location = prod_location;
		this.prod_desc = prod_desc;

	}



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



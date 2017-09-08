package com.app.baccoon.bean;

import java.io.Serializable;

public class FavouriteBean implements Serializable {

	private String pid;
	private String productName;
	private String productPrice;
	private String prod_category;
	private String productImageUrl;
	private String prod_desc;


	private String Post_Price="0";
	private String DHL_Price="0";
	private String isPost="0";
	private String isDHL="0";
	private String isBuyer="0";
	private String prod_currency;
	private String prod_fav;
	private String prod_location;
	private String noOfLikes;

	public String getNoOfLikes() {
		return noOfLikes;
	}

	public void setNoOfLikes(String noOfLikes) {
		this.noOfLikes = noOfLikes;
	}

	public String getProd_currency() {
		return prod_currency;
	}

	public void setProd_currency(String prod_currency) {
		this.prod_currency = prod_currency;
	}

	public String getIsBuyer() {
		return isBuyer;
	}

	public void setIsBuyer(String isBuyer) {
		this.isBuyer = isBuyer;
	}

	public String getIsDHL() {
		return isDHL;
	}

	public void setIsDHL(String isDHL) {
		this.isDHL = isDHL;
	}

	public String getIsPost() {
		return isPost;
	}

	public void setIsPost(String isPost) {
		this.isPost = isPost;
	}

	public String getDHL_Price() {
		return DHL_Price;
	}

	public void setDHL_Price(String DHL_Price) {
		this.DHL_Price = DHL_Price;
	}

	public String getPost_Price() {
		return Post_Price;
	}

	public void setPost_Price(String post_Price) {
		Post_Price = post_Price;
	}







	public FavouriteBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc,
						 String Post_Price, String DHL_Price, String isPost, String isDHL, String isBuyer, String prod_currency, String noOfLikes) {
		this.pid = pid;
		this.productName = productName;
		this.productPrice = productPrice;
		this.prod_category = prod_category;
		this.productImageUrl = productImageUrl;
		this.prod_fav = prod_fav;
		this.prod_location = prod_location;
		this.prod_desc = prod_desc;
		this.Post_Price=Post_Price;
		this.DHL_Price=DHL_Price;
		this.isBuyer=isBuyer;
		this.isDHL=isDHL;
		this.isPost=isPost;
		this.noOfLikes=noOfLikes;


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
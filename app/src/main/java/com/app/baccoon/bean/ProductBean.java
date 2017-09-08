package com.app.baccoon.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ProductBean implements Serializable {

	private String pid;
	private String productName;
	private String productPrice;
	private String prod_category;
	private String productImageUrl;
	private String prod_desc;
	private int make_offer;

	private String Post_Price="0";
	private String DHL_Price="0";
	private String isPost="0";
	private String isDHL="0";
	private String isBuyer="0";
	private String prod_currency="";
	private String citrus_json="";

	public String getIsSold() {
		return isSold;
	}
	public void setIsSold(String isSold) {
		this.isSold = isSold;
	}
	private String isSold="0";
	private String prod_location;
	private String noOfLikes;
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	private String sellerId;
	private int isLiked=0;
	private int isFav=0;
	public int getMake_Offer() {
		return make_offer;
	}

	public void setMake_offer(int make_offer) {
		this.make_offer = make_offer;
	}



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







	public ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_location, String prod_desc,
					   String Post_Price,String DHL_Price,String isPost, String isDHL,String isBuyer, String prod_currency,
					   String noOfLikes, int isLiked, int isFav, String sellerId, String isSold,int makeOffer) {
		this.pid = pid;
		this.productName = productName;
		this.productPrice = productPrice;
		this.prod_category = prod_category;
		this.productImageUrl = productImageUrl;
		this.prod_location = prod_location;
		this.prod_desc = prod_desc;
		this.Post_Price=Post_Price;
		this.DHL_Price=DHL_Price;
		this.isBuyer=isBuyer;
		this.isDHL=isDHL;
		this.isPost=isPost;
		this.noOfLikes=noOfLikes;
		this.isLiked=isLiked;
		this.isFav=isFav;
		this.sellerId=sellerId;
		this.isSold=isSold;
		this.make_offer=makeOffer;

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


	public int getIsFav() {
		return isFav;
	}

	public void setIsFav(int isFav) {
		this.isFav = isFav;
	}

	public int getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(int isLiked) {
		this.isLiked = isLiked;
	}
}
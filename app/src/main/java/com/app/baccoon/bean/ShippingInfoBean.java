package com.app.baccoon.bean;

public class ShippingInfoBean {
	private String ship_id;
	private String user_id;
	private String shipTo;
	private String shipAddress;
	private String shipCity;
	private String shipState;
	private String shipCountry;
	private String shipPhone;
	private String shipPincode;

	public ShippingInfoBean(String ship_id, String user_id, String shipTo, String shipAddress, String shipCity, String shipState, String shipCountry, String shipPhone, String shipPincode) {
		this.ship_id = ship_id;
		this.user_id = user_id;
		this.shipTo = shipTo;
		this.shipAddress = shipAddress;
		this.shipCity = shipCity;
		this.shipState = shipState;
		this.shipCountry = shipCountry;
		this.shipPhone = shipPhone;
		this.shipPincode = shipPincode;
	}




	public String getShip_id() {
		return ship_id;
	}

	public void setShip_id(String ship_id) {
		this.ship_id = ship_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public String getShipPhone() {
		return shipPhone;
	}

	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}

	public String getShipPincode() {
		return shipPincode;
	}

	public void setShipPincode(String shipPincode) {
		this.shipPincode = shipPincode;
	}








}

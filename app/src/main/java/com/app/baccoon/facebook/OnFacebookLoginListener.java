package com.app.baccoon.facebook;



public interface OnFacebookLoginListener {
	
	public void onFacebookLogin(String name, String email, String id, String imageUrl);
	public void onError(String message);
	/*//public void onFacebookFriends(ArrayList<ContactBean> fbFriends);
*/}

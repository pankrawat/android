package com.app.baccoon.utils;

import java.io.File;

/**
 * Created by gopalgupta on 23/02/16.
 */
public class API {

    public static String Make_Chat_Read ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/makeChatStatusRead";
    public static String Chat_Service_Reply ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/chatServiceReply";
    public static String URL="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/";
    public static String BASEURL="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/";
    public static String Signup="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/signup";
    public static String Login="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/login";
    public static String ForgotPassword="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/forgetPassword";
    public static String Home_screen="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/homeScreenNew";
    public static String Search="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productSearchNew";
    public static String Item_Sold="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userSoldProduct";
    public static String Seller_Contact="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userBuyProduct";
    public static String Shipping_Info="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userShipAddress";
    public static String Add_Shiping_Address="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/shipAddressInsertByUser";
    public static String Edit_Shiping_Address="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/editShippingAddress";
    public static String Delete_Shiping_Address ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/deleteShippingAddress";
    public static String Favorite_Product_List="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userFavProduct";
    public static String Favorite_Product_List_Search="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userFavProduct";
    public static String Create_Sell ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productInsert";
    public static String Make_Offer ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/chatService";
    public static String Update_Billing_Info ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userUpdateBilling";
    public static String Hit_Product_Favourite ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/addUserFavProduct";
    public static String Hit_Product_Like ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/addLikeToProduct";
    public static String Hit_Product_unFavourite ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/delUserFavProduct";
    public static String Hit_Product_unLike ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/delLikeToProduct";
    public static String Payment_Details_Send ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userPaymentDetail/";
    public static String Payment_Details_Send_Custom ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userWalletDetail/";
    public static String Get_All_Msg ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/userChatHistory";
    public static String ConvertMoney ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/currencyConvert";
    public static String Update_Wallet ="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/updateUserWalletDetail";
    public static String ImageUpload="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/profileImageUpload";
    public static String MyProducts="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/ProductDetails";
    public static String ProductDetails="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/";
    public static String Product_Sold_Out="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productSoldOut";
    public static String DeleteMyProduct=BASEURL+"deleteProduct";
    public static String UpdateMyProduct=BASEURL+"productUpdate";
    public static String GetNotificationList=BASEURL+"getFavProductNotification";
}


package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.fragment.PaymentFullMembershipFragment;
import com.app.baccoon.utils.CreditCardValidation;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.ToastUtil;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.Environment;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.classes.Month;
import com.citrus.sdk.classes.Year;
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.widgets.CardNumberEditText;
import com.citrus.widgets.ExpiryDate;
import com.google.android.gms.analytics.ecommerce.Product;

/**
 * Created by apps on 8/11/16.
 */


public class Citrus_Card_Payement extends Activity implements View.OnClickListener {
    private CardNumberEditText editCardNumber = null;
    private ExpiryDate editExpiryDate = null;
    private EditText editCVV;
    private EditText editCardHolderName;
    private static EditText cardHolderNickName;
  //  private CardNumberEditText cardHolderNumber;
    private TextView submitButton ;

    private CitrusClient citrusClient = null;
    private String BILL_URL="http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/bill.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citrus_screen);
        init();

    }

    private void init() {
        editCardNumber = (CardNumberEditText) findViewById(R.id.cardHolderNumber);
        editExpiryDate = (ExpiryDate) findViewById(R.id.cardExpiry);
        editCardHolderName = (EditText) findViewById(R.id.cardHolderName);
        cardHolderNickName = (EditText) findViewById(R.id.cardHolderNickName);
       // cardHolderNumber=(CardNumberEditText)findViewById(R.id.cardHolderNumber);
        editCVV = (EditText) findViewById(R.id.cardCvv);
        submitButton = (TextView) findViewById(R.id.load);
        submitButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        String cardHolderName = editCardHolderName.getText().toString();
        String cardNumber = editCardNumber.getText().toString();
        String cardCVV = editCVV.getText().toString();
        Month month = editExpiryDate.getMonth();
        Year year = editExpiryDate.getYear();
        Log.e("Card Number was",""+cardNumber);
       cardNumber= cardNumber.replaceAll("\\s+", "");
        Log.e("Card Number is", "" + cardNumber);
        if(v.getId()==R.id.load)
        {
            CreditCardValidation o =new CreditCardValidation();

            if(o.isValid(Long.parseLong(cardNumber)))
            {
                citrusClient = CitrusClient.getInstance(this); // Activity Context
                citrusClient.enableLog(true);

                citrusClient.init(
                        "p1lpjna4th-signup",
                        "352d6e99050b9567e3f9fc9fb6b78eeb",
                        "p1lpjna4th-signin",
                        "399f7137c1a7ec02f9f6f14b126ac656",
                        "p1lpjna4th",
                        Environment.SANDBOX);

                CreditCardOption creditCardOption = new CreditCardOption(cardHolderName, cardNumber, cardCVV, month, year);
           //     CreditCardOption creditCardOption = new CreditCardOption("Card Holder Name", "4111111111111111", "123", Month.getMonth("12"), Year.getYear("18"));

               Amount amount = new Amount("1");
                // Init PaymentType
                PaymentType.PGPayment pgPayment = null;
                try {
                    pgPayment = new PaymentType.PGPayment(amount, BILL_URL, creditCardOption, new CitrusUser("siddharth.sharma42@gmail.com","8091328938"));
                } catch (CitrusException e) {
                    e.printStackTrace();
                }

                citrusClient.simpliPay(pgPayment, new Callback<TransactionResponse>() {

                    @Override
                    public void success(TransactionResponse transactionResponse) {


                        Log.e("Transaction", " Done" + transactionResponse);
                        SharedPreference.getInstance(Citrus_Card_Payement.this).putString("citrus_json", transactionResponse.getJsonResponse());
                      //  SharedPreference.getInstance(Citrus_Card_Payement.this).putString("citrus_json",transactionResponse.get);
                      //  SharedPreference.getInstance(Citrus_Card_Payement.this).putString("citrus_json",transactionResponse.getTransactionId());
                       // ProductBean.setjson(transactionResponse.toString());


                        Intent intent = new Intent();
                        setResult(50,intent);
                        finish();
                    }

                    @Override
                    public void error(CitrusError error) {
                            finish();
                        ToastUtil.showLongSnackBar(getCurrentFocus(),"Transaction Failed");
                        Log.e("Error", " " + error.toString());
                    }
                });

            }
            else
            {
                ToastUtil.showShortToast(this,"Please enter valid card details");
            }
        }

    }

   /* public static boolean Check(String ccNumber)
   5555555555554444
5105105105105100

    {
        int sum = 0;
        boolean alternate = false;

        if(ccNumber.isEmpty() || ccNumber.length()<10 &&  cardHolderNickName.getText().toString().isEmpty())
            return false;

        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
*/


}




/* public static boolean Mod10Check(String creditCardNumber)
    {
        //// check whether input string is null or empty
        if (creditCardNumber.isEmpty())
        {
            return false;
        }

        //// 1.	Starting with the check digit double the value of every other digit
        //// 2.	If doubling of a number results in a two digits number, add up
        ///   the digits to get a single digit number. This will results in eight single digit numbers
        //// 3. Get the sum of the digits
        int sum=0;

        //// If the final sum is divisible by 10, then the credit card number
        //   is valid. If it is not divisible by 10, the number is invalid.
        return sumOfDigits % 10 == 0;
    }*/
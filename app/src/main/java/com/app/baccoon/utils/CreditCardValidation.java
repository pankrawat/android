package com.app.baccoon.utils;


import java.util.Scanner;

import android.util.Log;
public class CreditCardValidation {

    /**
     * @param args the command line arguments
     */
	// card type= visa, master, american express
	private String cardType;
	
	private int type=0;
	

    

	public String getCardType() {
		if(type==1)
		{
		setCardType("AMERICAN");
		}
		else if(type==2)
		{
			setCardType("VISA");
		}
		else if(type==3)
		{
			setCardType("MASTER");
		}
		else
		{
			setCardType("NONE");
		}
	return cardType;
}

public void setCardType(String cardType) {
	this.cardType = cardType;
}

	public boolean isValid(long number) {

        int total = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number);

        if ((total % 10 == 0) && (prefixMatched(number, 1) == true)) {
            return true;
        } else {
            return false;
        }
    }

    public int getDigit(int number) {

        if (number <= 9) {
            return number;
        } else {
            int firstDigit = number % 10;
            int secondDigit = (int) (number / 10);

            return firstDigit + secondDigit;
        }
    }

    public int sumOfOddPlace(long number) {
        int result = 0;

        while (number > 0) {
            result += (int) (number % 10);
            number = number / 100;
        }

        return result;
    }

    public int sumOfDoubleEvenPlace(long number) {

        int result = 0;
        long temp = 0;

        while (number > 0) {
            temp = number % 100;
            result += getDigit((int) (temp / 10) * 2);
            number = number / 100;
        }

        return result;
    }

    public boolean prefixMatched(long number, int d) {

        if ((getPrefix(number, d) == 3)
                || (getPrefix(number, d) == 4)
                || (getPrefix(number, d) == 5)
            //    || (getPrefix(number, d) == 6)
                ) {

            if (getPrefix(number, d) == 3) {
        //setCardType("AI");
            type=1;
               Log.e("card type","American Express Card ! ");
            } else if (getPrefix(number, d) == 4) {
            	type=2;
            	 Log.e("card type","Visa Card ! ");
            } else if (getPrefix(number, d) == 5) {
            	type=3;
                System.out.println("\nMaster Card !");
            }
//            else if (getPrefix(number, d) == 6) {
//                System.out.println("\nDiscover Card !");
//            }

            return true;
        
        } else {

            return false;

        }
    }

    public int getSize(long d) {

        int count = 0;

        while (d > 0) {
            d = d / 10;

            count++;
        }

        return count;

    }

    /**
     * Return the first k number of digits from number. If the number of digits
     * in number is less than k, return number.
     */
    public long getPrefix(long number, int k) {

        if (getSize(number) < k) {
            return number;
        } else {

            int size = (int) getSize(number);

            for (int i = 0; i < (size - k); i++) {
                number = number / 10;
            }

            return number;

        }

    }
//
//    public void main(String[] args) {
//
//        Scanner//
//  public void main(String[] args) {
//
//      Scanner sc = new Scanner(System.in);
//
//      System.out.print("Enter your Card Number : ");
//
//      long input = sc.nextLong();
//
//      if (isValid(input) == true) {
//          System.out.println("\n*****Your card is Valid*****");
//      } else {
//          System.out.println("\n!!!!Your Card is not Valid !!!!! ");
//      }
//
//  } sc = new Scanner(System.in);
//
//        System.out.print("Enter your Card Number : ");
//
//        long input = sc.nextLong();
//
//        if (isValid(input) == true) {
//            System.out.println("\n*****Your card is Valid*****");
//        } else {
//            System.out.println("\n!!!!Your Card is not Valid !!!!! ");
//        }
//
//    }
}
package com.app.baccoon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator{

	private Pattern pattern;
	  private Matcher matcher;
 
	  private static final String PASSWORD_PATTERN =
			  "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@.!]).{8,15})";



//	"^.*(?=.{6,})(?=.*[a-z])(?=.*[A-Z]).*$"

	//       "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()-+_=.}\\{`|\"]).{8,15})";
	        
	  public PasswordValidator(){
		  pattern = Pattern.compile(PASSWORD_PATTERN);
	  }
	  
	  /**
	   * Validate password with regular expression
	   * @param password password for validation
	   * @return true valid password, false invalid password
	   */
	  public boolean validate(final String password){
		  
		  matcher = pattern.matcher(password);
		  return matcher.matches();
	    	    
	  }
}

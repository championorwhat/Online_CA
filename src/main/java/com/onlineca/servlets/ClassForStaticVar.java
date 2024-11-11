package com.onlineca.servlets;

public class ClassForStaticVar {
	
	private static String emailID;
	
	public static String getEmailID() {
		
		return emailID;
	}
	
	public static void setEmailID(String emailID) {
		
		ClassForStaticVar.emailID = emailID;
	}	

}

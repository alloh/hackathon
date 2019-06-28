package com.saf.hackathon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String PHONE_NUMBER_PATTERN = "[0-9]?\\d{9,12}";
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_\\*{}\":;.,<>?/-`~])(?=\\S+$).{8,}$";

	public static boolean isValidEmail(final String input) {
		final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher;
		matcher = pattern.matcher(input);
		return matcher.matches();
	}

	public static boolean isValidPasword(String password) {
	
		if (password.matches(PASSWORD_PATTERN)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidPhoneNumber(String number) {
		final Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
		final Matcher match = pattern.matcher(number);
		if (match.matches() && number.startsWith("07") && number.length() == 10
				|| match.matches() && number.startsWith("7") && number.length() == 9
				|| match.matches() && number.startsWith("254") && number.length() == 12) {
			return true;
		}

		return false;
	}

}
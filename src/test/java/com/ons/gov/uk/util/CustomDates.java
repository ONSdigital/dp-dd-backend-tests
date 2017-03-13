package com.ons.gov.uk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CustomDates {

	public static String getTomorrowsDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Now use today date.
		c.add(Calendar.DATE, 1); // Adding 1 day
		return sdf.format(c.getTime());

	}
}

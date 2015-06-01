package com.orasi.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeConversion {

	public DateTimeConversion() {
		// TODO Auto-generated constructor stub
	}
	
	public static String convert(String date, String fromFormat, String toFormat){
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat,Locale.ENGLISH);
		Date parsedDate = null;
		try {
		    parsedDate = dateFormat.parse(date);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		return convert(parsedDate,toFormat);
	}
	
	public static String convert(Date date, String toFormat){
	    SimpleDateFormat dateFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
	    return dateFormat.format(date);
	}
	
	public static String getDaysOut(String daysOut, String format){
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
	
	
	@Deprecated
	public static String ConvertToDate(String daysOut){
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
	
	@Deprecated
	public String ConvertToDateMMDDYY(String daysOut){
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
	
	@Deprecated
	public static String format(String date, String format){
		return new SimpleDateFormat(format).format(date);
	}


	@Deprecated
	public String ConvertToDateYYYYMMDD(String daysOut){
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
}

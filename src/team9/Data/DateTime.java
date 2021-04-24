package team9.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime extends Date{ 
 
	private static final long serialVersionUID = -2349117123477060163L;

	private DateTime(Date date) {
		this.setTime(date.getTime());
	}
	
	/** 날짜와 시간이 모두 적혀있는 문자열을 파싱하여 객체로 만듭니다. */
	public static DateTime parseDateTime(String string) throws ParseException {

		var format = new SimpleDateFormat("yy-MM-dd hh:mm"); 
		
		return new DateTime(format.parse(string));
	}
	
	/** 날짜만 적혀있는 문자열을 파싱하여 객체로 만듭니다. */
	public static DateTime parseDate(String string) throws ParseException {
		SimpleDateFormat format;
		
		if(string.length() == 6) {
			format = new SimpleDateFormat("yyMMdd");
		}
		else if(string.length() == 8) {
			format = new SimpleDateFormat("yyyyMMdd");
		}
		else {
			throw new ParseException("파싱 오류", -1);
		}
		return new DateTime(format.parse(string)); 
	} 
	
	@Override
	public String toString() {
		return toString(false); 
	}
	
	/** 날짜만 적혀있는 문자열을 파싱하여 객체로 만듭니다.
	 * @param showOnlyDate 날짜만 표시할지에 대한 여부입니다.  
	 */
	public String toString(boolean showOnlyDate) {
		SimpleDateFormat format;
		 
		if(showOnlyDate) {
			format = new SimpleDateFormat("yyyy년 MM월 dd일"); 
		}
		else { 
			format = new SimpleDateFormat("yy-MM-dd hh:mm");
		}
		return format.format(this); 
	} 
}

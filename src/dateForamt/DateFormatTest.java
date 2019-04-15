package dateForamt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatTest {

	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>(){

		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};
	
	public Date convert(String source) throws ParseException {
		Date d = df.get().parse( source );
		return d;
	}
}

package java8;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class TestCase009 {
	public static void main( String[] args ) {
		TestCase009 tester = new TestCase009();
		tester.testLocalDateTime();
	}
	
	public void testLocalDateTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		System.out.println( "currnet datetime : " + currentTime );
		
		LocalDate date1 = currentTime.toLocalDate();
		System.out.println( "date1: " + date1 );
		
		Month month = currentTime.getMonth();
		int day = currentTime.getDayOfMonth();
		int seconds = currentTime.getSecond();
		
		System.out.println( " Month: " + month.getValue() + " day : " + day + " seconds : " + seconds );
		
		LocalDateTime date2 = currentTime.withDayOfMonth(10).withYear(2012);
		System.out.println( "date2: " + date2 );
		
		LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
		System.out.println( "date3 : " + date3 );
		
		LocalTime date4 = LocalTime.of( 22, 15 );
		System.out.println( "date4: " + date4 );
		
		LocalTime date5 = LocalTime.parse( "20:15:30" );
		System.out.println( "date5 : " + date5 );
		
		LocalDate today = LocalDate.now();
		System.out.println( "current date: " + today);
		
		LocalDate nextWeek = today.plus( 1, ChronoUnit.WEEKS );
		System.out.println( "next week : " + nextWeek );
		
		LocalDate today2 = today.plus( 1, ChronoUnit.MONTHS );
		Period period = Period.between( today2,  today );
		System.out.println( " Period : " + period );
		
		
		LocalDate nextTuesday = today.with( TemporalAdjusters.next(DayOfWeek.TUESDAY));
		System.out.println( "Next tuesday on : " + nextTuesday );
	}
}

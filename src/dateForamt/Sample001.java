package dateForamt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Sample001 {
	public static void main( String[] args ) throws InterruptedException, ExecutionException {
		
		final DateFormatTest t = new DateFormatTest();
		Callable<Date> task = new Callable<Date>(){
			public Date call() throws Exception {
				return t.convert("20100811");
			}
		};
		
		//lets try 2 threads only
		ExecutorService exec = Executors.newFixedThreadPool( 2 );
		List<Future<Date>> results = new ArrayList<Future<Date>>();
		
		// perform 5 date conversions
		for ( int i=0; i<5; i++ ) {
			results.add( exec.submit( task ) );
		}
		
		exec.shutdown(); 
		
		for ( Future<Date> result : results ) {
			System.out.println( result.get() );
		}
	}
}

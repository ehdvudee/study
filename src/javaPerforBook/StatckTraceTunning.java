package javaPerforBook;

import org.junit.Test;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode({ Mode.AverageTime })
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class StatckTraceTunning {

    public static void main( String[] args ) throws IOException, RunnerException {
        Main.main( args );
    }

    public void goOn() {
        try {
            try {
                try {
                    throw new NullPointerException( "null Exp" );
                } catch ( Exception e ) {
                    throw new IllegalArgumentException("illArgu Exp", e);
                }
            } catch ( Exception e ) {
                throw new AuthenticationException( "auth Exp", e );
            }
        } catch ( Exception e ) {
            throw new RuntimeException( "Run Exp", e );
        }
    }

    @Benchmark
    public void sample000() {
        try {
            goOn();
        } catch ( Throwable e ) {
            System.out.println( e.getStackTrace() );
        }
    }

    @Benchmark
    public void sample001() {
        try {
            goOn();
        } catch ( Throwable e ) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void sample002() {
        try {
            goOn();
        } catch ( Throwable e ) {
            for ( StackTraceElement ste : e.getStackTrace() ) {
                // System.out -> loger.error()
                System.out.println( e.getMessage() );
                System.out.println(
                        ste.getClassName() + "." +
                        ste.getMethodName() + " " +
                        ste.getLineNumber() + " " +
                        ste.getFileName() );
            }
        }

    }

    @Benchmark
    public void sample003() {
        try {
            goOn();
        } catch ( Throwable e ) {
            StringBuilder sb = storeErrInfo( new StringBuilder(), e );
            System.out.println( sb.toString() );
        }
    }

    private StringBuilder storeErrInfo( StringBuilder sb, Throwable e ) {
        if ( e != null ) {
            StackTraceElement[] ste = e.getStackTrace();
            sb.append( ste[0].getFileName() ).append( ":" )
                    .append( ste[0].getClass() ).append( "." )
                    .append( ste[0].getMethodName() ).append( " " )
                    .append( ste[0].getLineNumber() ).append( "\n");

            storeErrInfo( sb, e.getCause() );
        }

        return sb;
    }

}

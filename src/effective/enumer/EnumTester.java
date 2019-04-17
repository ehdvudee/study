package effective.enumer;

public class EnumTester {
    public static void main(String[] args) {
        System.out.println( Operation.DIVIDE.apply( 10, 5 ) );
        System.out.println( Operation.PLUS.apply( 10, 5 ) );


        System.out.println( Operation2.MEGAPLUS.apply( 10, 15 ) );
        System.out.println( Operation2.MEGAPLUS );
        for ( Operation2 oper : Operation2.values() ) {
            System.out.printf( "출발 선수 : %s  : %d %s %d =  %f %n", oper.name(), 10, oper, 15, oper.apply( 10, 15 ) );
        }

        System.out.println( PayrollDay.FRIDAY.pay( 10, 5000 ) );
    }
}

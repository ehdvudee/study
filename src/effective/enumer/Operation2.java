package effective.enumer;

public enum Operation2 {
    MEGAPLUS( "+" ) {
      double apply( double x, double y ) {
          double ret = x + x + y + x;

          System.out.print( "출력한다다아아앗: ");
          System.out.println( ret );

          return ret;
      }
    },
    MEGAMINUS( "-" ) {
        double apply( double x, double y ) {
            double ret = x - y - y - x;

            System.out.print( "출력하죠죠오오옹 : " );
            System.out.println( ret );

            return ret;
        }
    };

    private final String symbol;
    Operation2( String symbol ) { this.symbol = symbol; }

    @Override
    public String toString() {
        return symbol;
    }

    abstract double apply( double x, double y );
}

package effective.enumer;

public class WeightTable {
    public static void main(String[] args) {
        String input = "30";

        double earthWeight = Double.parseDouble( input );
        double mass = earthWeight / Planet.EARTH.surfaceGravity();

        for ( Planet p : Planet.values() ) {
            System.out.println( p  + " " + p.surfaceWeight( mass ) );
        }
    }
}

package effective.enumer;

public enum Planet {
    MERCURY( 1.302E+23, 2.439E6 ),
    VENUS( 2.302E+23, 2.439E6 ),
    EARTH( 3.302E+23, 2.439E6 ),
    MARS( 4.302E+23, 2.439E6 ),
    JUPITER( 5.302E+23, 2.439E6 ),
    SATURN( 6.302E+23, 2.439E6 ),
    URANUS( 7.302E+23, 2.439E6 );

    private final double mass;
    private final double radius;
    private double surfaceGravity;

    private static final double G = 6.67300E-11;

    Planet( double mass, double radius ) {
        this.mass = mass;
        this.radius = radius;
        this.surfaceGravity = G * mass / ( radius * radius );
    }

    public double mass() { return mass; }
    public double radius() { return radius; }
    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight( double mass ) {
        return mass * surfaceGravity;
    }
}

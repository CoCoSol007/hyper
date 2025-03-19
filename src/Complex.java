package src;

/// A class for complex numbers
public class Complex {

    /// real part of the complex number
    private final double re;

    /// imaginary part of the complex number
    private final double im;

    /// 1 + 0i
    public static Complex ONE = new Complex(1, 0);

    /// 0 + 0i
    public static Complex ZERO = new Complex(0, 0);

    /// 0 + 1i
    public static Complex I = new Complex(0, 1);

    /// -1 + 0i
    public static Complex MINUS_ONE = new Complex(-1, 0);

    /// 0 - 1i
    public static Complex MINUS_I = new Complex(0, -1);


    /// create a new object with the given real and imaginary parts
    public Complex(double real, double image) {
        re = real;
        im = image;
    }

    /// return a new Complex object whose value is r*e^(i*theta)
    public static Complex exponent(double r, double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    /// return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /// return modulus
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /// return argument, normalized to be between 0 and 2pi
    public double arg() {
        return Math.atan2(im, re) + Math.PI;
    }

    /// return a + b
    public Complex plus(Complex b) {
        Complex a = this;
        double real = a.re + b.re;
        double image = a.im + b.im;
        return new Complex(real, image);
    }

    /// return a - b
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double image = a.im - b.im;
        return new Complex(real, image);
    }

    /// return a * b
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double image = a.re * b.im + a.im * b.re;
        return new Complex(real, image);
    }

    /// return a * alpha with alpha a real number
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /// return the complex conjugate
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    /// return the multiplicative inverse
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    /// return the real part
    public double re() { return re; }

    /// return the imaginary part
    public double im() { return im; }

    /// return a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    /// return true if the complex numbers are equal
    public boolean equals(Complex x) {
        return (this.re == x.re) && (this.im == x.im);
    }
}

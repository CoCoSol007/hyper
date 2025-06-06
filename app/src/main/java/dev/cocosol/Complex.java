/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

/**
 * A class representing complex numbers.
 * 
 * This class provides various operations for complex numbers such as addition,
 * subtraction, multiplication,
 * division, and the calculation of modulus, argument, conjugate, and
 * reciprocal.
 */
public class Complex {

    /**
     * 1 + 0i, the multiplicative identity for complex numbers.
     */
    public static final Complex ONE = new Complex(1, 0);

    /**
     * 0 + 0i, the additive identity for complex numbers.
     */
    public static final Complex ZERO = new Complex(0, 0);

    /**
     * 0 + 1i, represents the imaginary unit.
     */
    public static final Complex I = new Complex(0, 1);

    /**
     * -1 + 0i, represents the negative real unit.
     */
    public static final Complex MINUS_ONE = new Complex(-1, 0);

    /**
     * 0 - 1i, represents the negative imaginary unit.
     */
    public static final Complex MINUS_I = new Complex(0, -1);

    /**
     * The real part of the complex number.
     */
    private final double re;

    /**
     * The imaginary part of the complex number.
     */
    private final double im;

    /**
     * Constructs a new complex number with the specified real and imaginary parts.
     *
     * @param real  the real part of the complex number
     * @param image the imaginary part of the complex number
     */
    public Complex(final double real, final double image) {
        this.re = real;
        this.im = image;
    }

    /**
     * Returns a new Complex number representing r * e^(i * theta).
     *
     * @param r     the modulus (magnitude) of the complex number
     * @param theta the argument (angle) of the complex number
     * @return a new Complex number
     */
    public static Complex exponent(final double r, final double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Returns a string representation of the invoking Complex number.
     *
     * @return a string representation of the complex number
     */
    @Override
    public String toString() {
        if (this.im == 0) {
            return this.re + "";
        }
        if (this.re == 0) {
            return this.im + "i";
        }
        if (this.im < 0) {
            return this.re + " - " + (-this.im) + "i";
        }
        return this.re + " + " + this.im + "i";
    }

    /**
     * Returns the modulus (magnitude) of the complex number.
     *
     * @return the modulus of the complex number
     */
    public double module() {
        return Math.sqrt(this.re * this.re + this.im * this.im);
    }

    /**
     * Returns the argument (angle) of the complex number.
     *
     * @return the argument of the complex number
     */
    public double getAngle() {
        return Math.atan2(this.im, this.re);
    }

    /**
     * Adds the current complex number to another complex number.
     *
     * @param b the complex number to add
     * @return the sum of the two complex numbers
     */
    public Complex plus(final Complex b) {
        final Complex a = this;
        final double real = a.re + b.re;
        final double image = a.im + b.im;
        return new Complex(real, image);
    }

    /**
     * Subtracts another complex number from the current complex number.
     *
     * @param b the complex number to subtract
     * @return the difference between the two complex numbers
     */
    public Complex minus(final Complex b) {
        final Complex a = this;
        final double real = a.re - b.re;
        final double image = a.im - b.im;
        return new Complex(real, image);
    }

    /**
     * Multiplies the current complex number by another complex number.
     *
     * @param b the complex number to multiply with
     * @return the product of the two complex numbers
     */
    public Complex times(final Complex b) {
        final Complex a = this;
        final double real = a.re * b.re - a.im * b.im;
        final double image = a.re * b.im + a.im * b.re;
        return new Complex(real, image);
    }

    /**
     * Scales the current complex number by a real number.
     *
     * @param alpha the real number to scale by
     * @return a new complex number scaled by alpha
     */
    public Complex scale(final double alpha) {
        return new Complex(alpha * this.re, alpha * this.im);
    }

    /**
     * Returns the complex conjugate of the current complex number.
     *
     * @return the complex conjugate
     */
    public Complex conjugate() {
        return new Complex(this.re, -this.im);
    }

    /**
     * Returns the multiplicative inverse (reciprocal) of the current complex
     * number.
     *
     * @return the reciprocal of the complex number
     */
    public Complex reciprocal() {
        final double scale = this.re * this.re + this.im * this.im;
        return new Complex(this.re / scale, -this.im / scale);
    }

    /**
     * Returns the real part of the complex number.
     *
     * @return the real part of the complex number
     */
    public double re() {
        return this.re;
    }

    /**
     * Returns the imaginary part of the complex number.
     *
     * @return the imaginary part of the complex number
     */
    public double im() {
        return this.im;
    }

    /**
     * Divides the current complex number by another complex number.
     *
     * @param b the complex number to divide by
     * @return the result of dividing the two complex numbers
     */
    public Complex divides(final Complex b) {
        final Complex a = this;
        return a.times(b.reciprocal());
    }

    /**
     * Compares the current complex number with another complex number for equality.
     *
     * @param x the complex number to compare with
     * @return true if the complex numbers are equal, false otherwise
     */
    public boolean equals(final Complex x) {
        return this.re == x.re && this.im == x.im;
    }
}

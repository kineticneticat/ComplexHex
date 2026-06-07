package dev.kineticcat.complexhex.stuff;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.utils.HexUtils;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.FunniesKt;
import dev.kineticcat.complexhex.api.casting.iota.ComplexNumberIota;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ComplexNumber {
    private static final Logger LOGGER = LogManager.getLogger(Complexhex.MOD_ID);
    public double real;
    public double imag;
    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    @Override
    public String toString() {
        return real + " + " + imag + "i";
    }

    public static ComplexNumber polar(double mod, double arg) {
        return new ComplexNumber(Math.cos(arg), Math.sin(arg)).mul(mod);
    }
    public static ComplexNumber fixNaN(ComplexNumber cn) {
        return new ComplexNumber(HexUtils.fixNAN(cn.real), HexUtils.fixNAN(cn.imag));
    }
    public ComplexNumberIota asIota() { return new ComplexNumberIota(this);}
    public ComplexNumber add(ComplexNumber B) {
        return new ComplexNumber(this.real + B.real, this.imag + B.imag);
    }
    public ComplexNumber add(double B) {
        return new ComplexNumber(this.real + B, this.imag);
    }
    public ComplexNumber sub(ComplexNumber B) {
        return new ComplexNumber(this.real - B.real, this.imag - B.imag);
    }
    public ComplexNumber sub(double B) {
        return new ComplexNumber(this.real - B, this.imag);
    }
    public ComplexNumber mul(double S) {
        return new ComplexNumber(this.real * S, this.imag * S);
    }
    public ComplexNumber scalarDiv(double S) {
        return new ComplexNumber(this.real / S, this.imag / S);
    }
    public ComplexNumber mul(ComplexNumber B) {
        return new ComplexNumber(
            this.real*B.real - this.imag * B.imag,
            this.real*B.imag + this.imag * B.real
        );
    }
    public ComplexNumber pow(ComplexNumber B) {
        double r = this.modulus();
        double theta = this.argument();return FunniesKt.CNpow(Math.E,
                B.mul(new ComplexNumber(Math.log(r), theta))
        );
    }
    public ComplexNumber pow(Double B) {
        double r = this.modulus();
        double theta = this.argument();
        return ComplexNumber.polar(Math.pow(r,B), B*theta);
    }
    public ComplexNumber conjugate() { return  new ComplexNumber(this.real, -this.imag);}
    public double modulus() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imag, 2));
    }
    public double argument() {
        return Math.atan2(this.imag, this.real);
    }
    public List<Iota> asActionResult() {
        return List.of(new ComplexNumberIota(this));
    }
}

package net.complexhex.stuff;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.complexhex.Complexhex;
import net.complexhex.api.casting.iota.complex.ComplexNumberIota;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class ComplexNumber {
    private static final Logger LOGGER = LogManager.getLogger(Complexhex.MOD_ID);
    public double real;
    public double imag;
    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }
    public static ComplexNumber eToTheIPI(double mod, double arg) {
        return new ComplexNumber(Math.cos(arg), Math.sin(arg)).scalarMul(mod);
    }
    public static ComplexNumber fixNaN(ComplexNumber cn) {
        return new ComplexNumber(HexUtils.fixNAN(cn.real), HexUtils.fixNAN(cn.imag));
    }

    public ComplexNumber add(ComplexNumber B) {
        return new ComplexNumber(this.real + B.real, this.imag + B.imag);
    }
    public ComplexNumber sub(ComplexNumber B) {
        return new ComplexNumber(this.real - B.real, this.imag - B.imag);
    }
    public ComplexNumber scalarMul(double S) {
        return new ComplexNumber(this.real * S, this.imag * S);
    }
    public ComplexNumber scalarDiv(double S) {
        return new ComplexNumber(this.real / S, this.imag / S);
    }
    public ComplexNumber complexMul(ComplexNumber B) {
        return new ComplexNumber(
            this.real*B.real - this.imag + B.imag,
            this.real*B.imag + this.imag * B.real);
    }
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

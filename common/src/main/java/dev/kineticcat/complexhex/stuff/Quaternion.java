package dev.kineticcat.complexhex.stuff;

import at.petrak.hexcasting.api.utils.HexUtils;
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota;
import net.minecraft.world.phys.Vec3;

public class Quaternion {
    public double a;
    public double b;
    public double c;
    public double d;
    public Quaternion(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    public static Quaternion angleAxis(double angle, Vec3 axis) {
        double C = Math.cos(angle/2);
        double S = Math.sin(angle/2);
        return new Quaternion(C, axis.x*S, axis.y*S, axis.z*S);
    }
    public QuaternionIota asIota() { return new QuaternionIota(this);}
    public static Quaternion fixNaN(Quaternion Q) {return new Quaternion(HexUtils.fixNAN(Q.a), HexUtils.fixNAN(Q.b), HexUtils.fixNAN(Q.c), HexUtils.fixNAN(Q.d));}
    public Quaternion add(Quaternion B) { return new Quaternion(this.a + B.a, this.b + B.b, this.c + B.c, this.d + B.d); }
    public Quaternion add(Double B) { return new Quaternion(this.a + B, this.b, this.c, this.d); }
    public Quaternion sub(Quaternion B) { return new Quaternion(this.a - B.a, this.b - B.b, this.c - B.c, this.d - B.d); }
    public Quaternion sub(Double B) { return new Quaternion(this.a - B, this.b, this.c, this.d); }
    public Quaternion mul(double B) { return new Quaternion(this.a * B, this.b * B, this.c * B, this.d * B); }
    public Quaternion div(double B) { return new Quaternion(this.a / B, this.b / B, this.c / B, this.d / B); }
    public Quaternion mul(Quaternion B) { Quaternion A = this; return new Quaternion(
            (A.a*B.a - A.b*B.b - A.c*B.c - A.d*B.d),
            (A.a*B.b + A.b*B.a + A.c*B.d - A.d*B.c),
            (A.a*B.c - A.b*B.d + A.c*B.a + A.d*B.b),
            (A.a*B.d + A.b*B.c - A.c*B.b + A.d*B.a)
    ); }
    public Quaternion inverse() { return new Quaternion(this.a, -this.b, -this.c, -this.d); }

    public Double length() { return Math.sqrt(Math.pow(this.a, 2)+Math.pow(this.b, 2)+Math.pow(this.c, 2)+Math.pow(this.d, 2));}
}

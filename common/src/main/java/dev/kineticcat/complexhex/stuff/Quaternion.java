package dev.kineticcat.complexhex.stuff;

import at.petrak.hexcasting.api.casting.iota.Iota;
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;

import java.util.List;

import static at.petrak.hexcasting.api.utils.HexUtils.fixNAN;

public class Quaternion extends Quaterniond {
    public Quaternion(double w, double x, double y, double z) {
        super(x, y, z, w);
    }

    public Quaternion() {
        super(0,0,0,0);
    }
    public Quaternion(Vec3 axis, double angle) {
        super(0,0,0,0);
        double C = Math.cos(angle/2);
        double S = Math.sin(angle/2);
        this.w = C;
        this.x = axis.x*S;
        this.y = axis.y*S;
        this.z = axis.z*S;
    }


    public QuaternionIota asIota() { return new QuaternionIota(this); }
    public List<Iota> asActionResult() { return List.of(new QuaternionIota(this)); }
    public Quaternion fixNaN() {
        return new Quaternion(fixNAN(this.w), fixNAN(this.x), fixNAN(this.y), fixNAN(this.z));
    }
    public Quaternion Qadd(Quaternion that) {return (Quaternion) super.add(that);}
    public Quaternion Qadd(Vec3 that) {return (Quaternion) super.add(new Quaternion(0, that.x, that.y, that.z));}
    public Quaternion Qsub(Quaternion that) {return (Quaternion) super.difference(that);}
    public Quaternion Qmul(Double that) {return (Quaternion) super.mul(that);}
    public Quaternion Qmul(Quaternion that) {return (Quaternion) super.mul(that);}
    public Quaternion Qdiv(Double that) {return (Quaternion) super.mul(1/that);}
    public Quaternion Qinvert() {return (Quaternion) super.invert();}
    public Vec3 imaginaryAsVec3() {return new Vec3(this.x, this.y, this.z);}

    @Override
    public String toString() {
        String text = "";
        text += this.w==0 ? "" : String.format("%.2f", this.w);
        text += text.equals("") ? ( this.x==0 ? "" : String.format("%.2f", this.x)+"i" )
                : ( this.x==0 ? "" : " " + (this.x<0?"-":"+") + " " + String.format("%.2f", Math.abs(this.x)) + "i" );
        text += text.equals("") ? ( this.y==0 ? "" : String.format("%.2f", this.y)+"j" )
                : ( this.y==0 ? "" : " " + (this.y<0?"-":"+") + " " + String.format("%.2f", Math.abs(this.y)) + "j" );
        text += text.equals("") ? ( this.z==0 ? "" : String.format("%.2f", this.z)+"k" )
                : ( this.z==0 ? "" : " " + (this.z<0?"-":"+") + " " + String.format("%.2f", Math.abs(this.z)) + "k" );
        text = text.equals("") ? String.format("%.2f", 0.0d) : text;
        return text;
    }
}

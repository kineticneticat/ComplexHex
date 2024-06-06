package dev.kineticcat.complexhex.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import dev.kineticcat.complexhex.stuff.ComplexNumber;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class ComplexNumberIota extends Iota {

    public ComplexNumberIota(ComplexNumber comp) {
        super(ComplexHexIotaTypes.COMPLEXNUMBER, comp);
    }
    public ComplexNumber getComplex() {
        return ComplexNumber.fixNaN((ComplexNumber) this.payload);
    }
    @Override
    public boolean isTruthy() {
        return !( this.getComplex().real == 0 && this.getComplex().imag == 0 );
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof ComplexNumberIota iota
                && tolerates(this.getComplex(), iota.getComplex());
    }

    public static boolean tolerates(ComplexNumber cnA, ComplexNumber cnB) {
        return DoubleIota.tolerates(cnA.real, cnB.real) && DoubleIota.tolerates(cnA.imag,cnB.imag);
    }

    @Override
    public @NotNull Tag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("real", this.getComplex().real);
        tag.putDouble("imag", this.getComplex().imag);
        return tag;
    }
    public static IotaType<ComplexNumberIota> TYPE = new IotaType<ComplexNumberIota>() {
        @Override
        public ComplexNumberIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return ComplexNumberIota.deserialize(tag);
        }

        @Override
        public Component display(Tag tag) {
            return ComplexNumberIota.display(ComplexNumberIota.deserialize(tag).getComplex());
        }

        @Override
        public int color() {
            return 0xff_fc0522;
        }
    };

    public static ComplexNumberIota deserialize(Tag tag) {
        CompoundTag ctag = HexUtils.downcast(tag, CompoundTag.TYPE);
        return new ComplexNumber(
                        ctag.getDouble("real"),
                        ctag.getDouble("imag")
                ).asIota();
    }
    private static final ChatFormatting ComplexNumberColour = ChatFormatting.DARK_RED;
    public static Component display(ComplexNumber cn) {
        if (cn.real == 0) return Component.literal(String.format("%.2f", cn.imag) + "i").withStyle(ComplexNumberColour);
        else
            if (cn.imag >= 0) return Component.literal(String.format("%.2f", cn.real) + " + " + String.format("%.2f", cn.imag) + "i").withStyle(ComplexNumberColour);
            else             return Component.literal(String.format("%.2f", cn.real) + " - " + String.format("%.2f", Math.abs(cn.imag)) + "i").withStyle(ComplexNumberColour);
    }
}

package dev.kineticcat.complexhex.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import dev.kineticcat.complexhex.stuff.Quaternion;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuaternionIota extends Iota {
    public QuaternionIota(Quaternion A) {
        super(ComplexHexIotaTypes.QUATERNION, A);
    }
    public Quaternion getQuaternion() {return Quaternion.fixNaN((Quaternion) this.payload); }
    @Override
    public boolean isTruthy() {
        Quaternion Q = this.getQuaternion();
        return !( Q.a==0 && Q.b==0 && Q.c==0 && Q.d==0);
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof QuaternionIota iota
                && tolerates(this.getQuaternion(), iota.getQuaternion());
    }

    private boolean tolerates(Quaternion QA, Quaternion QB) {
        return     DoubleIota.tolerates(QA.a, QB.a)
                && DoubleIota.tolerates(QA.b, QB.b)
                && DoubleIota.tolerates(QA.c, QB.c)
                && DoubleIota.tolerates(QA.d, QB.d);
    }

    @Override
    public @NotNull Tag serialize() {
        CompoundTag ctag = new CompoundTag();
        ctag.putDouble("a", this.getQuaternion().a);
        ctag.putDouble("b", this.getQuaternion().b);
        ctag.putDouble("c", this.getQuaternion().c);
        ctag.putDouble("d", this.getQuaternion().d);
        return ctag;
    }

    public static IotaType<QuaternionIota> TYPE =  new IotaType<QuaternionIota>() {
        @Nullable
        @Override
        public QuaternionIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return QuaternionIota.deserialize(tag);
        }

        @Override
        public Component display(Tag tag) {
            return QuaternionIota.display(QuaternionIota.deserialize(tag).getQuaternion());
        }

        @Override
        public int color() {
            return ChatFormatting.DARK_BLUE.getColor();
        }
    };

    public static QuaternionIota deserialize(Tag tag) {
        CompoundTag ctag = HexUtils.downcast(tag, CompoundTag.TYPE);
        return new Quaternion(
                ctag.getDouble("a"),
                ctag.getDouble("b"),
                ctag.getDouble("c"),
                ctag.getDouble("d")
        ).asIota();
    }
    public static ChatFormatting QuaternionColour = ChatFormatting.DARK_BLUE;
    public static Component display(Quaternion Q) {
        String text = "";
        text += Q.a==0 ? "" : String.format("%.2f", Q.a);
        text += text.equals("") ? ( Q.b==0 ? "" : String.format("%.2f", Q.b)+"i" )
                                : ( Q.b==0 ? "" : " " + (Q.b<0?"-":"+") + " " + String.format("%.2f", Math.abs(Q.b)) + "i" );
        text += text.equals("") ? ( Q.c==0 ? "" : String.format("%.2f", Q.c)+"j" )
                                : ( Q.c==0 ? "" : " " + (Q.c<0?"-":"+") + " " + String.format("%.2f", Math.abs(Q.c)) + "j" );
        text += text.equals("") ? ( Q.d==0 ? "" : String.format("%.2f", Q.d)+"k" )
                                : ( Q.d==0 ? "" : " " + (Q.d<0?"-":"+") + " " + String.format("%.2f", Math.abs(Q.d)) + "k" );
        text = text.equals("") ? String.format("%.2f", 0.0d) : text;
        return Component.literal(text).withStyle(QuaternionColour);
    }
}

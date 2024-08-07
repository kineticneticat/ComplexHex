package dev.kineticcat.complexhex.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import dev.kineticcat.complexhex.api.util.Quaternion;
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
    public Quaternion getQuaternion() {return ((Quaternion) this.payload).fixNaN(); }
    @Override
    public boolean isTruthy() {
        Quaternion Q = this.getQuaternion();
        return !( Q.w==0 && Q.x==0 && Q.y==0 && Q.z==0);
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof QuaternionIota iota
                && tolerates(this.getQuaternion(), iota.getQuaternion());
    }

    private boolean tolerates(Quaternion QA, Quaternion QB) {
        return     DoubleIota.tolerates(QA.w, QB.w)
                && DoubleIota.tolerates(QA.x, QB.x)
                && DoubleIota.tolerates(QA.y, QB.y)
                && DoubleIota.tolerates(QA.z, QB.z);
    }

    @Override
    public @NotNull Tag serialize() {
        CompoundTag ctag = new CompoundTag();
        ctag.putDouble("w", this.getQuaternion().w);
        ctag.putDouble("x", this.getQuaternion().x);
        ctag.putDouble("y", this.getQuaternion().y);
        ctag.putDouble("z", this.getQuaternion().z);
        return ctag;
    }

    public static IotaType<QuaternionIota> TYPE = new IotaType<QuaternionIota>() {
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
                ctag.getDouble("w"),
                ctag.getDouble("x"),
                ctag.getDouble("y"),
                ctag.getDouble("z")
        ).asIota();
    }
    public static ChatFormatting QuaternionColour = ChatFormatting.DARK_BLUE;
    public static Component display(Quaternion Q) {
        String text = "";
        text += Q.w==0 ? "" : String.format("%.2f", Q.w);
        text += text.equals("") ? ( Q.x==0 ? "" : String.format("%.2f", Q.x)+"i" )
                                : ( Q.x==0 ? "" : " " + (Q.x<0?"-":"+") + " " + String.format("%.2f", Math.abs(Q.x)) + "i" );
        text += text.equals("") ? ( Q.y==0 ? "" : String.format("%.2f", Q.y)+"j" )
                                : ( Q.y==0 ? "" : " " + (Q.y<0?"-":"+") + " " + String.format("%.2f", Math.abs(Q.y)) + "j" );
        text += text.equals("") ? ( Q.z==0 ? "" : String.format("%.2f", Q.z)+"k" )
                                : ( Q.z==0 ? "" : " " + (Q.z<0?"-":"+") + " " + String.format("%.2f", Math.abs(Q.z)) + "k" );
        text = text.equals("") ? String.format("%.2f", 0.0d) : text;
        return Component.literal(text).withStyle(QuaternionColour);
    }
}

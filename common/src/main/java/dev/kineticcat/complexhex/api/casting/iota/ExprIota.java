package dev.kineticcat.complexhex.api.casting.iota;


import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import dev.kineticcat.complexhex.util.ExprDeSer;
import dev.kineticcat.complexhex.api.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExprIota extends Iota {
    public ExprIota(@NotNull Expr expr) {
        super(TYPE, expr);
    }

    public Expr expr() {return (Expr) payload;}

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota that) {return that instanceof ExprIota && ((ExprIota) that).expr().equals(expr());}

    @Override
    public @NotNull Tag serialize() {
        return ExprDeSer.serialise(expr());
    }
    public static Expr deserialise(Tag tag) {return ExprDeSer.deserialise((CompoundTag) tag);}

    public static IotaType<ExprIota> TYPE = new IotaType<ExprIota>() {
        @Nullable
        @Override
        public ExprIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return new ExprIota(deserialise(tag).simp());
        }

        @Override
        public Component display(Tag tag) {
            return Component.literal(deserialise(tag).toString()).withStyle(ChatFormatting.GREEN);
        }

        @Override
        public int color() {
            return 0xff_00ff00;
        }
    };

}
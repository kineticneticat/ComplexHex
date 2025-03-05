package dev.kineticcat.complexhex.api.casting.iota;


import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import symjava.symbolic.Expr;

import static symjava.symbolic.Symbol.x;
import static symjava.symbolic.Symbol.y;

public class EquationIota extends Iota {
    protected EquationIota(@NotNull Object payload) {
        super(TYPE, payload);
        Expr expr = y.add(x);
        expr.diff(y);
    }

    @Override
    public boolean isTruthy() {
        return false;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return false;
    }

    @Override
    public @NotNull Tag serialize() {
        return null;
    }

    public static IotaType<EquationIota> TYPE = new IotaType<EquationIota>() {
        @Nullable
        @Override
        public EquationIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Component display(Tag tag) {
            return null;
        }

        @Override
        public int color() {
            return 0;
        }
    };

}

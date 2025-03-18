package dev.kineticcat.complexhex.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LongIota extends Iota {
    public LongIota(Long num) {
        super(TYPE, num);
    }

    public Long getLong() {return (Long) payload;}

    @Override
    public boolean isTruthy() {
        return getLong() != 0L;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof  LongIota iota
                && Objects.equals(getLong(), iota.getLong());
    }

    @Override
    public @NotNull Tag serialize() {
        return LongTag.valueOf(getLong());
    }

    public static IotaType<LongIota> TYPE = new IotaType<LongIota>() {
        @Nullable
        @Override
        public LongIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return deserialise(tag);
        }

        @Override
        public Component display(Tag tag) {
            return LongIota.display(deserialise(tag).getLong());
        }

        @Override
        public int color() {
            return 0;
        }
    };
    public static LongIota deserialise(Tag tag) {
        LongTag ltag = HexUtils.downcast(tag, LongTag.TYPE);
        return new LongIota(ltag.getAsLong());
    }
    public static Component display(Long l) {
        return Component.literal(String.format("%s", l)).withStyle(ChatFormatting.DARK_GREEN);
    }
}

package dev.kineticcat.complexhex.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import dev.kineticcat.complexhex.api.util.Field;
import dev.kineticcat.complexhex.util.DataStorage;
import kotlin.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jblas.util.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FieldIota extends Iota {
    public static String NAME_TAG = "Name";
    public static String SHAPE_TAG = "Shape";
    public FieldIota(@NotNull Pair<String, List<Integer>> name) {
        super(TYPE, name);
    }
    @SuppressWarnings("unchecked") // shut up
    private Pair<String, List<Integer>> getPair() { return (Pair<String, List<Integer>>) this.payload;}
    public String getName() { return getPair().getFirst();}
    public List<Integer> getShape() { return getPair().getSecond();}
    @Override
    public boolean isTruthy() { return true; }
    @Override
    protected boolean toleratesOther(Iota that) {return false;}

    @Override
    public @NotNull Tag serialize() {
        CompoundTag ctag = new CompoundTag();
        ctag.putString(NAME_TAG, getName());
        ctag.putIntArray(SHAPE_TAG, getShape());
        return ctag;
    }

    public static IotaType<FieldIota> TYPE = new IotaType<FieldIota>() {
        @Nullable
        @Override
        public FieldIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return FieldIota.deserialize(tag);
        }

        @Override
        public Component display(Tag tag) {
            FieldIota fi = FieldIota.deserialize(tag);
            return Component.literal(fi.getName() + ": " + fi.getShape().toString()).withStyle(ChatFormatting.BLUE);
        }

        @Override
        public int color() {
            return 0xFF_BBCCDD;
        }
    };

    public static FieldIota deserialize(Tag tag) {
        String Name = ((CompoundTag) tag).getString(NAME_TAG);
        int[] stupid = ((CompoundTag) tag).getIntArray(SHAPE_TAG);
        List<Integer> Shape = new ArrayList<>();
        for (Integer annoyance : stupid) {
            Shape.add(annoyance);
        }
        return new FieldIota(new Pair<>(Name, Shape));
    }
    private static String Capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String makeName() {
        return String.valueOf(Capitals.charAt(Random.nextInt(26)))
                + Capitals.charAt(Random.nextInt(26))
                + Capitals.charAt(Random.nextInt(26))
                + Capitals.charAt(Random.nextInt(26));
    }

    public Field getField(MinecraftServer server) {
        return Field.deserialise(DataStorage.getServerData(server).Fields.get(getName()));
    }

}

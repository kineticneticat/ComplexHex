package dev.kineticcat.complexhex.api.util;

import at.petrak.hexcasting.api.utils.NBTHelper;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.casting.iota.FieldIota;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

import static dev.kineticcat.complexhex.api.casting.iota.FieldIota.SHAPE_TAG;

public class Field {
    public static String DATA_TAG = "Data";
    public Field(List<Integer> shape, List<Double> data, Modes mode) {
        this.shape = shape;
        this.data = data;
        this.mode = mode;
    }
    public Field(List<Integer> shape, List<Double> data) {
        this.shape = shape;
        this.data = data;
        this.mode = Modes.REAL;
    }
    public static Field Zeros(List<Integer> shape) {
        Integer total = shape.stream().reduce((Integer a, Integer b) -> a*b).get();
        Field field = new Field(shape, new ArrayList<>(Collections.nCopies(total, 0.0)));
        Complexhex.LOGGER.info(field.data);
        return field;
    }
    public static Field Zeros(Integer... shape) {
        return Zeros(List.of(shape));
    }
    public List<Integer> shape;
    public List<Double> data;
    public Modes mode;
    public Integer getElement(List<Integer> index) {
        return 5;
    };
    public void setElement(List<Integer> index) {

    };

    public void applyField(Field that, BinaryOperator<Double> join) {
        for (int i=0; i<this.data.size(); i++) {
            this.data.set(i, join.apply(this.data.get(i), that.data.get(i)));
        }
    }
    public void applyUniform(Double that, BinaryOperator<Double> join) {
        this.data = this.data.stream().map((x) -> join.apply(x, that)).toList();
    }

    public CompoundTag serialise() {
        CompoundTag ctag = new CompoundTag();
        ctag.putIntArray(SHAPE_TAG, shape);
        ListTag ltag = new ListTag();
        for (Double num : data) {
            ltag.add(DoubleTag.valueOf(num));
        }
        ctag.put(DATA_TAG, ltag);
        return ctag;
    }
    public static Field deserialise(CompoundTag ctag) {
        List<Integer> shape = ctag.getList(SHAPE_TAG, ctag.TAG_INT).stream().map(tag -> ((NumericTag) tag).getAsInt()).toList();
        List<Double> data = ctag.getList(DATA_TAG, ctag.TAG_DOUBLE).stream().map(tag -> ((NumericTag) tag).getAsDouble()).toList();
        return new Field(shape, data);
    }
    public enum Modes {
        REAL,
        COMPLEX,
        VEC3,
        MARTRIX
    }
}

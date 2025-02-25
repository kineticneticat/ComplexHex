package dev.kineticcat.complexhex.api.util;

import at.petrak.hexcasting.api.utils.NBTHelper;
import dev.kineticcat.complexhex.api.casting.iota.FieldIota;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

public class Field {
    public static String DATA_TAG = "Data";
    public Field(List<Integer> shape, List<Double> data) {
        this.shape = shape;
        this.data = data;
    }
    public static Field Zeros(List<Integer> shape) {
        Integer total = shape.stream().reduce((Integer a, Integer b) -> a*b).get();
        return new Field(shape, new ArrayList<>(Collections.nCopies(total, 0.0)));
    }
    public List<Integer> shape;
    public List<Double> data;

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
        ctag.putIntArray(FieldIota.SHAPE_TAG, shape);
        ListTag ltag = new ListTag();
        for (Double num : data) {
            ltag.add(DoubleTag.valueOf(num));
        }
        ctag.put(DATA_TAG, ltag);
        return ctag;
    }
    public static Field deserialise(CompoundTag ctag) {
        List<Integer> shape = ctag.getList(FieldIota.SHAPE_TAG, ctag.TAG_INT).stream().map((tag) -> ((NumericTag) tag).getAsInt()).toList();
        List<Double> data = ctag.getList(DATA_TAG, ctag.TAG_INT).stream().map(NBTHelper::getAsDouble).toList();
        return new Field(shape, data);
    }
}

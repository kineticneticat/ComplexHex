package dev.kineticcat.complexhex.util;

import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

public class ExprDeSer {
    private static final String ARGS_TAG = "Args";
    private static final String TYPE_TAG = "Type";
    private static final Map<Class<?>, BiFunction<Expr, CompoundTag, CompoundTag>> SerialisationHandlers = new HashMap<>();
    private static final Map<String, Function<CompoundTag, Expr>> DeserialisationHandlers = new HashMap<>();
    public static CompoundTag serialise(Expr expr) {
        CompoundTag ctag = new CompoundTag();
        ctag.putString(TYPE_TAG, expr.getClass().getSimpleName());
        return SerialisationHandlers.getOrDefault(expr.getClass(), (ser, tag)-> {
            Complexhex.LOGGER.info("oops, expression {} cant be serialised", expr);
            return tag;
        }).apply(expr, ctag);
    }
    public static Expr deserialise(CompoundTag ctag) {
        String name = ctag.getString(TYPE_TAG);
        return DeserialisationHandlers.getOrDefault(name, (tag) -> {
            Complexhex.LOGGER.info("oops, tag {} cant be deserialised", tag);
            return new Infinity();
        }).apply(ctag);
    }

    static {
        BiFunction<Expr, CompoundTag, CompoundTag> SerUnaryHandler = (expr, ctag) -> {
            UnaryOp cast = (UnaryOp) expr;
            ctag.put(ARGS_TAG, serialise(cast.getA()));
            return ctag;
        };
        BiFunction<Expr, CompoundTag, CompoundTag> SerBinaryHandler = (expr, ctag) -> {
            BinaryOp cast = (BinaryOp) expr;
            ListTag ltag = new ListTag();
            ltag.add(serialise(cast.getA()));
            ltag.add(serialise(cast.getB()));
            ctag.put(ARGS_TAG, ltag);
            return ctag;
        };
        SerialisationHandlers.put(Cos.class, SerUnaryHandler);
        SerialisationHandlers.put(Sin.class, SerUnaryHandler);
        SerialisationHandlers.put(Tan.class, SerUnaryHandler);
        SerialisationHandlers.put(Abs.class, SerUnaryHandler);
        SerialisationHandlers.put(Pow.class, SerBinaryHandler);
        SerialisationHandlers.put(Modulo.class, SerBinaryHandler);
        SerialisationHandlers.put(Add.class, SerBinaryHandler);
        SerialisationHandlers.put(Sub.class, SerBinaryHandler);
        SerialisationHandlers.put(Log.class, SerBinaryHandler);
        SerialisationHandlers.put(Div.class, SerBinaryHandler);
        SerialisationHandlers.put(Mul.class, SerBinaryHandler);
        SerialisationHandlers.put(Value.class, (expr, ctag) -> {
            Value cast = (Value) expr;
            ctag.putDouble(ARGS_TAG, cast.x);
            return ctag;
        });
        SerialisationHandlers.put(Symbol.class, (expr, ctag) -> {
            Symbol cast = (Symbol) expr;
            ctag.putString(ARGS_TAG, cast.getLabel());
            return ctag;
        });
        SerialisationHandlers.put(ArcSin.class, SerUnaryHandler);
        SerialisationHandlers.put(ArcCos.class, SerUnaryHandler);
        SerialisationHandlers.put(ArcTan.class, SerUnaryHandler);
        SerialisationHandlers.put(Sinh.class, SerUnaryHandler);
        SerialisationHandlers.put(Cosh.class, SerUnaryHandler);
        SerialisationHandlers.put(Tanh.class, SerUnaryHandler);
        SerialisationHandlers.put(ArcSinh.class, SerUnaryHandler);
        SerialisationHandlers.put(ArcCosh.class, SerUnaryHandler);
        SerialisationHandlers.put(ArcTanh.class, SerUnaryHandler);
        SerialisationHandlers.put(ArcTan2.class, SerBinaryHandler);
        SerialisationHandlers.put(Sign.class, SerUnaryHandler);
        SerialisationHandlers.put(Floor.class, SerUnaryHandler);
        SerialisationHandlers.put(Ceiling.class, SerUnaryHandler);
        SerialisationHandlers.put(Equals.class, SerBinaryHandler);
        SerialisationHandlers.put(LessThan.class, SerBinaryHandler);
        SerialisationHandlers.put(LessThanOrEq.class, SerBinaryHandler);
        SerialisationHandlers.put(GreaterThan.class, SerBinaryHandler);
        SerialisationHandlers.put(GreaterThanOrEq.class, SerBinaryHandler);
        SerialisationHandlers.put(And.class, SerBinaryHandler);
        SerialisationHandlers.put(Or.class, SerBinaryHandler);
        SerialisationHandlers.put(Xor.class, SerBinaryHandler);
        SerialisationHandlers.put(Not.class, SerUnaryHandler);
        SerialisationHandlers.put(Piecewise.class, (expr, ctag) -> {
            Piecewise cast = (Piecewise) expr;
            ListTag ltag = new ListTag();
            ltag.add(serialise(cast.getCondition()));
            ltag.add(serialise(cast.getIfTrue()));
            ltag.add(serialise(cast.getIfFalse()));
            ctag.put(ARGS_TAG, ltag);
            return ctag;
        });
        SerialisationHandlers.put(Vector.class, (expr, ctag) -> {
            Vector cast = (Vector) expr;
            ListTag ltag = new ListTag();
            ltag.add(serialise(cast.x));
            ltag.add(serialise(cast.y));
            ltag.add(serialise(cast.z));
            ctag.put(ARGS_TAG, ltag);
            return ctag;
        });



        Function<UnaryOperator<Expr>, Function<CompoundTag, Expr>> DeserUnaryHandler = maker ->
                ctag -> maker.apply(deserialise(ctag.getCompound(ARGS_TAG)));
        Function<BinaryOperator<Expr>, Function<CompoundTag, Expr>> DeserBinaryHandler = maker ->
                ctag -> {
                    ListTag ltag = ctag.getList(ARGS_TAG, Tag.TAG_COMPOUND);
                    return maker.apply(deserialise(ltag.getCompound(0)), deserialise(ltag.getCompound(1)));
                };
        Function<Supplier<Expr>, Function<CompoundTag, Expr>> DeserNoneryHandler = maker ->
                ctag -> maker.get();

        DeserialisationHandlers.put(Cos.class.getSimpleName(), DeserUnaryHandler.apply(Cos::new));
        DeserialisationHandlers.put(Sin.class.getSimpleName(), DeserUnaryHandler.apply(Sin::new));
        DeserialisationHandlers.put(Tan.class.getSimpleName(), DeserUnaryHandler.apply(Tan::new));
        DeserialisationHandlers.put(Abs.class.getSimpleName(), DeserUnaryHandler.apply(Abs::new));
        DeserialisationHandlers.put(Pow.class.getSimpleName(), DeserBinaryHandler.apply(Pow::new));
        DeserialisationHandlers.put(Modulo.class.getSimpleName(), DeserBinaryHandler.apply(Modulo::new));
        DeserialisationHandlers.put(Add.class.getSimpleName(), DeserBinaryHandler.apply(Add::new));
        DeserialisationHandlers.put(Sub.class.getSimpleName(), DeserBinaryHandler.apply(Sub::new));
        DeserialisationHandlers.put(Log.class.getSimpleName(), DeserBinaryHandler.apply(Log::new));
        DeserialisationHandlers.put(Div.class.getSimpleName(), DeserBinaryHandler.apply(Div::new));
        DeserialisationHandlers.put(Mul.class.getSimpleName(), DeserBinaryHandler.apply(Mul::new));
        DeserialisationHandlers.put(Value.class.getSimpleName(), ctag -> new Value(ctag.getDouble(ARGS_TAG)));
        DeserialisationHandlers.put(Symbol.class.getSimpleName(), ctag -> new Symbol(ctag.getString(ARGS_TAG)));
        DeserialisationHandlers.put(Infinity.class.getSimpleName(), DeserNoneryHandler.apply(Infinity::new));

        DeserialisationHandlers.put(ArcSin.class.getSimpleName(), DeserUnaryHandler.apply(ArcSin::new));
        DeserialisationHandlers.put(ArcCos.class.getSimpleName(), DeserUnaryHandler.apply(ArcCos::new));
        DeserialisationHandlers.put(ArcTan.class.getSimpleName(), DeserUnaryHandler.apply(ArcTan::new));
        DeserialisationHandlers.put(Sinh.class.getSimpleName(), DeserUnaryHandler.apply(Sinh::new));
        DeserialisationHandlers.put(Cosh.class.getSimpleName(), DeserUnaryHandler.apply(Cosh::new));
        DeserialisationHandlers.put(Tanh.class.getSimpleName(), DeserUnaryHandler.apply(Tanh::new));
        DeserialisationHandlers.put(ArcSinh.class.getSimpleName(), DeserUnaryHandler.apply(ArcSinh::new));
        DeserialisationHandlers.put(ArcCosh.class.getSimpleName(), DeserUnaryHandler.apply(ArcCosh::new));
        DeserialisationHandlers.put(ArcTanh.class.getSimpleName(), DeserUnaryHandler.apply(ArcTanh::new));
        DeserialisationHandlers.put(ArcTan2.class.getSimpleName(), DeserBinaryHandler.apply(ArcTan2::new));
        DeserialisationHandlers.put(Sign.class.getSimpleName(), DeserUnaryHandler.apply(Sign::new));
        DeserialisationHandlers.put(Floor.class.getSimpleName(), DeserUnaryHandler.apply(Floor::new));
        DeserialisationHandlers.put(Ceiling.class.getSimpleName(), DeserUnaryHandler.apply(Ceiling::new));
        DeserialisationHandlers.put(Equals.class.getSimpleName(), DeserBinaryHandler.apply(Equals::new));
        DeserialisationHandlers.put(LessThan.class.getSimpleName(), DeserBinaryHandler.apply(LessThan::new));
        DeserialisationHandlers.put(LessThanOrEq.class.getSimpleName(), DeserBinaryHandler.apply(LessThanOrEq::new));
        DeserialisationHandlers.put(GreaterThan.class.getSimpleName(), DeserBinaryHandler.apply(GreaterThan::new));
        DeserialisationHandlers.put(GreaterThanOrEq.class.getSimpleName(), DeserBinaryHandler.apply(GreaterThanOrEq::new));
        DeserialisationHandlers.put(And.class.getSimpleName(), DeserBinaryHandler.apply(And::new));
        DeserialisationHandlers.put(Or.class.getSimpleName(), DeserBinaryHandler.apply(Or::new));
        DeserialisationHandlers.put(Xor.class.getSimpleName(), DeserBinaryHandler.apply(Xor::new));
        DeserialisationHandlers.put(Not.class.getSimpleName(), DeserUnaryHandler.apply(Not::new));
        DeserialisationHandlers.put(Piecewise.class.getSimpleName(), ctag -> {
            ListTag ltag = ctag.getList(ARGS_TAG, Tag.TAG_COMPOUND);
            return new Piecewise(deserialise(ltag.getCompound(0)), deserialise(ltag.getCompound(1)), deserialise(ltag.getCompound(2)));
        });
        DeserialisationHandlers.put(Vector.class.getSimpleName(), ctag -> {
            ListTag ltag = ctag.getList(ARGS_TAG, Tag.TAG_COMPOUND);
            return new Vector(deserialise(ltag.getCompound(0)), deserialise(ltag.getCompound(1)), deserialise(ltag.getCompound(2)));
        });
    }
}
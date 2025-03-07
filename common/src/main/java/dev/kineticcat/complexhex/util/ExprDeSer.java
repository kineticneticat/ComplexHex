package dev.kineticcat.complexhex.util;

import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import symjava.examples.fem.WeakForm;
import symjava.logic.And;
import symjava.logic.Not;
import symjava.logic.Or;
import symjava.logic.Xor;
import symjava.matrix.SymMatrix;
import symjava.matrix.SymVector;
import symjava.relational.*;
import symjava.symbolic.*;
import symjava.symbolic.arity.BinaryOp;
import symjava.symbolic.arity.UnaryOp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

import static symjava.symbolic.Symbol.x;

public class ExprDeSer {
    private static String ARGS_TAG = "Args";
    private static String TYPE_TAG = "Type";
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
        SerialisationHandlers.put(SymMatrix.class, (Expr expr, CompoundTag ctag) -> {
            SymMatrix cast = (SymMatrix) expr;
            ListTag ltag = new ListTag();
            for (int i=0; i<cast.rowDim(); i++) {
                ltag.add(serialise(cast.get(i)));
            }
            ctag.put(ARGS_TAG, ltag);
            return ctag;
        });
        BiFunction<Expr, CompoundTag, CompoundTag> SerUnaryHandler = (expr, ctag) -> {
            UnaryOp cast = (UnaryOp) expr;
            ctag.put(ARGS_TAG, serialise(cast.arg));
            return ctag;
        };
        BiFunction<Expr, CompoundTag, CompoundTag> SerBinaryHandler = (expr, ctag) -> {
            BinaryOp cast = (BinaryOp) expr;
            ListTag ltag = new ListTag();
            ltag.add(serialise(cast.arg1));
            ltag.add(serialise(cast.arg2));
            ctag.put(ARGS_TAG, ltag);
            return ctag;
        };
        SerialisationHandlers.put(Cos.class, SerUnaryHandler);
        SerialisationHandlers.put(Not.class, SerUnaryHandler);
        SerialisationHandlers.put(Sin.class, SerUnaryHandler);
        SerialisationHandlers.put(Tan.class, SerUnaryHandler);
        SerialisationHandlers.put(Reciprocal.class, SerUnaryHandler);
        SerialisationHandlers.put(Abs.class, SerUnaryHandler);
        SerialisationHandlers.put(Negate.class, SerUnaryHandler);

        SerialisationHandlers.put(SymVector.class, (expr, ctag) -> {
            SymVector cast = (SymVector) expr;
            ListTag ltag = new ListTag();
            for (Expr arg : cast.getData()) {
                ltag.add(serialise(arg));
            }
            ctag.put(ARGS_TAG, ltag);
            return ctag;
        });

        SerialisationHandlers.put(Eq.class, SerBinaryHandler);
            SerialisationHandlers.put(WeakForm.class, SerBinaryHandler);
        SerialisationHandlers.put(Pow.class, SerBinaryHandler);
            SerialisationHandlers.put(Exp.class, SerBinaryHandler);
        SerialisationHandlers.put(Remainder.class, SerBinaryHandler);
        SerialisationHandlers.put(Gt.class, SerBinaryHandler);
        SerialisationHandlers.put(Neq.class, SerBinaryHandler);
        SerialisationHandlers.put(Lt.class, SerBinaryHandler);
        SerialisationHandlers.put(Or.class, SerBinaryHandler);
        SerialisationHandlers.put(Add.class, SerBinaryHandler);
        SerialisationHandlers.put(Subtract.class, SerBinaryHandler);
        SerialisationHandlers.put(Ge.class, SerBinaryHandler);
        SerialisationHandlers.put(Log.class, SerBinaryHandler);
            SerialisationHandlers.put(Log10.class, SerBinaryHandler);
            SerialisationHandlers.put(Log2.class, SerBinaryHandler);
        SerialisationHandlers.put(Divide.class, SerBinaryHandler);
        SerialisationHandlers.put(Multiply.class, SerBinaryHandler);
        SerialisationHandlers.put(Le.class, SerBinaryHandler);
        SerialisationHandlers.put(Xor.class, SerBinaryHandler);
        SerialisationHandlers.put(And.class, SerBinaryHandler);
        SerialisationHandlers.put(Sqrt.class, SerBinaryHandler);

        SerialisationHandlers.put(SymInteger.class, (expr, ctag) -> {
            SymInteger cast = (SymInteger) expr;
            ctag.putInt(ARGS_TAG, cast.getIntValue());
            return ctag;
        });
        SerialisationHandlers.put(SymLong.class, (expr, ctag) -> {
            SymLong cast = (SymLong) expr;
            ctag.putLong(ARGS_TAG, cast.getLongValue());
            return ctag;
        });
        SerialisationHandlers.put(SymDouble.class, (expr, ctag) -> {
            SymDouble cast = (SymDouble) expr;
            ctag.putDouble(ARGS_TAG, cast.getDoubleValue());
            return ctag;
        });
        SerialisationHandlers.put(SymFloat.class, (expr, ctag) -> {
            SymFloat cast = (SymFloat) expr;
            ctag.putFloat(ARGS_TAG, cast.getFloatValue());
            return ctag;
        });

        SerialisationHandlers.put(Symbol.class, (expr, ctag) -> {
            Symbol cast = (Symbol) expr;
            ctag.putString(ARGS_TAG, cast.getLabel());
            return ctag;
        });
        SerialisationHandlers.put(SymComplex.class, (expr, ctag) -> {
            SymComplex cast = (SymComplex) expr;
            CompoundTag cntag = new CompoundTag();
            Expr[] args = cast.args();
            cntag.put("Real", serialise(args[0]));
            cntag.put("Imag", serialise(args[1]));
            ctag.put(ARGS_TAG, cntag);
            return ctag;
        });
        SerialisationHandlers.put(SymConst.class, (expr, ctag) -> {
            SymConst cast = (SymConst) expr;
            ctag.putDouble(ARGS_TAG, cast.getValue());
            return ctag;
        });
        SerialisationHandlers.put(Func.class, (expr, ctag) -> {
            Func cast = (Func) expr;
            ListTag ltag = new ListTag();
            for (Expr arg : cast.args()) {
                ltag.add(serialise(arg));
            }
            ctag.put(ARGS_TAG, ltag);
            ctag.putString("Name", cast.getLabel());
            ctag.put("Expr", serialise(cast.getExpr()));
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

        DeserialisationHandlers.put(SymMatrix.class.getSimpleName(), ctag -> {
            Expr[][] args = ctag
                    .getList(ARGS_TAG, Tag.TAG_COMPOUND)
                    .stream()
                    .map(tag->deserialise((CompoundTag) tag))
                    .map(expr -> ((SymVector) expr).getData())
                    .toList()
                    .toArray(new Expr[0][0]);
            return new SymMatrix(args);
        });
        DeserialisationHandlers.put(Cos.class.getSimpleName(), DeserUnaryHandler.apply(Cos::new));
        DeserialisationHandlers.put(Not.class.getSimpleName(), DeserUnaryHandler.apply(Not::new));
        DeserialisationHandlers.put(Sin.class.getSimpleName(), DeserUnaryHandler.apply(Sin::new));
        DeserialisationHandlers.put(Tan.class.getSimpleName(), DeserUnaryHandler.apply(Tan::new));
        DeserialisationHandlers.put(Reciprocal.class.getSimpleName(), DeserUnaryHandler.apply(Reciprocal::new));
        DeserialisationHandlers.put(Abs.class.getSimpleName(), DeserUnaryHandler.apply(Abs::new));
        DeserialisationHandlers.put(Negate.class.getSimpleName(), DeserUnaryHandler.apply(Negate::new));
        DeserialisationHandlers.put(Exp.class.getSimpleName(), DeserUnaryHandler.apply(Exp::new));
        DeserialisationHandlers.put(Log10.class.getSimpleName(), DeserUnaryHandler.apply(Log10::new));
        DeserialisationHandlers.put(Log2.class.getSimpleName(), DeserUnaryHandler.apply(Log2::new));

        DeserialisationHandlers.put(SymVector.class.getSimpleName(), ctag -> {
            Expr[] args = ctag.getList(ARGS_TAG, Tag.TAG_COMPOUND)
                    .stream()
                    .map(tag -> deserialise((CompoundTag) tag))
                    .toList().toArray(new Expr[0]);
            return new SymVector(args);
        });

        DeserialisationHandlers.put(Eq.class.getSimpleName(), DeserBinaryHandler.apply(Eq::new));
        DeserialisationHandlers.put(Pow.class.getSimpleName(), DeserBinaryHandler.apply(Pow::new));
        DeserialisationHandlers.put(Remainder.class.getSimpleName(), DeserBinaryHandler.apply(Remainder::new));
        DeserialisationHandlers.put(Gt.class.getSimpleName(), DeserBinaryHandler.apply(Gt::new));
        DeserialisationHandlers.put(Neq.class.getSimpleName(), DeserBinaryHandler.apply(Neq::new));
        DeserialisationHandlers.put(Lt.class.getSimpleName(), DeserBinaryHandler.apply(Lt::new));
        DeserialisationHandlers.put(Or.class.getSimpleName(), DeserBinaryHandler.apply(Or::new));
        DeserialisationHandlers.put(Add.class.getSimpleName(), DeserBinaryHandler.apply(Add::new));
        DeserialisationHandlers.put(Subtract.class.getSimpleName(), DeserBinaryHandler.apply(Subtract::new));
        DeserialisationHandlers.put(Ge.class.getSimpleName(), DeserBinaryHandler.apply(Ge::new));
        DeserialisationHandlers.put(Log.class.getSimpleName(), DeserBinaryHandler.apply(Log::new));
        DeserialisationHandlers.put(Divide.class.getSimpleName(), DeserBinaryHandler.apply(Divide::new));
        DeserialisationHandlers.put(Multiply.class.getSimpleName(), DeserBinaryHandler.apply(Multiply::new));
        DeserialisationHandlers.put(Le.class.getSimpleName(), DeserBinaryHandler.apply(Le::new));
        DeserialisationHandlers.put(Xor.class.getSimpleName(), DeserBinaryHandler.apply(Xor::new));
        DeserialisationHandlers.put(And.class.getSimpleName(), DeserBinaryHandler.apply(And::new));
        DeserialisationHandlers.put(Sqrt.class.getSimpleName(), DeserBinaryHandler.apply(Sqrt::new));



        DeserialisationHandlers.put(SymRandom.class.getSimpleName(), DeserNoneryHandler.apply(SymRandom::new));



        DeserialisationHandlers.put(SymInteger.class.getSimpleName(), ctag -> new SymInteger(ctag.getInt(ARGS_TAG)));
        DeserialisationHandlers.put(SymLong.class.getSimpleName(), ctag -> new SymLong(ctag.getLong(ARGS_TAG)));
        DeserialisationHandlers.put(SymDouble.class.getSimpleName(), ctag -> new SymDouble(ctag.getDouble(ARGS_TAG)));
        DeserialisationHandlers.put(SymFloat.class.getSimpleName(), ctag -> new SymFloat(ctag.getFloat(ARGS_TAG)));

        DeserialisationHandlers.put(Symbol.class.getSimpleName(), ctag -> new Symbol(ctag.getString(ARGS_TAG)));
        DeserialisationHandlers.put(Infinity.class.getSimpleName(), DeserNoneryHandler.apply(Infinity::new));
        DeserialisationHandlers.put(SymComplex.class.getSimpleName(), DeserBinaryHandler.apply(SymComplex::new));
        DeserialisationHandlers.put(Func.class.getSimpleName(), ctag -> {
            String name = ctag.getString("Name");
            Expr expr = deserialise(ctag.getCompound("Expr"));
            ListTag ltag = ctag.getList(ARGS_TAG, Tag.TAG_COMPOUND);
            List<Expr> args = ltag.stream().map(tag -> deserialise((CompoundTag) tag)).toList();
            return new Func(name, expr, args.toArray(args.toArray(new Expr[0])));
        });
    }

    public static void main(String[] args) {
        Expr expr = new Sqrt(new Add(new Pow(x, Expr.valueOf(2)), new Pow(x, Expr.valueOf(2))), Expr.valueOf(2));
        System.out.println("original expr:");
        System.out.println(expr);
        CompoundTag ser = serialise(expr);
        System.out.println("serialised:");
        System.out.println(ser);
        Expr deser = deserialise(ser);
        System.out.println("deserialised:");
        System.out.println(deser);
    }
}

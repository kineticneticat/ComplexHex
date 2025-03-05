package dev.kineticcat.complexhex.util;

import lambdacloud.core.lang.LCBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import symjava.math.Dot;
import symjava.math.Grad;
import symjava.matrix.SymMatrix;
import symjava.matrix.SymVector;
import symjava.numeric.NumFunc;
import symjava.symbolic.*;
import symjava.symbolic.arity.BinaryOp;
import symjava.symbolic.arity.NaryOp;
import symjava.symbolic.arity.TernaryOp;
import symjava.symbolic.arity.UnaryOp;

public class ExprDeSer {
    private static String ARGS_TAG = "Args";
    private static String NAME_TAG = "Name";
    private static String DATA_TAG = "Data";
    public static CompoundTag serialiseExpr(Expr expr) {
        CompoundTag ctag = new CompoundTag();
        ctag.putString(NAME_TAG, expr.getClass().getSimpleName());

        if      (expr instanceof LCBase)      {}
        else if (expr instanceof SymMatrix)   {}
        else if (expr instanceof UnaryOp)     {
            ctag.put(ARGS_TAG, serialiseExpr(((UnaryOp) expr).arg));
        }
        else if (expr instanceof Symbols)     {}
        else if (expr instanceof TernaryOp)   {
            ListTag ltag = new ListTag();
            ltag.add(serialiseExpr(((TernaryOp) expr).arg1));
            ltag.add(serialiseExpr(((TernaryOp) expr).arg2));
            ltag.add(serialiseExpr(((TernaryOp) expr).arg3));
            ctag.put(ARGS_TAG, ltag);
        }
        else if (expr instanceof Prod)        {}
        else if (expr instanceof Dot)         {}
        else if (expr instanceof Integrate)   {}
        else if (expr instanceof SymVector)   {
            if (expr instanceof Grad) {

            }
            else if (expr instanceof SymVector) {
                // n-dimensional Expr arrays
                ListTag ltag = new ListTag();
                for (Expr arg : ((SymVector) expr).getData()) {
                    ltag.add(serialiseExpr(arg));
                }
                ctag.put(ARGS_TAG, ltag);
            }
        }
        else if (expr instanceof BinaryOp)    {
            ctag.putString(NAME_TAG, expr.getClass().getSimpleName());
            ListTag ltag = new ListTag();
            ltag.add(serialiseExpr(((BinaryOp) expr).arg1));
          ltag.add(serialiseExpr(((BinaryOp) expr).arg2));
            ctag.put(ARGS_TAG, ltag);
        }
        else if (expr instanceof SymRandom)   {}
        else if (expr instanceof SymReal)     {
            if (expr instanceof SymInteger) {
                ctag.putInt(ARGS_TAG, ((SymInteger) expr).getIntValue());
            }
            else if (expr instanceof SymLong) {
                ctag.putLong(ARGS_TAG, ((SymLong) expr).getLongValue());
            }
            else if (expr instanceof SymDouble) {
                ctag.putDouble(ARGS_TAG, ((SymDouble) expr).getDoubleValue());
            }
            else if (expr instanceof SymFloat) {
                ctag.putFloat(ARGS_TAG, ((SymFloat) expr).getFloatValue());
            }
        }
        else if (expr instanceof Limit)       {}
        else if (expr instanceof Sum)         {}
        else if (expr instanceof Symbol)      {
            ctag.putString(DATA_TAG, expr.getLabel());
        }
        else if (expr instanceof NumFunc)     {}
        else if (expr instanceof Infinity)    {}
        else if (expr instanceof SymComplex)  {
            CompoundTag cntag = new CompoundTag();
            Expr[] args = expr.args();
            cntag.put("Real", serialiseExpr(args[0]));
            cntag.put("Imag", serialiseExpr(args[1]));
        }
        else if (expr instanceof SymConst)    {
            ctag.putDouble(DATA_TAG, ((SymConst) expr).getValue());
        }
        else if (expr instanceof NaryOp)      {
            ListTag ltag = new ListTag();
            for (Expr arg : ((NaryOp) expr).args()) {
                ltag.add(serialiseExpr(arg));
            }
            ctag.put(ARGS_TAG, ltag);
        }

        return ctag;
    }
}

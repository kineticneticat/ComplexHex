package dev.kineticcat.complexhex.casting.arithmetic;


import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OperatorBinaryEnv extends OperatorBasic {
    public TriFunction<Iota, Iota, CastingEnvironment, Iota> inner;

    public OperatorBinaryEnv(IotaMultiPredicate accepts, TriFunction<Iota, Iota, CastingEnvironment, Iota> inner) {
        super(2, accepts);
        this.inner = inner;
    }

    @Override
    public @NotNull Iterable<Iota> apply(Iterable<? extends Iota> iotas, @NotNull CastingEnvironment env) {
        var it = iotas.iterator();
        return List.of(inner.apply(it.next(), it.next(), env));
    }
}

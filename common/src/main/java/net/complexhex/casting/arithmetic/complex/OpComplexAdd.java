package net.complexhex.casting.arithmetic.complex;

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import net.complexhex.api.casting.iota.complex.ComplexHexIotaTypes;
import net.complexhex.api.casting.iota.complex.ComplexNumberIota;
import net.complexhex.stuff.ComplexNumber;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class OpComplexAdd extends OperatorBasic {


    public OpComplexAdd() {
        super(2, IotaMultiPredicate.all(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER)));
    }

    @NotNull
    @Override
    public Iterable<Iota> apply(Iterable<? extends Iota> iotas, @NotNull CastingEnvironment env) throws Mishap {
        Iterator<? extends Iota> it = iotas.iterator();
        ComplexNumber cnA = ((ComplexNumberIota) it.next()).getComplex();
        ComplexNumber cnB = ((ComplexNumberIota) it.next()).getComplex();
        ComplexNumber cnR = cnA.add(cnB);
        return cnR.asActionResult();
    }
}

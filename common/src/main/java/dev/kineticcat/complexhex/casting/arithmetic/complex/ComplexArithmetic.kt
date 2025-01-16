package dev.kineticcat.complexhex.casting.arithmetic.complex

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.ComplexNumberIota
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry.*
import dev.kineticcat.complexhex.stuff.ComplexNumber

// paraphrased from hexmod source lmao

object ComplexArithmetic : Arithmetic {
    private val ACCEPTS_C: IotaMultiPredicate = IotaMultiPredicate.all(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER))
    private val ACCEPTS_CD: IotaMultiPredicate = IotaMultiPredicate.either(
        IotaMultiPredicate.pair(IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER), IotaPredicate.ofType(HexIotaTypes.DOUBLE)),
        IotaMultiPredicate.pair(IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(ComplexHexIotaTypes.COMPLEXNUMBER))
    )
    private val ACCEPTS_CCorCD: IotaMultiPredicate = IotaMultiPredicate.either(ACCEPTS_C, ACCEPTS_CD)
    override fun arithName() = "complex_maths"
    private val OPS = listOf(
        ADD,
        SUB,
        MUL,
        DIV,
        ABS,
        CNARG,
        REAL,
        IMAGINARY,
        CONJUGATE
    )

    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {
            ADD        -> CDorCCbinaryC({ a, b -> a.add(b) }, {a, b -> a.add(b)})
            SUB        -> CDorCCbinaryC({ a, b -> a.sub(b) }, {a, b -> a.sub(b)})
            MUL        -> CDorCCbinaryC({ a, b -> a.mul(b) }, {a, b -> a.mul(b)})
            DIV        -> CDbinaryC    { a, b -> a.scalarDiv(b) }
            ABS        -> CunaryD      { a -> a.modulus() }
            CNARG      -> CunaryD      { a -> a.argument() }
            REAL       -> CunaryD      { a -> a.real }
            IMAGINARY  -> CunaryD      { a -> a.imag }
            CONJUGATE  -> CunaryC      { a -> a.conjugate() }
            else -> throw InvalidOperatorException("$pattern is not a valid operator in complex arithmetic")
        }
    }

    fun CunaryC(op: (ComplexNumber) -> (ComplexNumber)) = OperatorUnary(ACCEPTS_C)
    {i: Iota -> ComplexNumberIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex)) }
    fun CunaryD(op: (ComplexNumber) -> (Double)) = OperatorUnary(ACCEPTS_C)
    {i: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}

    private fun CD(cn: Iota, double: Iota, op: (ComplexNumber, Double) -> (ComplexNumber)): ComplexNumberIota {
        return ComplexNumberIota(
            op(
                Operator.downcast(cn, ComplexHexIotaTypes.COMPLEXNUMBER).complex,
                Operator.downcast(double, HexIotaTypes.DOUBLE).double
            )
        )
    }
    private fun CC(cn1: Iota, cn2: Iota, op: (ComplexNumber, ComplexNumber) -> (ComplexNumber)): ComplexNumberIota {
        return ComplexNumberIota(
            op(
                Operator.downcast(cn1, ComplexHexIotaTypes.COMPLEXNUMBER).complex,
                Operator.downcast(cn2, ComplexHexIotaTypes.COMPLEXNUMBER).complex
            )
        )
    }

    fun CCbinaryC(op: (ComplexNumber, ComplexNumber) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_C)
        {i: Iota, j: Iota -> CC(i, j, op) }
    fun CCbinaryD(op: (ComplexNumber, ComplexNumber) -> (Double)) = OperatorBinary(ACCEPTS_C)
        {i: Iota, j: Iota -> DoubleIota(op(Operator.downcast(i, ComplexHexIotaTypes.COMPLEXNUMBER).complex, Operator.downcast(j, ComplexHexIotaTypes.COMPLEXNUMBER).complex))}

    private fun CDbinaryC(op: (ComplexNumber, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_CCorCD)
        { i: Iota, j: Iota -> if (i is DoubleIota && j is ComplexNumberIota) {
            CD(j, i, op)
        } else if (i is ComplexNumberIota && j is DoubleIota) {
            CD(i, j, op)
        } else {
            throw InvalidOperatorException("i did an oopsie, report this pls :) (${i::class}, ${j::class})")
        }
    }
    // what the fuck is this
    fun CDorCCbinaryC(opA:(ComplexNumber, ComplexNumber) -> (ComplexNumber), opB:(ComplexNumber, Double) -> (ComplexNumber)) = OperatorBinary(ACCEPTS_CCorCD)
        {i: Iota, j:Iota -> if (i is ComplexNumberIota && j is ComplexNumberIota) {
            CC(i, j, opA)
        } else if (i is DoubleIota && j is ComplexNumberIota) {
            CD(j, i, opB)
        } else if (i is ComplexNumberIota && j is DoubleIota) {
            CD(i, j, opB)
        } else {
            throw InvalidOperatorException("i did an oopsie, report this pls :) (${j::class})")
        }
    }
}
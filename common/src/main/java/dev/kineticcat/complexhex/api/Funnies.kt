package dev.kineticcat.complexhex.api

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import dev.kineticcat.complexhex.api.casting.iota.ComplexNumberIota
import dev.kineticcat.complexhex.api.casting.iota.ExprIota
import dev.kineticcat.complexhex.api.casting.iota.LongIota
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.api.util.Expr
import dev.kineticcat.complexhex.api.util.Value
import dev.kineticcat.complexhex.stuff.ComplexNumber
import dev.kineticcat.complexhex.stuff.Quaternion
import kotlin.math.ln
import kotlin.math.pow

// stolen from hexcasting lmao
fun List<Iota>.getComplex(idx: Int, argc: Int = 0): ComplexNumber {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is ComplexNumberIota) {
        return x.complex
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "quaternion")
    }
}
fun List<Iota>.getQuaternion(idx: Int, argc: Int = 0): Quaternion {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is QuaternionIota) {
        return x.quaternion
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "quaternion")
    }
}
fun List<Iota>.getLong(idx: Int, argc: Int = 0): Long {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is LongIota) {
        return x.long
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "long")
    }
}

fun List<Iota>.getExpr(idx: Int, argc: Int = 0): Expr {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is ExprIota) {
        return x.expr()
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "expr")
    }
}
fun List<Iota>.getExprOrNum(idx: Int, argc: Int = 0): Expr {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (x) {
        is ExprIota -> x.expr()
        is DoubleIota -> Value(x.double)
        else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "expr_or_num")
    }
}

// yoinked from https://math.toronto.edu/mathnet/questionCorner/complexexp.html
fun CNpow(a: Double, power: ComplexNumber): ComplexNumber {
    val b = power.real
    val c = power.imag
    return ComplexNumber.polar(a.pow(b), c * ln(a))
}

inline val Long.asActionResult get() = listOf(LongIota(this))
inline val Quaternion.asActionResult get() = listOf(QuaternionIota(this))

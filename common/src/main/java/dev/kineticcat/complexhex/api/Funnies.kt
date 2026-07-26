package dev.kineticcat.complexhex.api

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import dev.kineticcat.complexhex.api.casting.iota.ComplexNumberIota
import dev.kineticcat.complexhex.api.casting.iota.ExprIota
import dev.kineticcat.complexhex.api.casting.iota.LongIota
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.api.util.Expr
import dev.kineticcat.complexhex.api.util.Symbol
import dev.kineticcat.complexhex.api.util.Value
import dev.kineticcat.complexhex.api.util.Vector
import dev.kineticcat.complexhex.stuff.ComplexNumber
import dev.kineticcat.complexhex.stuff.Quaternion
import org.jblas.DoubleMatrix
import org.joml.Matrix4f
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
fun List<Iota>.getExprLike(idx: Int, argc: Int = 0): Expr {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (x) {
        is ExprIota -> x.expr()
        is DoubleIota -> Value(x.double)
        is Vec3Iota -> Vector(x.vec3)
        else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "expr_or_num")
    }
}
fun List<Iota>.getSymbol(idx: Int, argc: Int = 0): Symbol {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is ExprIota) {
        return when (x.expr()) {
            is Symbol -> x.expr() as Symbol
            else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "symbol")
        }

    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "expr")
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

fun Matrix4f.toDoubleMatrix() = DoubleMatrix(4, 4,
    this.m00().toDouble(), this.m01().toDouble(), this.m02().toDouble(), this.m03().toDouble(),
    this.m10().toDouble(), this.m11().toDouble(), this.m12().toDouble(), this.m13().toDouble(),
    this.m20().toDouble(), this.m21().toDouble(), this.m22().toDouble(), this.m23().toDouble(),
    this.m30().toDouble(), this.m31().toDouble(), this.m32().toDouble(), this.m33().toDouble()
)

fun DoubleMatrix.toMatrix4f() = Matrix4f(
    this.get(0, 0).toFloat(), this.get(1, 0).toFloat(),this.get(2, 0).toFloat(),this.get(3, 0).toFloat(),
    this.get(0, 1).toFloat(), this.get(1, 1).toFloat(),this.get(2, 1).toFloat(),this.get(3, 1).toFloat(),
    this.get(0, 2).toFloat(), this.get(1, 2).toFloat(),this.get(2, 2).toFloat(),this.get(3, 2).toFloat(),
    this.get(0, 3).toFloat(), this.get(1, 3).toFloat(),this.get(2, 3).toFloat(),this.get(3, 3).toFloat(),
)
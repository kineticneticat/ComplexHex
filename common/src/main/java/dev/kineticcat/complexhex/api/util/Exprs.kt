package dev.kineticcat.complexhex.api.util

import dev.kineticcat.complexhex.api.log
import org.joml.Vector3d
import java.text.DecimalFormat
import kotlin.math.*

abstract class UnaryOp(open val A: Expr): Expr() {
    override fun equals(other: Any?): Boolean = other is UnaryOp && this.A == other.A
    override fun args(): List<Expr> = listOf(A)
    override fun substitute(from: Expr, to: Expr): Expr = if (this == from) to
    else javaClass.constructors[0].newInstance(A.substitute(from, to)) as Expr
    fun simplifyIns() = javaClass.constructors[0].newInstance(A.simplify()) as Expr
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + A.hashCode()
        return result
    }
}
abstract class BinaryOp(open val A: Expr, open val B: Expr): Expr() {
    override fun equals(other: Any?): Boolean = other is BinaryOp && this.A == other.A && this.B == other.B
    override fun args(): List<Expr> = listOf(A, B)

    override fun substitute(from: Expr, to: Expr): Expr = if (this == from) to
    else javaClass.constructors[0].newInstance(A.substitute(from, to), B.substitute(from, to)) as Expr

    fun simplifyIns(): Expr = javaClass.constructors[0].newInstance(A.simplify(), B.simplify()) as Expr
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + A.hashCode()
        result = 31 * result + B.hashCode()
        return result
    }
}

abstract class Value<T>(@JvmField val value: T): Expr() {
    override fun equals(other: Any?): Boolean = other is Number && this.value == other.value
    abstract operator fun plus(that: T): Expr
    abstract operator fun <S> plus(that: Value<S>): Expr
    abstract operator fun minus(that: T): Expr
    abstract operator fun <S> minus(that: Value<S>): Expr
    abstract operator fun times(that: T): Expr
    abstract operator fun <S> times(that: Value<S>): Expr
    abstract operator fun div(that: T): Expr
    abstract operator fun <S> div(that: Value<S>): Expr
    abstract fun isZero(): Boolean
    abstract fun isOne(): Boolean
    fun equals(that: Value<T>) = this.value == that.value
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}

class Number(@JvmField val x: Double): Value<Double>(x) {
    private val format = DecimalFormat("###,###.##")
    override fun simplify(): Expr = this

    override fun args(): List<Expr> = listOf()

    override fun diff(wrt: Expr): Expr = C0

    override fun toString(): String = format.format(x)

    override operator fun plus(that: Double): Expr = Number(x + that)
    override operator fun <S> plus(that: Value<S>): Expr =
        when (that.value) {
            is Double -> Number(x + that.value)
            else -> Add(this, that)
        }
    override operator fun minus(that: Double): Expr = Number(x - that)
    override operator fun <S> minus(that: Value<S>): Expr =
        when (that.value) {
            is Double -> Number(x - that.value)
            else -> Sub(this, that)
        }
    override operator fun times(that: Double): Expr = Number(x * that)
    override operator fun <S> times(that: Value<S>): Expr =
        when (that.value) {
            is Double -> Number(x * that.value)
            else -> Mul(this, that)
        }
    override operator fun div(that: Double): Expr = Number(x / that)
    override operator fun <S> div(that: Value<S>): Expr =
        when (that.value) {
            is Double -> Number(x / that.value)
            else -> Div(this, that)
        }

    override fun isZero(): Boolean = x == 0.0
    override fun isOne(): Boolean = x == 1.0

    companion object {
        val E = Number(Math.E)
        val PI = Number(Math.PI)
        val C0 = Number(0.0)
        val C1 = Number(1.0)
        val N1 = Number(-1.0)
    }
}

class Vector(@JvmField val vec: Vector3d): Value<Vector3d>(vec) {
    constructor(x: Double, y: Double, z: Double) : this(Vector3d(x, y, z))
    override fun plus(that: Vector3d): Expr = Vector(vec.add(that))
    override fun <S> plus(that: Value<S>): Expr =
        when (that.value) {
            is Vector3d -> Vector(vec.add(that.value))
            is Double -> Vector(vec.add(that.value, that.value, that.value))
            else -> Add(this, that)
        }

    override fun minus(that: Vector3d): Expr = Vector(value.sub(that))
    override fun <S> minus(that: Value<S>): Expr  =
        when (that.value) {
            is Vector3d -> Vector(vec.sub(that.value))
            is Double -> Vector(vec.sub(that.value, that.value, that.value))
            else -> Add(this, that)
        }

    override fun times(that: Vector3d): Expr = Vector(value.mul(that))
    override fun <S> times(that: Value<S>): Expr =
        when (that.value) {
            is Vector3d -> Number(vec.dot(that.value))
            is Double -> Vector(vec.mul(that.value))
            else -> Add(this, that)
        }

    override fun div(that: Vector3d): Expr = Vector(value.div(that))
    override fun <S> div(that: Value<S>): Expr =
        when (that.value) {
            is Vector3d -> Vector(vec.cross(that.value))
            is Double -> Vector(vec.div(that.value))
            else -> Add(this, that)
        }

    override fun diff(wrt: Expr): Expr = ZERO

    override fun toString(): String = "( ${vec.x}, ${vec.y}, ${vec.z} )"

    override fun isZero(): Boolean = vec == ZERO.vec
    override fun isOne(): Boolean = vec == ONE.vec
    companion object {
        val ZERO = Vector(0.0, 0.0, 0.0)
        val ONE = Vector(1.0, 1.0, 1.0)
        val I = Vector(1.0, 0.0, 0.0)
        val J = Vector(0.0, 1.0, 0.0)
        val K = Vector(0.0, 0.0, 1.0)
    }
}

class Symbol(var label: String): Expr() {

    override fun simplify(): Expr = this

    override fun equals(other: Any?): Boolean = other is Symbol && this.label == other.label

    override fun args(): List<Expr> = listOf()

    override fun diff(wrt: Expr): Expr = if (this == wrt) Number.C1 else Number.C0

    override fun toString(): String = label
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

    companion object {
        @JvmField
        val T = Symbol("t")
        @JvmField
        val U = Symbol("u")
        @JvmField
        val V = Symbol("v")
        @JvmField
        val X = Symbol("x")
        @JvmField
        val Y = Symbol("y")
        @JvmField
        val Z = Symbol("z")
    }
}
class Infinity: Expr() {
    override fun diff(wrt: Expr): Expr = Number(666666.0)
    override fun toString(): String = "oo"
}
class Undefined: Expr() {
    override fun diff(wrt: Expr): Expr = Number(666666.0)
    override fun toString(): String = "undef"
}

class Add(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A is Value<*> && A.isZero()) -> B.simplify()
        (B is Value<*> && B.isZero()) -> A.simplify()
        (A is Value<*> && B is Value<*>) -> A + B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = A.diff(wrt) + B.diff(wrt)
    override fun toString(): String = "$A + $B"
}
class Sub(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A is Value<*> && A.isZero()) -> B.simplify() * -1.0
        (B is Value<*> && B.isZero()) -> A.simplify()
        (A is Value<*> && B is Value<*>) -> A + B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = A.diff(wrt) - B.diff(wrt)
    override fun toString(): String = "$A - $B"
}
class Mul(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        ((A is Value<*> && A.isZero()) || B is Value<*> && B.isZero()) -> Number.C0
        (A is Value<*> && A.isOne()) -> B
        (B is Value<*> && B.isOne()) -> A
        (A is Value<*> && B is Value<*>) -> A * B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = (A.diff(wrt) * B) + (A * B.diff(wrt))
    override fun toString(): String = "$A * $B"
}
class Div(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Number.C0 -> Number.C0
        A == Vector.ZERO -> Vector.ZERO
        B == Number.C0 -> Infinity()
        B == Vector.ZERO -> Vector.ZERO
        B == Number.C1 -> A
        A is Value<*> && B is Value<*> -> A / B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = ((A.diff(wrt) * B) - (A * B.diff(wrt))) / Pow(B, Number(2.0))
    override fun toString(): String = "$A / $B"
}

class Pow(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Number.C1 -> Number.C1
        B == Number.C1 -> A
        A == Number.C0 -> Number.C0
        B == Number.C0 -> Number.C1
        A is Number && B is Number -> Number(A.x.pow(B.x))
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr {
        return if (B is Number) {
            B * Pow(A, B - 1.0)
        } else {
            val log = Log(B, Number.E)
            val term1 = B.diff(wrt) * log
            val term2 = B * A.diff(wrt) / A
            this * (term1 + term2)
        }
    }
    override fun toString(): String = "($A ^ $B)"
}

class Log(val n: Expr, val base: Expr): BinaryOp(n, base) {
    override fun simplify(): Expr = when {
        n == Number.C1 -> Number.C0
        base == Number.C1 -> Infinity()
        n == Number.C0 -> Infinity()
        base == Number.C0 -> Infinity()
        n is Number && base is Number -> Number(log(n.x, base.x))
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Number.C1 / (base * Log(n, Number.E))
    override fun toString(): String = "log_$n($base)"
}

class Sin(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Number -> Number(sin(A.x))
        is ArcSin -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Cos(A) * A.diff(wrt)
    override fun toString(): String = "sin($A)"
}
class Cos(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Number -> Number(cos(A.x))
        is ArcCos -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Sin(A) * -1.0 * A.diff(wrt)
    override fun toString(): String = "cos($A)"
}
class Tan(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Number -> Number(tan(A.x))
        is ArcTan -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Number.C1 / Pow(Cos(A), Number(2.0)) * A.diff(wrt)
    override fun toString(): String = "tan($A)"
}
class ArcSin(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Number -> Number(asin(A.x))
        is Sin -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Number.C1 / Pow(Number.C1 - Pow(A, Number(2.0)), Number(0.5)) * A.diff(wrt)
    override fun toString(): String = "asin($A)"
}
class ArcCos(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Number -> Number(acos(A.x))
        is Cos -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr =  Number(-1.0) / Pow(Number.C1 - Pow(A, Number(2.0)), Number(0.5)) * A.diff(wrt)
    override fun toString(): String = "acos($A)"
}
class ArcTan(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Number -> Number(atan(A.x))
        is Tan -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Number.C1 / Number.C1 + Pow(A, Number(2.0)) * A.diff(wrt)
    override fun toString(): String = "atan($A)"
}
class ArcTan2(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Number && B is Number) Number(atan2(A.x, B.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Number(666666.0)
    override fun toString(): String = "atan2($A)"
}

class Abs(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        Number.C0 -> A
        is Number -> Number(abs(A.x))
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Sign(A) * A.diff(wrt)
    override fun toString(): String = "|$A|"
}

class Sign(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Number) Number(sign(A.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Number.C0
    override fun toString(): String = "sign($A)"
}

class Floor(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Number) Number(floor(A.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Number.C0
    override fun toString(): String = "⌊$A⌋"
}
class Ceiling(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Number) Number(ceil(A.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Number.C0
    override fun toString(): String = "⌈$A⌉"
}

class Modulo(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Number && B is Number) Number(A.x % B.x) else simplifyIns()
    override fun diff(wrt: Expr): Expr = A.diff(wrt)
    override fun toString(): String = "mod($A, $B)"
}
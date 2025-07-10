package dev.kineticcat.complexhex.api.util

import dev.kineticcat.complexhex.api.log
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

class Value(@JvmField val x: Double): Expr() {
    val format = DecimalFormat("###,###.##")
    override fun simplify(): Expr = this

    override fun equals(other: Any?): Boolean = other is Value && this.x == other.x

    override fun args(): List<Expr> = listOf()

    override fun diff(wrt: Expr): Expr = Value.C0

    override fun toString(): String = format.format(x)

    override operator fun plus(that: Double): Expr = Value(x + that)
    operator fun plus(that: Value): Expr = Value(x + that.x)
    override operator fun minus(that: Double): Expr = Value(x - that)
    operator fun minus(that: Value): Expr = Value(x - that.x)
    override operator fun times(that: Double): Expr = Value(x * that)
    operator fun times(that: Value): Expr = Value(x * that.x)
    override operator fun div(that: Double): Expr = Value(x / that)
    operator fun div(that: Value): Expr = Value(x / that.x)
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + x.hashCode()
        return result
    }

    companion object {
        val E = Value(Math.E)
        val PI = Value(Math.PI)
        val C0 = Value(0.0)
        val C1 = Value(1.0)
        val N1 = Value(-1.0)
    }
}

class Symbol(var label: String): Expr() {

    override fun simplify(): Expr = this

    override fun equals(other: Any?): Boolean = other is Symbol && this.label == other.label

    override fun args(): List<Expr> = listOf()

    override fun diff(wrt: Expr): Expr = if (this == wrt) Value.C1 else Value.C0

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
    override fun diff(wrt: Expr): Expr = Value(666666.0)
    override fun toString(): String = "oo"
}
class Undefined: Expr() {
    override fun diff(wrt: Expr): Expr = Value(666666.0)
    override fun toString(): String = "undef"
}

class Add(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A == Value.C0) -> B.simplify()
        (B == Value.C0) -> A.simplify()
        (A is Value && B is Value) -> A + B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = A.diff(wrt) + B.diff(wrt)
    override fun toString(): String = "$A + $B"
}
class Sub(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A == Value.C0) -> B * -1.0
        (B == Value.C0) -> A
        (A is Value && B is Value) -> A + B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = A.diff(wrt) - B.diff(wrt)
    override fun toString(): String = "$A - $B"
}
class Mul(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A == Value.C0 || B == Value.C0) -> Value.C0
        (A == Value.C1) -> B
        (B == Value.C1) -> A
        (A is Value && B is Value) -> A * B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = (A.diff(wrt) * B) + (A * B.diff(wrt))
    override fun toString(): String = "$A * $B"
}
class Div(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Value.C0 -> Value.C0
        B == Value.C0 -> Infinity()
        A == Value.C1 -> A / B
        B == Value.C1 -> A
        A is Value && B is Value -> A / B
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = ((A.diff(wrt) * B) - (A * B.diff(wrt))) / Pow(B, Value(2.0))
    override fun toString(): String = "$A / $B"
}

class Pow(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Value.C1 -> Value.C1
        B == Value.C1 -> A
        A == Value.C0 -> Value.C0
        B == Value.C0 -> Value.C1
        A is Value && B is Value -> Value(A.x.pow(B.x))
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr {
        return if (B is Value) {
            B * Pow(A, B - 1.0)
        } else {
            val log = Log(B, Value.E)
            val term1 = B.diff(wrt) * log
            val term2 = B * A.diff(wrt) / A
            this * (term1 + term2)
        }
    }
    override fun toString(): String = "($A ^ $B)"
}

class Log(val n: Expr, val base: Expr): BinaryOp(n, base) {
    override fun simplify(): Expr = when {
        n == Value.C1 -> Value.C0
        base == Value.C1 -> Infinity()
        n == Value.C0 -> Infinity()
        base == Value.C0 -> Infinity()
        n is Value && base is Value -> Value(log(n.x, base.x))
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Value.C1 / (base * Log(n, Value.E))
    override fun toString(): String = "log_$n($base)"
}

class Sin(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Value -> Value(sin(A.x))
        is ArcSin -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Cos(A) * A.diff(wrt)
    override fun toString(): String = "sin($A)"
}
class Cos(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Value -> Value(cos(A.x))
        is ArcCos -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Sin(A) * -1.0 * A.diff(wrt)
    override fun toString(): String = "cos($A)"
}
class Tan(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Value -> Value(tan(A.x))
        is ArcTan -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Value.C1 / Pow(Cos(A), Value(2.0)) * A.diff(wrt)
    override fun toString(): String = "tan($A)"
}
class ArcSin(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Value -> Value(asin(A.x))
        is Sin -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Value.C1 / Pow(Value.C1 - Pow(A, Value(2.0)), Value(0.5)) * A.diff(wrt)
    override fun toString(): String = "asin($A)"
}
class ArcCos(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Value -> Value(acos(A.x))
        is Cos -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr =  Value(-1.0) / Pow(Value.C1 - Pow(A, Value(2.0)), Value(0.5)) * A.diff(wrt)
    override fun toString(): String = "acos($A)"
}
class ArcTan(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        is Value -> Value(atan(A.x))
        is Tan -> A.A
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Value.C1 / Value.C1 + Pow(A, Value(2.0)) * A.diff(wrt)
    override fun toString(): String = "atan($A)"
}
class ArcTan2(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(atan2(A.x, B.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Value(666666.0)
    override fun toString(): String = "atan2($A)"
}

class Abs(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when (A) {
        Value.C0 -> A
        is Value -> Value(abs(A.x))
        else -> simplifyIns()
    }
    override fun diff(wrt: Expr): Expr = Sign(A) * A.diff(wrt)
    override fun toString(): String = "|$A|"
}

class Sign(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(sign(A.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Value.C0
    override fun toString(): String = "sign($A)"
}

class Floor(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(floor(A.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Value.C0
    override fun toString(): String = "⌊$A⌋"
}
class Ceiling(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(ceil(A.x)) else simplifyIns()
    override fun diff(wrt: Expr): Expr = Value.C0
    override fun toString(): String = "⌈$A⌉"
}

class Modulo(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(A.x % B.x) else simplifyIns()
    override fun diff(wrt: Expr): Expr = A.diff(wrt)
    override fun toString(): String = "mod($A, $B)"
}
package dev.kineticcat.complexhex.api.util

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import net.minecraft.world.phys.Vec3
import java.text.DecimalFormat
import kotlin.math.*

abstract class UnaryOp(open val A: Expr): Expr() {
    override fun equals(other: Any?): Boolean = other is UnaryOp && this::class == other::class && this.A == other.A
    override fun args(): List<Expr> = listOf(A)
    override fun contains(that: Expr) = super.contains(that) || A.contains(that)

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
    override fun equals(other: Any?): Boolean = other is BinaryOp && this::class == other::class && this.A == other.A && this.B == other.B
    override fun args(): List<Expr> = listOf(A, B)

    override fun contains(that: Expr) = super.contains(that) || A.contains(that) || B.contains(that)

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

    override fun diff(wrt: Symbol): Expr = ZERO

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
        val ZERO = Value(0.0)
        val ONE = Value(1.0)
        val N1 = Value(-1.0)
    }
}

class Symbol(var label: String): Expr() {

    override fun simplify(): Expr = this

    override fun equals(other: Any?): Boolean = other is Symbol && this.label == other.label

    override fun args(): List<Expr> = listOf()

    override fun diff(wrt: Symbol): Expr = if (this == wrt) Value.ONE else Value.ZERO

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
class Infinity(val positive: Boolean = true): Expr() {
    override fun diff(wrt: Symbol): Expr = Value(666666.0)
    override fun toString(): String = "oo"
}
class Undefined: Expr() {
    override fun diff(wrt: Symbol): Expr = Value(666666.0)
    override fun toString(): String = "undef"
}

class Add(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Value.ZERO -> B.simplify()
        B == Value.ZERO -> A.simplify()
        A is Value && B is Value -> A + B
        A is Value && B is Vector && B.isPure() -> B + A
        A is Vector && A.isPure() && B is Value -> A + B
        A is Vector && B is Vector -> A + B
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) + B.diff(wrt)
    override fun toString(): String = "($A + $B)"
}
class Sub(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Value.ZERO -> B * -1.0
        B == Value.ZERO -> A
        A is Value && B is Value -> A - B
        A is Value && B is Vector && B.isPure() -> (B - A) * -1.0
        A is Vector && A.isPure() && B is Value -> A - B
        A is Vector && B is Vector -> A - B
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) - B.diff(wrt)
    override fun toString(): String = "($A - $B)"
}
class Mul(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A == Value.ZERO || B == Value.ZERO) -> Value.ZERO
        (A == Value.ONE) -> B
        (B == Value.ONE) -> A
        A == B -> Sqr(A)
        A is Div && A.B == B -> A.A
        B is Div && B.B == A -> B.A
        A is Mul && (A.A == Value.N1 || A.B == Value.N1) && B is Mul && (B.A == Value.N1 || B.B == Value.N1) -> Mul(A / -1.0, B / -1.0).simplify()
        (A is Value && B is Value) -> A * B
        A is Value && B is Vector && B.isPure() -> A * B
        A is Vector && A.isPure() && B is Value -> B * A
        A is Vector && B is Vector -> A * B
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = (A.diff(wrt) * B) + (A * B.diff(wrt))
    override fun toString(): String = if (B == Value.N1) "-$A" else "($A * $B)"
}
class Div(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Value.ZERO -> Value.ZERO
        B == Value.ZERO -> Infinity()
        B == Value.ONE -> A
        A is Mul && A.A == B -> A.B
        A is Mul && A.B == B -> A.A
        A is Value && B is Value -> A / B
        A is Value && B is Vector && B.isPure() -> Value.ONE / (B / A)
        A is Vector && A.isPure() && B is Value -> A / B
        A is Vector && B is Vector -> A / B
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = ((A.diff(wrt) * B) - (A * B.diff(wrt))) / Sqr(B)
    override fun toString(): String = "($A / $B)"
}

class Pow(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = when {
        A == Value.ONE -> Value.ONE
        B == Value.ONE -> A
        A == Value.ZERO -> Value.ZERO
        B == Value.ZERO -> Value.ONE
        A is Mul -> Mul(Pow(A.A, B), Pow(A.B, B))
        A is Value && B is Value -> Value(A.x.pow(B.x))
        A is Value && B is Vector && B.isPure() -> Vector.binaryElementwise(B, A) { a, b -> Pow(a, b)}
        A is Vector && A.isPure() && B is Value -> Vector.binaryElementwise(A, B) { a, b -> Pow(a, b)}
        A is Vector && A.isPure() && B is Vector && B.isPure() -> Vector.binaryElementwise(A, B) { a, b -> Pow(a, b)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr {
        return if (B is Value) {
            B * Pow(A, B - 1.0)
        } else {
            val log = Log(B, Value.E)
            val term1 = B.diff(wrt) * log
            val term2 = B * A.diff(wrt) / A
            this * (term1 + term2)
        }
    }
    override fun toString(): String = "($A ** $B)"
}
fun Sqrt(A: Expr) = Pow(A, Value(0.5))
fun Sqr(A: Expr) = Pow(A, Value(2.0))

class Log(val n: Expr, val base: Expr): BinaryOp(n, base) {
    override fun simplify(): Expr = when {
        n == Value.ONE -> Value.ZERO
        base == Value.ONE -> Infinity()
        n == Value.ZERO -> Infinity()
        base == Value.ZERO -> Infinity()
        n is Value && base is Value -> Value(log(n.x, base.x))
        A is Value && B is Vector && B.isPure() -> Vector.binaryElementwise(B, A) { a, b -> Log(a, b)}
        A is Vector && A.isPure() && B is Value -> Vector.binaryElementwise(A, B) { a, b -> Log(a, b)}
        A is Vector && A.isPure() && B is Vector && B.isPure() -> Vector.binaryElementwise(A, B) { a, b -> Log(a, b)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = 1.0 / (base * Ln(n))
    override fun toString(): String = "log_$n($base)"
}
fun Ln(A: Expr) = Log(A, Value.E)

class Sin(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(sin(A.x))
        A is ArcSin -> A.A
        A is Undefined -> Undefined()
        A is Infinity -> Undefined()
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> Sin(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = Cos(A) * A.diff(wrt)
    override fun toString(): String = "sin($A)"
}
class Cos(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(cos(A.x))
        A is ArcCos -> A.A
        A is Undefined -> Undefined()
        A is Infinity -> Undefined()
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> Cos(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = -1.0 * A.diff(wrt) * Sin(A)
    override fun toString(): String = "cos($A)"
}
class Tan(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(tan(A.x))
        A is ArcTan -> A.A
        A is Undefined -> Undefined()
        A is Infinity -> Undefined()
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> Tan(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / Sqr(Cos(A))
    override fun toString(): String = "tan($A)"
}
class ArcSin(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value && abs(A.x) > 1 -> Undefined()
        A is Value -> Value(asin(A.x))
        A is Sin -> A.A
        A is Undefined -> Undefined()
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> ArcSin(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / Sqrt(1.0 - Sqr(A))
    override fun toString(): String = "asin($A)"
}
class ArcCos(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value && abs(A.x) > 1 -> Undefined()
        A is Value -> Value(acos(A.x))
        A is Cos -> A.A
        A is Undefined -> Undefined()
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> ArcCos(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr =  Value.N1 * A.diff(wrt) / Sqrt(1.0 - Sqr(A))
    override fun toString(): String = "acos($A)"
}
class ArcTan(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(atan(A.x))
        A is Tan -> A.A
        A is Undefined -> Undefined()
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> ArcTan(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / (1.0 + Sqr(A))
    override fun toString(): String = "atan($A)"
}
class ArcTan2(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr =
        when {
            A == Value.ZERO && B == Value.ZERO -> Value.ZERO
            A == Value.ZERO && B is Value -> if (B.x > 0.0) Value.ZERO else Value.PI
            A is Value && B is Value -> Value(atan2(A.x, B.x))
            else -> simplifyIns()
        }
    override fun diff(wrt: Symbol): Expr = Value(666666.0)
    override fun toString(): String = "atan2($A, $B)"
}

class Abs(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A == Value.ZERO -> A
        A is Value -> Value(abs(A.x))
        A is Vector && A.isPure() -> Pow(A * A, Value(0.5))
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = Sign(A) * A.diff(wrt)
    override fun toString(): String = "|$A|"
}

class Sign(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(sign(A.x)) else simplifyIns()
    override fun diff(wrt: Symbol): Expr = Value.ZERO
    override fun toString(): String = "sign($A)"
}

class Floor(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(floor(A.x)) else simplifyIns()
    override fun diff(wrt: Symbol): Expr = Value.ZERO
    override fun toString(): String = "⌊$A⌋"
}
class Ceiling(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(ceil(A.x)) else simplifyIns()
    override fun diff(wrt: Symbol): Expr = Value.ZERO
    override fun toString(): String = "⌈$A⌉"
}

class Modulo(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(A.x % B.x) else simplifyIns()
    override fun diff(wrt: Symbol): Expr = A.diff(wrt)
    override fun toString(): String = "mod($A, $B)"
}

abstract class ActuallyBinaryOp(override val A: Expr, override val B: Expr): BinaryOp(A, B) {
    override fun diff(wrt: Symbol): Expr = Value.ZERO
}

class Equals(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = when {
        (A is Value && B is Value) -> if (A.x == B.x) Value.ONE else Value.ZERO
        (A is Vector && B is Vector) -> if (A == B) Value.ONE else Value.ZERO
        else -> simplifyIns()
    }
    override fun toString(): String = "[$A == $B]"
}
class LessThan(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if (A.x < B.x) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A < $B]"
}
class LessThanOrEq(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if (A.x <= B.x) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A <= $B]"
}
class GreaterThan(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if (A.x > B.x) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A > $B]"
}
class GreaterThanOrEq(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if (A.x >= B.x) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A >= $B]"
}

class And(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if ((A.x != 0.0) and (B.x != 0.0)) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A && $B]"
}
class Or(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if ((A.x != 0.0) or (B.x != 0.0)) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A || $B]"
}
class Xor(override val A: Expr, override val B: Expr): ActuallyBinaryOp(A, B) {
    override fun simplify(): Expr = if (A is Value && B is Value) Value(if ((A.x != 0.0) xor (B.x != 0.0)) 1.0 else 0.0) else simplifyIns()
    override fun toString(): String = "[$A ^ $B]"
}
class Not(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = if (A is Value) Value(if ((A.x != 0.0)) 1.0 else 0.0) else simplifyIns()
    override fun diff(wrt: Symbol): Expr = Value.ZERO
    override fun toString(): String = "[¬$A]"
}

class Piecewise(val condition: Expr, val ifTrue: Expr, val ifFalse: Expr): Expr() {
    override fun simplify(): Expr {
        return when {
            condition is Value -> if (condition.x == 0.0) ifFalse.simplify() else ifTrue.simplify()
            condition.simp() is Value -> if ((condition.simp() as Value).x == 0.0) ifFalse.simplify() else ifTrue.simplify()
            else -> Piecewise(condition.simplify(), ifTrue.simplify(), ifFalse.simplify())
        }
    }
    override fun substitute(from: Expr, to: Expr): Expr = if (this == from) to
    else Piecewise(condition.substitute(from, to), ifTrue.substitute(from, to), ifFalse.substitute(from, to))
    override fun diff(wrt: Symbol): Expr = Piecewise(condition, ifTrue.diff(wrt), ifFalse.diff(wrt))

    override fun contains(that: Expr): Boolean = super.contains(that) || condition.contains(that) || ifTrue.contains(that) || ifFalse.contains((that))

    override fun toString(): String = "{$condition: $ifTrue, $ifFalse}"

}

class Vector(@JvmField val x: Expr, @JvmField val y: Expr, @JvmField val z: Expr): Expr() {
    constructor(x: Double, y: Double, z: Double): this(Value(x), Value(y), Value(z))
    constructor(vec: Vec3): this(vec.x, vec.y, vec.z)
    override fun diff(wrt: Symbol): Expr = Vector(x.diff(wrt), y.diff(wrt), z.diff(wrt))
    override fun contains(that: Expr): Boolean = super.contains(that) || x.contains(that) || y.contains(that) || z.contains(that)
    override fun toString(): String = "($x, $y, $z)"
    override fun simplify(): Expr {
        val X = x.simplify()
        return Vector(X, y.simplify(), z.simplify())
    }

    override fun equals(other: Any?): Boolean {
        return other is Vector && this.x == other.x && this.y == other.y && this.z == other.z
    }

    override fun substitute(from: Expr, to: Expr): Expr = if (from == this) to
    else Vector(x.substitute(from, to), y.substitute(from, to), z.substitute(from, to))

    operator fun plus(that: Vector) = binaryElementwise(this, that) {a, b -> a+b}
    override operator fun plus(that: Expr) = binaryElementwise(this, that) { a, b -> a+b}
    operator fun minus(that: Vector) = binaryElementwise(this, that) {a, b -> a-b}
    override operator fun minus(that: Expr) = binaryElementwise(this, that) { a, b -> a-b}
    operator fun times(that: Vector): Expr = this.x * that.x + this.y * that.y + this.z * that.z
    override operator fun times(that: Expr) = binaryElementwise(this, that) { a, b -> a*b}
    operator fun div(that: Vector): Expr = Vector(this.y * that.z - this.z * that.y, this.z * that.x - this.x * that.z, this.x * this.y - this.y * that.x)
    override operator fun div(that: Expr) = binaryElementwise(this, that) { a, b -> a/b}

    fun isPure() = x is Value && y is Value && z is Value
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    companion object {
        fun binaryElementwise(A: Vector, B: Vector, op: (Expr, Expr) -> Expr) = Vector(op(A.x, B.x), op(A.y, B.y), op(A.z, B.z))
        fun binaryElementwise(A: Vector, B: Expr, op: (Expr, Expr) -> Expr) = Vector(op(A.x, B), op(A.y, B), op(A.z, B))
        fun unaryElementwise(A: Vector, op: (Expr) -> Expr) = Vector(op(A.x), op(A.y), op(A.z))
    }

}

class Sinh(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(sinh(A.x))
        A is ArcSinh -> A.A
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> Sinh(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = Cosh(A) * A.diff(wrt)
    override fun toString(): String = "sinh($A)"
}
class Cosh(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(cosh(A.x))
        A is ArcCosh -> A.A
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> Cosh(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = Sinh(A) * A.diff(wrt)
    override fun toString(): String = "cosh($A)"
}
class Tanh(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(tanh(A.x))
        A is ArcTanh -> A.A
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> Tanh(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / Sqr(Cosh(A))
    override fun toString(): String = "tanh($A)"
}
class ArcSinh(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(asinh(A.x))
        A is Sinh -> A.A
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> ArcSinh(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / Sqrt(Sqr(A) + 1.0)
    override fun toString(): String = "asinh($A)"
}
class ArcCosh(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(acosh(A.x))
        A is Cosh -> A.A
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> ArcCosh(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / (Sqrt(A - 1.0) * Sqrt(A + 1.0))
    override fun toString(): String = "acosh($A)"
}
class ArcTanh(override val A: Expr): UnaryOp(A) {
    override fun simplify(): Expr = when {
        A is Value -> Value(atanh(A.x))
        A is ArcSin -> A.A
        A is Vector && A.isPure() -> Vector.unaryElementwise(A) {a -> ArcTanh(a)}
        else -> simplifyIns()
    }
    override fun diff(wrt: Symbol): Expr = A.diff(wrt) / (1.0 - Sqr(A))
    override fun toString(): String = "atanh($A)"
}

operator fun Double.plus(that: Expr) = Add(Value(this), that)
operator fun Double.minus(that: Expr) = Sub(Value(this), that)
operator fun Double.times(that: Expr) = Mul(Value(this), that)
operator fun Double.div(that: Expr) = Div(Value(this), that)

package dev.kineticcat.complexhex.api.util

import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.casting.iota.ExprIota
import dev.kineticcat.complexhex.util.ExprDeSer
import net.minecraft.nbt.CompoundTag

//import at.petrak.hexcasting.api.casting.iota.Iota
//import dev.kineticcat.complexhex.api.casting.iota.ExprIota

abstract class Expr {
    open fun simplify(): Expr = this
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }
    open fun args(): List<Expr>? = null
    abstract fun diff(wrt: Symbol): Expr

    open fun contains(that: Expr): Boolean = this == that

    open fun substitute(from: Expr, to: Expr) = if (this == from) to else this
    abstract override fun toString(): String
    override fun hashCode(): Int {
        var result = super.hashCode()
        args().let {x -> x ?: listOf()}.forEach { x -> result += 31 * result + x.hashCode() }
        return result
    }

    open operator fun plus(that: Expr): Expr =
        when {
            that == Value.ZERO -> this
            this == Value.ZERO -> that
            else -> Add(this, that)
        }
    open operator fun plus(that: Double): Expr = Add(this, Value(that))
    open operator fun minus(that: Expr): Expr =
        when {
            that == Value.ZERO -> this
            this == Value.ZERO -> that * -1.0
            else -> Sub(this, that)
        }
    open operator fun minus(that: Double): Expr = Sub(this, Value(that))
    open operator fun times(that: Expr): Expr =
        when {
            that == Value.ZERO -> Value.ZERO
            this == Value.ZERO -> Value.ZERO
            this == Value.ONE -> that
            that == Value.ONE -> this
            else -> Mul(this, that)
        }
    open operator fun times(that: Double): Expr = Mul(this, Value(that))
    open operator fun div(that: Expr): Expr =
        when {
            that == Value.ZERO -> Infinity()
            this == Value.ZERO -> Value.ZERO
            that == Value.ONE -> this
            else -> Div(this, that)
        }
    open operator fun div(that: Double): Expr = Div(this, Value(that))

    fun simp(): Expr {
        var expr = this
        while (expr != expr.simplify()) {
            expr = expr.simplify()
        }
        return expr
    }
    fun subsimp(from: Expr, to: Expr): Expr = substitute(from, to).simp()
    operator fun invoke(from: String, to: Double) = subsimp(Symbol(from), Value(to))
    fun serialise() = ExprDeSer.serialise(this)
    fun asIota(): ExprIota = ExprIota(this)
    fun asActionResult(): List<Iota> = listOf(asIota())

    companion object {
        @JvmStatic
        fun deserialise(ctag: CompoundTag) = ExprDeSer.deserialise(ctag)
    }
}

fun main() {
//    val expr = Vector(-1.0 * Cosh(Symbol.V) * Cos(Symbol.U), -1.0 * Cosh(Symbol.V) * Sin(Symbol.U), Symbol.V)
//    println(expr)
//    val sub = expr("u", 0.0)("v", 0.0)
//    println(sub.simp())
//    val expr = Cos(Value(0.0)).simplify()
//    println(expr)
//    val expr = Symbol.V * 2.0 - 1.0
//    println(expr)
//    for (i in 0 until 10) {
//        print("${i / 10.0} -> ")
//        println(expr("v", i.toDouble()/10.0))
//    }
//    val expr = -1.0 * Cosh(Symbol.V) * Cos(Symbol.U)
//    println(expr.simp())
//    println(expr.diff(Symbol.U).simp())
    val A = Vector(1.0, 2.0, 3.0)
    val B = Vector(3.0, 2.0, 1.0)
    println((A * B).simp())

}
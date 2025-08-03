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
    abstract fun diff(wrt: Expr): Expr

    open fun substitute(from: Expr, to: Expr) = if (this == from) to else this
    abstract override fun toString(): String
    override fun hashCode(): Int {
        var result = super.hashCode()
        args().let {x -> x ?: listOf()}.forEach { x -> result += 31 * result + x.hashCode() }
        return result
    }

    operator fun plus(that: Expr): Expr = Add(this, that)
    open operator fun plus(that: Double): Expr = Add(this, Number(that))
    operator fun minus(that: Expr): Expr = Sub(this, that)
    open operator fun minus(that: Double): Expr = Sub(this, Number(that))
    operator fun times(that: Expr): Expr = Mul(this, that)
    open operator fun times(that: Double): Expr = Mul(this, Number(that))
    operator fun div(that: Expr): Expr = Div(this, that)
    open operator fun div(that: Double): Expr = Div(this, Number(that))

    fun simp(): Expr {
        var expr = this
        while (expr != expr.simplify()) expr = expr.simplify()
        return expr
    }
    fun subsimp(from: Expr, to: Expr): Expr = substitute(from, to).simp()
    operator fun invoke(from: String, to: Double) = subsimp(Symbol(from), Number(to))
    fun serialise() = ExprDeSer.serialise(this)
    fun asIota(): ExprIota = ExprIota(this)
    fun asActionResult(): List<Iota> = listOf(asIota())

    companion object {
        @JvmStatic
        fun deserialise(ctag: CompoundTag) = ExprDeSer.deserialise(ctag)
    }
}

fun main() {
    val expr = Cos(Symbol.T * 4.0 * Number.PI)
    println(expr)
    println(expr.subsimp(Symbol.T, Number(0.5)))
}
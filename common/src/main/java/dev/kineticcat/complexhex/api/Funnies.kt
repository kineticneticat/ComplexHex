package dev.kineticcat.complexhex.api

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.pigment.FrozenPigment
import dev.kineticcat.complexhex.api.casting.iota.ExprIota
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.api.util.Expr
import dev.kineticcat.complexhex.api.util.Quaternion
import dev.kineticcat.complexhex.api.util.Value
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import kotlin.math.ln

// stolen from hexcasting lmao
fun List<Iota>.getQuaternion(idx: Int, argc: Int = 0): Quaternion {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is QuaternionIota) {
        return x.quaternion
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "quaternion")
    }
}
// from hexal
fun nextColour(pigment:FrozenPigment, random: RandomSource): Int {
    return pigment.colorProvider.getColor(
        random.nextFloat() * 16384,
        Vec3(
            random.nextFloat().toDouble(),
            random.nextFloat().toDouble(),
            random.nextFloat().toDouble()
        ).scale((random.nextFloat() * 3).toDouble())
    )
}

fun log(n: Double, b: Double): Double = ln(n) / ln(b)

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
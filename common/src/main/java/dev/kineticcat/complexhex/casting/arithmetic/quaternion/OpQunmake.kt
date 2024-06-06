package dev.kineticcat.complexhex.casting.arithmetic.quaternion

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.stuff.Quaternion
import net.minecraft.world.phys.Vec3
import kotlin.math.sign

object OpQunmake : OperatorBasic(1, QuaternionArithmetic.ACCEPTS_Q) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        val Q = Operator.downcast(it.next(), ComplexHexIotaTypes.QUATERNION).quaternion
        return listOf(DoubleIota(Q.a), Vec3Iota(Vec3(Q.b, Q.c, Q.d)))
    }

}
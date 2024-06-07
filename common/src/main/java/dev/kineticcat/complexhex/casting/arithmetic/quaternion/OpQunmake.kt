package dev.kineticcat.complexhex.casting.arithmetic.quaternion

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import net.minecraft.world.phys.Vec3

object OpQunmake : OperatorBasic(1, QuaternionArithmetic.ACCEPTS_Q) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val Q = (iotas.iterator().next() as QuaternionIota).quaternion
        return listOf(DoubleIota(Q.a), Vec3Iota(Vec3(Q.b, Q.c, Q.d)))
    }
}
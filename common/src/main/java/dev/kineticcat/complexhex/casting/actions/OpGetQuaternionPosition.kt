package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.api.QuaternionTpUtils

object OpGetQuaternionPosition : ConstMediaAction {
    override val argc: Int = 0
    override val mediaCost: Long = MediaConstants.QUENCHED_BLOCK_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return QuaternionTpUtils.getQuaternionPosition(
            env.world.dimension(),
            env.world.seed
        ).Qadd(env.castingEntity?.position()).asActionResult();
    }
}
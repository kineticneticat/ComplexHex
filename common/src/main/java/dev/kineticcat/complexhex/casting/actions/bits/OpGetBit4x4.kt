package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import dev.kineticcat.complexhex.api.toDoubleMatrix
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import net.minecraft.world.entity.Display
import ram.talia.moreiotas.api.asActionResult

object OpGetBit4x4 : ConstMediaAction{
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val e = args.getEntity(0)
        if (e !is Display) throw MishapBadEntity.of(e, "bit")
        env.assertEntityInRange(e)
        return (e as DisplayInvoker).invokeCreateTransformation(e.entityData).matrix.toDoubleMatrix().asActionResult
    }
}


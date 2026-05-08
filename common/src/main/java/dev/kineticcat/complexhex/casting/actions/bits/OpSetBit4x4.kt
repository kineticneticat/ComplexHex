package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.api.toMatrix4f
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import net.minecraft.world.entity.Display
import org.jblas.DoubleMatrix
import org.jblas.ranges.IntervalRange
import ram.talia.moreiotas.api.getMatrix

object OpSetBit4x4 : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0)
        var mat = args.getMatrix(1)
        env.assertEntityInRange(e)
        if (e !is Display) throw MishapBadEntity.of(e, "bit")
        if (!(mat.rows == 4 && mat.columns == 4))

        mat = when (mat.rows to mat.columns) {
            3 to 3 -> DoubleMatrix.eye(4).put(IntervalRange(0,3), IntervalRange(0,3), mat)
            3 to 4 -> DoubleMatrix.eye(4).put(IntervalRange(0,3), IntervalRange(0,4), mat)
            4 to 3 -> DoubleMatrix.eye(4).put(IntervalRange(0,4), IntervalRange(0,3), mat)
            4 to 4 -> mat
            else -> throw MishapInvalidIota.of(args[1], 0, "transformationmatrix")
        }

        return SpellAction.Result(
            Spell(e, mat),
            MediaConstants.DUST_UNIT,
            listOf(ParticleSpray.burst(env.castingEntity?.position() ?: env.mishapSprayPos(), 1.0))
        )
    }

    private data class Spell(val e: Display, val mat: DoubleMatrix) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            (e as DisplayInvoker).invokeSetTransformation(Transformation(mat.toMatrix4f()))
            e.tick()
        }

    }
}
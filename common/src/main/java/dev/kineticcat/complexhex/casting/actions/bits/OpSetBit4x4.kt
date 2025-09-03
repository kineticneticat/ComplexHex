package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.api.toMatrix4f
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import net.minecraft.world.entity.Display
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.getMatrix

object OpSetBit4x4 : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0)
        val mat = args.getMatrix(1)
        env.assertEntityInRange(e)
        if (e !is Display) throw MishapBadEntity.of(e, "bit")
        if (!(mat.rows == 4 && mat.columns == 4)) throw MishapInvalidIota.of(args[1], 0, "4x4matrix")
        return SpellAction.Result(
            Spell(e, mat),
            0L,
            listOf(ParticleSpray.burst(e.position(), 1.0))
        )
    }

    private data class Spell(val e: Display, val mat: DoubleMatrix) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            (e as DisplayInvoker).invokeSetTransformation(Transformation(mat.toMatrix4f()))
            e.tick()
        }

    }
}
package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.api.getQuaternion
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import dev.kineticcat.complexhex.stuff.Quaternion
import net.minecraft.world.entity.Display
import org.joml.Quaternionf


object OpRotateBIT : SpellAction {
    override val argc = 2
    private val cost = 0L
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val quaternion = args.getQuaternion(1, argc)


        env.assertEntityInRange(e)
        if (e !is Display) throw MishapBadEntity.of(e, "bit")

        val pos = (e as Display).position()

        return SpellAction.Result(
            Spell(e, quaternion),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display, val quat: Quaternion) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {

            val oldScale = BIT.entityData.get((BIT as DisplayInvoker).GetScaleDataID())
            val oldPos = BIT.entityData.get((BIT as DisplayInvoker).GetTranslationDataID())

            (BIT as DisplayInvoker).invokeSetTransformation(Transformation(oldPos, Quaternionf(quat), oldScale, null))
            BIT.tick() //??????
        }
    }
}
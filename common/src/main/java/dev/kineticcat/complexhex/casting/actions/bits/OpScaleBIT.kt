package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import net.minecraft.world.entity.Display
import net.minecraft.world.phys.Vec3


object OpScaleBIT : SpellAction {
    override val argc = 2
    private var cost = 0L
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val vec = args.getVec3(1, argc)


        env.assertEntityInRange(e)
        if (e !is Display) throw MishapBadEntity.of(e, "bit")

        cost = vec.length().toLong()

        val pos = (e as Display).position()

        return SpellAction.Result(
            Spell(e, vec),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display, val vec: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {

            val oldRotation = BIT.entityData.get((BIT as DisplayInvoker).GetLeftRotationDataID())
            val oldPos = BIT.entityData.get((BIT as DisplayInvoker).GetTranslationDataID())

            (BIT as DisplayInvoker).invokeSetTransformation(Transformation(oldPos, oldRotation, vec.toVector3f(), null))
            BIT.tick() //??????
        }
    }
}
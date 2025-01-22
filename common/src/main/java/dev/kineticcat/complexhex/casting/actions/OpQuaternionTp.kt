package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.api.QuaternionTpUtils
import dev.kineticcat.complexhex.api.getQuaternion
import dev.kineticcat.complexhex.casting.mishap.MishapBadDimDelta
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

object OpQuaternionTp : SpellAction {
    override val argc: Int = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val kidnapee = args.getEntity(0)
        val delta = args.getQuaternion(1)

        env.assertEntityInRange(kidnapee)

        val plonk = QuaternionTpUtils.getTarget(
            env.world.dimension(),
            delta,
            env.world.seed,
            env.world.server
        ) ?: throw MishapBadDimDelta()


        return SpellAction.Result(
            Spell(kidnapee, plonk.first, plonk.second),
            MediaConstants.QUENCHED_BLOCK_UNIT * 2,
            listOf()
        )
    }
    private data class Spell(val kidnapee: Entity, val target: ResourceKey<Level>, val pos: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
//            env.world.server.getLevel(target)?.let { kidnapee.changeDimension(it) }
//            kidnapee.setPos(pos)
            kidnapee.teleportTo(env.world.server.getLevel(target), pos.x, pos.y, pos.z, emptySet(), kidnapee.yRot, kidnapee.xRot)
        }

    }
}
package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3


object OpToggleParticles : SpellAction {
    override val argc = 0
    var tag = "disable_particles"
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        throw IllegalStateException()
    }

    private val cost = MediaConstants.DUST_UNIT
    override fun executeWithUserdata(
        args: List<Iota>,
        env: CastingEnvironment,
        userData: CompoundTag
    ): SpellAction.Result {


        if (!userData.contains(tag)) {
            userData.putBoolean(tag, true)
        } else {
            userData.putBoolean(tag, !userData.getBoolean(tag))
        }

        return SpellAction.Result(
            Spell(),
            cost,
            listOf(ParticleSpray.burst( if (env.castingEntity is LivingEntity) (env.castingEntity as LivingEntity).position() else Vec3.ZERO, 1.0))
        )
    }

    private class Spell() : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            // bingus bongus it does nothing
        }
    }
}
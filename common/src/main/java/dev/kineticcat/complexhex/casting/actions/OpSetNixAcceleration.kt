package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.entity.NixEntity
import net.minecraft.world.phys.Vec3


object OpSetNixAcceleration : SpellAction {
    override val argc = 2
    private val cost = MediaConstants.CRYSTAL_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val nix = args.getEntity(0, argc)
        // force acc to be mag 1
        val acc = args.getVec3(1, argc).normalize()

        if ( nix !is NixEntity) throw MishapBadEntity.of(nix, "nix")
        env.assertEntityInRange(nix)

        return SpellAction.Result(
            Spell(nix, acc),
            cost,
            listOf(ParticleSpray.burst(nix.position(), 1.0))
        )
    }

    private data class Spell(val nix: NixEntity, val acc: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            nix.acceleration = acc
            nix.age = 0
            nix.tick()
        }
    }
}
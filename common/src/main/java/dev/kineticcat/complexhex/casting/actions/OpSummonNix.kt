package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.api.casting.castables.SpellActionButPushesAnIota
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import dev.kineticcat.complexhex.entity.NixEntity
import net.minecraft.world.phys.Vec3


object OpSummonNix : SpellActionButPushesAnIota {
    override val argc = 1
    private val cost = MediaConstants.CRYSTAL_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellActionButPushesAnIota.Result {
        val pos = args.getVec3(0, argc)

        env.assertVecInRange(pos)
        val nix = NixEntity(ComplexHexEntities.NIX, env.world)

        return SpellActionButPushesAnIota.Result(
            Spell(pos, nix),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0)),
            nix.asActionResult
        )
    }

    private data class Spell(val pos: Vec3, val nix: NixEntity) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            nix.pigment = env.pigment
            nix.setPos(pos)
            env.world.addFreshEntity(nix)
        }
    }
}
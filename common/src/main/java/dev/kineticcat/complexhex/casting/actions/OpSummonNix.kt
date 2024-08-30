package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.casting.mishap.MishapBadString
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import dev.kineticcat.complexhex.entity.NixEntity
import dev.kineticcat.complexhex.mixin.BITInvokers.BlockDisplayInvoker
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import ram.talia.moreiotas.api.getString


object OpSummonNix : SpellAction {
    override val argc = 1
    private val cost = MediaConstants.CRYSTAL_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getVec3(0, argc)

        env.assertVecInRange(pos)

        return SpellAction.Result(
            Spell(pos),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val pos: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val nix = NixEntity(ComplexHexEntities.NIX, env.world)
            nix.pigment = env.pigment
            nix.setPos(pos)
            env.world.addFreshEntity(nix)
        }
    }
}
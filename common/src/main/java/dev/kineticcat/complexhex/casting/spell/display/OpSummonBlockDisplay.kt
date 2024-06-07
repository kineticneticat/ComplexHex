package dev.kineticcat.complexhex.casting.spell.display

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.casting.mishap.MishapBadString
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display.BlockDisplay
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.getString


class OpSummonBlockDisplay : SpellAction {
    override val argc = 2
    private val cost = 2 * MediaConstants.DUST_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getVec3(0, argc)
        val name = args.getString(1, argc)

        env.assertVecInRange(pos)
        val split = name.split(":")
        if (!BuiltInRegistries.BLOCK.containsKey(ResourceLocation(split[0], split[1])))
            throw MishapBadString.of(name, "opsummonblockdisplay")

        return SpellAction.Result(
            Spell(pos, name),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val pos: Vec3, val name: String) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val blockdisplay = BlockDisplay(EntityType.BLOCK_DISPLAY, env.world)
            blockdisplay.setPos(pos)
            env.world.addFreshEntity(blockdisplay)
        }
    }
}

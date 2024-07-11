package dev.kineticcat.complexhex.casting.patterns.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.mixin.BITInvokers.ItemDisplayInvoker
import dev.kineticcat.complexhex.mixin.BITInvokers.TextDisplayInvoker
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.getString


object OpSummonTextDisplay : SpellAction {
    override val argc = 2
    private val cost = MediaConstants.CRYSTAL_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getVec3(0, argc)
        val text = args.getString(1, argc)

        env.assertVecInRange(pos)


        return SpellAction.Result(
            Spell(pos, text),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val pos: Vec3, val text: String) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val textdisplay = Display.TextDisplay(EntityType.TEXT_DISPLAY, env.world).apply {
                setPos(pos.x, pos.y, pos.z);
            }
            (textdisplay as TextDisplayInvoker).invokeSetText(Component.literal(text))
            env.world.addFreshEntity(textdisplay)
            textdisplay.tick()
        }
    }
}
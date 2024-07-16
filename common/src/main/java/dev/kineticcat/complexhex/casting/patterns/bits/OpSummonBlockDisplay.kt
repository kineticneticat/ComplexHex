package dev.kineticcat.complexhex.casting.patterns.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.casting.mishap.MishapBadString
import dev.kineticcat.complexhex.mixin.BITInvokers.BlockDisplayInvoker
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import ram.talia.moreiotas.api.getString
import java.util.regex.Pattern


object OpSummonBlockDisplay : SpellAction {
    override val argc = 2
    private val cost = MediaConstants.CRYSTAL_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getVec3(0, argc)
        val name = args.getString(1, argc)

        env.assertVecInRange(pos)
        if (!BuiltInRegistries.BLOCK.containsKey(ResourceLocation(name)))
            throw MishapBadString.of(name, "blockid")

        val blockstate = BuiltInRegistries.BLOCK.get(ResourceLocation(name)).defaultBlockState()

        return SpellAction.Result(
            Spell(pos, blockstate),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val pos: Vec3, val blockstate: BlockState) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val blockdisplay = Display.BlockDisplay(EntityType.BLOCK_DISPLAY, env.world).apply {
                setPos(pos.x, pos.y, pos.z);
            }
            (blockdisplay as BlockDisplayInvoker).invokeSetBlockState(blockstate)
            (blockdisplay as BlockDisplayInvoker).invokeSetTransformation(Transformation(Vector3f(-0.5f, -0.5f, -0.5f), null, null, null))
            env.world.addFreshEntity(blockdisplay)
            blockdisplay.tick()
        }
    }
}
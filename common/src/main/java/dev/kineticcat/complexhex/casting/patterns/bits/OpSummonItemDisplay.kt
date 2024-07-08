package dev.kineticcat.complexhex.casting.patterns.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.casting.mishap.MishapBadString
import dev.kineticcat.complexhex.mixin.BITInvokers.ItemDisplayInvoker
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.getString


object OpSummonItemDisplay : SpellAction {
    override val argc = 2
    private val cost = 2 * MediaConstants.DUST_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getVec3(0, argc)
        val name = args.getString(1, argc)

        env.assertVecInRange(pos)
        if (!BuiltInRegistries.ITEM.containsKey(ResourceLocation(name)))
            throw MishapBadString.of(name, "opsummonitemdisplay")

        val item = BuiltInRegistries.ITEM.get(ResourceLocation(name))
        val itemstack = ItemStack(item)


        return SpellAction.Result(
            Spell(pos, itemstack),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val pos: Vec3, val itemstack: ItemStack) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val itemdisplay = Display.ItemDisplay(EntityType.ITEM_DISPLAY, env.world).apply {
                setPos(pos.x, pos.y, pos.z);
            }
            (itemdisplay as ItemDisplayInvoker).invokeSetItemStack(itemstack)
            env.world.addFreshEntity(itemdisplay)
            itemdisplay.tick()
        }
    }
}
package dev.kineticcat.complexhex.casting.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.DyeColor

class MishapBadAssembly(val err: Component) : Mishap() {
        override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.LIME)

        override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}

        override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context) = error(
                "bad_assembly",
                err
        )

        companion object {
                @JvmStatic
                fun of(stub: String): MishapBadAssembly {
                        return MishapBadAssembly("complexhex.mishap.bad_assembly.$stub".asTranslatedComponent)
                }
        }
}
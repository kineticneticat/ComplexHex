package dev.kineticcat.complexhex.casting.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.DyeColor

class MishapBadString(val text: String, msg: Component) : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment =
        dyeColor(DyeColor.BROWN)

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {

    }

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context) = error("string")

    companion object {
        @JvmStatic
        fun of(text: String, stub: String): MishapBadString {
            return MishapBadString(text, "complexhex.mishap.bad_string.$stub".asTranslatedComponent)
        }
    }
}
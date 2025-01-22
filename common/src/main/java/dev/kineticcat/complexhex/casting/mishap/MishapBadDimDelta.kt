package dev.kineticcat.complexhex.casting.mishap

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor

class MishapBadDimDelta : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = dyeColor(DyeColor.PINK)

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = error(
        "bad_dimdelta",
    )

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {
        env.castingEntity?.knockback(10.0, (env.world.random.nextDouble()*2)-1, (env.world.random.nextDouble()*2)-1)
    }
}
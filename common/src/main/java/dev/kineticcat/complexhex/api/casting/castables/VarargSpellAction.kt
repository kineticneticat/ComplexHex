package dev.kineticcat.complexhex.api.casting.castables

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.minecraft.nbt.CompoundTag

interface VarargSpellAction : Action {

    fun argc(stack: List<Iota>): Int

    fun hasCastingSound(env: CastingEnvironment): Boolean = true

    fun awardsCastingStat(env: CastingEnvironment): Boolean = true

    fun execute(
        args: List<Iota>,
        argc: Int,
        env: CastingEnvironment
    ): SpellAction.Result

    fun executeWithUserdata(
        args: List<Iota>, argc: Int, env: CastingEnvironment, userData: CompoundTag
    ): SpellAction.Result {
        return this.execute(args, argc, env)
    }

    override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
        val stack = image.stack.toMutableList()
        val argc = this.argc(stack.reversed())

        if (argc > stack.size)
            throw MishapNotEnoughArgs(argc, stack.size)
        val args = stack.takeLast(argc)
        for (_i in 0 until argc) stack.removeLast()

        // execute!
        val userDataMut = image.userData.copy()
        val result = this.executeWithUserdata(args, argc, env, userDataMut)

        val sideEffects = mutableListOf<OperatorSideEffect>()

        if (result.cost > 0)
            sideEffects.add(OperatorSideEffect.ConsumeMedia(result.cost))

        sideEffects.add(
            OperatorSideEffect.AttemptSpell(
                result.effect,
                this.hasCastingSound(env),
                this.awardsCastingStat(env)
            )
        )

        for (spray in result.particles)
            sideEffects.add(OperatorSideEffect.Particles(spray))

        val image2 = image.copy(stack = stack, opsConsumed = image.opsConsumed + result.opCount, userData = userDataMut)

        val sound = if (this.hasCastingSound(env)) HexEvalSounds.SPELL else HexEvalSounds.MUTE
        return OperationResult(image2, sideEffects, continuation, sound)
    }
}
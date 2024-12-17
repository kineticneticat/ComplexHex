package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.casting.actions.OpToggleParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OperatorSideEffect.Particles.class)
public class ToggleParticlesMixin {
    @Inject(
            method = "performEffect(Lat/petrak/hexcasting/api/casting/eval/vm/CastingVM;)Z",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    public void mixinPerformEffect(CastingVM harness, CallbackInfoReturnable<Boolean> cir) {
        Complexhex.LOGGER.info(harness.getImage().getUserData().getBoolean(OpToggleParticles.INSTANCE.getTag()));
        if (harness.getImage().getUserData().contains(OpToggleParticles.INSTANCE.getTag())) {
            if (harness.getImage().getUserData().getBoolean(OpToggleParticles.INSTANCE.getTag())) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}

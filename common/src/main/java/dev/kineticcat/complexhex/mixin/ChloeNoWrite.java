package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MishapOthersName.class)
public interface ChloeNoWrite {
//    @Inject(method="getTrueNameFromDatum()Lnet/minecraft/world/entity/player/Player;", remap = false)
//    private static void wawa(CallbackInfo ci) {
//    }
}

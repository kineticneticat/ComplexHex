package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.client.ClientTickCounter;
import at.petrak.hexcasting.client.render.GaslightingTracker;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kineticcat.complexhex.client.render.CoolerGaslightingTracker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(ClientTickCounter.class)
public class ClientTickCounterMixin {
    @Redirect(method = "clientTickEnd", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/client/render/GaslightingTracker;postFrameCheckRendered()V"), remap = false)
    private static void redirect() {
        for (var gaslight : CoolerGaslightingTracker.GASLIGHTS.entrySet()) {
            gaslight.getValue().postFrameCheckRendered();
        }
        GaslightingTracker.postFrameCheckRendered();
    }

}

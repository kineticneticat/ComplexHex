package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.casting.iota.ChloeIota;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(MishapOthersName.class)
public class ChloeNoWrite {

    @Inject(method="getTrueNameFromDatum", at = @At("HEAD"), remap = false)
    private static void wawa(Iota datum, Player caster, CallbackInfoReturnable<Player> cir) throws MishapInvalidIota {
        Complexhex.LOGGER.info("Mixin Called!");
        if (datum instanceof ChloeIota ichlota && ichlota.state() == ChloeIota.State.STATIC) {
            throw MishapInvalidIota.of(datum, 0, "ichlota.static");
        }
    }
}

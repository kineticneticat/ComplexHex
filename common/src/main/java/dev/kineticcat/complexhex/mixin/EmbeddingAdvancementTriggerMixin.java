package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.common.casting.actions.lists.OpSplat;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dev.kineticcat.complexhex.stuff.ComplexHexAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OpSplat.class)
public class EmbeddingAdvancementTriggerMixin {
    @Inject(method= "execute(Ljava/util/List;Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)Ljava/util/List;", at=@At("TAIL"), remap = false)
    public void TestForEmbed(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        if (!(env.getCastingEntity() != null && env.getCastingEntity() instanceof ServerPlayer player)) return;
        // this is so stupid
        for (Iota iota : Operator.downcast(args.get(0), HexIotaTypes.LIST).getList()) {
            if (!(iota instanceof PatternIota)) {
                ComplexHexAdvancements.EMBEDDING.trigger(player);
            }
        }
    }
}

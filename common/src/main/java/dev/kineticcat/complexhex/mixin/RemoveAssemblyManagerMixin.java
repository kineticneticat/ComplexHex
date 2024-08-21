package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.utils.NBTHelper;
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.kineticcat.complexhex.casting.actions.assemblies.OpBeginAssembly.managerUUIDTag;

@Mixin(CircleExecutionState.class)
public class RemoveAssemblyManagerMixin {
    @Shadow(remap = false)
    public CastingImage currentImage;

    @SuppressWarnings("DataFlowIssue")
    @Inject(
            method = "tick(Lat/petrak/hexcasting/api/casting/circles/BlockEntityAbstractImpetus;)Z",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void tickWrap (BlockEntityAbstractImpetus impetus, CallbackInfoReturnable<Boolean> cir) {
        boolean halt = cir.getReturnValueZ();
        if (!halt) {
            CompoundTag userData = currentImage.getUserData();
            if (NBTHelper.hasUUID(userData, managerUUIDTag)) {
                // has pos therefore OpEndAssembly was never used and so kill the manager
                AssemblyManagerEntity manager = (AssemblyManagerEntity) ((ServerLevel)impetus.getLevel()).getEntity(userData.getUUID(managerUUIDTag));
                if (manager == null) {
                    return; // no manager, what the fuck
                }
                manager.kill(); // murder
            }
        }
    }
}

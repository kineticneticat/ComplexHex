package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.utils.NBTHelper;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.kineticcat.complexhex.api.util.utilStuffLmao;

import static dev.kineticcat.complexhex.casting.actions.assemblies.OpBeginAssembly.managerPosTag;

@Mixin(CircleExecutionState.class)
public class RemoveAssemblyManagerMixin {
    @Shadow(remap = false)
    public CastingImage currentImage;
    @Inject(
            method = "tick(Lat/petrak/hexcasting/api/casting/circles/BlockEntityAbstractImpetus;)Z",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void tickWrap (BlockEntityAbstractImpetus impetus, CallbackInfoReturnable<Boolean> cir) {
        boolean halt = cir.getReturnValueZ();
        if (halt) {
            CompoundTag userData = currentImage.getUserData();

            if (NBTHelper.hasCompound(userData, managerPosTag)) {
                // has pos therefore OpEndAssembly was never used and so kill the manager
                Vec3 managerPos = utilStuffLmao.CompoundTagToVec3(NBTHelper.getCompound(userData, managerPosTag));
                AssemblyManagerEntity manager = utilStuffLmao.getManagerAtPos(managerPos, impetus.getLevel());
                if (manager == null) {
                    Complexhex.LOGGER.error(managerPos);
                    return;
                }
                ; // no manager, what the fuck
                manager.kill(); // murder
            }
        }
    }
}

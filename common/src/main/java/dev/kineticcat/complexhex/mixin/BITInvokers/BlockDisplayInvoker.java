package dev.kineticcat.complexhex.mixin.BITInvokers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.BlockDisplay.class)
public interface BlockDisplayInvoker {
    @Invoker("setBlockState")
    public void invokeSetBlockState(BlockState blockState);
    @Invoker("getBlockState")
    BlockState invokeGetBlockState();
}

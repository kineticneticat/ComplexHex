package dev.kineticcat.complexhex.mixin.BITInvokers;

import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.ItemDisplay.class)
public interface ItemDisplayInvoker {
    @Invoker("setItemStack")
    void invokeSetItemStack(ItemStack itemStack);
    @Invoker("getItemStack")
    ItemStack invokeGetItemstack();
}

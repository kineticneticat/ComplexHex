package dev.kineticcat.complexhex.mixin.BITInvokers;

import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.ItemDisplay.class)
abstract public class ItemDisplayInvoker {
    @Invoker("setItemStack")
    abstract public void invokeSetItemStack(ItemStack itemStack);
}

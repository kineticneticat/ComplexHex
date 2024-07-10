package dev.kineticcat.complexhex.mixin.BITInvokers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
abstract public class TextDisplayInvoker {
    @Invoker("setText")
    abstract public void invokeSetText(Component component);
}

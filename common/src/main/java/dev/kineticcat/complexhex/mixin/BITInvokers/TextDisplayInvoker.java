package dev.kineticcat.complexhex.mixin.BITInvokers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
public interface TextDisplayInvoker {
    @Invoker("setText")
    public void invokeSetText(Component component);
    @Invoker("getText")
    Component invokeGetText();
}

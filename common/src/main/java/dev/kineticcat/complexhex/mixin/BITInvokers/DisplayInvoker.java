package dev.kineticcat.complexhex.mixin.BITInvokers;

import com.mojang.math.Transformation;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.class)
public interface DisplayInvoker {
    @Invoker("setTransformation")
    void invokeSetTransformation(Transformation transformation);
}

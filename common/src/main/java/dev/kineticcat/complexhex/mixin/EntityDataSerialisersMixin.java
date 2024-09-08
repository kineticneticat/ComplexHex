package dev.kineticcat.complexhex.mixin;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityDataSerializers.class)
public interface EntityDataSerialisersMixin {
    @Invoker("registerSerializer")
    static void invokeRegisterSerializer(EntityDataSerializer<?> entityDataSerializer) {
        throw new AssertionError();
    }
}

package dev.kineticcat.complexhex.mixin.BITInvokers;

import com.mojang.math.Transformation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.class)
public interface DisplayInvoker {
    @Invoker("setTransformation")
    void invokeSetTransformation(Transformation transformation);
    @Accessor("renderState")
    Display.RenderState getRenderState();
    @Accessor("DATA_TRANSLATION_ID")
    EntityDataAccessor<Vector3f> GetTranslationDataID();
    @Accessor("DATA_LEFT_ROTATION_ID")
    EntityDataAccessor<Quaternionf> GetLeftRoatationDataID();
    @Accessor("DATA_SCALE_ID")
    EntityDataAccessor<Vector3f> GetScaleDataID();
}

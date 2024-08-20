package dev.kineticcat.complexhex.client.render;

import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static dev.kineticcat.complexhex.Complexhex.id;

public class HexalWispRenderer extends EntityRenderer<AssemblyManagerEntity> {
    private static final ResourceLocation WISP = new ResourceLocation("hexcasting", "textures/particle/cloud.png");

    public HexalWispRenderer (EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation (@NotNull AssemblyManagerEntity manager) {
        return WISP;
    }
}
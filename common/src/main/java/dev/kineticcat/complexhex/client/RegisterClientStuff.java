package dev.kineticcat.complexhex.client;

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry;
import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.kineticcat.complexhex.block.ComplexHexBlocks;
import dev.kineticcat.complexhex.block.entity.ComplexHexBlockEntities;
import dev.kineticcat.complexhex.block.entity.HexboxBlockEntity;
import dev.kineticcat.complexhex.client.render.be.HexboxBlockEntityRenderer;
import dev.kineticcat.complexhex.client.render.entity.ParametricLineRenderer;
import dev.kineticcat.complexhex.client.render.entity.ParametricSurfaceRenderer;
import dev.kineticcat.complexhex.entity.ComplexHexEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class RegisterClientStuff {

    public static void init() {

        ScryingLensOverlayRegistry.addDisplayer(ComplexHexBlocks.HEXBOX,
                (lines, state, pos, observer, world, direction) -> {
                    if (world.getBlockEntity(pos) instanceof HexboxBlockEntity box) {
                        box.applyScryingLensOverlay(lines, world);
                    }
                }
        );

        IClientXplatAbstractions.INSTANCE.registerEntityRenderer(ComplexHexEntities.PARAMETRIC_LINE, ParametricLineRenderer::new);
        IClientXplatAbstractions.INSTANCE.registerEntityRenderer(ComplexHexEntities.PARAMETRIC_SURFACE, ParametricSurfaceRenderer::new);
        BlockEntityRenderers.register(ComplexHexBlockEntities.HEXBOX, ctx -> new HexboxBlockEntityRenderer());
    }
}
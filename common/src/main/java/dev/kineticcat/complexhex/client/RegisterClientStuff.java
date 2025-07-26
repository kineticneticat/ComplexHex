package dev.kineticcat.complexhex.client;

import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.kineticcat.complexhex.client.render.entity.ParametricLineRenderer;
import dev.kineticcat.complexhex.client.render.entity.ParametricSurfaceRenderer;
import dev.kineticcat.complexhex.entity.ComplexHexEntities;

public class RegisterClientStuff {

    public static void init() {
        IClientXplatAbstractions.INSTANCE.registerEntityRenderer(ComplexHexEntities.PARAMETRIC_LINE, ParametricLineRenderer::new);
        IClientXplatAbstractions.INSTANCE.registerEntityRenderer(ComplexHexEntities.PARAMETRIC_SURFACE, ParametricSurfaceRenderer::new);
    }
}
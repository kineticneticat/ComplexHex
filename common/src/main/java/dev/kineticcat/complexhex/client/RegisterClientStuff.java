package dev.kineticcat.complexhex.client;

import at.petrak.hexcasting.client.render.GaslightingTracker;
import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.kineticcat.complexhex.block.ComplexHexBlocks;
import dev.kineticcat.complexhex.block.entity.BlockEntityBurntAmethyst;
import dev.kineticcat.complexhex.block.entity.ComplexHexBlockEntities;
import dev.kineticcat.complexhex.client.render.ComplexHexGaslighting;
import dev.kineticcat.complexhex.client.render.be.BlockEntityBurntAmethystRenderer;
import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static dev.kineticcat.complexhex.Complexhex.LOGGER;
import static dev.kineticcat.complexhex.Complexhex.id;

public class RegisterClientStuff {
    public static Map<ResourceLocation, List<BakedModel>> BURNT_AMETHYST_VARIANTS = new HashMap<>();
    public static void init() {
        registerGaslight3(ComplexHexItems.BURNT_AMETHYST_SHARD);
        registerGaslight3(ComplexHexBlocks.BURNT_AMETHYST.asItem());
    }
    private static void registerGaslight3(Item item) {
        IClientXplatAbstractions.INSTANCE.registerItemProperty(item,
            ComplexHexGaslighting.BURNT_AMETHYST_GASLIGHT.GASLIGHTING_PRED,
                (stack, level, holder, holderID) ->
                        Math.abs(GaslightingTracker.getGaslightingAmount() % 3));
    }
    public static void registerBlockEntityRenderers(@NotNull BlockEntityRendererRegisterererer registerer) {
        registerer.registerBlockEntityRenderer(ComplexHexBlockEntities.BURNT_AMETHYST_BLOCK,
                BlockEntityBurntAmethystRenderer::new);
    }

    @FunctionalInterface
    public interface BlockEntityRendererRegisterererer {
        <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type,
                                                                 BlockEntityRendererProvider<? super T> berp);
    }
    public static void onModelRegister(ResourceManager recMan, Consumer<ResourceLocation> extraModels) {
        var blockLoc = BuiltInRegistries.BLOCK.getKey(ComplexHexBlocks.BURNT_AMETHYST);
        var locStart = "block/";

        for (int i = 0; i < 3; i++) {
            extraModels.accept(id( locStart + blockLoc.getPath() + "_" + i));
        }
    }

    public static void onModelBake(ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
        var blockLoc = BuiltInRegistries.BLOCK.getKey(ComplexHexBlocks.BURNT_AMETHYST);
        var locStart = "block/";

        var list = new ArrayList<BakedModel>();
        for (int i = 0; i < 3; i++) {
            var variantLoc = id(locStart + blockLoc.getPath() + "_" + i);
            var model = map.get(variantLoc);
            list.add(model);
        }
        LOGGER.info(blockLoc);
        BURNT_AMETHYST_VARIANTS.put(blockLoc, list);

    }
}

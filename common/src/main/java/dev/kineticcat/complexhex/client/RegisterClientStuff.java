package dev.kineticcat.complexhex.client;

import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.kineticcat.complexhex.block.ComplexHexBlocks;
import dev.kineticcat.complexhex.block.entity.ComplexHexBlockEntities;
import dev.kineticcat.complexhex.client.render.ComplexHexGaslighting;
import dev.kineticcat.complexhex.client.render.CoolerGaslightingTracker;
import dev.kineticcat.complexhex.client.render.entity.HexalWispRenderer;
import dev.kineticcat.complexhex.client.render.be.BlockEntityBurntAmethystRenderer;
import dev.kineticcat.complexhex.client.render.entity.NixRenderer;
import dev.kineticcat.complexhex.entity.ComplexHexEntities;
import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static dev.kineticcat.complexhex.Complexhex.id;

public class RegisterClientStuff {
    public static Map<ResourceLocation, List<BakedModel>> BURNT_AMETHYST_VARIANTS = new HashMap<>();
    public static final List<Block> BURNT_AMETHYST_TYPES = List.of(
            ComplexHexBlocks.BURNT
    );
    public static void init() {
        registerGaslight3(ComplexHexItems.BURNT_SHARD);
        registerGaslight3(ComplexHexBlocks.BURNT.asItem());
        registerGaslight3(ComplexHexItems.AWAKENED_BURNT_SHARD);
        IClientXplatAbstractions.INSTANCE.setRenderLayer(ComplexHexBlocks.BURNT, RenderType.translucent());
        IClientXplatAbstractions.INSTANCE.registerEntityRenderer(ComplexHexEntities.ASSEMBLY_MANAGER, HexalWispRenderer::new);
        IClientXplatAbstractions.INSTANCE.registerEntityRenderer(ComplexHexEntities.NIX, NixRenderer::new);
    }
    private static void registerGaslight3(Item item) {
        IClientXplatAbstractions.INSTANCE.registerItemProperty(item,
            ComplexHexGaslighting.BURNT_AMETHYST_GASLIGHT.GASLIGHTING_PRED,
                (stack, level, holder, holderID) ->
                        Math.abs(CoolerGaslightingTracker.getTracker(id("burnt_variants")).getGaslightingAmount() % 3));
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
        for (Block block : BURNT_AMETHYST_TYPES) {
            var blockLoc = BuiltInRegistries.BLOCK.getKey(block);
            var locStart = "block/";

            for (int i = 0; i < 3; i++) {
                extraModels.accept(id(locStart + blockLoc.getPath() + "_" + i));
            }
        }
    }

    public static void onModelBake(ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
        for (Block block : BURNT_AMETHYST_TYPES) {
            var blockLoc = BuiltInRegistries.BLOCK.getKey(block);
            var locStart = "block/";

            var list = new ArrayList<BakedModel>();
            for (int i = 0; i < 3; i++) {
                var variantLoc = id(locStart + blockLoc.getPath() + "_" + i);
                var model = map.get(variantLoc);
                list.add(model);
            }
            BURNT_AMETHYST_VARIANTS.put(blockLoc, list);

        }
    }
}

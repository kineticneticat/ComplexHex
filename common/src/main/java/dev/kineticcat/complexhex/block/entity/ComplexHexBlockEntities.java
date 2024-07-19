package dev.kineticcat.complexhex.block.entity;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kineticcat.complexhex.block.ComplexHexBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ComplexHexBlockEntities {
    public static void registerTiles(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        for (var e : BLOCK_ENTITIES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITIES = new LinkedHashMap<>();

    public static final BlockEntityType<BlockEntityBurntAmethyst> BURNT_AMETHYST_BLOCK = register(
            "burnt_amethyst_block", BlockEntityBurntAmethyst.fromKnownBlock(ComplexHexBlocks.BURNT_AMETHYST), ComplexHexBlocks.BURNT_AMETHYST
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String id,
            BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        var ret = IXplatAbstractions.INSTANCE.createBlockEntityType(func, blocks);
        var old = BLOCK_ENTITIES.put(new ResourceLocation(HexAPI.MOD_ID, id), ret);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return ret;
    }
}

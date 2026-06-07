package dev.kineticcat.complexhex.block;

import at.petrak.hexcasting.common.lib.HexBlocks;
import com.mojang.datafixers.util.Pair;
import dev.kineticcat.complexhex.item.ComplexHexCreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static dev.kineticcat.complexhex.Complexhex.id;

public class ComplexHexBlocks {
    public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
        for (var e : BLOCKS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerBlockItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : BLOCK_ITEMS.entrySet()) {
            r.accept(new BlockItem(e.getValue().getFirst(), e.getValue().getSecond()), e.getKey());
        }
    }
    public static void registerBlockCreativeTab(Consumer<Block> r, CreativeModeTab tab) {
        for (var block : BLOCK_TABS.getOrDefault(tab, List.of())) {
            r.accept(block);
        }
    }
    private static final Map<ResourceLocation, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<ResourceLocation, Pair<Block, Item.Properties>> BLOCK_ITEMS = new LinkedHashMap<>();
    private static final Map<CreativeModeTab, List<Block>> BLOCK_TABS = new LinkedHashMap<>();

    public static final RecordBlock.Inert INERT_RECORD = block(
            "inert_record",
            new RecordBlock.Inert(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).pushReaction(PushReaction.DESTROY))
    );
    public static final RecordBlock.Quenched QUENCHED_RECORD = block(
            "quenched_record",
            new RecordBlock.Quenched(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK))
    );

    public static final HexboxBlock HEXBOX = blockItem(
            "hexbox",
            new HexboxBlock(BlockBehaviour.Properties.copy(HexBlocks.SLATE_BLOCK).pushReaction(PushReaction.BLOCK)),
            new Item.Properties()
    );

    public static <T extends Block> T block(String name, T block) {
        Block old = BLOCKS.put(id(name), block);
        if (old != null) {
            throw new IllegalArgumentException("complexhex's dev's a dumbass: " + name);
        }
        return block;
    }
    public static <T extends Block> T blockItem(String name, T block, Item.Properties props, CreativeModeTab tab) {
        block(name, block);
        Pair<Block, Item.Properties> old = BLOCK_ITEMS.put(id(name), new Pair<>(block, props));
        if (old != null) {
            throw new IllegalArgumentException("complexhex's dev's a dumbass: " + name);
        }
        BLOCK_TABS.computeIfAbsent(tab, t-> new ArrayList<>()).add(block);
        return block;
    }
    public static <T extends Block> T blockItem(String name, T block, Item.Properties props) {
        return blockItem(name, block, props, ComplexHexCreativeTabs.COMPLEXHEX);
    }

}
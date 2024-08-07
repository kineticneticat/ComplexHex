package dev.kineticcat.complexhex.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ItemAwakenedBurntShard extends BlockItem {

    public ItemAwakenedBurntShard(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) { return true; }

    @Override
    public boolean canPlace(@NotNull BlockPlaceContext blockPlaceContext, @NotNull BlockState blockState) { return false; }

    @Override
    public @NotNull String getDescriptionId() {
        return "item.complexhex.awakened_burnt_shard";
    }
}

package dev.kineticcat.complexhex.block;

import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockAwakenedBurnt extends Block {
    public BlockAwakenedBurnt(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(@NotNull BlockState newState, Level level, @NotNull BlockPos position, @NotNull BlockState OldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            ItemStack shard = new ItemStack(ComplexHexItems.AWAKENED_BURNT_SHARD);
            ItemEntity shardEntity = new ItemEntity(level, position.getX(), position.getY(), position.getZ(), shard);
            level.addFreshEntity(shardEntity);
            level.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
        }
    }
}

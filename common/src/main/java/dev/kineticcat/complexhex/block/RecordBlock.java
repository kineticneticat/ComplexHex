package dev.kineticcat.complexhex.block;

import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RecordBlock extends Block {
    public RecordBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(0,0,0,16,1,16);
    }
    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    public static class Quenched extends RecordBlock {
        public Quenched(Properties properties) {
            super(properties);
        }
        @Override
        public void onPlace(@NotNull BlockState newState, Level level, @NotNull BlockPos position, @NotNull BlockState OldState, boolean movedByPiston) {
            if (!level.isClientSide) {
                ItemStack record = new ItemStack(ComplexHexItems.QUENCHED_RECORD);
                ItemEntity recordEntity = new ItemEntity(level, position.getX(), position.getY(), position.getZ(), record);
                level.addFreshEntity(recordEntity);
                level.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
            }
        }
    }
    public static class Inert extends RecordBlock {
        public Inert(Properties properties) {
            super(properties);
        }
    }
}

package dev.kineticcat.complexhex.block;

import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RecordBlock extends HorizontalDirectionalBlock {

    public RecordBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
         return switch (blockState.getValue(FACING)) {
             case NORTH, SOUTH -> Block.box(0,0,3,16,1,13);
             case EAST, WEST -> Block.box(3,0,0,13,1,16);
             case DOWN, UP -> Block.box(0,0,0,16,16,16);
        };
    }
    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
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

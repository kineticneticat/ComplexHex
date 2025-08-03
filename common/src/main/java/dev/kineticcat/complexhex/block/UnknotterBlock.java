package dev.kineticcat.complexhex.block;

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent;
import at.petrak.hexcasting.api.casting.circles.ICircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import dev.kineticcat.complexhex.block.entity.UnknotterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class UnknotterBlock extends Block implements EntityBlock, ICircleComponent {

    public static final BooleanProperty ENERGISED = BlockCircleComponent.ENERGIZED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.ATTACH_FACE;
    public UnknotterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(ENERGISED, false)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new UnknotterBlockEntity(pos, state); }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(ATTACH_FACE)) {
            case FLOOR -> Block.box(0, 0, 0, 16, 4, 16);
            case CEILING -> Block.box(0, 12, 0, 16, 16, 16);
            case WALL -> switch (pState.getValue(FACING)) {
                case NORTH -> Block.box(0, 0, 12, 16, 16, 16);
                case EAST -> Block.box(0, 0, 0, 4, 16, 16);
                case SOUTH -> Block.box(0, 0, 0, 16, 16, 4);
                // NORTH; up and down don't happen (but we need branches for them)
                default -> Block.box(12, 0, 0, 16, 16, 16);
            };
        };
    }

    @Override
    public ControlFlow acceptControlFlow(CastingImage imageIn, CircleCastEnv env, Direction enterDir, BlockPos pos, BlockState bs, ServerLevel world) {
        var exitDirsSet = this.possibleExitDirections(pos, bs, world);
        exitDirsSet.remove(enterDir.getOpposite());

        var exitDirs = exitDirsSet.stream().map((dir) -> this.exitPositionFromDirection(pos, dir));

        return new ControlFlow.Continue(imageIn, exitDirs.toList());
    }

    @Override
    public boolean canEnterFromDirection(Direction enterDir, BlockPos pos, BlockState bs, ServerLevel world) {
        var thisNormal = this.normalDir(bs);
        return enterDir != thisNormal.getOpposite(); // && enterDir != thisNormal;
    }

    public Direction normalDir(BlockState bs) {
        return getConnectedDirection(bs).getOpposite();
    }

    @Override
    public EnumSet<Direction> possibleExitDirections(BlockPos pos, BlockState bs, Level world) {
        var allDirs = EnumSet.allOf(Direction.class);
        var normal = this.normalDir(bs);
        allDirs.remove(normal);
        return allDirs;
    }

    @Override
    public BlockState startEnergized(BlockPos pos, BlockState bs, Level world) {
        var newState = bs.setValue(ENERGISED, true);
        world.setBlockAndUpdate(pos, newState);

        return newState;
    }

    @Override
    public boolean isEnergized(BlockPos pos, BlockState bs, Level world) {
        return bs.getValue(ENERGISED);
    }

    @Override
    public BlockState endEnergized(BlockPos pos, BlockState bs, Level world) {
        var newState = bs.setValue(ENERGISED, false);
        world.setBlockAndUpdate(pos, newState);
        return newState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ATTACH_FACE, ENERGISED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        for (Direction direction : pContext.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState()
                        .setValue(ATTACH_FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, pContext.getHorizontalDirection().getOpposite());
            } else {
                blockstate = this.defaultBlockState()
                        .setValue(ATTACH_FACE, AttachFace.WALL)
                        .setValue(FACING, direction.getOpposite());
            }

            if (blockstate.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, getConnectedDirection(pState).getOpposite());
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        return pReader.getBlockState(blockpos).isFaceSturdy(pReader, blockpos, pDirection.getOpposite());
    }

    protected static Direction getConnectedDirection(BlockState pState) {
        return switch (pState.getValue(ATTACH_FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> pState.getValue(FACING);
        };
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}

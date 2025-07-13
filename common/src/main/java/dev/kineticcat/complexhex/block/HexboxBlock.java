package dev.kineticcat.complexhex.block;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.circles.ICircleComponent;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kineticcat.complexhex.block.entity.ComplexHexBlockEntities;
import dev.kineticcat.complexhex.block.entity.HexboxBlockEntity;
import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class HexboxBlock extends Block implements EntityBlock, ICircleComponent {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final IntegerProperty DISC_TYPE = IntegerProperty.create("disc", 0, 2);
    public HexboxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, false).setValue(DISC_TYPE, 0));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED).add(DISC_TYPE);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (!(level.getBlockEntity(pos) instanceof HexboxBlockEntity box)) {
                return InteractionResult.FAIL;
            }
            if (state.getValue(ACTIVATED)) {
                if (player.isDiscrete()) {
                    BlockState newstate = state.setValue(ACTIVATED, false);
                    level.setBlockAndUpdate(pos, newstate);
                    box.clearDisplay();
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            } else {
                if (player.isDiscrete()) {
                    BlockState newstate = state.setValue(ACTIVATED, true);
                    level.setBlockAndUpdate(pos, newstate);
                    box.setOwner((ServerPlayer) player);
                    return InteractionResult.SUCCESS;
                }
                switch (state.getValue(DISC_TYPE)) {
                    case 0:
                        ItemStack item = player.getItemInHand(hand);
                        if (item.is(ComplexHexItems.INERT_RECORD)) {
                            BlockState newstate = state.setValue(DISC_TYPE, 1);
                            level.setBlockAndUpdate(pos, newstate);
                            item.shrink(1);
                            return InteractionResult.CONSUME;
                        } else if (item.is(ComplexHexItems.QUENCHED_RECORD)) {
                            BlockState newstate = state.setValue(DISC_TYPE, 2);
                            ADIotaHolder holder = IXplatAbstractions.INSTANCE.findDataHolder(item);
                            if (holder == null) { return InteractionResult.FAIL; }
                            box.setHex((ListIota) holder.readIota((ServerLevel) level));
                            level.setBlockAndUpdate(pos, newstate);
                            item.shrink(1);
                            return InteractionResult.CONSUME;
                        } else {
                            return InteractionResult.PASS;
                        }
                    case 1:
//                        Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5, 1.01, 0.5).offsetRandom(level.random, 0.7F);
//                        ItemEntity discItemEntity = new ItemEntity(level, vec3.x, vec3.y, vec3.z, );
                        BlockState newstate = state.setValue(DISC_TYPE, 0);
                        level.setBlockAndUpdate(pos, newstate);
//                        level.addFreshEntity(discItemEntity);
                        popResourceFromFace(level, pos, Direction.UP, new ItemStack(ComplexHexItems.INERT_RECORD));
                        return InteractionResult.SUCCESS;
                    case 2:
//                        vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5, 1.01, 0.5).offsetRandom(level.random, 0.7F);
                        ItemStack discItem = new ItemStack(ComplexHexItems.QUENCHED_RECORD);
                        ADIotaHolder holder = IXplatAbstractions.INSTANCE.findDataHolder(discItem);
                        if (holder == null) {return InteractionResult.FAIL;}
                        if (box.getHex((ServerLevel) level) != null) {
                            holder.writeIota(box.getHex((ServerLevel) level), false);
                        }
                        box.setHex((ListIota) null);
                        newstate = state.setValue(DISC_TYPE, 0);
                        level.setBlockAndUpdate(pos, newstate);
//                        level.addFreshEntity(new ItemEntity(level, vec3.x, vec3.y, vec3.z, discItem));
                        popResourceFromFace(level, pos, Direction.UP, discItem);
                        return InteractionResult.SUCCESS;
                }
            }
            // shouldnt be possible to get here, but intellij thinks otherwise
            return InteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HexboxBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(0,0,0,16,14,16);
    }
    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock()) && blockState.getValue(DISC_TYPE) != 0) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HexboxBlockEntity box) {
                Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5, 1.01, 0.5).offsetRandom(level.random, 0.7F);
                ItemStack discItem = new ItemStack(ComplexHexItems.INERT_RECORD);
                if (blockState.getValue(DISC_TYPE) == 2) {
                    discItem = new ItemStack(ComplexHexItems.QUENCHED_RECORD);
                    ADIotaHolder holder = IXplatAbstractions.INSTANCE.findDataHolder(discItem);
                    if (holder == null) {
                        return;
                    }
                    if (box.getHex((ServerLevel) level) != null) {
                        holder.writeIota(box.getHex((ServerLevel) level), false);
                    }
                }
                level.addFreshEntity(new ItemEntity(level, vec3.x, vec3.y, vec3.z, discItem));
            }
        }
        super.onRemove(blockState, level, pos, blockState2, bl);
    }

//    @Override
//    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        Complexhex.LOGGER.info("ticked");
//        if (level.getBlockEntity(pos) instanceof HexboxBlockEntity box && state.getValue(ACTIVATED)) {
//            box.tick();
//            Complexhex.LOGGER.info("ticking");
//        }
//    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
                                                                  BlockEntityType<T> type) {
        if (!pLevel.isClientSide) {
            return createTickerHelper(type, ComplexHexBlockEntities.HEXBOX,
                    HexboxBlockEntity::serverTick);
        } else {
            return null;
        }
    }

    // uegh
    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> type, BlockEntityType<E> targetType, BlockEntityTicker<? super E> ticker) {
        return targetType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public ControlFlow acceptControlFlow(CastingImage imageIn, CircleCastEnv env, Direction enterDir, BlockPos pos, BlockState bs, ServerLevel level) {
        if (!(level.getBlockEntity(pos) instanceof HexboxBlockEntity box)) return new ControlFlow.Stop();
        var wawa = possibleExitDirections(pos, bs, level);
        wawa.remove(enterDir.getOpposite());
        var exits = wawa.stream().map(dir -> exitPositionFromDirection(pos, dir));
        var iota = imageIn.getStack().isEmpty() ? null : imageIn.getStack().get(0);
        box.setIota(iota);
        return new ControlFlow.Continue(imageIn, exits.toList());
    }

    @Override
    public boolean canEnterFromDirection(Direction enterDir, BlockPos pos, BlockState bs, ServerLevel level) {
        return true;
    }

    @Override
    public EnumSet<Direction> possibleExitDirections(BlockPos pos, BlockState bs, Level level) {
        return EnumSet.allOf(Direction.class);
    }

    @Override
    public BlockState startEnergized(BlockPos pos, BlockState bs, Level level) {
        BlockState newstate = bs.setValue(ACTIVATED, true);
        level.setBlockAndUpdate(pos, newstate);
        return newstate;
    }

    @Override
    public boolean isEnergized(BlockPos pos, BlockState bs, Level level) {
        return bs.getValue(ACTIVATED);
    }

    @Override
    public BlockState endEnergized(BlockPos pos, BlockState bs, Level level) {
        BlockState newstate = bs.setValue(ACTIVATED, false);
        level.setBlockAndUpdate(pos, newstate);
        return newstate;
    }


}

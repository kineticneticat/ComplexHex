package dev.kineticcat.complexhex.block;

import dev.kineticcat.complexhex.block.entity.HexboxBlockEntity;
import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HexboxBlock extends Block implements EntityBlock {
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
            if (state.getValue(ACTIVATED)) {
                return InteractionResult.PASS;
            } else {
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
                            level.setBlockAndUpdate(pos, newstate);
                            item.shrink(1);
                            return InteractionResult.CONSUME;
                        } else {
                            return InteractionResult.PASS;
                        }
                    case 1:
                        Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5, 1.01, 0.5).offsetRandom(level.random, 0.7F);
                        ItemEntity discItem = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(ComplexHexItems.INERT_RECORD));
                        BlockState newstate = state.setValue(DISC_TYPE, 0);
                        level.setBlockAndUpdate(pos, newstate);
                        level.addFreshEntity(discItem);
                        return InteractionResult.SUCCESS;
                    case 2:
                        vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5, 1.01, 0.5).offsetRandom(level.random, 0.7F);
                        discItem = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(ComplexHexItems.QUENCHED_RECORD));
                        newstate = state.setValue(DISC_TYPE, 0);
                        level.setBlockAndUpdate(pos, newstate);
                        level.addFreshEntity(discItem);
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
}

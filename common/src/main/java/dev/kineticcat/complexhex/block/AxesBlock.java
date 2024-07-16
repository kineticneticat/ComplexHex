package dev.kineticcat.complexhex.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class AxesBlock extends Block {
    public static final BooleanProperty POSITIVE_X = BooleanProperty.create("positive_x");
    public static final BooleanProperty POSITIVE_Y = BooleanProperty.create("positive_y");
    public static final BooleanProperty POSITIVE_Z = BooleanProperty.create("positive_z");
    public static final BooleanProperty NEGATIVE_X = BooleanProperty.create("negative_x");
    public static final BooleanProperty NEGATIVE_Y = BooleanProperty.create("negative_y");
    public static final BooleanProperty NEGATIVE_Z = BooleanProperty.create("negative_z");
    public AxesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(POSITIVE_X, true)
                .setValue(POSITIVE_Y, true)
                .setValue(POSITIVE_Z, true)
                .setValue(NEGATIVE_X, true)
                .setValue(NEGATIVE_Y, true)
                .setValue(NEGATIVE_Z, true)
        );
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POSITIVE_X, POSITIVE_Y, POSITIVE_Z, NEGATIVE_X, NEGATIVE_Y, NEGATIVE_Z);
    }

}

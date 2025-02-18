package dev.kineticcat.complexhex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HexboxBlockEntity extends BlockEntity {
    public HexboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ComplexHexBlockEntities.HEXBOX, blockPos, blockState);
    }
}

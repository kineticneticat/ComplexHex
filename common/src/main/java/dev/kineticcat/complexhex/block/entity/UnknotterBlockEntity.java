package dev.kineticcat.complexhex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnknotterBlockEntity extends BlockEntity {
    public UnknotterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ComplexHexBlockEntities.UNKNOTTER, blockPos, blockState);
    }


}

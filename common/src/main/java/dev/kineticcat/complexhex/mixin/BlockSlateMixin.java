package dev.kineticcat.complexhex.mixin;

import at.petrak.hexcasting.common.blocks.circles.BlockSlate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockSlate.class)
public abstract class BlockSlateMixin {
    @Final
    @Shadow(remap = false)
    public static DirectionProperty FACING;

    public BlockState rotate(BlockState arg, Rotation arg2) {
        return (BlockState)arg.setValue(FACING, arg2.rotate((Direction)arg.getValue(FACING)));
    }

    public BlockState mirror(BlockState arg, Mirror arg2) {
        return arg.rotate(arg2.getRotation((Direction)arg.getValue(FACING)));
    }
}

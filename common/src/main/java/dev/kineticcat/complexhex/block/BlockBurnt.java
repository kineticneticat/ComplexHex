package dev.kineticcat.complexhex.block;

import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import dev.kineticcat.complexhex.block.entity.BlockEntityBurntAmethyst;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlockBurnt extends Block implements EntityBlock {

    public BlockBurnt(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityBurntAmethyst(this, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        ParticleOptions options = new ConjureParticleOptions(0x8932b8);
        Vec3 center = Vec3.atCenterOf(pos);
        for (Direction direction : Direction.values()) {
            int dX = direction.getStepX();
            int dY = direction.getStepY();
            int dZ = direction.getStepZ();

            int count = random.nextInt(10) / 4;
            for (int i = 0; i < count; i++) {
                double pX = center.x + (dX == 0 ? Mth.nextDouble(random, -0.5D, 0.5D) : (double) dX * 0.55D);
                double pY = center.y + (dY == 0 ? Mth.nextDouble(random, -0.5D, 0.5D) : (double) dY * 0.55D);
                double pZ = center.z + (dZ == 0 ? Mth.nextDouble(random, -0.5D, 0.5D) : (double) dZ * 0.55D);
                double vPerp = Mth.nextDouble(random, 0.0, 0.01);
                double vX = vPerp * dX;
                double vY = vPerp * dY;
                double vZ = vPerp * dZ;
                level.addParticle(options, pX, pY, pZ, vX, vY, vZ);
            }
        }
    }
}

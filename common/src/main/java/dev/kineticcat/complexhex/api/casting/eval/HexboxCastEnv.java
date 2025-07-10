package dev.kineticcat.complexhex.api.casting.eval;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.player.Sentinel;
import dev.kineticcat.complexhex.block.entity.HexboxBlockEntity;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HexboxCastEnv extends CastingEnvironment {
    public HexboxCastEnv(ServerLevel world, FakePlayer fake, FrozenPigment pigment, BlockPos pos) {
        super(world);
        this.fake = fake;
        this.pos = pos;
    }
    public FakePlayer fake;
    public BlockPos pos;

    public HexboxBlockEntity getHexbox() {
        if (world.getBlockEntity(pos) instanceof HexboxBlockEntity box) {
            return box;
        }
        return null;
    }

    @Override
    public @Nullable LivingEntity getCastingEntity() {
        return fake;
    }

    @Override
    public MishapEnvironment getMishapEnvironment() {
        return new MishapEnvironment(world, null) {
            @Override
            public void yeetHeldItemsTowards(Vec3 targetPos) {}
            @Override
            public void dropHeldItems() {}
            @Override
            public void drown() {}
            @Override
            public void damage(float healthProportion) {}
            @Override
            public void removeXp(int amount) {}
            @Override
            public void blind(int ticks) {}
        };
    }

    @Override
    public void postExecution(CastResult result) {
        super.postExecution(result);
        var box = getHexbox();
        if (box == null) return;
        for (var sideEffect : result.getSideEffects()) {
            if (sideEffect instanceof OperatorSideEffect.DoMishap doMishap) {
                var msg = doMishap.getMishap().errorMessageWithName(this, doMishap.getErrorCtx());
                if (msg != null) {
                    box.postMishap(msg);
                }
            }
        }

    }

    @Override
    public Vec3 mishapSprayPos() {
        return Vec3.upFromBottomCenterOf(pos, 1);
    }

    @Override
    protected long extractMediaEnvironment(long cost, boolean simulate) {
        var box = getHexbox();
        if (box == null) {
            return cost;
        }
        long mediaAvaliable = box.media;
        if (mediaAvaliable < 0) {
            return 0;
        }
        long mediaToTake = Math.min(cost, mediaAvaliable);
        cost -= mediaToTake;
        if (!simulate) {
            box.setMedia(mediaAvaliable - mediaToTake);
        }
        return cost;
    }

    @Override
    protected boolean isVecInRangeEnvironment(Vec3 vec) {
        if (pos.getCenter().distanceToSqr(vec) <= 16) {
            return true;
        }
        Sentinel sentinel = HexAPI.instance().getSentinel(fake);
        return sentinel != null
                && sentinel.extendsRange()
                && fake.level().dimension() == sentinel.dimension()
                && sentinel.position().distanceToSqr(vec) <= 4.00000000001;
    }

    @Override
    protected boolean hasEditPermissionsAtEnvironment(BlockPos pos) {
        return true;
    }

    @Override
    public InteractionHand getCastingHand() {
        return InteractionHand.MAIN_HAND;
    }

    @Override
    protected List<ItemStack> getUsableStacks(StackDiscoveryMode mode) {
        return new ArrayList<>();
    }

    @Override
    protected List<HeldItemInfo> getPrimaryStacks() {
        return List.of();
    }

    @Override
    public boolean replaceItem(Predicate<ItemStack> stackOk, ItemStack replaceWith, @Nullable InteractionHand hand) {
        return false;
    }

    @Override
    public FrozenPigment getPigment() {
        HexboxBlockEntity box = getHexbox();
        if (box == null) {
            return FrozenPigment.DEFAULT.get();
        }
        return box.pigment;
    }

    @Override
    public @Nullable FrozenPigment setPigment(@Nullable FrozenPigment pigment) {
        HexboxBlockEntity box = getHexbox();
        if (box == null) {
            return FrozenPigment.DEFAULT.get();
        }
        box.pigment = pigment;
        return pigment;
    }

    @Override
    public void produceParticles(ParticleSpray particles, FrozenPigment colorizer) {
        particles.sprayParticles(this.world, getPigment());
    }

    @Override
    public void printMessage(Component message) {
        var box = getHexbox();
        if (box == null) {
            return;
        }
        box.postPrint(message);
    }
}

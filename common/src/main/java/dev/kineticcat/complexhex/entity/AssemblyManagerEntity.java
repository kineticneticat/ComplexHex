package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dev.kineticcat.complexhex.api.FunniesKt.nextColour;

public class AssemblyManagerEntity extends Entity {

    private static final String TAG_VERTICES = "vertices";
    private static final String TAG_PIGMENT = "pigment";

    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(AssemblyManagerEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private List<Vec3> Vertices = new ArrayList<>();
    private FrozenPigment Pigment;

    public AssemblyManagerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void addVertex(Vec3 vec) {
        Vertices.add(vec);
        setPos(centre());
    }

    public Vec3 centre() {
        Vec3 total = Vec3.ZERO;
        for (Vec3 vert : Vertices) {
            total = total.add(vert);
        }
        return total.scale(1f /Vertices.size());
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        ListTag NodesTag = NBTHelper.getList(compoundTag, TAG_VERTICES, Tag.TAG_LIST);
        NodesTag = NodesTag == null ? new ListTag() : NodesTag;
        List<Vec3> out = new ArrayList<>();
        for (Tag tag : NodesTag) {
            CompoundTag ctag = NBTHelper.getAsCompound(tag);
            out.add(new Vec3(
                    ctag.getDouble("X"),
                    ctag.getDouble("Y"),
                    ctag.getDouble("Z")
            ));
        }
        Vertices = out;
        entityData.set(PIGMENT, compoundTag.getCompound(TAG_PIGMENT));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        ListTag NodesTag = new ListTag();
        for (Vec3 vec : Vertices) {
            CompoundTag ctag = new CompoundTag();
            ctag.putDouble("X", vec.x);
            ctag.putDouble("Y", vec.y);
            ctag.putDouble("Z", vec.z);
            NodesTag.add(ctag);
        }
        compoundTag.put(TAG_VERTICES, NodesTag);
        NBTHelper.putCompound(compoundTag, TAG_PIGMENT, entityData.get(PIGMENT));
    }

    public FrozenPigment setPigment(FrozenPigment pigment) {
        entityData.set(PIGMENT, pigment.serializeToNBT());
        return pigment;
    }

    public void playWispParticles(FrozenPigment pigment) {
        int radius = 1;

//        val repeats = switch ((ParticleStatus) Minecraft.getInstance().options.particles().get()) {
//            case ParticleStatus.ALL: 50
//            ParticleStatus.DECREASED -> 20
//            ParticleStatus.MINIMAL -> 5
//        }
        int repeats;
        switch ( (ParticleStatus) Minecraft.getInstance().options.particles().get() ) {
            case ALL: repeats = 50;
            case DECREASED: repeats = 20;
            case MINIMAL: repeats = 5;
            default: repeats = 0;
        }

        for (Vec3 vert : Vertices) {
            for (int i = 0; i <= repeats; i++) {
                int colour = nextColour(pigment, random);

                level().addParticle(
                        new ConjureParticleOptions(colour),
                        (vert.x + radius * random.nextGaussian()),
                        (vert.y + radius * random.nextGaussian()),
                        (vert.z + radius * random.nextGaussian()),
                        0.0125 * (random.nextDouble() - 0.5),
                        0.0125 * (random.nextDouble() - 0.5),
                        0.0125 * (random.nextDouble() - 0.5)
                );
            }
        }
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            FrozenPigment pigment = FrozenPigment.fromNBT(entityData.get(PIGMENT));
            playWispParticles(pigment);
        }
    }

    public void triggerAssembly() {}

}

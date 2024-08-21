package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import com.mojang.math.Axis;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.mixin.EntityDataSerialisersMixin;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3d;

import java.util.ArrayList;
import java.util.List;

import static dev.kineticcat.complexhex.api.FunniesKt.nextColour;

public class AssemblyManagerEntity extends Entity {

    public static EntityDataSerializer<ListTag> LIST_TAG = new EntityDataSerializer<ListTag>() {
        @Override
        public void write(FriendlyByteBuf friendlyByteBuf, ListTag ltag) {
            CompoundTag ctag = new CompoundTag();
            ctag.put("data", ltag);
            friendlyByteBuf.writeNbt(ctag);
        }

        @Override
        public ListTag read(FriendlyByteBuf friendlyByteBuf) {
            return friendlyByteBuf.readNbt().getList("data", Tag.TAG_COMPOUND);
        }

        @Override
        public ListTag copy(ListTag ltag) {
            return ltag.copy();
        }
    };

    // why the fuck does it *have* to be registered >:(
    public static void registerthestupidthing() {
        EntityDataSerialisersMixin.invokeRegisterSerializer(LIST_TAG);
    }

    private static final String TAG_VERTICES = "vertices";
    private static final String TAG_PIGMENT = "pigment";

    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(AssemblyManagerEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<ListTag> VERTICES =
            SynchedEntityData.defineId(AssemblyManagerEntity.class, LIST_TAG);

    public List<Vec3> getVertices() { return tagAsVerts(entityData.get(VERTICES));}
    public void setVertices(List<Vec3> verts) { entityData.set(VERTICES, vertsAsTag(verts));}
    public FrozenPigment getPigment() { return FrozenPigment.fromNBT(entityData.get(PIGMENT));}
    public void setPigment(FrozenPigment pigment) { entityData.set(PIGMENT, pigment.serializeToNBT());}

    public AssemblyManagerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void addVertex(Vec3 vec) {
        List<Vec3> verts = getVertices();
        verts.add(vec);
        setVertices(verts);
        setPos(centre());
    }

    public Vec3 centre() {
        Vec3 total = Vec3.ZERO;
        for (Vec3 vert : getVertices()) {
            total = total.add(vert);
        }
        return total.scale(1f /getVertices().size());
    }

    public Vec3 normToCentre(Vec3 vec) {
        return centre().subtract(vec).normalize();
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
        entityData.define(VERTICES, new ListTag());
    }

    private ListTag vertsAsTag(List<Vec3> verts) {
        ListTag ltag = new ListTag();
        for (Vec3 vec : verts) {
            CompoundTag ctag = new CompoundTag();
            ctag.putDouble("X", vec.x);
            ctag.putDouble("Y", vec.y);
            ctag.putDouble("Z", vec.z);
            ltag.add(ctag);
        }
        return ltag;
    }
    private List<Vec3> tagAsVerts(ListTag ltag) {
        List<Vec3> out = new ArrayList<>();
        for (Tag tag : ltag) {
            CompoundTag ctag = NBTHelper.getAsCompound(tag);
            out.add(new Vec3(
                    ctag.getDouble("X"),
                    ctag.getDouble("Y"),
                    ctag.getDouble("Z")
            ));
        }
        return out;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        entityData.set(PIGMENT, compoundTag.getCompound(TAG_PIGMENT));
        entityData.set(VERTICES, compoundTag.getList(TAG_VERTICES, Tag.TAG_COMPOUND));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.put(TAG_VERTICES, entityData.get(VERTICES));
        compoundTag.put(TAG_PIGMENT, entityData.get(PIGMENT));
    }



    public void playVertexParticles(FrozenPigment pigment, List<Vec3> verts) {
        double radius = 0.1;

        int repeats = switch (Minecraft.getInstance().options.particles().get()) {
            case ALL -> 50;
            case DECREASED -> 20;
            case MINIMAL -> 5;
        };

        for (Vec3 vert : verts) {
            Vec3 stretch = normToCentre(vert);
            for (int i = 0; i <= repeats; i++) {
                int colour = nextColour(pigment, random);
                Vec3 gauss = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                // component of gauss along stretch
                Vec3 proj = stretch.scale(gauss.dot(stretch));
                // components perp to stretch
                Vec3 perp = gauss.subtract(proj);
                // scale the parallel component and add it back to the perps
                Vec3 stretched = proj.scale(2).add(perp.scale(.5));

                level().addParticle(
                        new ConjureParticleOptions(colour),
                        (vert.x + radius * stretched.x),
                        (vert.y + radius * stretched.y),
                        (vert.z + radius * stretched.z),
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
            playVertexParticles(getPigment(), getVertices());
        }
    }

    public void triggerAssembly() {}

}

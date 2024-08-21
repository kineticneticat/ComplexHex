package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.mixin.EntityDataSerialisersMixin;
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

    private List<Vec3> Vertices = new ArrayList<>();

    public AssemblyManagerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void addVertex(Vec3 vec) {
        Vertices.add(vec);
        setPos(centre());
        entityData.set(VERTICES, vertsAsTag(Vertices));
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
        ListTag NodesTag = NBTHelper.getList(compoundTag, TAG_VERTICES, Tag.TAG_LIST);
        NodesTag = NodesTag == null ? new ListTag() : NodesTag;
        Vertices = tagAsVerts(NodesTag);
        entityData.set(PIGMENT, compoundTag.getCompound(TAG_PIGMENT));
        entityData.set(VERTICES, NodesTag);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.put(TAG_VERTICES, entityData.get(VERTICES));
        NBTHelper.putCompound(compoundTag, TAG_PIGMENT, entityData.get(PIGMENT));
        Complexhex.LOGGER.info(compoundTag);
    }

    public FrozenPigment setPigment(FrozenPigment pigment) {
        entityData.set(PIGMENT, pigment.serializeToNBT());
        return pigment;
    }

    public void playVertexParticles(FrozenPigment pigment, List<Vec3> verts) {
        double radius = 0.1;

        int repeats = switch (Minecraft.getInstance().options.particles().get()) {
            case ALL -> 50;
            case DECREASED -> 20;
            case MINIMAL -> 5;
        };

        for (Vec3 vert : verts) {
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
            List<Vec3> verts = tagAsVerts(entityData.get(VERTICES));
            playVertexParticles(pigment, verts);
        }
    }

    public void triggerAssembly() {}

}

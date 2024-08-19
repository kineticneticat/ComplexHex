package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AssemblyManagerEntity extends Entity {

    private final String TAG_VERTICES = "Vertices";

    private List<Vec3> Vertices = new ArrayList<>();

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
    }

    public void triggerAssembly() {

    }

}

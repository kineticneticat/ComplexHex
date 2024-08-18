package dev.kineticcat.complexhex.api.util;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import dev.kineticcat.complexhex.entity.ComplexHexEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class utilStuffLmao {
    public static CompoundTag Vec3ToCompoundTag(Vec3 pos) {
        CompoundTag ctag = new CompoundTag();
        ctag.putDouble("X", pos.x);
        ctag.putDouble("Y", pos.y);
        ctag.putDouble("Z", pos.z);
        return ctag;
    }
    public static Vec3 CompoundTagToVec3(CompoundTag ctag) {
        return new Vec3(
                ctag.getDouble("X"),
                ctag.getDouble("Y"),
                ctag.getDouble("Z")
        );
    }

    public static AssemblyManagerEntity getManagerAtPos(Vec3 pos, Level level) {
        AABB aabb = new AABB(pos.add(new Vec3(-0.5, -0.5, -0.5)), pos.add(new Vec3(0.5, 0.5, 0.5)));
        List<AssemblyManagerEntity> entities = level.getEntities(ComplexHexEntities.ASSEMBLY_MANAGER, aabb, e -> e instanceof AssemblyManagerEntity);
        if (entities.size() == 0) return null;
        // assume only 1 manager since it shouldn't be possible to have multiple in one spot
        return entities.get(0);
    }
}

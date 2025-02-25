package dev.kineticcat.complexhex.util;

import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.api.util.Field;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataStorage extends SavedData {
    public Map<String, CompoundTag> Fields = new HashMap<>();
    @Override
    public CompoundTag save(CompoundTag ctag) {
        Fields.forEach(ctag::put);
        return ctag;
    }

    private static DataStorage createFromTag(CompoundTag ctag) {
        DataStorage data = new DataStorage();
        for (String key : ctag.getAllKeys()) {
            data.Fields.put(key, ctag.getCompound(key));
        }
        return data;
    }
    public static DataStorage getServerData(MinecraftServer server) {
        DimensionDataStorage dds = Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage();
        return dds.computeIfAbsent(DataStorage::createFromTag, DataStorage::new, Complexhex.MOD_ID);
    }
    public static void setField(ServerLevel level, String name, Field field) {
        DataStorage ds = getServerData(level.getServer());
        ds.Fields.put(name, field.serialise());
        ds.setDirty();
    }

}

package dev.kineticcat.complexhex.datagen.tag;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import at.petrak.hexcasting.xplat.Platform;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

import static at.petrak.hexcasting.api.HexAPI.modLoc;

// copied from hexcasting lmao

public class ComplexHexActionTagProvider extends TagsProvider<ActionRegistryEntry> {
    public ComplexHexActionTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, IXplatAbstractions.INSTANCE.getActionRegistry().key(), provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // In-game almost all great spells are always per-world
        for (var normalGreat : new String[]{
            "summonblockdisplay", "summonitemdisplay", "summontextdisplay"
        }) {
            var loc = modLoc(normalGreat);
            var key = ResourceKey.create(IXplatAbstractions.INSTANCE.getActionRegistry().key(), loc);
            tag(ersatzActionTag(HexTags.Actions.REQUIRES_ENLIGHTENMENT)).add(key);
            tag(ersatzActionTag(HexTags.Actions.CAN_START_ENLIGHTEN)).add(key);
            tag(ersatzActionTag(HexTags.Actions.PER_WORLD_PATTERN)).add(key);
        }
    }

    private static TagKey<ActionRegistryEntry> ersatzActionTag(TagKey<ActionRegistryEntry> real) {
        if (IXplatAbstractions.INSTANCE.platform() == Platform.FABRIC) {
            var fakeKey = ResourceKey.<ActionRegistryEntry>createRegistryKey(
                new ResourceLocation("foobar", "hexcasting/tags/action"));
            return TagKey.create(fakeKey, real.location());
        } else {
            return real;
        }
    }
}
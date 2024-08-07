package dev.kineticcat.complexhex.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemAwakenedBurntShard extends Item {
    public ItemAwakenedBurntShard(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) { return true; }
}

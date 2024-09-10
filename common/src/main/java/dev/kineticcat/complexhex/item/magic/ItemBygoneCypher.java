package dev.kineticcat.complexhex.item.magic;

import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.mod.HexConfig;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBygoneCypher extends ItemPackagedHex {
    public ItemBygoneCypher(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean breakAfterDepletion() {
        return true;
    }

    @Override
    public int cooldown() {
        return HexConfig.common().cypherCooldown();
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag flag) {
        if (!hasHex(stack)) {super.appendHoverText(stack, level, tooltip, flag); return;}
        if (level == null) {return;}
        tooltip.add(ListIota.TYPE.display(NBTHelper.getList(stack, TAG_PROGRAM, Tag.TAG_COMPOUND)));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public boolean canDrawMediaFromInventory(ItemStack stack) {
        return false;
    }
}

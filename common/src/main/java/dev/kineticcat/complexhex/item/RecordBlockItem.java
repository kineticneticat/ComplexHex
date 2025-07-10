package dev.kineticcat.complexhex.item;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecordBlockItem extends BlockItem {


    public RecordBlockItem(Block block, Properties properties) {
        super(block, properties);
    }


    public static class Inert extends RecordBlockItem {
        public Inert(Block block, Properties properties) {
            super(block, properties);
        }
       @Override
        public @NotNull String getDescriptionId() {
            return "item.complexhex.inert_record";
        }

    }
    public static class Quenched extends RecordBlockItem implements IotaHolderItem {
        public static final String TAG_DATA = "data";
        public Quenched(Block block, Properties properties) {
            super(block, properties);
        }
        @Override
        public boolean canPlace(@NotNull BlockPlaceContext blockPlaceContext, @NotNull BlockState blockState) { return false; }
        @Override
        public @NotNull String getDescriptionId() {
            return "item.complexhex.quenched_record";
        }

        @Override
        public @Nullable CompoundTag readIotaTag(ItemStack stack) {
            return NBTHelper.getCompound(stack, TAG_DATA);
        }

        @Override
        public boolean writeable(ItemStack stack) { return true;}

        public boolean canWrite(ItemStack stack, Iota datum) {
            return datum instanceof ListIota;
        }
        @Override
        public void writeDatum(ItemStack stack, Iota datum) {
            if (datum == null) {
                stack.removeTagKey(TAG_DATA);
            } else {
                NBTHelper.put(stack, TAG_DATA, IotaType.serialize(datum));
            }
        }
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents,
                                    TooltipFlag pIsAdvanced) {
            IotaHolderItem.appendHoverText(this, pStack, pTooltipComponents, pIsAdvanced);
        }


    }

}

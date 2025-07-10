package dev.kineticcat.complexhex.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class RecordJacketItem extends Item {
    public static final String TAG_RECORD = "Record";
    public static final String TAG_COVER = "Cover";
    public RecordJacketItem(Properties properties) {
        super(properties);
    }

    public ItemStack record;
//    public String cover = "a".repeat(768);

    @Override
    public boolean overrideStackedOnOther(ItemStack jacket, Slot slot, ClickAction action, Player player) {

        if (action != ClickAction.SECONDARY) return false;
        else {
            CompoundTag ctag = jacket.getOrCreateTag();
            ItemStack records = slot.getItem();
            if (ctag.contains(TAG_RECORD)) {
                if (records.isEmpty()) {
                    slot.safeInsert(ItemStack.of(ctag.getCompound(TAG_RECORD)));
                    slot.setChanged();
                    ctag.remove(TAG_RECORD);
                    return true;
                }
                return false;
            } else {
                if(!records.isEmpty()) {
                    ItemStack record = slot.safeTake(records.getCount(), 1, player);
                    slot.setChanged();
                    ctag.put(TAG_RECORD, record.getOrCreateTag());
                    return true;
                }
                return false;
            }
        }

//        if (action != ClickAction.SECONDARY) return false;
//        else {
//            CompoundTag ctag = jacket.getOrCreateTag();
//            ItemStack records = slot.getItem();
//            if (records.isEmpty()) {
//                popRecord(jacket).ifPresent(record -> {
//                    ctag.remove(TAG_RECORD);
//                    jacket.save(ctag);
//                    slot.safeInsert(record);
//                });
//            } else {
//                if (records.getItem() instanceof RecordBlockItem.Quenched) {
//                    grabRecord(records).ifPresent(record -> {
//                        // NOTE for tomorrow: record is in multiple levels of jackets, probably because of all the saving and setting
//                        ctag.put(TAG_RECORD, slot.safeTake(records.getCount(), 1, player).getOrCreateTag());
//                        jacket.save(ctag);
//                    });
//                }
//            }
//            return true;
//        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack jacket, ItemStack records, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (records.isEmpty()) {
                popRecord(jacket).ifPresent(access::set);
            } else {
                grabRecord(records).ifPresent(slot::safeInsert);
            }
            return true;
        } else return false;
    }

    public boolean hasRecord(ItemStack jacket) {
        var ctag = jacket.getOrCreateTag();
        return ctag.contains(TAG_RECORD);
    }
    public Optional<ItemStack> popRecord(ItemStack jacket) {
        if (hasRecord(jacket)) {
            var ctag = jacket.getOrCreateTag();
            var record = ItemStack.of(ctag.getCompound(TAG_RECORD));
            return Optional.of(record);
        } else return Optional.empty();
    }
    public Optional<ItemStack> grabRecord(ItemStack records) {
        if (!records.isEmpty()) {
            var record = records.copyWithCount(1);
            return Optional.of(record);
        } else return Optional.empty();
    }



}

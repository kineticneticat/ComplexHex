package dev.kineticcat.complexhex.item.magic;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.item.HexHolderItem;
import at.petrak.hexcasting.api.mod.HexConfig;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.items.magic.ItemMediaHolder;
import at.petrak.hexcasting.common.msgs.MsgNewSpiralPatternsS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemMultifact extends ItemMediaHolder implements HexHolderItem {
    public static final String TAG_PROGRAM_LIST = "hexes";
    public static final String TAG_PIGMENT = "pigment";
    public static final String TAG_CURRENT_IDX = "nextHex";
    public static final String TAG_STACK = "stack";
    public static final String TAG_NAMES = "hexnames";



    public ItemMultifact(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canProvideMedia(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRecharge(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canDrawMediaFromInventory(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasHex(ItemStack stack) {
        return NBTHelper.hasList(stack, TAG_PROGRAM_LIST, Tag.TAG_LIST);
    }

    public int getCurrentIdx(ItemStack stack) {
        return NBTHelper.getInt(stack, TAG_CURRENT_IDX, Tag.TAG_INT);
    }
    public int getAmount(ItemStack stack) {
        ListTag hexes = NBTHelper.getList(stack, TAG_PROGRAM_LIST, Tag.TAG_LIST);
        return (hexes != null) ? hexes.size() : 0;
    }
    public int getNextIdx(ItemStack stack) {
        return (getCurrentIdx(stack)+1) % getAmount(stack);
    }
    public @Nullable ListTag getHexTag(ItemStack stack, int idx) {
        ListTag hexes = NBTHelper.getList(stack, TAG_PROGRAM_LIST, Tag.TAG_LIST);
        hexes = (hexes != null) ? hexes : new ListTag();
        return hexes.getList(idx);
    }

    public @Nullable List<Iota> getHex(ItemStack stack, ServerLevel level, int idx) {
        ListTag hex = getHexTag(stack, idx);
        hex = hex == null ? new ListTag() : hex;
        List<Iota> out = new ArrayList<>();
        for (var patTag : hex) {
            CompoundTag ctag = NBTHelper.getAsCompound(patTag);
            out.add(IotaType.deserialize(ctag, level));
        }
        return out;
    }

    @Override
    public @Nullable List<Iota> getHex(ItemStack stack, ServerLevel level) {
        return getHex(stack, level, getCurrentIdx(stack));
    }


    // hexes can only be appended, have to clear whole thing to redo
    @Override
    public void writeHex(ItemStack stack, List<Iota> program, @Nullable FrozenPigment pigment, long media) {
        ListTag newHex = new ListTag();
        for (Iota pat : program) {
            newHex.add(IotaType.serialize(pat));
        }

        // media will be 0 on subsequent additions since the max is fixed by the first cast
        boolean isFirst = !hasHex(stack);

        ListTag hexes = isFirst ? new ListTag() : (NBTHelper.getList(stack, TAG_PROGRAM_LIST, Tag.TAG_LIST));
        if (hexes == null) hexes = new ListTag();
        hexes.add(newHex);
        NBTHelper.putList(stack, TAG_PROGRAM_LIST, hexes);

        if (pigment != null)
            NBTHelper.putCompound(stack, TAG_PIGMENT, pigment.serializeToNBT());

        if (isFirst) {
            withMedia(stack, media, media);
            NBTHelper.putCompound(stack, TAG_NAMES, new CompoundTag());
            NBTHelper.putList(stack, TAG_STACK, new ListTag());
            NBTHelper.putInt(stack, TAG_CURRENT_IDX, 0);
        }
    }
    @Override
    public void clearHex(ItemStack stack) {
        NBTHelper.remove(stack, TAG_PROGRAM_LIST);
        NBTHelper.remove(stack, TAG_PIGMENT);
        NBTHelper.remove(stack, TAG_CURRENT_IDX);
        NBTHelper.remove(stack, TAG_MEDIA);
        NBTHelper.remove(stack, TAG_MAX_MEDIA);
        NBTHelper.remove(stack, TAG_NAMES);
    }

    @Override
    public @Nullable FrozenPigment getPigment(ItemStack stack) {
        var ctag = NBTHelper.getCompound(stack, TAG_PIGMENT);
        if (ctag == null)
            return null;
        return FrozenPigment.fromNBT(ctag);
    }

    public static ListTag serialiseStack(List<Iota> stack) {
        ListTag ltag = new ListTag();
        for (Iota iota : stack) ltag.add(IotaType.serialize(iota));
        return ltag;
    }

    public static List<Iota> deserialiseStack(ListTag ltag, ServerLevel level) {
        List<Iota> stack = new ArrayList<>();
        for (Tag ctag : ltag) stack.add(IotaType.deserialize((CompoundTag) ctag, level));
        return stack;
    }

    public List<Iota> getCastingStack(ItemStack stack, ServerLevel level) {
        ListTag ltag = NBTHelper.getList(stack, TAG_STACK, Tag.TAG_COMPOUND);
        ltag = ltag == null ? new ListTag() : ltag;
        return deserialiseStack(ltag, level);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!hasHex(stack)) return InteractionResultHolder.fail(stack);
        if (level.isClientSide) return InteractionResultHolder.success(stack);

        List<Iota> hex = getHex(stack, (ServerLevel) level);
        if (hex == null) return InteractionResultHolder.fail(stack);
        ServerPlayer sPlayer = (ServerPlayer) player;
        PackagedItemCastEnv env = new PackagedItemCastEnv(sPlayer, hand);
        CastingVM harness = CastingVM.empty(env);
        CastingImage image = harness.getImage().copy(
                getCastingStack(stack, (ServerLevel) level),
                harness.getImage().getParenCount(),
                harness.getImage().getParenthesized(),
                harness.getImage().getEscapeNext(),
                harness.getImage().getOpsConsumed(),
                harness.getImage().getUserData()
        );
        harness.setImage(image);

        ExecutionClientView clientView = harness.queueExecuteAndWrapIotas(hex, sPlayer.serverLevel());

        List<HexPattern> patterns = hex.stream()
                .filter(i -> i instanceof PatternIota)
                .map(i -> ((PatternIota) i).getPattern())
                .toList();
        var packet = new MsgNewSpiralPatternsS2C(sPlayer.getUUID(), patterns, 140);
        IXplatAbstractions.INSTANCE.sendPacketToPlayer(sPlayer, packet);
        IXplatAbstractions.INSTANCE.sendPacketTracking(sPlayer, packet);

        player.awardStat(Stats.ITEM_USED.get(this));

        sPlayer.getCooldowns().addCooldown(this, HexConfig.common().artifactCooldown());

        if (clientView.getResolutionType().getSuccess()) {
            new ParticleSpray(player.position(), new Vec3(0.0, 1.5, 0.0), 0.4, Math.PI/3, 30)
                    .sprayParticles(sPlayer.serverLevel(), env.getPigment());
        }

        var sound = env.getSound().sound();
        if (sound != null) {
            Vec3 soundPos = sPlayer.position();
            sPlayer.level().playSound(null, soundPos.x, soundPos.y, soundPos.z,
                    sound, SoundSource.PLAYERS, 1f, 1);
        }

        if (getNextIdx(stack) == 0) {
            // round finished
            NBTHelper.put(stack, TAG_STACK, new ListTag());
        } else {
            NBTHelper.put(stack, TAG_STACK, serialiseStack(harness.getImage().getStack()));

        }

        NBTHelper.putInt(stack, TAG_CURRENT_IDX, (getCurrentIdx(stack)+1) % getAmount(stack) );

        CompoundTag names = NBTHelper.getCompound(stack, TAG_NAMES);
        String nameKey = String.valueOf(getCurrentIdx(stack));
        String name = NBTHelper.getString(names, nameKey);
        if (name != null) {
            stack.setHoverName(Component.Serializer.fromJson(name));
        } else {
            stack.resetHoverName();
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        int current = getCurrentIdx(stack);
        String nameKey = String.valueOf(current);
        CompoundTag names = NBTHelper.getOrCreateCompound(stack, TAG_NAMES);
        if (stack.hasCustomHoverName()) {
            names.putString(nameKey, Component.Serializer.toJson(stack.getHoverName()));
        } else {
            names.remove(nameKey);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (!hasHex(stack)) { super.appendHoverText(stack, level, tooltip, isAdvanced); return; }

        CompoundTag names = NBTHelper.getOrCreateCompound(stack, TAG_NAMES);
        for (int i = 0; i < getAmount(stack); i++) {
            String nameKey = String.valueOf(i);
            MutableComponent cname;

            String name = names.getString(nameKey);

            cname = Component.Serializer.fromJson(name);

            if (cname == null) {
                ListTag ltag = getHexTag(stack, i);
                cname = (MutableComponent) ListIota.TYPE.display(ltag);
            }

            cname = cname.withStyle(ChatFormatting.DARK_PURPLE);
            if (i == getCurrentIdx(stack)) {
                tooltip.add(Component.literal("-> ").withStyle(ChatFormatting.LIGHT_PURPLE).append(cname));
            } else {
                tooltip.add(cname);
            }
        }

        tooltip.add(Component.literal(""));
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }

}

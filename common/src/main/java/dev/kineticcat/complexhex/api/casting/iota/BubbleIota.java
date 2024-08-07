package dev.kineticcat.complexhex.api.casting.iota;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BubbleIota extends Iota {

    public BubbleIota(Iota iota) {
        super(ComplexHexIotaTypes.BUBBLE, iota);
    }
    public <T extends Iota> T getContainedIota() {
        return (T) this.payload;
    }
    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof BubbleIota iota
                && tolerates(this.getContainedIota(), iota.getContainedIota());
    }

    public static boolean tolerates(Iota A, Iota B) {
        return Iota.typesMatch(A, B) && Iota.tolerates(A, B);
    }

    @Override
    public Tag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("content", IotaType.serialize(this.getContainedIota()));
        return tag;
    }

    @Override
    public boolean executable() { return true; }
    @Override
    public Iterable<Iota> subIotas() {
        if (this.getContainedIota() instanceof BubbleIota bubble) {
            return bubble.subIotas();
        } else return List.of(this.getContainedIota());
    }

    // what the fuck lmao
    @Override
    public @NotNull CastResult execute(CastingVM vm, ServerLevel world, SpellContinuation continuation) {

        Iota push = this.getContainedIota();
        List<Iota> stack = vm.getImage().getStack();
        stack.add(push);
        CastingImage image2 = vm.getImage().copy(
                stack,
                vm.getImage().getParenCount(),
                vm.getImage().getParenthesized(),
                vm.getImage().getEscapeNext(),
                vm.getImage().getOpsConsumed() + 1,
                vm.getImage().getUserData()
        );

        return new CastResult(
                this,
                continuation,
                image2,
                List.of(),
                ResolvedPatternType.EVALUATED,
                HexEvalSounds.MUTE
        );

    }

    public static IotaType<BubbleIota> TYPE = new IotaType<BubbleIota>() {
        @Override
        public BubbleIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            CompoundTag ctag = HexUtils.downcast(tag, CompoundTag.TYPE);
            CompoundTag content = HexUtils.downcast(ctag.get("content"), CompoundTag.TYPE);
            return new BubbleIota(IotaType.deserialize(content, world));
        }

        @Override
        public Component display(Tag tag) {
            CompoundTag ctag = HexUtils.downcast(tag, CompoundTag.TYPE);
            Component iotaDisplay = IotaType.getDisplay(HexUtils.downcast(ctag.get("content"), CompoundTag.TYPE));
            return Component.translatable("complexhex.tooltip.bubble_contents", iotaDisplay).withStyle(ChatFormatting.AQUA);
        }

        @Override
        public int color() {
            return 0xff_fc0522;
        }
    };
    private static final ChatFormatting ComplexNumberColour = ChatFormatting.DARK_RED;
}

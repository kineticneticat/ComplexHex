package dev.kineticcat.complexhex.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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
    public static class Quenched extends RecordBlockItem {
        public Quenched(Block block, Properties properties) {
            super(block, properties);
        }
        @Override
        public boolean canPlace(@NotNull BlockPlaceContext blockPlaceContext, @NotNull BlockState blockState) { return false; }
        @Override
        public @NotNull String getDescriptionId() {
            return "item.complexhex.quenched_record";
        }
    }

}

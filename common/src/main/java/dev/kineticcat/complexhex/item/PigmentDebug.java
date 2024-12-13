package dev.kineticcat.complexhex.item;

import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.kineticcat.complexhex.util.DefinetlyNotACopyOfItemUUIDPigment.colourPlease;

public class PigmentDebug extends Item {
    public PigmentDebug(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        int[] colours = colourPlease(player.getUUID());
        Complexhex.LOGGER.info(colours);
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }
}

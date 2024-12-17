package dev.kineticcat.complexhex.item;

import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.UUID;

import static dev.kineticcat.complexhex.util.DefinetlyNotACopyOfItemUUIDPigment.colourPlease;

public class PigmentDebug extends Item {
    public PigmentDebug(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide()) {
            UUID kineticcat = UUID.fromString("23e372d5-6daa-4047-8f3a-c151fcc2799b");
            int[] colours = colourPlease(kineticcat);
            Complexhex.LOGGER.info(new Color(colours[0]).toString());
            Complexhex.LOGGER.info(new Color(colours[1]).toString());
            Complexhex.LOGGER.info(kineticcat.getMostSignificantBits());
            Complexhex.LOGGER.info(kineticcat.getLeastSignificantBits());
        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }
}

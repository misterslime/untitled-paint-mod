package com.azzyberries.untitledpaintmod.items;

import com.azzyberries.untitledpaintmod.UntitledPaintMod;
import com.azzyberries.untitledpaintmod.world.PaintSavedData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PaintbrushItem extends Item {
    public PaintbrushItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {

        if (context.getLevel().isClientSide())
            return InteractionResult.PASS;

        if (context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.AIR))
            return InteractionResult.PASS;

        PaintSavedData paintData = PaintSavedData.get(Objects.requireNonNull(Objects.requireNonNull(context.getLevel().getServer()).getLevel(context.getLevel().dimension())));

        paintData.paintPosition(context.getClickedPos());

        UntitledPaintMod.LOGGER.info(Integer.toString(paintData.paintPositions()));

        return InteractionResult.SUCCESS;
    }
}

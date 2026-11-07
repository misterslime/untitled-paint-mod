package com.azzyberries.untitledpaintmod.mixin;

import com.azzyberries.untitledpaintmod.UntitledPaintMod;
import com.azzyberries.untitledpaintmod.world.PaintSavedData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Debug(export = true)
@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "shouldRenderFace", at = @At("RETURN"), cancellable = true)
    private static void onSkipRendering(BlockState state, BlockGetter level, BlockPos offset, Direction face, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ClientLevel)
            UntitledPaintMod.LOGGER.info("meows like a bunny");

        if (state.is(Blocks.DIAMOND_BLOCK)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

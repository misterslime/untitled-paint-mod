package com.azzyberries.untitledpaintmod.world;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;


public class PaintSavedData extends SavedData {
    private final ObjectOpenHashSet<Vec3i> blockPaints;

    public PaintSavedData() {
        this.blockPaints = new ObjectOpenHashSet<Vec3i>();
    }

    public static PaintSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<PaintSavedData>(
                        PaintSavedData::new,
                        (tag, provider) -> PaintSavedData.load(level, tag, provider)),
                "paint_data");
    }

    public static PaintSavedData load(ServerLevel level, CompoundTag tag, HolderLookup.Provider lookupProvider) {
        PaintSavedData blockPaintData = new PaintSavedData();

        if (!tag.contains("block_paint_positions", Tag.TAG_LIST))
            return blockPaintData;

        ListTag blockPaintPositions = tag.getList("block_paint_positions", Tag.TAG_LONG);

        for (int i = 0; i < blockPaintPositions.size(); i++) {
            long packedPos = getLong(blockPaintPositions, i);
            Vec3i blockPos = new Vec3i(BlockPos.getX(packedPos), BlockPos.getY(packedPos), BlockPos.getZ(packedPos));
            blockPaintData.blockPaints.add(blockPos);
        }

        return blockPaintData;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        ListTag blockPaintPositions = new ListTag();

        for (Vec3i value : blockPaints) {
            blockPaintPositions.add(LongTag.valueOf(BlockPos.asLong(value.getX(), value.getY(), value.getZ())));
        }

        compoundTag.put("block_paint_positions", blockPaintPositions);

        return compoundTag;
    }

    public boolean positionIsPainted(BlockPos pos) {
        return this.blockPaints.contains(pos);
    }

    public void paintPosition(BlockPos pos) {
        this.blockPaints.add(pos);
        this.setDirty();
    }

    public void cleanPaint(BlockPos pos) {
        this.blockPaints.remove(pos);
        this.setDirty();
    }

    public int paintPositions() {
        return this.blockPaints.size();
    }

    // why doesn't this just exist in ListTag
    public static long getLong(ListTag listTag, int index) {
        if (index >= 0 && index < listTag.size()) {
            Tag tag = listTag.get(index);
            if (tag.getId() == Tag.TAG_LONG) {
                return ((LongTag)tag).getAsLong();
            }
        }

        return 0;
    }
}

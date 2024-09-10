package uk.protonull.civ.chesttracker.pseudo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public final class PseudoChestBlockEntity extends BlockEntity {
    public PseudoChestBlockEntity(
        final @NotNull BlockPos pos
    ) {
        super(BlockEntityType.CHEST, pos, Blocks.CHEST.defaultBlockState());
    }

    @Override
    public void setLevel(
        final Level level
    ) {
        throw new NotImplementedException("Why is this being called?!");
    }
}

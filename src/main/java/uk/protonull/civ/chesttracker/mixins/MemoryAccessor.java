package uk.protonull.civ.chesttracker.mixins;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import red.jackf.chesttracker.api.memory.Memory;

@Mixin(Memory.class)
public interface MemoryAccessor {
    @Accessor("position")
    @NotNull BlockPos civchesttracker$getPosition();

    @Accessor("position")
    void civchesttracker$setPosition(
        final @NotNull BlockPos position
    );
}

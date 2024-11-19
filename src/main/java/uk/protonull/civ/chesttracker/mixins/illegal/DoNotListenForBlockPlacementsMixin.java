package uk.protonull.civ.chesttracker.mixins.illegal;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import red.jackf.chesttracker.api.providers.context.BlockPlacedContext;
import red.jackf.chesttracker.api.providers.defaults.DefaultProvider;

@Mixin(DefaultProvider.class)
public abstract class DoNotListenForBlockPlacementsMixin {
    /**
     * @author Protonull
     * @reason Prevent ChestTracker from listening for block placements.
     */
    @Overwrite(remap = false)
    public void onBlockPlaced(
        final @NotNull BlockPlacedContext context
    ) {
        // DO NOTHING
    }
}

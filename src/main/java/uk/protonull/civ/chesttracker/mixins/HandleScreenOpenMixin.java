package uk.protonull.civ.chesttracker.mixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jackf.chesttracker.api.providers.context.ScreenOpenContext;
import red.jackf.chesttracker.api.providers.defaults.DefaultProvider;
import uk.protonull.civ.chesttracker.handlers.HandleOpen;

@Mixin(DefaultProvider.class)
public abstract class HandleScreenOpenMixin {
    @Inject(
        method = "onScreenOpen",
        at = @At("TAIL"),
        remap = false
    )
    protected void ctt$inject$onScreenOpen$handleInventoryOpen(
        final @NotNull ScreenOpenContext context,
        final @NotNull CallbackInfo ci
    ) {
        HandleOpen.handleScreenOpen(context);
    }
}

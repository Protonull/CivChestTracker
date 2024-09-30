package uk.protonull.civ.chesttracker.mixins.cruft;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.api.providers.ServerProvider;
import red.jackf.chesttracker.impl.DefaultChestTrackerPlugin;

@Mixin(DefaultChestTrackerPlugin.class)
public abstract class DoNotRegisterHypixelProviderMixin {
    @Redirect(
        method = "load",
        at = @At(
            value = "INVOKE",
            target = "Lred/jackf/chesttracker/api/providers/ProviderUtils;registerProvider(Lred/jackf/chesttracker/api/providers/ServerProvider;)Lred/jackf/chesttracker/api/providers/ServerProvider;",
            ordinal = 1
        ),
        remap = false
    )
    protected <T extends ServerProvider> T civchesttracker$voidRegistration(
        final @NotNull T provider
    ) {
        return provider; // Do nothing
    }
}

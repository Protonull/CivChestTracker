package uk.protonull.civ.chesttracker.mixins.cruft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import red.jackf.chesttracker.impl.DefaultChestTrackerPlugin;

@Mixin(DefaultChestTrackerPlugin.class)
public abstract class DoNotRegisterDefaultChestTrackerPluginMixin {
    /**
     * @author Protonull
     * @reason Do not register the default plugin.
     */
    @Overwrite(remap = false)
    public void load() {
        // DO NOTHING
    }
}

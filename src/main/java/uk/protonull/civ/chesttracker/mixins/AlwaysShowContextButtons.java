package uk.protonull.civ.chesttracker.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import red.jackf.chesttracker.impl.gui.invbutton.ui.InventoryButton;

@Mixin(InventoryButton.class)
public abstract class AlwaysShowContextButtons {
    @Shadow(remap = false)
    private boolean isDragging;

    @ModifyVariable(
        method = "showExtraButtons",
        at = @At("HEAD"),
        argsOnly = true,
        remap = false
    )
    protected boolean civchesttracker$alwaysShowUnlessDragging(
        final boolean shouldShow
    ) {
        if (this.isDragging) {
            return shouldShow;
        }
        return true;
    }
}

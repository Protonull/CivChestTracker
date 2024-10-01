package uk.protonull.civ.chesttracker.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.api.gui.ScreenBlacklist;
import red.jackf.chesttracker.api.providers.MemoryLocation;
import red.jackf.chesttracker.impl.gui.invbutton.ui.InventoryButton;
import red.jackf.chesttracker.impl.gui.invbutton.ui.RenameButton;
import red.jackf.chesttracker.impl.gui.invbutton.ui.SecondaryButton;
import red.jackf.chesttracker.impl.memory.MemoryBankImpl;
import uk.protonull.civ.chesttracker.gui.widgets.OpenedInventoryButtons;
import uk.protonull.civ.chesttracker.mixing.InventoryWindow;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

@Mixin(InventoryButton.class)
public abstract class InventoryContextButtonsMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryButton.class);

    @Shadow(remap = false)
    private boolean isDragging;

    @Final
    @Shadow(remap = false)
    private List<SecondaryButton> secondaryButtons;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lred/jackf/chesttracker/api/gui/ScreenBlacklist;isBlacklisted(Ljava/lang/Class;)Z"
        ),
        remap = false
    )
    protected boolean civchesttracker$replaceBlacklistCheck(
        final @NotNull Class<? extends Screen> screenClass,
        // local captures
        final @Local(argsOnly = true) AbstractContainerScreen<?> parent
    ) {
        addExtraButtons(screenClass, parent);
        return true;
    }

    @Unique
    private void addExtraButtons(
        final @NotNull Class<? extends Screen> screenClass,
        final @NotNull AbstractContainerScreen<?> parent
    ) {
        this.secondaryButtons.clear();
        if (ScreenBlacklist.isBlacklisted(screenClass)) {
            this.secondaryButtons.add(OpenedInventoryButtons.BLACKLISTED);
            return;
        }
        final MemoryBankImpl serverStorage = Shortcuts.getChestTrackerStorage();
        if (serverStorage == null) {
            this.secondaryButtons.add(new OpenedInventoryButtons.NoMemoryBank(parent));
            return;
        }
        final MemoryLocation target = InventoryWindow.getIdentifier(parent);
        if (target != null) {
            this.secondaryButtons.add(new OpenedInventoryButtons.ForgetContainer(parent, serverStorage, target));
            this.secondaryButtons.add(new RenameButton(parent, serverStorage, target));
            return;
        }
        final ResourceLocation dimensionKey = Shortcuts.getDimensionKeyFromInventoryWindow(parent);
        // Inventory is not being tracked yet, add button to offer tracking
        this.secondaryButtons.add(new OpenedInventoryButtons.TrackContainer(
            parent,
            (blockPos) -> InventoryWindow.setIdentifier(parent, MemoryLocation.inWorld(dimensionKey, blockPos))
        ));
    }

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

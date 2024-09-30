package uk.protonull.civ.chesttracker.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
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
import red.jackf.chesttracker.impl.memory.MemoryKeyImpl;
import uk.protonull.civ.chesttracker.gui.screens.ContainerLocationDeciderScreen;
import uk.protonull.civ.chesttracker.gui.widgets.ForgetContainerButton;
import uk.protonull.civ.chesttracker.gui.widgets.TrackContainerButton;
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

    @Shadow
    protected static void setRestoreLocation(
        final @NotNull AbstractContainerScreen<?> screen,
        final @NotNull MemoryLocation location
    ) { }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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
        final @Local(argsOnly = true) AbstractContainerScreen<?> parent,
        final @Local(argsOnly = true) Optional<MemoryLocation> target
    ) {
        addExtraButtons(screenClass, parent, target.orElse(null));
        return true;
    }

    @Unique
    private void addExtraButtons(
        final @NotNull Class<? extends Screen> screenClass,
        final @NotNull AbstractContainerScreen<?> parent,
        final MemoryLocation target
    ) {
        this.secondaryButtons.clear();
        if (ScreenBlacklist.isBlacklisted(screenClass)) {
            LOGGER.info("screen is blacklisted!");
            return;
        }
        final MemoryBankImpl serverStorage = Shortcuts.getChestTrackerStorage();
        if (serverStorage == null) {
            LOGGER.info("Server storage is null!");
            return;
        }
        if (target != null) {
            this.secondaryButtons.add(new ForgetContainerButton(() -> {
                serverStorage.removeMemory(target.memoryKey(), target.position());
                Shortcuts.forciblyRefreshScreen(parent);
            }));
            this.secondaryButtons.add(new RenameButton(parent, serverStorage, target));
            return;
        }
        // TODO: Lol
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final ResourceLocation dimensionKey = player.clientLevel.dimension().location();
        final MemoryKeyImpl dimensionStorage = Shortcuts.getStorageForDimension(serverStorage, dimensionKey);
        if (dimensionStorage == null) {
            return;
        }
        // Inventory is not being tracked yet, add button to offer tracking
        this.secondaryButtons.add(new TrackContainerButton(() -> {
            Minecraft.getInstance().setScreen(new ContainerLocationDeciderScreen(
                parent,
                (blockPos) -> setRestoreLocation(parent, MemoryLocation.inWorld(dimensionKey, blockPos)),
                player
            ));
        }));
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

package uk.protonull.civ.chesttracker.mixing;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import red.jackf.chesttracker.api.providers.MemoryLocation;

public interface InventoryWindow {
    @NotNull LocalPlayer civchesttracker$getPlayer();

    @Nullable MemoryLocation civchesttracker$getInventoryIdentifier();

    void civchesttracker$setInventoryIdentifier(
        MemoryLocation inventoryIdentifier
    );

    static @NotNull LocalPlayer getPlayer(
        final @NotNull AbstractContainerScreen<?> screen
    ) {
        return ((InventoryWindow) screen).civchesttracker$getPlayer();
    }

    static @Nullable MemoryLocation getIdentifier(
        final @NotNull AbstractContainerScreen<?> screen
    ) {
        return ((InventoryWindow) screen).civchesttracker$getInventoryIdentifier();
    }

    static void setIdentifier(
        final @NotNull AbstractContainerScreen<?> screen,
        final MemoryLocation inventoryIdentifier
    ) {
        ((InventoryWindow) screen).civchesttracker$setInventoryIdentifier(inventoryIdentifier);
    }
}

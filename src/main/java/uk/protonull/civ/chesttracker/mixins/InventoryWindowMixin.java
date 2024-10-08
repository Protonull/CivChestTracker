package uk.protonull.civ.chesttracker.mixins;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.api.providers.MemoryLocation;
import uk.protonull.civ.chesttracker.mixing.InventoryWindow;

@Mixin(AbstractContainerScreen.class)
public abstract class InventoryWindowMixin implements InventoryWindow {
    @Unique
    private LocalPlayer player;

    @Unique
    private MemoryLocation inventoryIdentifier;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;getDisplayName()Lnet/minecraft/network/chat/Component;"
        )
    )
    protected @NotNull Component civchesttracker$interceptPlayerInstance(
        final @NotNull Inventory playerInventory
    ) {
        this.player = (LocalPlayer) playerInventory.player;
        return playerInventory.getDisplayName();
    }

    @Override
    public @NotNull LocalPlayer civchesttracker$getPlayer() {
        return this.player;
    }

    @Override
    public @Nullable MemoryLocation civchesttracker$getInventoryIdentifier() {
        return this.inventoryIdentifier;
    }

    @Override
    public void civchesttracker$setInventoryIdentifier(
        final MemoryLocation inventoryIdentifier
    ) {
        this.inventoryIdentifier = inventoryIdentifier;
    }
}

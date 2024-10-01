package uk.protonull.civ.chesttracker.utilities;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import red.jackf.chesttracker.api.memory.Memory;
import red.jackf.chesttracker.impl.memory.MemoryBankAccessImpl;
import red.jackf.chesttracker.impl.memory.MemoryBankImpl;
import red.jackf.chesttracker.impl.memory.MemoryKeyImpl;
import uk.protonull.civ.chesttracker.mixing.InventoryWindow;
import uk.protonull.civ.chesttracker.mixins.MemoryAccessor;

public final class Shortcuts {
    public static @Nullable MemoryBankImpl getChestTrackerStorage() {
        return MemoryBankAccessImpl.INSTANCE.getLoadedInternal().orElse(null);
    }

    public static @NotNull BlockPos getMemoryLocation(
        final @NotNull Memory memory
    ) {
        return Objects.requireNonNull(((MemoryAccessor) (Object) memory).civchesttracker$getPosition());
    }

    public static void setMemoryLocation(
        final @NotNull Memory memory,
        final @NotNull BlockPos pos
    ) {
        ((MemoryAccessor) (Object) memory).civchesttracker$setPosition(Objects.requireNonNull(pos));
    }

    public static void forciblyRefreshScreen(
        final @NotNull Screen screen
    ) {
        screen.init(
            Minecraft.getInstance(),
            screen.width,
            screen.height
        );
    }

    public static @NotNull ResourceLocation getDimensionKeyFromInventoryWindow(
        final @NotNull AbstractContainerScreen<?> screen
    ) {
        return InventoryWindow.getPlayer(screen).clientLevel.dimension().location();
    }

    public static @NotNull BlockPos asBlockPos(
        final @NotNull Vec3 vec3
    ) {
        return new BlockPos(
            (int) Math.floor(vec3.x),
            (int) Math.floor(vec3.y),
            (int) Math.floor(vec3.z)
        );
    }

    public static @NotNull Vec3 multiply(
        final @NotNull Vec3 vec3,
        final int amount
    ) {
        return vec3.multiply(
            (double) amount,
            (double) amount,
            (double) amount
        );
    }

    public static @Nullable Memory destructivelyFindFirstContainerWithName(
        final @NotNull MemoryKeyImpl dimensionStorage,
        final @NotNull Component inventoryName
    ) {
        Memory found = null;
        for (final var iter = dimensionStorage.getMemories().values().iterator(); iter.hasNext();) {
            final Memory memory = iter.next();
            if (!inventoryName.equals(memory.savedName())) {
                continue;
            }
            if (found == null) {
                found = memory;
                continue;
            }
            iter.remove();
        }
        return found;
    }

    public static void printMessage(
        final @NotNull Component message
    ) {
        Minecraft.getInstance().gui.getChat().addMessage(message);
    }
}

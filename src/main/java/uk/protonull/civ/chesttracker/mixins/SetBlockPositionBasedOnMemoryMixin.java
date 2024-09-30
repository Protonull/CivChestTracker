package uk.protonull.civ.chesttracker.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.Optional;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.api.ClientBlockSource;
import red.jackf.chesttracker.api.memory.Memory;
import red.jackf.chesttracker.api.providers.InteractionTracker;
import red.jackf.chesttracker.api.providers.context.ScreenOpenContext;
import red.jackf.chesttracker.api.providers.defaults.DefaultProvider;
import red.jackf.chesttracker.impl.memory.MemoryBankImpl;
import red.jackf.chesttracker.impl.memory.MemoryKeyImpl;
import red.jackf.chesttracker.impl.util.CachedClientBlockSource;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

@Mixin(DefaultProvider.class)
public abstract class SetBlockPositionBasedOnMemoryMixin {
    @SuppressWarnings("UnstableApiUsage")
    @Redirect(
        method = "onScreenOpen",
        at = @At(
            value = "INVOKE",
            target = "Lred/jackf/chesttracker/api/providers/InteractionTracker;getLastBlockSource()Ljava/util/Optional;"
        ),
        remap = false
    )
    protected Optional<ClientBlockSource> civchesttracker$onlyReturnPresetLocations(
        final @NotNull InteractionTracker instance,
        final @NotNull @Local(argsOnly = true) ScreenOpenContext context
    ) {
        final MemoryBankImpl serverStorage = Shortcuts.getChestTrackerStorage();
        if (serverStorage == null) {
            return Optional.empty();
        }
        final LocalPlayer player = Shortcuts.getPlayerFromInventoryWindow(context.getScreen());
        final MemoryKeyImpl dimensionStorage = Shortcuts.getStorageForDimension(
            serverStorage,
            player.clientLevel.dimension().location()
        );
        if (dimensionStorage == null) {
            return Optional.empty();
        }
        final Memory matchingContainer = destructivelyFindFirstContainerWithName(
            dimensionStorage,
            context.getScreen().getTitle()
        );
        if (matchingContainer == null) {
            return Optional.empty();
        }
        return Optional.of(new CachedClientBlockSource(
            player.clientLevel,
            Shortcuts.getMemoryLocation(matchingContainer)
        ));
    }

    @Unique
    private static @Nullable Memory destructivelyFindFirstContainerWithName(
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
}

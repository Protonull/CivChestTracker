package uk.protonull.civ.chesttracker;

import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.CommonButtons;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import red.jackf.chesttracker.api.ChestTrackerPlugin;
import red.jackf.chesttracker.api.gui.ScreenBlacklist;
import red.jackf.chesttracker.api.memory.Memory;
import red.jackf.chesttracker.api.providers.MemoryLocation;
import red.jackf.chesttracker.api.providers.ProviderUtils;
import red.jackf.chesttracker.api.providers.ServerProvider;
import red.jackf.chesttracker.api.providers.context.ScreenCloseContext;
import red.jackf.chesttracker.api.providers.context.ScreenOpenContext;
import red.jackf.chesttracker.impl.memory.MemoryBankImpl;
import red.jackf.chesttracker.impl.memory.MemoryKeyImpl;
import red.jackf.jackfredlib.client.api.gps.Coordinate;
import uk.protonull.civ.chesttracker.gui.screens.ContainerLocationDeciderScreen;
import uk.protonull.civ.chesttracker.mixing.InventoryWindow;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

public final class CivChestTrackerPlugin implements ChestTrackerPlugin {
    public static final String ID = "civchesttracker";

    /**
     * Pretty heavily based on {@link red.jackf.chesttracker.impl.DefaultChestTrackerPlugin}
     */
    @Override
    public void load() {
        ScreenBlacklist.add(
            // workstations with no item retention
            CartographyTableScreen.class,
            EnchantmentScreen.class,
            GrindstoneScreen.class,
            ItemCombinerScreen.class,
            LoomScreen.class,
            StonecutterScreen.class,
            BeaconScreen.class,
            // inventory (surv & creative)
            EffectRenderingInventoryScreen.class
        );

        ProviderUtils.registerProvider(new Provider());

        ContainerLocationDeciderScreen.setupPickerRendering();
    }

    private static final class Provider extends ServerProvider {
        @Override
        public ResourceLocation id() {
            return ResourceLocation.fromNamespaceAndPath("civchesttracker", "provider");
        }

        @Override
        public boolean appliesTo(
            final @NotNull Coordinate coordinate
        ) {
            CommonButtons lol;

            return true; // Apply to singleplayer, multiplayer, etc
        }

        @Override
        public void onScreenOpen(
            final @NotNull ScreenOpenContext context
        ) {
            final AbstractContainerScreen<?> screen = context.getScreen();
            if (ScreenBlacklist.isBlacklisted(screen.getClass())) {
                return;
            }
            final MemoryBankImpl serverStorage = Shortcuts.getChestTrackerStorage();
            if (serverStorage == null) {
                return;
            }
            final ResourceLocation dimensionKey = Shortcuts.getDimensionKeyFromInventoryWindow(screen);
            final MemoryKeyImpl dimensionStorage = serverStorage.getKeyInternal(dimensionKey).orElse(null);
            if (dimensionStorage == null) {
                return;
            }
            final Memory foundContainer = Shortcuts.destructivelyFindFirstContainerWithName(
                dimensionStorage,
                context.getScreen().getTitle()
            );
            if (foundContainer == null) {
                return;
            }
            InventoryWindow.setIdentifier(screen, MemoryLocation.inWorld(
                dimensionKey,
                Shortcuts.getMemoryLocation(foundContainer)
            ));
        }

        @Override
        public void onScreenClose(
            final @NotNull ScreenCloseContext context
        ) {
            final MemoryBankImpl serverStorage = Shortcuts.getChestTrackerStorage();
            if (serverStorage == null) {
                return;
            }
            final MemoryLocation inventoryIdentifier = InventoryWindow.getIdentifier(context.getScreen());
            if (inventoryIdentifier == null) {
                return;
            }
            final boolean alreadyExisted = serverStorage.getMemory(inventoryIdentifier).isPresent();
            final var memory = new Memory(
                context.getItems(),
                context.getTitle(),
                List.of(),
                Optional.empty(),
                Memory.UNKNOWN_LOADED_TIMESTAMP,
                Memory.UNKNOWN_WORLD_TIMESTAMP,
                Memory.UNKNOWN_REAL_TIMESTAMP
            );
            Shortcuts.setMemoryLocation(memory, inventoryIdentifier.position());
            serverStorage.addMemory(
                inventoryIdentifier.memoryKey(),
                inventoryIdentifier.position(),
                memory
            );
            Shortcuts.printMessage(
                Component
                    .literal(alreadyExisted
                        ? "Updated inventory '" + context.getTitle().getString() + "'"
                        : "Now tracking inventory '" + context.getTitle().getString() + "'"
                    )
                    .withStyle(ChatFormatting.GREEN)
            );
        }
    }
}

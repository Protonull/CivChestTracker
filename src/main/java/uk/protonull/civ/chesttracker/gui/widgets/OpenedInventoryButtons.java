package uk.protonull.civ.chesttracker.gui.widgets;

import com.google.common.util.concurrent.Runnables;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import red.jackf.chesttracker.api.providers.MemoryLocation;
import red.jackf.chesttracker.impl.gui.invbutton.ui.SecondaryButton;
import red.jackf.chesttracker.impl.gui.screen.MemoryBankManagerScreen;
import red.jackf.chesttracker.impl.memory.MemoryBankImpl;
import red.jackf.chesttracker.impl.util.GuiUtil;
import uk.protonull.civ.chesttracker.CivChestTrackerPlugin;
import uk.protonull.civ.chesttracker.gui.screens.ContainerLocationDeciderScreen;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

public final class OpenedInventoryButtons {
    public static final SecondaryButton BLACKLISTED = new SecondaryButton(
        new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(CivChestTrackerPlugin.ID, "cannot"),
            ResourceLocation.fromNamespaceAndPath(CivChestTrackerPlugin.ID, "cannot_highlighted")
        ),
        Component.translatable("civchesttracker.inventoryButton.blacklisted"),
        Runnables.doNothing()
    );

    public static final class NoMemoryBank extends SecondaryButton {
        private static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(CivChestTrackerPlugin.ID, "unknown"),
            ResourceLocation.fromNamespaceAndPath(CivChestTrackerPlugin.ID, "unknown_highlighted")
        );
        public NoMemoryBank(
            final @NotNull AbstractContainerScreen<?> parent
        ) {
            super(
                SPRITES,
                Component.translatable("civchesttracker.inventoryButton.nomemorybank"),
                () -> Minecraft.getInstance().setScreen(new MemoryBankManagerScreen(parent, Runnables.doNothing()))
            );
        }
    }

    public static final class ForgetContainer extends SecondaryButton {
        private static final WidgetSprites SPRITES = GuiUtil.twoSprite("inventory_button/forget");
        public ForgetContainer(
            final @NotNull AbstractContainerScreen<?> parent,
            final @NotNull MemoryBankImpl serverStorage,
            final @NotNull MemoryLocation target
        ) {
            super(
                SPRITES,
                Component.translatable("civchesttracker.inventoryButton.forgetcontainer"),
                () -> {
                    serverStorage.removeMemory(target.memoryKey(), target.position());
                    Shortcuts.forciblyRefreshScreen(parent);
                }
            );
        }
    }

    public static final class TrackContainer extends SecondaryButton {
        private static final WidgetSprites SPRITES = GuiUtil.twoSprite("inventory_button/remember_container/never");
        public TrackContainer(
            final @NotNull AbstractContainerScreen<?> parent,
            final @NotNull Consumer<@NotNull BlockPos> setInventoryLocation
        ) {
            super(
                SPRITES,
                Component.translatable("civchesttracker.inventoryButton.trackcontainer"),
                () -> Minecraft.getInstance().setScreen(new ContainerLocationDeciderScreen(
                    parent,
                    setInventoryLocation
                ))
            );
        }
    }
}

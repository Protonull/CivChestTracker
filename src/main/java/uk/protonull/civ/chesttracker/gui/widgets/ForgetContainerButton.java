package uk.protonull.civ.chesttracker.gui.widgets;

import java.util.Objects;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import red.jackf.chesttracker.impl.gui.invbutton.ui.SecondaryButton;
import red.jackf.chesttracker.impl.util.GuiUtil;

public class ForgetContainerButton extends SecondaryButton {
    private static final WidgetSprites SPRITES = GuiUtil.twoSprite("inventory_button/forget");

    public ForgetContainerButton(
        final @NotNull Runnable onClick
    ) {
        super(
            SPRITES,
            Component.translatable("civchesttracker.inventoryButton.remove"),
            Objects.requireNonNull(onClick)
        );
    }
}

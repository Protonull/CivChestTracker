package uk.protonull.civ.chesttracker.gui.screens;

import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.protonull.civ.chesttracker.mixing.InventoryWindow;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

public class ContainerLocationDeciderScreen extends Screen {
    private static final int PICKER_COLOUR_RBG = 0xFF_FD_12;
    private static final float PICKER_COLOUR_R = Mth.map((float) (PICKER_COLOUR_RBG >> 16 & 0xFF), 0f, 255f, 0f, 1f);
    private static final float PICKER_COLOUR_G = Mth.map((float) (PICKER_COLOUR_RBG >> 8 & 0xFF), 0f, 255f, 0f, 1f);
    private static final float PICKER_COLOUR_B = Mth.map((float) (PICKER_COLOUR_RBG & 0xFF), 0f, 255f, 0f, 1f);

    private static volatile @Nullable BlockPos currentPickerLocation = null;
    @ApiStatus.Internal
    public static volatile boolean invertedScrollDirection = false; // TODO: Put this on a config somewhere

    private static final int MIN_DISTANCE = 1;
    private static final int MAX_DISTANCE = 5;

    private final AbstractContainerScreen<?> parent;
    private final Consumer<BlockPos> setInventoryLocation;
    private final Vec3 playerEyePosition;
    private final Vec3 playerLookDirection;

    private int distance = 1;

    public ContainerLocationDeciderScreen(
        final @NotNull AbstractContainerScreen<?> parent,
        final @NotNull Consumer<@NotNull BlockPos> setInventoryLocation
    ) {
        super(Component.translatable("civchesttracker.picker.header"));
        this.parent = Objects.requireNonNull(parent);
        this.setInventoryLocation = Objects.requireNonNull(setInventoryLocation);
        {
            final LocalPlayer player = InventoryWindow.getPlayer(parent);
            this.playerEyePosition = player.getEyePosition();
            this.playerLookDirection = player.getLookAngle().normalize();
        }
        calculateNewDeciderLocation();
    }

    @Override
    protected void init() {
        addRenderableWidget(new Button(
            (this.width / 2) - (Button.DEFAULT_WIDTH / 2),
            this.height - 40 - Button.DEFAULT_HEIGHT,
            Button.DEFAULT_WIDTH,
            Button.DEFAULT_HEIGHT,
            Component.translatable("civchesttracker.picker.confirm"),
            (button) -> {
                final BlockPos blockPos = currentPickerLocation;
                if (blockPos != null) {
                    this.setInventoryLocation.accept(blockPos);
                }
                onClose();
            },
            Button.DEFAULT_NARRATION
        ));
    }

    private void calculateNewDeciderLocation() {
        currentPickerLocation = Shortcuts.asBlockPos(this.playerEyePosition.add(
            Shortcuts.multiply(this.playerLookDirection, this.distance)
        ));
    }

    @Override
    public boolean mouseScrolled(
        final double mouseX,
        final double mouseY,
        final double scrollX,
        final double scrollY
    ) {
        if ((!invertedScrollDirection && scrollY > 0) || (invertedScrollDirection && scrollY < 0)) { // Scrolled down
            this.distance = Math.min(this.distance + 1, MAX_DISTANCE);
        }
        else if ((!invertedScrollDirection && scrollY < 0) || (invertedScrollDirection && scrollY > 0)) { // Scrolled up
            this.distance = Math.max(this.distance - 1, MIN_DISTANCE);
        }
        calculateNewDeciderLocation();
        return true;
    }

    @Override
    public void renderBackground(
        final @NotNull GuiGraphics guiGraphics,
        final int mouseX,
        final int mouseY,
        final float partialTick
    ) {
        // Prevent the blurry background from being rendered as the player needs to be able to see the world.
    }

    @Override
    public void render(
        final @NotNull GuiGraphics guiGraphics,
        final int mouseX,
        final int mouseY,
        final float partialTick
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(
            this.font,
            Component.translatable("civchesttracker.picker.header"),
            this.width / 2,
            20,
            PICKER_COLOUR_RBG
        );
    }

    @Override
    public void onClose() {
        currentPickerLocation = null;
        this.minecraft.setScreen(this.parent);
    }

    @ApiStatus.Internal
    public static void setupPickerRendering() {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register((context) -> {
            final BlockPos blockPos = currentPickerLocation;
            if (blockPos == null) {
                return;
            }
            DebugRenderer.renderFilledBox(
                context.matrixStack(),
                context.consumers(),
                blockPos,
                -0.2f, // This is EXTRA scale, not total scale
                PICKER_COLOUR_R,
                PICKER_COLOUR_G,
                PICKER_COLOUR_B,
                0.4f
            );
            DebugRenderer.renderFilledBox(
                context.matrixStack(),
                context.consumers(),
                blockPos,
                0.02f, // This is EXTRA scale, not total scale
                PICKER_COLOUR_R,
                PICKER_COLOUR_G,
                PICKER_COLOUR_B,
                0.4f
            );
        });
    }
}

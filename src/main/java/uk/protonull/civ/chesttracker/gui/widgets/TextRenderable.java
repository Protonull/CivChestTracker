package uk.protonull.civ.chesttracker.gui.widgets;

import java.util.Objects;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public sealed abstract class TextRenderable implements Renderable {
    public @NotNull Font font;
    public int x;
    public int y;
    public @NotNull Component text;
    public int colour;

    protected TextRenderable(
        final @NotNull Font font,
        final int x,
        final int y,
        final @NotNull Component text
    ) {
        this(font, x, y, text, 0xFF_FF_FF);
    }

    protected TextRenderable(
        final @NotNull Font font,
        final int x,
        final int y,
        final @NotNull Component text,
        final int colour
    ) {
        this.font = Objects.requireNonNull(font);
        this.x = x;
        this.y = y;
        this.text = Objects.requireNonNull(text);
        this.colour = colour;
    }

    public static final class LeftAligned extends TextRenderable {
        public LeftAligned(
            final @NotNull Font font,
            final int x,
            final int y,
            final @NotNull Component text
        ) {
            super(font, x, y, text);
        }
        public LeftAligned(
            final @NotNull Font font,
            final int x,
            final int y,
            final @NotNull Component text,
            final int colour
        ) {
            super(font, x, y, text, colour);
        }
        @Override
        public void render(
            final @NotNull GuiGraphics guiGraphics,
            final int mouseX,
            final int mouseY,
            final float partialTick
        ) {
            guiGraphics.drawString(this.font, this.text, this.x, this.y, this.colour);
        }
    }

    public static final class CentreAligned extends TextRenderable {
        public CentreAligned(
            final @NotNull Font font,
            final int x,
            final int y,
            final @NotNull Component text
        ) {
            super(font, x, y, text);
        }
        public CentreAligned(
            final @NotNull Font font,
            final int x,
            final int y,
            final @NotNull Component text,
            final int colour
        ) {
            super(font, x, y, text, colour);
        }
        @Override
        public void render(
            final @NotNull GuiGraphics guiGraphics,
            final int mouseX,
            final int mouseY,
            final float partialTick
        ) {
            guiGraphics.drawCenteredString(this.font, this.text, this.x, this.y, this.colour);
        }
    }

    public static final class RightAligned extends TextRenderable {
        public RightAligned(
            final @NotNull Font font,
            final int x,
            final int y,
            final @NotNull Component text
        ) {
            super(font, x, y, text);
        }
        public RightAligned(
            final @NotNull Font font,
            final int x,
            final int y,
            final @NotNull Component text,
            final int colour
        ) {
            super(font, x, y, text, colour);
        }
        @Override
        public void render(
            final @NotNull GuiGraphics guiGraphics,
            final int mouseX,
            final int mouseY,
            final float partialTick
        ) {
            guiGraphics.drawString(this.font, this.text, this.x - this.font.width(this.text), this.y, this.colour);
        }
    }
}

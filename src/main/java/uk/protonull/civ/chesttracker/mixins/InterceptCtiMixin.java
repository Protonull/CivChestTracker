package uk.protonull.civ.chesttracker.mixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jackf.chesttracker.impl.providers.InteractionTrackerImpl;
import red.jackf.chesttracker.impl.util.CachedClientBlockSource;

@Mixin(ClientPacketListener.class)
public abstract class InterceptCtiMixin {
    @Unique
    private static final Pattern LOCATION_PATTERN = Pattern.compile("^Location: (-?\\d+) (-?\\d+) (-?\\d+)$");

    @Shadow
    private ClientLevel level;

    /**
     * This works because Citadel sends the reinforcement message before the inventory is opened.
     */
    @Inject(
        method = "handleSystemChat",
        at = @At("TAIL")
    )
    protected void civchesttracker$handleCtiMessage(
        final @NotNull ClientboundSystemChatPacket packet,
        final @NotNull CallbackInfo ci
    ) {
        if (this.level == null) {
            return;
        }
        final String plainMessage = getComponentPlainText(packet.content());
        if (!plainMessage.equals("Not reinforced") && !plainMessage.startsWith("Reinforced at ")) {
            return;
        }
        BlockPos blockPos = null;
        for (final String line : extractHoverTexts(packet.content())) {
            final Matcher matcher = LOCATION_PATTERN.matcher(line);
            if (matcher.matches()) {
                blockPos = new BlockPos(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
                );
                break;
            }
        }
        if (blockPos == null) {
            return;
        }
        InteractionTrackerImpl.INSTANCE.setLastBlockSource(new CachedClientBlockSource(
            this.level,
            blockPos
        ));
    }

    @Unique
    private static @NotNull Collection<@NotNull String> extractHoverTexts(
        final @NotNull Component message
    ) {
        final var hovers = new ArrayList<String>();
        for (final Component part : message.toFlatList()) {
            final Style style = part.getStyle();
            if (style == null) {
                continue; // Just in case
            }
            final HoverEvent hover = style.getHoverEvent();
            if (hover == null) {
                continue;
            }
            final Component hoverText = hover.getValue(HoverEvent.Action.SHOW_TEXT);
            if (hoverText == null || Component.EMPTY.equals(hoverText)) {
                continue;
            }
            hovers.addAll(
                List.of(getComponentPlainText(hoverText).split("\n"))
            );
        }
        return hovers;
    }

    @Unique
    private static @NotNull String getComponentPlainText(
        final @NotNull Component component
    ) {
        return component.getString().replaceAll("§.", "").trim();
    }
}

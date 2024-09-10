package uk.protonull.civ.chesttracker.handlers;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import red.jackf.chesttracker.api.providers.MemoryLocation;
import red.jackf.chesttracker.api.providers.context.ScreenOpenContext;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

public final class HandleOpen {
    public static void handleScreenOpen(
        final @NotNull ScreenOpenContext context
    ) {
        final ResourceLocation inventoryLocation; {
            final Component screenTitle = context.getScreen().getTitle();

            final String screenTitleTranslationKey = extractTranslationKey(screenTitle);
            if (StringUtils.isNotEmpty(screenTitleTranslationKey) && I18n.exists(screenTitleTranslationKey)) {
                System.out.println("inventory has default title: " + screenTitleTranslationKey);
                // If the screen title is a translation key, it's *probably* a default title, which means the container
                // is unnamed, meaning there's nothing to identify the container.
                return;
            }

            final String title = Shortcuts.toPlainString(screenTitle);
            System.out.println("inventory has title: " + title);
            if (StringUtils.isBlank(title)) {
                inventoryLocation = Shortcuts.resource("blank");
            }
            else {
                // TODO: Check whether it's a common GUI title!

                inventoryLocation = Shortcuts.resource(
                    HexFormat.of()
                        .formatHex(Shortcuts.sha1().digest(title.getBytes(StandardCharsets.UTF_8)))
                        .toLowerCase(Locale.ENGLISH)
                );
            }
        }
        System.out.println("inventory location: " + inventoryLocation);
        context.setMemoryLocation(MemoryLocation.override(inventoryLocation, BlockPos.ZERO));
    }

    private static @Nullable String extractTranslationKey(
        final @NotNull Component component
    ) {
        final Iterator<Component> iter = component.toFlatList().iterator();
        if (!iter.hasNext()) {
            return null;
        }
        final Component part = iter.next();
        if (iter.hasNext()) {
            return null;
        }
        if (part.getContents() instanceof final TranslatableContents contents) {
            return contents.getKey();
        }
        return null;
    }
}

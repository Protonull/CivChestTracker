package uk.protonull.civ.chesttracker.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Shortcuts {
    public static @NotNull ResourceLocation resource(
        final @NotNull String value
    ) {
        return ResourceLocation.fromNamespaceAndPath("civchesttracker", value);
    }

    @Contract("!null -> !null")
    public static @Nullable String toPlainString(
        final Component title
    ) {
        if (title == null) {
            return null;
        }
        final var content = new StringBuilder();
        for (final Component child : title.toFlatList()) {
            content.append(child.getString());
        }
        return content.toString();
    }

    public static @NotNull MessageDigest sha1() {
        try {
            return MessageDigest.getInstance("SHA-1");
        }
        catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("This shouldn't be possible... JVMs are required to have SHA-1!");
        }
    }
}

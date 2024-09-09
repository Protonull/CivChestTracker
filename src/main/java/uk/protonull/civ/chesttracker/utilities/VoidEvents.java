package uk.protonull.civ.chesttracker.utilities;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.jetbrains.annotations.NotNull;
import red.jackf.chesttracker.impl.events.AfterPlayerDestroyBlock;
import red.jackf.chesttracker.impl.events.AfterPlayerPlaceBlock;

@SuppressWarnings({"UnstableApiUsage", "NonExtendableApiUsage"})
public final class VoidEvents {
    public static final Event<UseBlockCallback> USE_BLOCK_CALLBACK = new VoidEvent<>();
    public static final Event<AfterPlayerPlaceBlock> AFTER_PLAYER_PLACE_BLOCK = new VoidEvent<>();
    public static final Event<AfterPlayerDestroyBlock> AFTER_PLAYER_DESTROY_BLOCK = new VoidEvent<>();

    private static final class VoidEvent<T> extends Event<T> {
        @Override
        public void register(
            final @NotNull T listener
        ) {
            // Do nothing
        }
    }
}

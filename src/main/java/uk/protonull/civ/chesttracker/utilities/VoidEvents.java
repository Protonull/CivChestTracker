package uk.protonull.civ.chesttracker.utilities;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import red.jackf.chesttracker.impl.events.AfterPlayerDestroyBlock;
import red.jackf.chesttracker.impl.events.AfterPlayerPlaceBlock;

@SuppressWarnings({"UnstableApiUsage", "NonExtendableApiUsage"})
public final class VoidEvents {
    public static final Event<UseBlockCallback> USE_BLOCK_CALLBACK = new Event<>() {
        @Override
        public void register(final UseBlockCallback listener) {
            // Do nothing
        }
    };

    public static final Event<AfterPlayerPlaceBlock> AFTER_PLAYER_PLACE_BLOCK = new Event<>() {
        @Override
        public void register(final AfterPlayerPlaceBlock listener) {
            // Do nothing
        }
    };

    public static final Event<AfterPlayerDestroyBlock> AFTER_PLAYER_DESTROY_BLOCK = new Event<>() {
        @Override
        public void register(final AfterPlayerDestroyBlock listener) {
            // Do nothing
        }
    };
}

package uk.protonull.civ.chesttracker.utilities;

import java.util.Objects;
import net.fabricmc.fabric.api.event.Event;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("NonExtendableApiUsage")
public final class VoidEvent<T> extends Event<T> {
    public VoidEvent() {

    }

    public VoidEvent(
        final @NotNull T voidHandler
    ) {
        this.invoker = Objects.requireNonNull(voidHandler);
    }

    @Override
    public void register(
        final @NotNull T listener
    ) {
        // Do nothing
    }
}

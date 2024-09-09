package uk.protonull.civ.chesttracker.mixins.illegal;

import java.util.Optional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import red.jackf.chesttracker.api.ClientBlockSource;
import red.jackf.chesttracker.impl.providers.InteractionTrackerImpl;

@SuppressWarnings("UnstableApiUsage")
@Mixin(InteractionTrackerImpl.class)
public abstract class AlwaysReturnNoBlockSourceMixin {
    /**
     * @author Protonull
     * @reason Always return an empty optional.
     */
    @Overwrite(remap = false)
    public Optional<ClientBlockSource> getLastBlockSource() {
        return Optional.empty();
    }
}

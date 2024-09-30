package uk.protonull.civ.chesttracker.mixins.illegal;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import red.jackf.chesttracker.api.ClientBlockSource;
import red.jackf.chesttracker.impl.providers.InteractionTrackerImpl;

@Mixin(InteractionTrackerImpl.class)
public abstract class NukeInteractionTrackerImplMixin {
    /**
     * @author Protonull
     * @reason Prevent anything from being setup
     */
    @Overwrite(remap = false)
    public static void setup() {
        // DO NOTHING
    }

    /**
     * @author Protonull
     * @reason This doesn't really need an overwrite, but I'm petty.
     */
    @Overwrite(remap = false)
    public Optional<ClientLevel> getPlayerLevel() {
        return Optional.ofNullable(Minecraft.getInstance().level);
    }

    /**
     * @author Protonull
     * @reason Always return no last-interacted block.
     */
    @SuppressWarnings("UnstableApiUsage")
    @Overwrite(remap = false)
    public Optional<ClientBlockSource> getLastBlockSource() {
        return Optional.empty();
    }

    /**
     * @author Protonull
     * @reason Never store a last-interacted block.
     */
    @SuppressWarnings("UnstableApiUsage")
    @Overwrite(remap = false)
    public void setLastBlockSource(
        final ClientBlockSource source
    ) {
        // DO NOTHING
    }
}

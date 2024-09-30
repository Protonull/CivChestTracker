package uk.protonull.civ.chesttracker.mixins.illegal;

import net.fabricmc.fabric.api.event.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.jackf.chesttracker.impl.events.AfterPlayerDestroyBlock;
import uk.protonull.civ.chesttracker.utilities.VoidEvent;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AfterPlayerDestroyBlock.class)
public interface NukeAfterPlayerDestroyBlockMixin {
    @Shadow(remap = false)
    Event<AfterPlayerDestroyBlock> EVENT = new VoidEvent<>(
        (cbs) -> {}
    );
}

package uk.protonull.civ.chesttracker.mixins.illegal;

import net.fabricmc.fabric.api.event.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.jackf.chesttracker.impl.events.AfterPlayerPlaceBlock;
import uk.protonull.civ.chesttracker.utilities.VoidEvent;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AfterPlayerPlaceBlock.class)
public interface NukeAfterPlayerPlaceBlockMixin {
    @Shadow(remap = false)
    Event<AfterPlayerPlaceBlock> EVENT = new VoidEvent<>(
        (level, pos, state, placed) -> {}
    );
}

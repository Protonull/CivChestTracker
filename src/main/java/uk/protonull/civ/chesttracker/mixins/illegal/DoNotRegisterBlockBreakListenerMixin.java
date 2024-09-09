package uk.protonull.civ.chesttracker.mixins.illegal;

import net.fabricmc.fabric.api.event.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.impl.events.AfterPlayerDestroyBlock;
import red.jackf.chesttracker.impl.memory.MemoryIntegrity;
import uk.protonull.civ.chesttracker.utilities.VoidEvents;

@SuppressWarnings({"UnstableApiUsage"})
@Mixin(MemoryIntegrity.class)
public abstract class DoNotRegisterBlockBreakListenerMixin {
    @Redirect(
        method = "setup",
        at = @At(
            value = "FIELD",
            target = "Lred/jackf/chesttracker/impl/events/AfterPlayerDestroyBlock;EVENT:Lnet/fabricmc/fabric/api/event/Event;"
        ),
        remap = false
    )
    private static Event<AfterPlayerDestroyBlock> civchesttracker$preventEventRegistration() {
        return VoidEvents.AFTER_PLAYER_DESTROY_BLOCK;
    }
}

package uk.protonull.civ.chesttracker.mixins.illegal;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.impl.providers.InteractionTrackerImpl;
import uk.protonull.civ.chesttracker.utilities.VoidEvents;

@Mixin(InteractionTrackerImpl.class)
public abstract class DoNotRegisterUseBlockListenerMixin {
    @Redirect(
        method = "setup",
        at = @At(
            value = "FIELD",
            target = "Lnet/fabricmc/fabric/api/event/player/UseBlockCallback;EVENT:Lnet/fabricmc/fabric/api/event/Event;"
        ),
        remap = false
    )
    private static Event<UseBlockCallback> civchesttracker$preventEventRegistration() {
        return VoidEvents.USE_BLOCK_CALLBACK;
    }
}

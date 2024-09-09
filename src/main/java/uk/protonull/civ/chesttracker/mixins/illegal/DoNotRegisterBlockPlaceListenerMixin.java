package uk.protonull.civ.chesttracker.mixins.illegal;

import net.fabricmc.fabric.api.event.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.impl.events.AfterPlayerPlaceBlock;
import red.jackf.chesttracker.impl.providers.ProviderHandler;
import uk.protonull.civ.chesttracker.utilities.VoidEvents;

@SuppressWarnings({"UnstableApiUsage"})
@Mixin(ProviderHandler.class)
public abstract class DoNotRegisterBlockPlaceListenerMixin {
    @Redirect(
        method = "setupEvents",
        at = @At(
            value = "FIELD",
            target = "Lred/jackf/chesttracker/impl/events/AfterPlayerPlaceBlock;EVENT:Lnet/fabricmc/fabric/api/event/Event;"
        ),
        remap = false
    )
    protected Event<AfterPlayerPlaceBlock> civchesttracker$preventEventRegistration() {
        return VoidEvents.AFTER_PLAYER_PLACE_BLOCK;
    }
}

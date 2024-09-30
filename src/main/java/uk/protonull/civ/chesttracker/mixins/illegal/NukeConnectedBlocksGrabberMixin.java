package uk.protonull.civ.chesttracker.mixins.illegal;

import java.util.List;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import red.jackf.whereisit.api.search.ConnectedBlocksGrabber;
import uk.protonull.civ.chesttracker.utilities.VoidEvent;

@Mixin(ConnectedBlocksGrabber.class)
public interface NukeConnectedBlocksGrabberMixin {
    @Shadow(remap = false)
    Event<ConnectedBlocksGrabber> EVENT = new VoidEvent<>(
        (positions, pos, level, state) -> {}
    );

    /**
     * @author Protonull
     * @reason Always only return the provided position.
     */
    @Overwrite
    static List<BlockPos> getConnected(
        final @NotNull Level level,
        final @NotNull BlockState state,
        final @NotNull BlockPos pos
    ) {
        return List.of(pos.immutable());
    }
}

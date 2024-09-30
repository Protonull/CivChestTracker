package uk.protonull.civ.chesttracker;

import org.jetbrains.annotations.ApiStatus;
import uk.protonull.civ.chesttracker.gui.screens.ContainerLocationDeciderScreen;

public final class CivChestTracker {
    @ApiStatus.Internal
    public static void bootstrap() {
        ContainerLocationDeciderScreen.setupPickerRendering();
    }
}

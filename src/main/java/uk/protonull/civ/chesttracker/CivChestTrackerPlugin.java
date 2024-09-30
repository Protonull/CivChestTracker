package uk.protonull.civ.chesttracker;

import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import red.jackf.chesttracker.api.ChestTrackerPlugin;
import red.jackf.chesttracker.api.gui.ScreenBlacklist;
import red.jackf.chesttracker.api.providers.ProviderUtils;
import red.jackf.chesttracker.api.providers.ServerProvider;
import red.jackf.chesttracker.api.providers.context.ScreenCloseContext;
import red.jackf.chesttracker.api.providers.context.ScreenOpenContext;
import red.jackf.jackfredlib.client.api.gps.Coordinate;
import uk.protonull.civ.chesttracker.gui.screens.ContainerLocationDeciderScreen;

public final class CivChestTrackerPlugin implements ChestTrackerPlugin {
    /**
     * Pretty heavily based on {@link red.jackf.chesttracker.impl.DefaultChestTrackerPlugin}
     */
    @Override
    public void load() {
        ScreenBlacklist.add(
            // workstations with no item retention
            CartographyTableScreen.class,
            EnchantmentScreen.class,
            GrindstoneScreen.class,
            ItemCombinerScreen.class,
            LoomScreen.class,
            StonecutterScreen.class,
            BeaconScreen.class,
            // inventory (surv & creative)
            EffectRenderingInventoryScreen.class
        );

        ProviderUtils.registerProvider(new Provider());

        ContainerLocationDeciderScreen.setupPickerRendering();
    }

    private static final class Provider extends ServerProvider {
        @Override
        public ResourceLocation id() {
            return ResourceLocation.fromNamespaceAndPath("civchesttracker", "provider");
        }

        @Override
        public boolean appliesTo(
            final @NotNull Coordinate coordinate
        ) {
            return true; // Apply to singleplayer, multiplayer, etc
        }

        @Override
        public void onScreenOpen(
            final @NotNull ScreenOpenContext context
        ) {

        }

        @Override
        public void onScreenClose(
            final ScreenCloseContext context
        ) {

        }
    }
}

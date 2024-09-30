package uk.protonull.civ.chesttracker.mixins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import red.jackf.chesttracker.api.memory.Memory;
import red.jackf.chesttracker.impl.util.ModCodecs;
import uk.protonull.civ.chesttracker.utilities.Shortcuts;

@Mixin(Memory.class)
public abstract class MemoryMixin {
    @Shadow(remap = false)
    public static final Codec<Memory> CODEC = RecordCodecBuilder.create((instance) -> instance
        .group(
            ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items").forGetter(Memory::fullItems),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(Memory::savedName),
            ModCodecs.BLOCK_POS_STRING.fieldOf("location").forGetter(Shortcuts::getMemoryLocation),
            Codec.LONG.optionalFieldOf("loadedTimestamp", Memory.UNKNOWN_LOADED_TIMESTAMP).forGetter(Memory::loadedTimestamp),
            Codec.LONG.optionalFieldOf("worldTimestamp", Memory.UNKNOWN_WORLD_TIMESTAMP).forGetter(Memory::inGameTimestamp),
            ExtraCodecs.INSTANT_ISO8601.optionalFieldOf("realTimestamp", Memory.UNKNOWN_REAL_TIMESTAMP).forGetter(Memory::realTimestamp)
        )
        .apply(
            instance,
            (items, name, location, loadedTimestamp, worldTimestamp, realTimestamp) -> {
                final var memory = new Memory(
                    items,
                    name,
                    List.of(),
                    Optional.empty(),
                    loadedTimestamp,
                    worldTimestamp,
                    realTimestamp
                );
                Shortcuts.setMemoryLocation(memory, location);
                return memory;
            }
        )
    );

    @Mutable
    @Final
    @Shadow
    private Component name;

    @Mutable
    @Final
    @Shadow(remap = false)
    private List<BlockPos> otherPositions;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Mutable
    @Final
    @Shadow(remap = false)
    private Optional<Block> container;

    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lred/jackf/chesttracker/api/memory/Memory;name:Lnet/minecraft/network/chat/Component;",
            opcode = Opcodes.PUTFIELD
        ),
        remap = false
    )
    protected void civchesttracker$ensureNotNullName(
        final Memory instance,
        final Component value
    ) {
        this.name = Objects.requireNonNull(value);
    }

    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lred/jackf/chesttracker/api/memory/Memory;otherPositions:Ljava/util/List;",
            opcode = Opcodes.PUTFIELD
        ),
        remap = false
    )
    protected void civchesttracker$neverSetOtherPositions(
        final @NotNull Memory instance,
        final @NotNull List<BlockPos> value
    ) {
        this.otherPositions = List.of();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Redirect(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lred/jackf/chesttracker/api/memory/Memory;container:Ljava/util/Optional;",
            opcode = Opcodes.PUTFIELD
        ),
        remap = false
    )
    protected void civchesttracker$neverSetContainer(
        final @NotNull Memory instance,
        final @NotNull Optional<Block> value
    ) {
        this.container = Optional.empty();
    }

    @Redirect(
        method = "populate",
        at = @At(
            value = "FIELD",
            target = "Lred/jackf/chesttracker/api/memory/Memory;position:Lnet/minecraft/core/BlockPos;",
            opcode = Opcodes.PUTFIELD
        )
    )
    protected void civchesttracker$preventAutomaticLocationUpdates(
        final @NotNull Memory instance,
        final @NotNull BlockPos value
    ) {
        // Do nothing.

        // This is to prevent ChestTracker from automatically updating the container's location, if it manages to do so.
        // Instead, the location should ONLY happen through our MemoryAccessor mixin.
    }
}

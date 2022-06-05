package tt432.thelixir.capability.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.capability.AbstractCapabilityProvider;
import tt432.thelixir.capability.Registry;

/**
 * @author DustW
 **/
public class TheElixirPlayerCapabilityProvider extends AbstractCapabilityProvider<TheElixirPlayerCapabilityProvider> {

    final LazyOptional<TheElixirPlayerCapability> holder;

    public TheElixirPlayerCapabilityProvider(Player player) {
        super(TheElixirPlayerCapabilityProvider.class);
        holder = LazyOptional.of(() -> new TheElixirPlayerCapability(player));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return Registry.CAPABILITY.orEmpty(cap, holder);
    }

    @Override
    public CompoundTag serializeNBT() {
        return holder.resolve().get().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        holder.resolve().get().deserializeNBT(nbt);
    }
}

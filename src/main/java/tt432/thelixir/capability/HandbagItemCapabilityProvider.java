package tt432.thelixir.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author DustW
 **/
public class HandbagItemCapabilityProvider extends AbstractCapabilityProvider<HandbagItemCapabilityProvider> {
    private static final String ITEMS_KEY = "items";

    protected LazyOptional<ItemStackHandler> handler;

    public HandbagItemCapabilityProvider() {
        super(HandbagItemCapabilityProvider.class);

        handler = LazyOptional.of(() -> new ItemStackHandler(6));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        var result = new CompoundTag();
        result.put(ITEMS_KEY, handler.resolve().get().serializeNBT());
        return result;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        handler.resolve().get().deserializeNBT(nbt.getCompound(ITEMS_KEY));
    }
}

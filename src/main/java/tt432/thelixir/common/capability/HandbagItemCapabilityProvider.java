package tt432.thelixir.common.capability;

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
    protected int row;
    protected int column;

    public HandbagItemCapabilityProvider(int row, int column) {
        super(HandbagItemCapabilityProvider.class);

        handler = LazyOptional.of(() -> new ItemStackHandler(row * column));
        this.row = row;
        this.column = column;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, handler.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        var result = new CompoundTag();
        result.putInt("row", row);
        result.putInt("col", column);
        result.put(ITEMS_KEY, handler.resolve().get().serializeNBT());
        return result;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ItemStackHandler handler = this.handler.resolve().get();
        handler.deserializeNBT(nbt.getCompound(ITEMS_KEY));

        if (nbt.contains("row") && nbt.contains("col")) {
            row = nbt.getInt("row");
            column = nbt.getInt("col");
        }
        else {
            row = handler.getSlots();
            column = 1;
        }
    }
}

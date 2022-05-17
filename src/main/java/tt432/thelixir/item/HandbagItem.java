package tt432.thelixir.item;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.capability.HandbagItemCapabilityProvider;
import tt432.thelixir.item.expand.ThelixirItem;

import java.util.Objects;

/**
 * @author DustW
 **/
public class HandbagItem extends Item implements ThelixirItem {
    private static final String ITEMS_KEY = "items";

    public HandbagItem(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new HandbagItemCapabilityProvider();
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        var result = Objects.requireNonNullElse(super.getShareTag(stack), new CompoundTag());
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler ->
                result.put(ITEMS_KEY, ((ItemStackHandler) handler).serializeNBT())
        );
        return result;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);

        if (nbt != null && nbt.contains(ITEMS_KEY)) {
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                ((ItemStackHandler) handler).deserializeNBT(nbt.getCompound(ITEMS_KEY));
            });
        }
    }

    @NotNull
    @Override
    public ClickResult onClickInContainer(Slot menuSlot, int menuSlotIndex, ItemStack carried, int mouseButton, ClickType clickType, AbstractContainerMenu menu, Player player) {
        if (clickType != ClickType.PICKUP || menuSlotIndex == -1 || menuSlotIndex == -999) {
            return ClickResult.FAIL;
        }

        var capOptional = menuSlot.getItem()
                .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve();

        if (capOptional.isPresent()) {
            var handler = capOptional.get();

            boolean changed = false;

            if (carried.getItem() instanceof HandbagItem) {
                return ClickResult.FAIL;
            }

            if (!carried.isEmpty() && mouseButton == InputConstants.MOUSE_BUTTON_LEFT) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    if ((carried = handler.insertItem(i, carried, false)).isEmpty()) {
                        changed = true;
                        break;
                    }
                }
            } else if (mouseButton == InputConstants.MOUSE_BUTTON_RIGHT) {
                for (int i = handler.getSlots() - 1; i >= 0; i--) {
                    if (!(carried = handler.extractItem(i, 64, false)).isEmpty()) {
                        changed = true;
                        break;
                    }
                }
            }

            return new ClickResult(carried, changed);
        }

        return ClickResult.FAIL;
    }
}

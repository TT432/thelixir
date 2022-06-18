package tt432.thelixir.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.capability.HandbagItemCapabilityProvider;

import java.util.Objects;

/**
 * @author DustW
 **/
public class HandbagItem extends Item {
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

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {


        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (!pSlot.allowModification(pPlayer)) {
            return false;
        }

        var capOptional = pStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve();

        if (capOptional.isPresent()) {
            var handler = capOptional.get();

            boolean changed = false;

            if (pOther.getItem() instanceof HandbagItem) {
                return false;
            }

            if (!pOther.isEmpty()) {
                if (pAction == ClickAction.PRIMARY) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        if ((pOther = handler.insertItem(i, pOther, false)).isEmpty()) {
                            pAccess.set(ItemStack.EMPTY);
                            changed = true;
                            break;
                        }
                    }
                }
            } else if (pAction == ClickAction.SECONDARY) {
                for (int i = handler.getSlots() - 1; i >= 0; i--) {
                    ItemStack itemStack = handler.extractItem(i, 64, false);

                    if (!itemStack.isEmpty()) {
                        pAccess.set(itemStack);
                        changed = true;
                        break;
                    }
                }
            }

            return changed;
        }

        return super.overrideOtherStackedOnMe(pStack, pOther, pSlot, pAction, pPlayer, pAccess);
    }
}

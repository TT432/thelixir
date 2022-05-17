package tt432.thelixir.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tt432.thelixir.item.expand.ThelixirItem;

/**
 * @author DustW
 **/
@Mixin(AbstractContainerMenu.class)
public abstract class MixinAbstractContainerMenu {
    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @Shadow
    public abstract ItemStack getCarried();

    @Shadow
    public abstract void setCarried(ItemStack pStack);

    @Inject(method = "doClick", cancellable = true, at = @At("HEAD"))
    private void tDoClick(int menuSlot, int mouseButton, ClickType clickType, Player player, CallbackInfo ci) {
        if (menuSlot == -1 || menuSlot == -999) {
            return;
        }

        var mSlot = slots.get(menuSlot);
        var stack = mSlot.getItem();

        if (stack.getItem() instanceof ThelixirItem ti) {
            var result = ti.onClickInContainer(mSlot, menuSlot, getCarried(),
                    mouseButton, clickType, (AbstractContainerMenu) (Object) this, player);

            if (result.success()) {
                ci.cancel();
                setCarried(result.item());
                mSlot.setChanged();
            }
        }
    }
}

package tt432.thelixir.item.expand;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * 对 item 做一点扩展
 * @author DustW
 **/
public interface ThelixirItem {
    /**
     * 在 Container 中点击 slot 时，若 slot 内的物品是该物品则触发
     * @param menuSlot        Slot 对象
     * @param menuSlotIndex   Slot 所在的 index
     * @param carried         鼠标拿着的物品堆的对象
     * @param mouseButton     鼠标的按键，0：左键，1：右键，2：中键
     * @param clickType       ClickType
     * @param player          打开这个 container 的玩家
     * @return                结果，若 success 为 true 则会将 container 的 carried 替换为 item
     * @see tt432.thelixir.mixin.MixinAbstractContainerMenu
     */
    @Nonnull
    ClickResult onClickInContainer(Slot menuSlot, int menuSlotIndex,
                                   ItemStack carried,
                                   int mouseButton, ClickType clickType,
                                   AbstractContainerMenu menu,
                                   Player player);

    record ClickResult(@Nonnull ItemStack item, boolean success) {
        public static final ClickResult FAIL = new ClickResult(ItemStack.EMPTY, false);
    }
}

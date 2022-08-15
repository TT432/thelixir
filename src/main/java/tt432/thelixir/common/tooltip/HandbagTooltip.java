package tt432.thelixir.common.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/**
 * @author DustW
 */
public record HandbagTooltip(NonNullList<ItemStack> items, int row, int col) implements TooltipComponent { }

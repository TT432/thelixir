package tt432.thelixir.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.capability.player.TheElixirPlayerCapability;
import tt432.thelixir.sound.ModSoundEvents;

import java.util.List;

/**
 * @author DustW
 **/
public class FoxTailItem extends Item {
    public FoxTailItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        TheElixirPlayerCapability.ifActiveElse(pPlayer, TheElixirPlayerCapability.FOX_TAIL, cap -> {
            cap.setActive(TheElixirPlayerCapability.FOX_TAIL, false);

            if (pLevel.isClientSide) {
                pLevel.playLocalSound(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                        ModSoundEvents.FOX_TAIL.get(), pPlayer.getSoundSource(),
                        1.0F, 1.0F, false);
            }
        }, cap -> cap.setActive(TheElixirPlayerCapability.FOX_TAIL, true));

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltips, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltips, pIsAdvanced);

        tooltips.add(new TranslatableComponent("tooltip.fox_tail.1"));
    }
}

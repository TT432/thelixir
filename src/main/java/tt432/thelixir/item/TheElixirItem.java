package tt432.thelixir.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import tt432.thelixir.capability.Registry;
import tt432.thelixir.capability.player.TheElixirPlayerCapability;

/**
 * @author DustW
 **/
public class TheElixirItem extends Item {
    public TheElixirItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.getCapability(Registry.CAPABILITY).ifPresent(handler -> {
            if (!handler.isActive(TheElixirPlayerCapability.THE_ELIXIR)) {
                if (pPlayer instanceof ServerPlayer serverplayer) {
                    serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);

                    handler.setActive(TheElixirPlayerCapability.THE_ELIXIR, true);
                }
                else {
                    Minecraft mc = Minecraft.getInstance();

                    mc.particleEngine.createTrackingEmitter(pPlayer, ParticleTypes.TOTEM_OF_UNDYING, 30);

                    pLevel.playLocalSound(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                            SoundEvents.TOTEM_USE, pPlayer.getSoundSource(),
                            1.0F, 1.0F, false);

                    mc.gameRenderer.displayItemActivation(new ItemStack(this));
                }
            }
        });

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}

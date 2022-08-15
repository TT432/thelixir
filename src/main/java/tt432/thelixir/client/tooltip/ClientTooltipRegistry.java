package tt432.thelixir.client.tooltip;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tt432.thelixir.common.tooltip.HandbagTooltip;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientTooltipRegistry {
    @SubscribeEvent
    public static void onEvent(FMLClientSetupEvent event) {
        MinecraftForgeClient.registerTooltipComponentFactory(HandbagTooltip.class, ClientHandbagTooltip::of);
    }
}

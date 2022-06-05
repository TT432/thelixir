package tt432.thelixir.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tt432.thelixir.Thelixir;
import tt432.thelixir.capability.player.TheElixirPlayerCapability;
import tt432.thelixir.capability.player.TheElixirPlayerCapabilityProvider;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber()
public class Registry {

    public static final Capability<TheElixirPlayerCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(CAPABILITY).isPresent()) {
                // The player does not already have this capability so we need to add the capability provider here
                event.addCapability(new ResourceLocation(Thelixir.MOD_ID, "player"),
                        new TheElixirPlayerCapabilityProvider(player));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player player = event.getPlayer();
        if (event.isWasDeath()) {
            if (player.level instanceof ServerLevel serverLevel) {
                if (serverLevel.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                    event.getOriginal().reviveCaps();

                    event.getOriginal().getCapability(CAPABILITY).ifPresent(oldStore ->
                            event.getPlayer().getCapability(CAPABILITY).ifPresent(newStore ->
                                    newStore.deserializeNBT(oldStore.serializeNBT())));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(TheElixirPlayerCapability.class);
    }
}

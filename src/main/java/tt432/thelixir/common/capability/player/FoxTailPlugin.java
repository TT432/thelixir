package tt432.thelixir.common.capability.player;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber
public class FoxTailPlugin extends PlayerPlugin {

    private static EntityDataAccessor<Boolean> FOX_TAIL;

    @SubscribeEvent
    public static void onEvent(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof Player player) {
            if (FOX_TAIL == null) {
                FOX_TAIL = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
            }

            player.getEntityData().define(FOX_TAIL, false);
        }
    }

    @Override
    public boolean isActive(Player player) {
        return player.getEntityData().get(FOX_TAIL);
    }

    @Override
    public void setActive(boolean active, Player player) {
        player.getEntityData().set(FOX_TAIL, active);
    }
}

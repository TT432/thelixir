package tt432.thelixir.capability.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.capability.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber
public class TheElixirPlayerCapability implements INBTSerializable<CompoundTag> {

    Map<String, PlayerPlugin> playerPluginMap = built();
    Player player;

    public static final String THE_ELIXIR = "THE_ELIXIR";

    public TheElixirPlayerCapability(Player player) {
        this.player = player;
    }

    private Map<String, PlayerPlugin> built() {
        Map<String, PlayerPlugin> result = new HashMap<>();

        result.put(THE_ELIXIR, new TheElixirPlugin());

        return result;
    }

    public void register(String name, PlayerPlugin plugin) {
        playerPluginMap.put(name, plugin);
    }

    public void unregister(String name) {
        playerPluginMap.remove(name);
    }


    public boolean isActive(String name) {
        return playerPluginMap.containsKey(name) && playerPluginMap.get(name).isActive(player);
    }

    public boolean setActive(String name, boolean active) {
        if (playerPluginMap.containsKey(name)) {
            playerPluginMap.get(name).setActive(active, player);
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.reviveCaps();
            player.getCapability(Registry.CAPABILITY).ifPresent(cap -> {
                if (!cap.onDeath(event.getSource().getEntity(), event.getSource())) {
                    event.setCanceled(true);
                }
            });
        }
    }

    public boolean onDeath(@Nullable Entity attacker, DamageSource source) {
        return allTest(plugin -> plugin.onDeath(attacker, source, player));
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(Registry.CAPABILITY).ifPresent(cap -> {
                if (!cap.onHurt(event.getSource().getEntity())) {
                    event.setCanceled(true);
                }
            });
        }
    }

    public boolean onHurt(@Nullable Entity attacker) {
        return allTest(plugin -> plugin.onHurt(attacker, player));
    }

    protected boolean allTest(Predicate<PlayerPlugin> predicate) {
        var result = true;

        for (var plugin : playerPluginMap.values()) {
            if (!plugin.isActive(player)) {
                continue;
            }

            if (!predicate.test(plugin)) {
                result = false;
            }
        }

        return result;
    }

    @Override
    public CompoundTag serializeNBT() {
        var result = new CompoundTag();

        for (var plugin : playerPluginMap.entrySet()) {
            var tag = plugin.getValue().serializeNBT();
            tag.putBoolean("_active", plugin.getValue().isActive(player));
            result.put(plugin.getKey(), tag);
        }

        return result;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (var plugin : playerPluginMap.entrySet()) {
            if (nbt.contains(plugin.getKey())) {
                var tag = nbt.getCompound(plugin.getKey());
                plugin.getValue().deserializeNBT(tag);
                plugin.getValue().setActive(tag.getBoolean("_active"), player);
            }
        }
    }
}

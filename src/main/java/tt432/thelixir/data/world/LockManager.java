package tt432.thelixir.data.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DustW
 **/
public class LockManager extends SavedData {
    public static final String NAME = "lock_manager";

    public static LockManager get(Level worldIn) {
        if (worldIn instanceof ServerLevel serverLevel) {
            var storage = serverLevel.getDataStorage();

            return storage.computeIfAbsent((tag) -> {
                var result = new LockManager();

                if (tag.contains("locks")) {
                    var list = tag.getList("locks", 10);
                    for (int i = 0; i < list.size(); i++) {
                        var tag1 = list.getCompound(i);
                        var actionValue = tag1.getDouble("action_value");
                        var actionTick = tag1.getInt("action_tick");
                        var entity = serverLevel.getEntity(tag1.getUUID("uuid"));

                        if (entity instanceof LivingEntity livingEntity) {
                            result.locks.put(livingEntity,
                                    new LockAction(livingEntity, actionValue, actionTick));
                        }
                    }
                }

                return result;
            }, LockManager::new, NAME);
        }

        return null;
    }

    protected final Map<LivingEntity, LockAction> locks = new HashMap<>();

    public boolean locked(LivingEntity entity) {
        return locks.containsKey(entity);
    }

    public void lock(LivingEntity entity) {
        if (!locked(entity)) {
            locks.put(entity, new LockAction(entity));
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag list = new ListTag();

        locks.forEach((entity, lockAction) -> {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid", entity.getUUID());
            tag.putDouble("action_value", lockAction.baseValue);
            tag.putInt("action_tick", lockAction.tick);
            list.add(tag);
        });

        pCompoundTag.put("locks", list);
        return pCompoundTag;
    }

    public static class LockAction {
        LivingEntity entity;
        double baseValue;

        private LockAction(LivingEntity entity, double baseValue, int tick) {
            this.entity = entity;
            this.baseValue = baseValue;
            this.tick = tick;
        }

        public LockAction(LivingEntity entity) {
            MinecraftForge.EVENT_BUS.register(this);
            this.entity = entity;
            var attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);

            if (attribute != null) {
                baseValue = attribute.getBaseValue();
                attribute.setBaseValue(0);
            }
        }

        int tick = 0;

        @SubscribeEvent
        public void tick(TickEvent.ServerTickEvent event) {
            if (tick++ == 5 * 20) {
                if (entity.level.isClientSide || !entity.isAlive()) {
                    return;
                }

                var attribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);

                if (attribute != null) {
                    attribute.setBaseValue(baseValue);
                }

                get(entity.level).locks.remove(entity);
            }
        }
    }
}

package tt432.thelixir.capability.player;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber
public class TheElixirPlugin extends PlayerPlugin {

    private static EntityDataAccessor<Boolean> THE_ELIXIR;

    @SubscribeEvent
    public static void onEvent(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof Player player) {
            THE_ELIXIR = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
            player.getEntityData().define(THE_ELIXIR, false);
        }
    }

    @Override
    public boolean isActive(Player player) {
        return player.getEntityData().get(THE_ELIXIR);
    }

    @Override
    public void setActive(boolean active, Player player) {
        player.getEntityData().set(THE_ELIXIR, active);
    }

    @Override
    public boolean onDeath(@Nullable Entity attacker, DamageSource source, Player player) {
        var level = player.level;

        if (level.isClientSide) {
            return true;
        }

        player.setHealth(player.getMaxHealth());

        // 取消仇恨
        if (level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            AABB aabb = (new AABB(player.blockPosition())).inflate(32.0D, 10.0D, 32.0D);
            player.level.getEntitiesOfClass(Mob.class, aabb, EntitySelector.NO_SPECTATORS).stream()
                    .filter((mob) -> mob instanceof NeutralMob)
                    .forEach((mob) -> ((NeutralMob)mob).playerDied(player));
        }

        //计分板数据更新
        player.getScoreboard().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, player.getScoreboardName(), Score::increment);

        //统计数据（被生物杀死）更新
        if (attacker != null) {
            player.awardStat(Stats.ENTITY_KILLED_BY.get(attacker.getType()));
            CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((ServerPlayer) player, attacker, source);
        }

        //死亡相关统计数据更新
        player.awardStat(Stats.DEATHS);
        player.awardStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        player.awardStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));

        ForgeEventFactory.firePlayerRespawnEvent(player, false);

        player.getCombatTracker().recheckStatus();

        return false;
    }
}

package tt432.thelixir.capability.player;

import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.net.ModNetManager;
import tt432.thelixir.net.server.TheExlxirSyncS2C;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber
public class TheElixirPlugin extends PlayerPlugin {

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

    @Override
    protected void activeChanged(Player player) {
        if (player instanceof ServerPlayer sPlayer) {
            ModNetManager.sendToPlayer(new TheExlxirSyncS2C(isActive(player)), sPlayer);
        }
    }
}

package tt432.thelixir.capability.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

/**
 * @author DustW
 **/
public class PlayerPlugin implements INBTSerializable<CompoundTag> {

    private boolean active;

    public boolean isActive(Player player) {
        return active;
    }

    public void setActive(boolean active, Player player) {
        if (this.active != active) {
            this.active = active;
            activeChanged(player);
        }
    }

    public boolean onDeath(@Nullable Entity attacker, DamageSource source, Player player) {
        return true;
    }

    public boolean onHurt(@Nullable Entity attacker, Player player) {
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    protected void activeChanged(Player player) {

    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}

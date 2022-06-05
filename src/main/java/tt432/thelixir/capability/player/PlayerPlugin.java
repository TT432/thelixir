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

    boolean active;

    public boolean isActive(Player player) {
        return active;
    }

    public void setActive(boolean active, Player player) {
        this.active = active;
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

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}

package tt432.thelixir.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import tt432.thelixir.data.world.LockManager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber
public class BreezeSword extends SwordItem {
    public BreezeSword(Properties pProperties) {
        super(new ForgeTier(42, "Breeze".hashCode(), 42, 3, 7,
                        Tags.Blocks.CHESTS, () -> Ingredient.of(new ItemStack(Items.IRON_INGOT))),
                1, 42, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, level, tooltips, pIsAdvanced);

        tooltips.add(new TranslatableComponent("tooltip.breeze_sword.attack.1"));
        tooltips.add(new TranslatableComponent("tooltip.breeze_sword.attack.2"));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        var count = getCounter(stack);

        target.invulnerableTime = 0;

        switch (count) {
            case 0 -> pickup(target);
            case 1 -> randomBuff(attacker, target);
            default -> {}
        }

        addDamageBonus(attacker.level, stack);

        nextCounter(stack);

        return super.hurtEnemy(stack, target, attacker);
    }

    public static final String MODIFIER_NAME = "BreezeSwordDamage";
    public static final UUID MODIFIER_UUID = UUID.fromString("0323246a-f25e-b553-9e0d-368ec9c65866");

    protected static AttributeModifier getDamageModifier(ItemStack itemStack) {
        return  new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME,
                getDamageBonus(itemStack), AttributeModifier.Operation.ADDITION);
    }

    @SubscribeEvent
    public static void onEvent(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof BreezeSword) {
            if (getDamageBonus(event.getItemStack()) > 0 && event.getSlotType() == EquipmentSlot.MAINHAND) {
                var modifier = getDamageModifier(event.getItemStack());
                event.removeModifier(Attributes.ATTACK_DAMAGE, modifier);
                event.addModifier(Attributes.ATTACK_DAMAGE, modifier);
            }
        }
    }

    @SubscribeEvent
    public static void onEvent(LivingKnockBackEvent event) {
        if (event.getEntityLiving().getKillCredit() instanceof Player player &&
                player.getMainHandItem().getItem() == ModItems.BREEZE_SWORD.get()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pLevel.isClientSide) {
            return;
        }

        if (getTimestamp(pStack) + (20 * 6) < pLevel.getGameTime()) {
            clearDamageBonus(pStack);
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public static final String TIMESTAMP_KEY = "timestamp";

    public static void setTimestamp(Level level, ItemStack itemStack) {
        itemStack.getOrCreateTag().putLong(TIMESTAMP_KEY, level.getGameTime());
    }

    public static long getTimestamp(ItemStack itemStack) {
        if (itemStack.getTag() != null && itemStack.getTag().contains(TIMESTAMP_KEY)) {
            return itemStack.getTag().getLong(TIMESTAMP_KEY);
        }

        return 0;
    }

    public static final String DAMAGE_BONUS_KEY = "damage_bonus";

    protected static int getDamageBonus(ItemStack itemStack) {
        if (itemStack.getTag() != null && itemStack.getTag().contains(DAMAGE_BONUS_KEY)) {
            return itemStack.getTag().getInt(DAMAGE_BONUS_KEY);
        }

        return 0;
    }

    protected void addDamageBonus(Level level, ItemStack itemStack) {
        var current = getDamageBonus(itemStack) + 1;
        var max = getMaxDamageBonus(itemStack);
        itemStack.getOrCreateTag().putInt(DAMAGE_BONUS_KEY, Math.min(current, max));
        setTimestamp(level, itemStack);
    }

    protected void clearDamageBonus(ItemStack itemStack) {
        itemStack.getOrCreateTag().remove(DAMAGE_BONUS_KEY);
    }

    protected int getMaxDamageBonus(ItemStack itemStack) {
        return 42;
    }

    static List<MobEffect> effects;

    protected void randomBuff(LivingEntity attacker, LivingEntity target) {
        if (target.level.isClientSide) {
            return;
        }

        if (effects == null) {
            effects = ForgeRegistries.MOB_EFFECTS.getValues().stream().filter(effect ->
                    effect.getCategory() == MobEffectCategory.HARMFUL).collect(Collectors.toList());
        }

        var effect = effects.get(target.level.random.nextInt(effects.size()));

        if (effect.isInstantenous()) {
            effect.applyInstantenousEffect(attacker, attacker, target, 1, 1.0D);
        } else {
            target.addEffect(new MobEffectInstance(effect, 5 * 20, 1));
        }
    }

    /** 挑飞 */
    protected void pickup(LivingEntity target) {
        if (target.level.isClientSide) {
            return;
        }

        var vec3 = target.getDeltaMovement();
        target.setDeltaMovement(vec3.x, vec3.y + .42, vec3.z);
        LockManager.get(target.level).lock(target);
    }

    public static final String COUNTER_KEY = "counter";

    protected int getCounter(ItemStack itemStack) {
        if (itemStack.getTag() != null && itemStack.getTag().contains(COUNTER_KEY)) {
            return itemStack.getTag().getInt(COUNTER_KEY);
        }

        return -1;
    }

    protected void nextCounter(ItemStack itemStack) {
        var current = getCounter(itemStack) + 1;
        var max = getMaxCounter(itemStack);
        itemStack.getOrCreateTag().putInt(COUNTER_KEY, current >= max ? 0 : current);
    }

    protected int getMaxCounter(ItemStack itemStack) {
        return 3;
    }
}

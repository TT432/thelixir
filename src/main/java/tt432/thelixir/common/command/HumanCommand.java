package tt432.thelixir.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import tt432.thelixir.common.capability.Registry;
import tt432.thelixir.common.capability.player.TheElixirPlayerCapability;

import java.util.Collection;

/**
 * @author DustW
 */
public class HumanCommand implements Command<CommandSourceStack> {
    public static final HumanCommand INSTANCE = new HumanCommand();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        try {
            Collection<? extends Entity> entity = EntityArgument.getEntities(context, "targets");

            entity.forEach(e -> {
                if (e instanceof ServerPlayer player) {
                    player.getCapability(Registry.CAPABILITY).ifPresent(handler -> {
                        if (handler.isActive(TheElixirPlayerCapability.THE_ELIXIR)) {
                            handler.setActive(TheElixirPlayerCapability.THE_ELIXIR, false);

                            player.sendMessage(new TextComponent("你被强制变回了人类"), player.getUUID());
                            context.getSource().sendSuccess(
                                    new TextComponent("已将 " + player.getDisplayName().getString() + " 变回了人类"),
                                    true);
                        }
                        else {
                            context.getSource().sendSuccess(
                                    new TextComponent(player.getDisplayName().getString() + " 已经是人类了"),
                                    true);
                        }
                    });
                }
                else {
                    context.getSource().sendSuccess(
                            new TextComponent("请对玩家使用"),
                            true);
                }
            });
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

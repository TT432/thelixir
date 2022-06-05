package tt432.thelixir.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tt432.thelixir.Thelixir;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber
public class ModCommands {
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(Thelixir.MOD_ID).then(
                        Commands.literal("human").then(
                                Commands.argument("targets", EntityArgument.entities())
                                        .requires((commandSource) -> commandSource.hasPermission(2))
                                        .executes(HumanCommand.INSTANCE)
                        )
                )
        );
        dispatcher.register(Commands.literal("elixir").redirect(cmd));
    }
}
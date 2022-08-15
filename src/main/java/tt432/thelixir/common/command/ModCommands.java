package tt432.thelixir.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tt432.thelixir.Thelixir;
import tt432.thelixir.common.item.ModItems;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber
public class ModCommands {
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal(Thelixir.MOD_ID).then(
                        Commands.literal("human").then(
                                Commands.argument("targets", EntityArgument.entities())
                                        .requires(commandSource -> commandSource.hasPermission(2))
                                        .executes(HumanCommand.INSTANCE)
                        )
                ).then(
                        Commands.literal("bag").then(
                                        Commands.argument("row", IntegerArgumentType.integer())
                                                .then(
                                                        Commands.argument("col", IntegerArgumentType.integer())
                                                                .requires(commandSource -> commandSource.hasPermission(2))
                                                                .executes(stack -> {
                                                                    CommandSourceStack source = stack.getSource();
                                                                    ServerPlayer player = source.getPlayerOrException();
                                                                    CompoundTag tag = new CompoundTag();
                                                                    tag.putInt("row", stack.getArgument("row", Integer.TYPE));
                                                                    tag.putInt("col", stack.getArgument("col", Integer.TYPE));
                                                                    player.addItem(new ItemStack(ModItems.HANDBAG.get(), 1, tag));
                                                                    return 1;
                                                                })
                                                )
                                )
                                .requires(commandSource -> commandSource.hasPermission(2))
                                .executes(stack -> {
                                    CommandSourceStack source = stack.getSource();
                                    ServerPlayer player = source.getPlayerOrException();
                                    CompoundTag tag = new CompoundTag();
                                    tag.putInt("row", 6);
                                    tag.putInt("col", 6);
                                    player.addItem(new ItemStack(ModItems.HANDBAG.get(), 1, tag));
                                    return 1;
                                })
                )
        );
    }
}
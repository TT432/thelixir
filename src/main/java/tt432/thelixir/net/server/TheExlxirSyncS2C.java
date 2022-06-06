package tt432.thelixir.net.server;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import tt432.thelixir.capability.Registry;
import tt432.thelixir.capability.player.TheElixirPlayerCapability;

import java.util.function.Supplier;

/**
 * @author DustW
 **/
public class TheExlxirSyncS2C {
    boolean active;

    public TheExlxirSyncS2C(boolean active) {
        this.active = active;
    }

    public TheExlxirSyncS2C(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.readBoolean();
    }

    public static void toBytes(TheExlxirSyncS2C packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(packet.active);
    }

    public static boolean handle(TheExlxirSyncS2C packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            Minecraft.getInstance().player.getCapability(Registry.CAPABILITY).ifPresent(handler -> {
                handler.setActive(TheElixirPlayerCapability.THE_ELIXIR, packet.active);
            });
        });
        return true;
    }
}

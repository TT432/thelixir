package tt432.thelixir;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tt432.thelixir.item.ModItems;
import tt432.thelixir.item.VanillaItems;

/**
 * @author DustW
 */
@Mod(Thelixir.MOD_ID)
public class Thelixir {
    public static final String MOD_ID = "thelixir";

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return ItemStack.EMPTY;
        }
    };

    public Thelixir() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
        VanillaItems.ITEMS.register(bus);
    }
}

package tt432.thelixir;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tt432.thelixir.common.block.ModBlocks;
import tt432.thelixir.common.item.ModItems;
import tt432.thelixir.net.ModNetManager;
import tt432.thelixir.common.sound.ModSoundEvents;

/**
 * @author DustW
 */
@Mod(Thelixir.MOD_ID)
public class Thelixir {
    public static final String MOD_ID = "thelixir";

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.THE_ELIXIR.get());
        }
    };

    public Thelixir() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
        ModItems.VANILLA.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModSoundEvents.SOUNDS.register(bus);

        ModNetManager.register();
    }
}

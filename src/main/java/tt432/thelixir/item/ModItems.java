package tt432.thelixir.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tt432.thelixir.Thelixir;

/**
 * @author DustW
 **/
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thelixir.MOD_ID);

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(Thelixir.MOD_TAB);
    }

    public static final RegistryObject<Item> HANDBAG = ITEMS.register("handbag",
            () -> new HandbagItem(defaultProperties()));

    public static final RegistryObject<Item> THE_ELIXIR = ITEMS.register("the_elixir",
            () -> new TheElixirItem(defaultProperties()));
}

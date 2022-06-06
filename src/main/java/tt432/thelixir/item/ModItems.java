package tt432.thelixir.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tt432.thelixir.Thelixir;
import tt432.thelixir.block.ModBlocks;

/**
 * @author DustW
 **/
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thelixir.MOD_ID);

    private static Item.Properties defaultProperties() {
        return new Item.Properties().tab(Thelixir.MOD_TAB);
    }

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), defaultProperties()));
    }

    public static final RegistryObject<Item> HANDBAG = ITEMS.register("handbag",
            () -> new HandbagItem(defaultProperties()));

    public static final RegistryObject<Item> THE_ELIXIR = ITEMS.register("the_elixir",
            () -> new TheElixirItem(defaultProperties()));


    public static final RegistryObject<Item> SHOOTING_PLATFORM = block(ModBlocks.SHOOTING_PLATFORM);


    public static final DeferredRegister<Item> VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final RegistryObject<Item> HUG = VANILLA.register("hug", () -> new HugItem(new Item.Properties()));
}

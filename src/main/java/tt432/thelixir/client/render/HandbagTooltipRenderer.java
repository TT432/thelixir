package tt432.thelixir.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import tt432.thelixir.Thelixir;
import tt432.thelixir.item.HandbagItem;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HandbagTooltipRenderer {
    public static final ResourceLocation GUI = new ResourceLocation(Thelixir.MOD_ID, "textures/gui/handbag.png");

    @SubscribeEvent
    public static void onEvent(RenderTooltipEvent.Pre event) {
        var item = event.getItemStack();

        if (!(item.getItem() instanceof HandbagItem)) {
            return;
        }

        var x = event.getX() + 8;
        var y = event.getY() - 16;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);

        var relY = y - 41;
        var ps = event.getPoseStack();
        AbstractContainerScreen.blit(ps, x, relY,
                0, 0, 0,
                58, 41,
                64, 64);

        var mc = Minecraft.getInstance();
        var ir = mc.getItemRenderer();

        RenderSystem.enableDepthTest();

        ir.blitOffset += 200;

        item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            for (int i = 0; i < 6; i++) {
                var slot = handler.getStackInSlot(i);
                renderItem(ir, mc.font, x + 3 + 18 * i, relY + 3 + i / 3 * 18, slot, String.valueOf(slot.getCount()));
            }
        });

        ir.blitOffset -= 200;
    }

    static void renderItem(ItemRenderer ir, Font font, int x, int y, ItemStack item, String text) {
        ir.renderAndDecorateItem(item, x, y);
        ir.renderGuiItemDecorations(font, item, x, y, text);
    }
}

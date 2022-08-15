package tt432.thelixir.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tt432.thelixir.common.tooltip.HandbagTooltip;

/**
 * @author DustW
 */
public record ClientHandbagTooltip(NonNullList<ItemStack> items, int row, int col) implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");

    private static final int MARGIN_Y = 4;
    private static final int BORDER_WIDTH = 1;
    private static final int TEX_SIZE = 128;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;

    public static ClientHandbagTooltip of(HandbagTooltip tooltip) {
        return new ClientHandbagTooltip(tooltip.items(), tooltip.row(), tooltip.col());
    }

    public int getHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y + 2 + MARGIN_Y;
    }

    public int getWidth(Font pFont) {
        return this.gridSizeX() * SLOT_SIZE_X + 2;
    }

    @Override
    public void renderImage(Font pFont, int pMouseX, int pMouseY, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = pMouseX + i1 * SLOT_SIZE_X + BORDER_WIDTH;
                int k1 = pMouseY + l * SLOT_SIZE_Y + BORDER_WIDTH;
                this.renderSlot(j1, k1, k++, pFont, pPoseStack, pItemRenderer, pBlitOffset);
            }
        }

        this.drawBorder(pMouseX, pMouseY, i, j, pPoseStack, pBlitOffset);
    }

    private void renderSlot(int pX, int pY, int pItemIndex, Font pFont, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
        if (pItemIndex >= this.items.size()) {
            this.blit(pPoseStack, pX, pY, pBlitOffset, Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(pItemIndex);
            this.blit(pPoseStack, pX, pY, pBlitOffset, Texture.SLOT);
            pItemRenderer.renderAndDecorateItem(itemstack, pX + BORDER_WIDTH, pY + BORDER_WIDTH, pItemIndex);
            pItemRenderer.renderGuiItemDecorations(pFont, itemstack, pX + BORDER_WIDTH, pY + 1);
        }
    }

    private void drawBorder(int pX, int pY, int pSlotWidth, int pSlotHeight, PoseStack pPoseStack, int pBlitOffset) {
        this.blit(pPoseStack, pX, pY, pBlitOffset, Texture.BORDER_CORNER_TOP);
        this.blit(pPoseStack, pX + pSlotWidth * SLOT_SIZE_X + BORDER_WIDTH, pY, pBlitOffset, Texture.BORDER_CORNER_TOP);

        for(int i = 0; i < pSlotWidth; ++i) {
            this.blit(pPoseStack, pX + BORDER_WIDTH + i * SLOT_SIZE_X, pY, pBlitOffset, Texture.BORDER_HORIZONTAL_TOP);
            this.blit(pPoseStack, pX + BORDER_WIDTH + i * SLOT_SIZE_X, pY + pSlotHeight * SLOT_SIZE_Y, pBlitOffset, Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for(int j = 0; j < pSlotHeight; ++j) {
            this.blit(pPoseStack, pX, pY + j * SLOT_SIZE_Y + BORDER_WIDTH, pBlitOffset, Texture.BORDER_VERTICAL);
            this.blit(pPoseStack, pX + pSlotWidth * SLOT_SIZE_X + BORDER_WIDTH, pY + j * SLOT_SIZE_Y + BORDER_WIDTH, pBlitOffset, Texture.BORDER_VERTICAL);
        }

        this.blit(pPoseStack, pX, pY + pSlotHeight * SLOT_SIZE_Y, pBlitOffset, Texture.BORDER_CORNER_BOTTOM);
        this.blit(pPoseStack, pX + pSlotWidth * SLOT_SIZE_X + BORDER_WIDTH, pY + pSlotHeight * SLOT_SIZE_Y, pBlitOffset, Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(PoseStack pPoseStack, int pX, int pY, int pBlitOffset, Texture pTexture) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(pPoseStack, pX, pY, pBlitOffset, pTexture.x, pTexture.y, pTexture.w, pTexture.h, TEX_SIZE, TEX_SIZE);
    }

    private int gridSizeX() {
        return row;
    }

    private int gridSizeY() {
        return col;
    }

    enum Texture {
        SLOT(0, 0, SLOT_SIZE_X, SLOT_SIZE_Y),
        BLOCKED_SLOT(0, 40, SLOT_SIZE_X, SLOT_SIZE_Y),
        BORDER_VERTICAL(0, SLOT_SIZE_X, BORDER_WIDTH, SLOT_SIZE_Y),
        BORDER_HORIZONTAL_TOP(0, SLOT_SIZE_Y, SLOT_SIZE_X, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, SLOT_SIZE_X, 1),
        BORDER_CORNER_TOP(0, SLOT_SIZE_Y, BORDER_WIDTH, 1),
        BORDER_CORNER_BOTTOM(0, 60, BORDER_WIDTH, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int pX, int pY, int pW, int pH) {
            this.x = pX;
            this.y = pY;
            this.w = pW;
            this.h = pH;
        }
    }
}

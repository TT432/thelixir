package tt432.thelixir.client.render;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Fox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tt432.thelixir.capability.player.TheElixirPlayerCapability;

/**
 * @author DustW
 **/
@Mod.EventBusSubscriber(Dist.CLIENT)
public class FoxTailRender {
    private static final ResourceLocation SNOW_FOX_TEXTURE = new ResourceLocation("textures/entity/fox/snow_fox.png");

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent event) {
        var player = event.getPlayer();

        if (player.isSpectator()) {
            return;
        }

        TheElixirPlayerCapability.ifActive(player, TheElixirPlayerCapability.FOX_TAIL, (cap) -> {
            ModelPart foxModelRoot = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.FOX);
            FoxModel<Fox> foxModel = new FoxModel<>(foxModelRoot);
            ModelPart tail = foxModelRoot.getChild("body").getChild("tail");

            var buffers = event.getMultiBufferSource();
            var poseStack = event.getPoseStack();
            var model = event.getRenderer().getModel();
            var packedLight = event.getPackedLight();

            RenderType type = foxModel.renderType(SNOW_FOX_TEXTURE);
            var buffer = buffers.getBuffer(type);

            poseStack.pushPose();

            poseStack.mulPose(Vector3f.YN.rotationDegrees(180 + player.yBodyRot));

            if (player.isCrouching()) {
                poseStack.mulPose(Vector3f.XP.rotation(model.body.xRot));
                poseStack.translate(0, 1 + -11 / 16F, -16 / 16F);
            } else {
                poseStack.translate(0, 1 + -11 / 16F, -12 / 16F);
            }

            poseStack.mulPose(Vector3f.XP.rotationDegrees(55));

            tail.render(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        });
    }
}

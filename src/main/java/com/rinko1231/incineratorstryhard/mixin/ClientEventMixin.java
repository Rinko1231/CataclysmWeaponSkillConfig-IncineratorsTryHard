package com.rinko1231.incineratorstryhard.mixin;

import com.github.L_Ender.cataclysm.Cataclysm;
import com.github.L_Ender.cataclysm.ClientProxy;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Axis;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import com.github.L_Ender.cataclysm.client.event.ClientEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.L_Ender.cataclysm.client.event.ClientEvent.FLAME_STRIKE;
import static com.github.L_Ender.cataclysm.client.event.ClientEvent.drawVertex;

@Mixin(value = ClientEvent.class, remap = false)
public abstract class ClientEventMixin {


        @Inject(method = "onPreRenderEntity", at = @At("HEAD"), cancellable = true)
        private static void ModifyonPreRenderEntity(RenderLivingEvent.Pre event, CallbackInfo ci) {
            LivingEntity player = event.getEntity();
            boolean usingIncinerator = player.isUsingItem() && player.getUseItem().is((Item)ModItems.THE_INCINERATOR.get());
            boolean usingImmolator = player.isUsingItem() && player.getUseItem().is((Item)ModItems.THE_IMMOLATOR.get());
            if (usingIncinerator) {
                int i = player.getTicksUsingItem();
                float f2 = (float)player.tickCount + event.getPartialTick();
                PoseStack matrixStackIn = event.getPoseStack();
                int maxChargeTime = IncineratorsTryHardConfig.chargingTimeToMaxCircle.get(); // 新的蓄力时间上限
                int originalMaxTime = 60;
                float f3 = (float) Mth.clamp(i, 1, maxChargeTime) / maxChargeTime * originalMaxTime;
                matrixStackIn.pushPose();
                VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(event.getMultiBufferSource(), RenderType.entityTranslucentEmissive(FLAME_STRIKE), true);
                matrixStackIn.translate((double)0.0F, 0.001, (double)0.0F);
                matrixStackIn.scale(f3 * 0.05F, f3 * 0.05F, f3 * 0.05F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F + f2));
                PoseStack.Pose lvt_19_1_ = matrixStackIn.last();
                drawVertex(lvt_19_1_, ivertexbuilder, -1, 0, -1, 0.0F, 0.0F, 1, 0, 1, 240);
                drawVertex(lvt_19_1_, ivertexbuilder, -1, 0, 1, 0.0F, 1.0F, 1, 0, 1, 240);
                drawVertex(lvt_19_1_, ivertexbuilder, 1, 0, 1, 1.0F, 1.0F, 1, 0, 1, 240);
                drawVertex(lvt_19_1_, ivertexbuilder, 1, 0, -1, 1.0F, 0.0F, 1, 0, 1, 240);
                matrixStackIn.popPose();
        }

            if (usingImmolator) {
                int i = player.getTicksUsingItem();
                float f2 = (float)player.tickCount + event.getPartialTick();
                PoseStack matrixStackIn = event.getPoseStack();
                int maxChargeTime = IncineratorsTryHardConfig.chargingTimeToMaxCircleForImmolator.get(); // 新的蓄力时间上限
                int originalMaxTime = 45;
                float f3 = (float) Mth.clamp(i, 1, maxChargeTime) / maxChargeTime * originalMaxTime;
                matrixStackIn.pushPose();
                VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(event.getMultiBufferSource(), RenderType.entityTranslucentEmissive(FLAME_STRIKE), true);
                matrixStackIn.translate((double)0.0F, 0.001, (double)0.0F);
                matrixStackIn.scale(f3 * 0.05F, f3 * 0.05F, f3 * 0.05F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F + f2));
                PoseStack.Pose lvt_19_1_ = matrixStackIn.last();
                drawVertex(lvt_19_1_, ivertexbuilder, -1, 0, -1, 0.0F, 0.0F, 1, 0, 1, 240);
                drawVertex(lvt_19_1_, ivertexbuilder, -1, 0, 1, 0.0F, 1.0F, 1, 0, 1, 240);
                drawVertex(lvt_19_1_, ivertexbuilder, 1, 0, 1, 1.0F, 1.0F, 1, 0, 1, 240);
                drawVertex(lvt_19_1_, ivertexbuilder, 1, 0, -1, 1.0F, 0.0F, 1, 0, 1, 240);
                matrixStackIn.popPose();
            }
            if (ClientProxy.blockedEntityRenders.contains(event.getEntity().getUUID())) {
                if (!Cataclysm.PROXY.isFirstPersonPlayer(event.getEntity())) {
                    NeoForge.EVENT_BUS.post(new RenderLivingEvent.Post(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
                    event.setCanceled(true);
                }

                ClientProxy.blockedEntityRenders.remove(event.getEntity().getUUID());
            }
     ci.cancel();
    }


}

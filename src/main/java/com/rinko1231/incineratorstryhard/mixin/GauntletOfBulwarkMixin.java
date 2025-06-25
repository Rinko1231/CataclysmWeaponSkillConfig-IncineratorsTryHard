package com.rinko1231.incineratorstryhard.mixin;



import com.github.L_Ender.cataclysm.Attachment.ChargeAttachment;
import com.github.L_Ender.cataclysm.config.CMConfig;

import com.github.L_Ender.cataclysm.init.ModDataAttachments;
import com.github.L_Ender.cataclysm.items.Gauntlet_of_Bulwark;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gauntlet_of_Bulwark.class)
public abstract class GauntletOfBulwarkMixin extends Item {


    public GauntletOfBulwarkMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft, CallbackInfo ci) {
        if (!entityLiving.isShiftKeyDown() && !entityLiving.isFallFlying()) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            int t = Mth.clamp(i, 1, 5);
            float f7 = entityLiving.getYRot();
            float f = entityLiving.getXRot();
            if (i >= 20) {
                float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 3.0F * ((float)t / 6.0F);
                f1 *= f5 / f4;
                f3 *= f5 / f4;
                entityLiving.push((double)f1, (double)0.0F, (double)f3);
                if (entityLiving.onGround()) {
                    float f6 = 1.1999999F;
                    entityLiving.move(MoverType.SELF, new Vec3((double)0.0F, (double)f6 / (double)2.0F, (double)0.0F));
                }

                ChargeAttachment charge = (ChargeAttachment)entityLiving.getData(ModDataAttachments.CHARGE_ATTACHMENT);
                charge.setCharge(true);
                charge.setTimer(t * 2);
                charge.seteffectiveChargeTime(t * 2);
                charge.setknockbackSpeedIndex((float)t * 0.35F);
                charge.setdamagePerEffectiveCharge(1.2F);
                    charge.setdamagePerEffectiveCharge(IncineratorsTryHardConfig.ChargeDamageMultiplierOfGauntletOfBulwark.get().floatValue());
                    charge.setdx(f1 * 0.5F);
                    charge.setdZ(f3 * 0.5F);


                if (!level.isClientSide) {
                    ((Player)entityLiving).getCooldowns().addCooldown(this, CMConfig.GauntletOfBulwarkCooldown);
                }
            }
        }
      ci.cancel();
    }


}

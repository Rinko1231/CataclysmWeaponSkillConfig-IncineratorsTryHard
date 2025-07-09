package com.rinko1231.incineratorstryhard.mixin;

import com.github.L_Ender.cataclysm.init.ModEffect;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.github.L_Ender.cataclysm.items.Ignitium_Armor;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Ignitium_Armor.class, remap = false)
public abstract class IgnitiumArmorMixin {

    @Inject(method = "onKeyPacket", at = @At("HEAD"), cancellable = true)
    public void ModifyOnKeyPacket(Player player, ItemStack itemStack, int Type, CallbackInfo ci) {
        if (Type == 5 && player != null && !player.getCooldowns().isOnCooldown((Item) ModItems.IGNITIUM_HELMET.get())) {
            boolean flag = false;

            for(Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate((double)16.0F))) {
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)entity;
                    MobEffectInstance effectinstance1 = living.getEffect((MobEffect)ModEffect.EFFECTBLAZING_BRAND.get());
                    int i = 1;
                    if (effectinstance1 != null) {
                        i += effectinstance1.getAmplifier();
                        living.removeEffectNoUpdate((MobEffect)ModEffect.EFFECTBLAZING_BRAND.get());
                    } else {
                        --i;
                    }

                    i = Mth.clamp(i, 0, 2);
                    MobEffectInstance effectinstance = new MobEffectInstance((MobEffect)ModEffect.EFFECTBLAZING_BRAND.get(), 160, i, true, true, true);
                    flag = living.addEffect(effectinstance);
                }

                if (flag) {
                    player.getCooldowns().addCooldown((Item)ModItems.IGNITIUM_HELMET.get(), IncineratorsTryHardConfig.ignitiumHelmetCoolDown.get());
                }
            }
        }
        ci.cancel();
    }
}
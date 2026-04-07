package com.rinko1231.incineratorstryhard.mixin;

import com.github.L_Ender.cataclysm.config.CMCommonConfig;
import com.github.L_Ender.cataclysm.entity.effect.Wave_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Player_Ceraunus_Entity;

import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.items.Ceraunus;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.L_Ender.cataclysm.items.Ceraunus.getThrownUuid;

@Mixin(value= Ceraunus.class)
public abstract class CeraunusMixin extends Item {

    public CeraunusMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private void Mixin$setThrownEntity(ItemStack stack, Player_Ceraunus_Entity cube) {
        stack.getOrCreateTag().putUUID("thrown_anchor", cube.getUUID());
    }


    @Unique
    private static float _1211$getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }


    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    public void ModifyReleaseUsing(ItemStack p_43394_, Level p_43395_, LivingEntity p_43396_, int p_43397_, CallbackInfo ci) {
        if (p_43396_ instanceof Player player) {
            int i = this.getUseDuration(p_43394_) - p_43397_;
            float f = _1211$getPowerForTime(i);
            if (!((double)f < IncineratorsTryHardConfig.ceraunusChargingTime.get()) && !p_43395_.isClientSide) {
                float yawRadians = (float)Math.toRadians((double)(90.0F + player.getYRot()));
                double vecX = Math.cos((double)yawRadians);
                double vecZ = Math.sin((double)yawRadians);
                double vec = (double)2.0F;
                double spawnX = p_43396_.getX() + vecX * vec;
                double spawnY = p_43396_.getY();
                double spawnZ = p_43396_.getZ() + vecZ * vec;
                int numberOfWaves = IncineratorsTryHardConfig.ceraunusNumberOfWaves.get();
                float angleStep = 25.0F;
                double firstAngleOffset = (double)(numberOfWaves - 1) / (double)2.0F * (double)angleStep;
                if (p_43396_.isShiftKeyDown()) {
                    player.getCooldowns().addCooldown(this, CMCommonConfig.Ceraunus.cooldown);
                    p_43395_.playSound((Player)null, player.getX(), player.getY(), player.getZ(), (SoundEvent) ModSounds.HEAVY_SMASH.get(), SoundSource.PLAYERS, 0.6F, 1.0F);

                    for(int k = 0; k < numberOfWaves; ++k) {
                        double angle = (double)player.getYRot() - firstAngleOffset + (double)((float)k * angleStep);
                        double rad = Math.toRadians(angle);
                        double dx = -Math.sin(rad);
                        double dz = Math.cos(rad);
                        Wave_Entity WaveEntity = new Wave_Entity(p_43395_, p_43396_, IncineratorsTryHardConfig.ceraunusLifeOfWaves.get(), (float)CMCommonConfig.Ceraunus.waveDamage);
                        WaveEntity.setPos(spawnX, spawnY, spawnZ);
                        WaveEntity.setState(1);
                        WaveEntity.setYRot(-((float)(Mth.atan2(dx, dz) * (180D / Math.PI))));
                        p_43396_.level().addFreshEntity(WaveEntity);
                    }
                } else if (getThrownUuid(p_43394_) == null) {
                    Player_Ceraunus_Entity launchedBlock = new Player_Ceraunus_Entity(p_43395_, player);
                    launchedBlock.setBaseDamage((double)((float)player.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                    launchedBlock.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, IncineratorsTryHardConfig.ceraunusThrownVelocity.get().floatValue(), 1.0F);
                    if (p_43395_.addFreshEntity(launchedBlock)) {
                        this.Mixin$setThrownEntity(p_43394_, launchedBlock);
                    }
                }
            }
        }
        ci.cancel();
    }
}
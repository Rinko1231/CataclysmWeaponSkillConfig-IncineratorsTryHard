package com.rinko1231.incineratorstryhard.mixin;


import com.github.L_Ender.cataclysm.entity.projectile.Lightning_Spear_Entity;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.items.Astrape;
import com.github.L_Ender.cataclysm.config.CMCommonConfig;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Astrape.class)
public abstract class AstrapeMixin extends Item {


    public AstrapeMixin(Properties properties) {
        super(properties);
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
    public void ModifyReleaseUsing(ItemStack p_43394_, Level p_43395_, LivingEntity p_43396_, int p_43397_, CallbackInfo ci){
        if (p_43396_ instanceof Player player) {
            int i = this.getUseDuration(p_43394_) - p_43397_;
            float f = _1211$getPowerForTime(i);
            if (!((double) f < IncineratorsTryHardConfig.astrapeChargingTime.get())) {
                p_43395_.playSound((Player) null, player.getX(), player.getY(), player.getZ(), (SoundEvent) ModSounds.EMP_ACTIVATED.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
                if (!p_43395_.isClientSide) {
                    Vec3 lookDirection = player.getLookAngle();
                    Vec3 vec3 = new Vec3(lookDirection.x, lookDirection.y, lookDirection.z);
                    float yRot = (float)(Mth.atan2(vec3.z, vec3.x) * (180D / Math.PI)) + 90.0F;
                    float xRot = (float)(-(Mth.atan2(vec3.y, Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z)) * (180D / Math.PI)));
                    Lightning_Spear_Entity lightning = new Lightning_Spear_Entity(player, vec3, p_43395_, (float)CMCommonConfig.Astrape.damage, (double)2.5F);
                    lightning.setYRot(yRot);
                    lightning.setXRot(xRot);
                    lightning.accelerationPower = IncineratorsTryHardConfig.astrapeAccelerationPower.get();
                    lightning.setPos(lightning.getX(), player.getY((double)0.75F), lightning.getZ());
                    lightning.setAreaDamage((float)CMCommonConfig.Astrape.areaDamage);
                    lightning.setAreaRadius(IncineratorsTryHardConfig.astrapeAreaRadius.get().floatValue());
                    boolean flag = p_43395_.addFreshEntity(lightning);
                    if (flag) {
                        player.getCooldowns().addCooldown(this, CMCommonConfig.Astrape.cooldown);
                    }
                }
            }
        }

        ci.cancel();
    }


}
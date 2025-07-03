package com.rinko1231.incineratorstryhard.mixin;

import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.projectile.Lightning_Spear_Entity;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.items.Astrape;
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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin (value = Astrape.class)
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

    @Shadow
    public abstract int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity pEntity);

    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    public void ModifyReleaseUsing(ItemStack p_43394_, Level p_43395_, LivingEntity p_43396_, int p_43397_, CallbackInfo ci) {
        if (p_43396_ instanceof Player player) {
            int i = this.getUseDuration(p_43394_, p_43396_) - p_43397_;
            float f = _1211$getPowerForTime(i);
            if (!((double) f < IncineratorsTryHardConfig.astrapeChargingTime.get())) {
                p_43395_.playSound((Player) null, player.getX(), player.getY(), player.getZ(), (SoundEvent) ModSounds.EMP_ACTIVATED.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
                if (!p_43395_.isClientSide) {
                    float d7 = p_43396_.getYRot();
                    float d = p_43396_.getXRot();
                    float d1 = -Mth.sin(d7 * ((float)Math.PI / 180F)) * Mth.cos(d * ((float)Math.PI / 180F));
                    float d2 = -Mth.sin(d * ((float)Math.PI / 180F));
                    float d3 = Mth.cos(d7 * ((float)Math.PI / 180F)) * Mth.cos(d * ((float)Math.PI / 180F));
                    double theta = (double)d7 * (Math.PI / 180D);
                    ++theta;
                    double vecX = Math.cos(theta);
                    double vecZ = Math.sin(theta);
                    double x = p_43396_.getX() + vecX;
                    double y = p_43396_.getY() + (double)(p_43396_.getBbHeight() / 2.0F);
                    double Z = p_43396_.getZ() + vecZ;
                    Vec3 vec3 = (new Vec3((double)d1, (double)d2, (double)d3)).normalize();
                    float yRot = (float)(Mth.atan2(vec3.z, vec3.x) * (180D / Math.PI)) + 90.0F;
                    float xRot = (float)(-(Mth.atan2(vec3.y, Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z)) * (180D / Math.PI)));
                    Lightning_Spear_Entity lightning = new Lightning_Spear_Entity(player, vec3.normalize(), p_43395_, (float)CMConfig.AstrapeDamage);
                    lightning.accelerationPower = IncineratorsTryHardConfig.astrapeAccelerationPower.get();
                    lightning.setYRot(yRot);
                    lightning.setXRot(xRot);
                    lightning.setPosRaw(x, y, Z);
                    lightning.setAreaDamage((float)CMConfig.AstrapeAreaDamage);
                    lightning.setAreaRadius(IncineratorsTryHardConfig.astrapeAreaRadius.get().floatValue());
                    boolean flag = p_43395_.addFreshEntity(lightning);
                    if (flag) {
                        player.getCooldowns().addCooldown(this, CMConfig.AstrapeCooldown);
                    }
                }
            }
        }

ci.cancel();
    }


}

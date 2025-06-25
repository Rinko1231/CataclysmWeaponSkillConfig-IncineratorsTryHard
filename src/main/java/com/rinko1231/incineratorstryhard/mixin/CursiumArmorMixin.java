package com.rinko1231.incineratorstryhard.mixin;


import com.github.L_Ender.cataclysm.init.ModItems;
import com.github.L_Ender.cataclysm.items.Cursed_bow;
import com.github.L_Ender.cataclysm.items.Cursium_Armor;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Cursium_Armor.class, remap = false)
public abstract class CursiumArmorMixin {

    /**
     * @author Rinko1231
     * @reason It's too long
     */
    @Overwrite
    public void onKeyPacket(Player player, ItemStack itemStack, int Type) {
        if (Type == 5 && player != null && !player.getCooldowns().isOnCooldown((Item)ModItems.CURSIUM_HELMET.get())) {
            boolean flag = false;

            for(Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate((double)24.0F))) {
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)entity;
                    flag = true;
                    living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 160));
                }

                if (flag) {
                    player.getCooldowns().addCooldown((Item)ModItems.CURSIUM_HELMET.get(), IncineratorsTryHardConfig.cursiumHelmetCoolDown.get());
                }
            }
        }

        if (Type == 7 && player != null && player.onGround() && !player.getCooldowns().isOnCooldown((Item)ModItems.CURSIUM_BOOTS.get())) {
            float speed = IncineratorsTryHardConfig.cursiumBootsSkillSpeed.get().floatValue();
            float dodgeYaw = (float)Math.toRadians((double)(player.getYRot() + 90.0F));
            Vec3 m = player.getDeltaMovement().add((double)speed * Math.cos((double)dodgeYaw), (double)0.0F, (double)speed * Math.sin((double)dodgeYaw));
            player.setDeltaMovement(m.x, 0.4, m.z);
            player.getCooldowns().addCooldown((Item)ModItems.CURSIUM_BOOTS.get(), IncineratorsTryHardConfig.cursiumBootsCoolDown.get());
        }

    }


}

package com.rinko1231.incineratorstryhard.mixin;


import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Harbinger_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Laser_Beam_Entity;
import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;

import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Laser_Beam_Entity.class, remap = false)
public abstract class LaserBeamEntityMixin extends Projectile {


    protected LaserBeamEntityMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    /**
     * @author Rinko1231
     * @reason Watch out for fire
     */
    @Overwrite
    protected void onHitBlock(@NotNull BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);
        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (entity instanceof Player && !IncineratorsTryHardConfig.laserGatlingIgnite.get())
                return;
            if (CMConfig.HarbingerLightFire) {
                BlockPos blockpos = p_37384_.getBlockPos().relative(p_37384_.getDirection());
                if (this.level().isEmptyBlock(blockpos)) {
                    this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
                }
            } else if (!(entity instanceof Mob) || EventHooks.canEntityGrief(this.level(), entity)) {
                BlockPos blockpos = p_37384_.getBlockPos().relative(p_37384_.getDirection());
                if (this.level().isEmptyBlock(blockpos)) {
                    this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
                }
            }
        }

    }

    @Inject(method = "getInertia", at = @At("HEAD"), cancellable = true)
    private void injectCustomInertia(CallbackInfoReturnable<Float> cir) {
        Entity owner = this.getOwner();
        if (owner != null && !(owner instanceof The_Harbinger_Entity)) {
            cir.setReturnValue(IncineratorsTryHardConfig.laserSpeedMultiplier.get().floatValue());
        } else {
            cir.setReturnValue(1.0f);
        }
    }


}

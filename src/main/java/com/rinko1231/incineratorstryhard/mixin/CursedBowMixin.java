package com.rinko1231.incineratorstryhard.mixin;


import com.github.L_Ender.cataclysm.config.CMCommonConfig;
import com.github.L_Ender.cataclysm.entity.projectile.Phantom_Arrow_Entity;
import com.github.L_Ender.cataclysm.items.Cursed_bow;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Cursed_bow.class, remap = false)
public abstract class CursedBowMixin extends ProjectileWeaponItem {

    public CursedBowMixin(Properties p_43009_) {
        super(p_43009_);
    }


    @Inject(method = "shoot", at= @At("HEAD"),cancellable = true)
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, Entity target, CallbackInfo ci) {
        float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
        float f2 = (float)((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for(int i = 0; i < projectileItems.size(); ++i) {
            ItemStack itemstack = (ItemStack)projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                boolean hommingArrows = itemstack.is(Items.ARROW);
                int arrowcount = itemstack.is(Items.ARROW) ? 3 : 2;
                float offsetangle = itemstack.is(Items.ARROW) ? 12.0F : 3.0F;
                boolean flag1 = shooter.hasInfiniteMaterials() || itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, weapon, shooter);

                for(int j = 0; j < arrowcount; ++j) {
                    AbstractArrow abstractarrow = this.createArrow(level, shooter, weapon, itemstack, isCrit);
                    abstractarrow = this._1211$customArrow(abstractarrow);
                    if (hommingArrows) {
                        label71: {
                            if (target instanceof LivingEntity) {
                                LivingEntity tango = (LivingEntity)target;
                                if (!target.isAlliedTo(shooter)) {
                                    Phantom_Arrow_Entity hommingArrowEntity = new Phantom_Arrow_Entity(level, shooter, tango);
                                    hommingArrowEntity.setBaseDamage(CMCommonConfig.CursedBow.damage * (double)velocity);
                                    abstractarrow = hommingArrowEntity;
                                    break label71;
                                }
                            }

                            Phantom_Arrow_Entity hommingArrowEntity = new Phantom_Arrow_Entity(level, shooter);
                            hommingArrowEntity.setBaseDamage(CMCommonConfig.CursedBow.damage * (double)velocity);
                            abstractarrow = hommingArrowEntity;
                        }
                    } else {
                        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)0.5F);
                    }


                    if (j != 1 || _1211$infinity(shooter,weapon)) {//避免无限附魔刷箭
                        abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    } else if (flag1 || shooter.hasInfiniteMaterials() && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                        abstractarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                    }


                    abstractarrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + ((float)j - (float)(arrowcount - 1) / 2.0F) * offsetangle, 0.0F, velocity * 3.0F, inaccuracy);
                    if (f == 1.0F) {
                        abstractarrow.setCritArrow(true);
                    }

                    level.addFreshEntity(abstractarrow);
                    if (weapon.isEmpty()) {
                        break;
                    }
                }
            }
        }
     ci.cancel();
    }
  @Shadow protected abstract AbstractArrow createArrow(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) ;

    @Unique
    public AbstractArrow _1211$customArrow(AbstractArrow arrow) {
        return arrow;
    }

    @Unique
    public boolean _1211$infinity(LivingEntity player, ItemStack weapon) {
        Holder<Enchantment> infinity = _1211$getHolder(player.level(), Enchantments.INFINITY);
        int level = EnchantmentHelper.getTagEnchantmentLevel(infinity, weapon);
    return level>0;
    }

    @Unique
    private static Holder<Enchantment> _1211$getHolder(Level level, ResourceKey<Enchantment> enchantment) {
        return level.holderLookup(enchantment.registryKey()).getOrThrow(enchantment);
    }


/*
//Mojang玩数据驱动导致的
    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true)
    public void canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (IncineratorsTryHardConfig.cursedBowEnchantmentUnlock.get())
          cir.setReturnValue(enchantment.category == EnchantmentCategory.BOW);
    }
*/

    /*
    @Overwrite
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeleft) {
        if (living instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = player.getProjectile(stack);
            Entity pointedEntity = this.getPlayerLookTarget(level, living);
            int i = this.getUseDuration(stack) - timeleft;
            i = ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) {
                return;
            }

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    boolean flag1 = player.getAbilities().instabuild || itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player);
                    if (!level.isClientSide) {
                        Item arrowcount = itemstack.getItem();
                        ArrowItem var10000;
                        if (arrowcount instanceof ArrowItem) {
                            ArrowItem arrow = (ArrowItem)arrowcount;
                            var10000 = arrow;
                        } else {
                            var10000 = (ArrowItem)Items.ARROW;
                        }

                        ArrowItem arrowItem = var10000;
                        boolean hommingArrows = itemstack.is(Items.ARROW);
                        int arrowcount2 = itemstack.is(Items.ARROW) ? 3 : 2;
                        float offsetangle = itemstack.is(Items.ARROW) ? 12.0F : 3.0F;

                        for(int j = 0; j < arrowcount2; ++j) {
                            AbstractArrow abstractarrow = arrowItem.createArrow(level, itemstack, player);
                            abstractarrow = this.customArrow(abstractarrow);
                            int p = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                            if (hommingArrows) {
                                label148: {
                                    if (pointedEntity instanceof LivingEntity) {
                                        LivingEntity target = (LivingEntity)pointedEntity;
                                        if (!target.isAlliedTo(living)) {
                                            Phantom_Arrow_Entity hommingArrowEntity = new Phantom_Arrow_Entity(level, living, target);
                                            hommingArrowEntity.setBaseDamage(CMConfig.PlayerPhantomArrowbasedamage * (double)f);
                                            if (p > 0) {
                                                hommingArrowEntity.setBaseDamage(hommingArrowEntity.getBaseDamage() + (double)p * 0.35 + (double)0.5F);
                                            }

                                            abstractarrow = hommingArrowEntity;
                                            break label148;
                                        }
                                    }

                                    Phantom_Arrow_Entity hommingArrowEntity = new Phantom_Arrow_Entity(level, living);
                                    hommingArrowEntity.setBaseDamage(CMConfig.PlayerPhantomArrowbasedamage * (double)f);
                                    if (p > 0) {
                                        hommingArrowEntity.setBaseDamage(hommingArrowEntity.getBaseDamage() + (double)p * 0.35 + (double)0.5F);
                                    }

                                    abstractarrow = hommingArrowEntity;
                                }
                            } else if (p > 0) {
                                abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)p * 0.7 + (double)0.5F);
                            }

                            if (j != 1) {
                                abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            } else if (flag1 || player.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                                if (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, player))
                                   abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                else
                                    abstractarrow.pickup = AbstractArrow.Pickup.ALLOWED;

                            }

                            abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot() + ((float)j - (float)(arrowcount2 - 1) / 2.0F) * offsetangle, 0.0F, f * 3.0F, 1.0F);
                            if (f == 1.0F) {
                                abstractarrow.setCritArrow(true);
                            }

                            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                            if (k > 0) {
                                abstractarrow.setKnockback(k);
                            }

                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                                abstractarrow.setSecondsOnFire(100);
                            }

                            level.addFreshEntity(abstractarrow);
                        }
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }*/



}

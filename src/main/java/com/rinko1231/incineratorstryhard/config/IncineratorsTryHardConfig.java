package com.rinko1231.incineratorstryhard.config;


import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;


public class IncineratorsTryHardConfig
{
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.DoubleValue maxHealthDamagePercent;
    public static ModConfigSpec.DoubleValue basicSkillDamage;
    public static ModConfigSpec.DoubleValue ignisCircleRadius;
    public static ModConfigSpec.IntValue chargingTime;
    public static ModConfigSpec.IntValue chargingTimeToMaxCircle;

    public static ModConfigSpec.DoubleValue maxHealthDamagePercentForImmolator;
    public static ModConfigSpec.DoubleValue basicSkillDamageForImmolator;
    public static ModConfigSpec.DoubleValue ignisCircleRadiusForImmolator;
    public static ModConfigSpec.IntValue chargingTimeForImmolator;
    public static ModConfigSpec.IntValue chargingTimeToMaxCircleForImmolator;
    public static ModConfigSpec.DoubleValue quakeRangeForImmolator;
    public static ModConfigSpec.DoubleValue quakeDamageMultiplierForImmolator;

    public static ModConfigSpec.DoubleValue TidalClawsTentacleDamage;
    public static ModConfigSpec.DoubleValue TidalClawsTentacleFirstRange;
    public static ModConfigSpec.DoubleValue TidalClawsTentacleSecondRange;
    public static ModConfigSpec.DoubleValue TidalClawsHookMaxRange;
    public static ModConfigSpec.DoubleValue TidalClawsHookMaxSpeed;

    public static ModConfigSpec.DoubleValue ChargeDamageMultiplierOfGauntletOfBulwark;

    public static ModConfigSpec.IntValue coolDownForBloomStonePauldrons;

    public static ModConfigSpec.DoubleValue annihilatorSkillRange;
    public static ModConfigSpec.DoubleValue annihilatorSkillDamageMultiplier;
    public static ModConfigSpec.IntValue annihilatorChargingTime;
    public static ModConfigSpec.IntValue annihilatorSkillCoolDown;

    public static ModConfigSpec.DoubleValue gauntletOfGuardSkillRange;
    public static ModConfigSpec.DoubleValue GauntletOfGuardSkillVectorScale;

    public static ModConfigSpec.BooleanValue cursedBowEnchantmentUnlock;

    public static ModConfigSpec.IntValue cursiumHelmetCoolDown;
    public static ModConfigSpec.IntValue cursiumBootsCoolDown;
    public static ModConfigSpec.DoubleValue cursiumBootsSkillSpeed;

    public static ModConfigSpec.IntValue ignitiumHelmetCoolDown;

    public static ModConfigSpec.BooleanValue laserGatlingIgnite;

    static
    {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        BUILDER.translation("config.incineratorstryhard.config").push("Cataclysm's Weapon Config");
        BUILDER.comment("Incinerator's Try Hard Config");

        BUILDER.push("The_Incinerator");

        chargingTime = BUILDER
                .translation("description.cataclysm_weapon_config.incinerator_charging_time")
                .defineInRange("Charging time (ticks) of The Incinerator", 60,1,100);

        chargingTimeToMaxCircle = BUILDER
                .translation("description.cataclysm_weapon_config.incinerator_charging_time_to_max_circle")
                .defineInRange("(Client Only) Max Charging time (ticks) to Form the Biggest Magic Circle underfoot when charging", 60,1,100);

        basicSkillDamage = BUILDER
                .translation("description.cataclysm_weapon_config.incinerator_basic_skill_damage")
                .defineInRange("Basic Damage dealt by The Incinerator's Skill", 6.0,0.0, Double.MAX_VALUE);

        maxHealthDamagePercent = BUILDER
                .translation("description.cataclysm_weapon_config.incinerator_max_health_damage_percent")
                .defineInRange("Max Health Damage Percentage dealt by The Incinerator's Skill", 2.0,0.0,100);

        ignisCircleRadius = BUILDER
                .translation("description.cataclysm_weapon_config.incinerator_ignis_circle_radius")
                .defineInRange("The Radius of the ignis circles released by The Incinerator's Skill", 1.0,0.0,100);

        BUILDER.pop();

        BUILDER.push("The_Immolator");

        chargingTimeForImmolator = BUILDER
                .translation("description.cataclysm_weapon_config.immolator_charging_time")
                .defineInRange("Charging time (ticks) of The Immolator", 45,1,100);

        chargingTimeToMaxCircleForImmolator = BUILDER
                .translation("description.cataclysm_weapon_config.immolator_charging_time_to_max_circle")
                .defineInRange("(Client Only) Max Charging time (ticks) to Form the Biggest Magic Circle underfoot when charging", 45,1,100);

        basicSkillDamageForImmolator = BUILDER
                .translation("description.cataclysm_weapon_config.immolator_basic_skill_damage")
                .defineInRange("Basic Ignis Circle Damage dealt by The Immolator's Skill", 6.0,0.0, Double.MAX_VALUE);

        maxHealthDamagePercentForImmolator = BUILDER
                .translation("description.cataclysm_weapon_config.immolator_max_health_damage_percent")
                .defineInRange("Max Health Damage Percentage dealt by The Immolator's Skill", 2.0,0.0,100);

        ignisCircleRadiusForImmolator = BUILDER
                .translation("description.cataclysm_weapon_config.immolator_ignis_circle_radius")
                .defineInRange("The Radius of the ignis circles released by The Immolator's Skill", 2.5,0.0,100);

        quakeRangeForImmolator= BUILDER
                .defineInRange("[Questionable, maybe unused method] The Radius of the Quake released by The Immolator's Skill", 6.0,0.01,250);

        quakeDamageMultiplierForImmolator = BUILDER
                .defineInRange("[Questionable, maybe unused method] Quake Damage Multiplier of The Immolator's Skill", 2.0,0.01, Double.MAX_VALUE);


        BUILDER.pop();

        BUILDER.push("Tidal_Claws");

        TidalClawsTentacleDamage = BUILDER
                .translation("description.cataclysm_weapon_config.tidal_claws_tentacle_damage")
                .defineInRange("Tentacle Damage released by Tidal Claws", 3.0,0.0, Double.MAX_VALUE);

        TidalClawsTentacleFirstRange = BUILDER
                .translation("description.cataclysm_weapon_config.tidal_claws_tentacle_first_range")
                .defineInRange("Tentacle's First Detection Range released by Tidal Claws", 16.0,1.0, Double.MAX_VALUE);

        TidalClawsTentacleSecondRange = BUILDER
                .translation("description.cataclysm_weapon_config.tidal_claws_tentacle_second_range")
                .defineInRange("Tentacle's Second Detection Range released by Tidal Claws", 16.0,1.0, Double.MAX_VALUE);

        TidalClawsHookMaxRange = BUILDER
                .translation("description.cataclysm_weapon_config.tidal_claws_hook_max_range")
                .defineInRange("Hook's Max Range released by Tidal Claws", 30.0,0.01, Double.MAX_VALUE);

        TidalClawsHookMaxSpeed = BUILDER
                .translation("description.cataclysm_weapon_config.tidal_claws_hook_max_speed")
                .defineInRange("Hook's Max Speed released by Tidal Claws", 12.0,0.01, Double.MAX_VALUE);



        BUILDER.pop();

        BUILDER.push("Gauntlet_of_Bulwark");

        ChargeDamageMultiplierOfGauntletOfBulwark = BUILDER
                .translation("description.cataclysm_weapon_config.charging_damage_multiplier_bulwark")
                .defineInRange("Charge Damage Multiplier Of Gauntlet Of Bulwark", 1.2,1.0, Double.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("Bloom_Stone_Pauldrons");

        coolDownForBloomStonePauldrons = BUILDER
                .translation("description.cataclysm_weapon_config.cooldown_bloom_stone_pauldrons")
                .defineInRange("Cooldown time (ticks) of Bloom Stone Pauldrons to shoot Amethyst Cluster", 240,1,Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("The_Annihilator");

        annihilatorSkillRange= BUILDER
                .translation("description.cataclysm_weapon_config.annihilator_skill_range")
                .defineInRange("Skill Range Of The Annihilator", 6,0.01, Double.MAX_VALUE);

        annihilatorSkillDamageMultiplier= BUILDER
                .translation("description.cataclysm_weapon_config.annihilator_skill_damage_multiplier")
                .defineInRange("Skill Damage Multiplier Of The Annihilator", 2,0.01, Double.MAX_VALUE);

        annihilatorChargingTime = BUILDER
                .translation("description.cataclysm_weapon_config.annihilator_charging_time")
                .defineInRange("Charging time (ticks) of The Annihilator", 40,1,1000);

        annihilatorSkillCoolDown = BUILDER
                .translation("description.cataclysm_weapon_config.annihilator_skill_cooldown")
                .defineInRange("Skill Cooldown time (ticks) of The Annihilator", 100,1,Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("Gauntlet_of_Guard");

        gauntletOfGuardSkillRange = BUILDER
                .translation("description.cataclysm_weapon_config.gauntlet_of_guard_skill_range")
                .defineInRange("Attraction Radius Of Gauntlet Of Guard", 11,0.01, Double.MAX_VALUE);

        GauntletOfGuardSkillVectorScale = BUILDER
                .translation("description.cataclysm_weapon_config.gauntlet_of_guard_skill_vector_scale")
                .defineInRange("Scale of Normalized Attraction Vector Of Gauntlet Of Bulwark", 0.1,0.01, Double.MAX_VALUE);


        BUILDER.pop();

        BUILDER.push("Cursed_Bow");

        cursedBowEnchantmentUnlock =BUILDER
                .translation("description.cataclysm_weapon_config.cursed_bow")
                .translation("description.cataclysm_weapon_config.cursed_bow_broken")
                .define("Whether Cursed Bow can be enchanted on Enchantment Table, and with Infinity and Flaming Arrow",true);

        BUILDER.pop();

        BUILDER.push("Cursium_Armor");

        cursiumHelmetCoolDown = BUILDER
                .translation("description.cataclysm_weapon_config.cursium_helmet_cooldown")
                .defineInRange("Skill Cooldown time (ticks) of Cursium Helmet", 200,1,Integer.MAX_VALUE);

        cursiumBootsCoolDown = BUILDER
                .translation("description.cataclysm_weapon_config.cursium_boots_cooldown")
                .defineInRange("Skill Cooldown time (ticks) of Cursium Boots", 200,1,Integer.MAX_VALUE);

        cursiumBootsSkillSpeed= BUILDER
                .translation("description.cataclysm_weapon_config.cursium_boots_skill_speed")
                .defineInRange("Skill Speed of Cursium Boots", -1.800F, -114514, 114514);

        BUILDER.pop();

        BUILDER.push("Ignitium_Armor");

        ignitiumHelmetCoolDown = BUILDER
                .translation("description.cataclysm_weapon_config.ignitium_helmet_cooldown")
                .defineInRange("Skill Cooldown time (ticks) of Ignitium Helmet", 300,1,Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("Laser_Gatling");

        laserGatlingIgnite =BUILDER
                .translation("description.cataclysm_weapon_config.laser_gatling_ignite")
                .define("Whether Laser Gatling can ignite blocks when used by the player", true);

        SPEC = BUILDER.build();
    }



}
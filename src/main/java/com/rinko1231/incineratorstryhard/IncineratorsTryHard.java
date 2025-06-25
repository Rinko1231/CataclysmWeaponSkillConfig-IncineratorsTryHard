package com.rinko1231.incineratorstryhard;

import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;


@Mod(IncineratorsTryHard.MODID)
public class IncineratorsTryHard
{
    public static final String MODID = "incineratorstryhard";


    public IncineratorsTryHard(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, IncineratorsTryHardConfig.SPEC,"IncineratorsTryHard.toml");
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

}

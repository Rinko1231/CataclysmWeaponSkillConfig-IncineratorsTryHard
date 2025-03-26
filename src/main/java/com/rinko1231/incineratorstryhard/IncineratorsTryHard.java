package com.rinko1231.incineratorstryhard;

import com.rinko1231.incineratorstryhard.config.IncineratorsTryHardConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(IncineratorsTryHard.MODID)
public class IncineratorsTryHard
{
    public static final String MODID = "incineratorstryhard";

    public IncineratorsTryHard()
    {
        IncineratorsTryHardConfig.setup();
        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Player joined: " + event.getEntity().getName());
    }
}

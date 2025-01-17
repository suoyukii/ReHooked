package com.oe.rehooked;

import com.mojang.logging.LogUtils;
import com.oe.rehooked.entities.ReHookedEntities;
import com.oe.rehooked.item.ReHookedItems;
import com.oe.rehooked.network.handlers.PacketHandler;
import com.oe.rehooked.network.packets.client.CHookCapabilityPacket;
import com.oe.rehooked.network.packets.client.processing.CHookCapabilityProcessor;
import com.oe.rehooked.particle.ReHookedParticles;
import com.oe.rehooked.sound.ReHookedSounds;
import com.oe.rehooked.tabs.ReHookedCreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ReHookedMod.MOD_ID)
public class ReHookedMod {
    public static final String MOD_ID = "rehooked";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ReHookedMod() {
        LOGGER.info("ReHooked started initializing...");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register packets
        PacketHandler.register();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> 
                () -> PacketHandler.addHandler(CHookCapabilityPacket.class, CHookCapabilityProcessor::handle));

        // register mod sounds
        ReHookedSounds.register(modEventBus);
        // register mod particles
        ReHookedParticles.register(modEventBus);
        // Register mod entities
        ReHookedEntities.register(modEventBus);
        // Register mod creative tab
        ReHookedCreativeModeTab.register(modEventBus);
        // Register mod items
        ReHookedItems.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        LOGGER.info("ReHooked finished registration.");
    }
}

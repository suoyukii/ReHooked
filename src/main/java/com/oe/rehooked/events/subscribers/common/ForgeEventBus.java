package com.oe.rehooked.events.subscribers.common;

import com.oe.rehooked.ReHookedMod;
import com.oe.rehooked.capabilities.hooks.IPlayerHookHandler;
import com.oe.rehooked.capabilities.hooks.PlayerHookCapabilityProvider;
import com.oe.rehooked.network.handlers.PacketHandler;
import com.oe.rehooked.network.packets.client.CPushPlayerPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ReHookedMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBus {
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.getOriginal().level().isClientSide()) return;
        // revive old capabilities
        event.getOriginal().reviveCaps();
        // get the provider
        LazyOptional<IPlayerHookHandler> hookCap = IPlayerHookHandler.FromPlayer(event.getOriginal());
        // in case of death copy the old data to the new capability
        if (event.isWasDeath()) {
            hookCap.ifPresent(oldStore ->
                    IPlayerHookHandler.FromPlayer(event.getEntity())
                            .ifPresent(newStore -> newStore.copyFrom(oldStore)));
        }
        // invalidate old capabilities
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().level().isClientSide()) return;
        if (event.getObject() instanceof Player) {
            if (!IPlayerHookHandler.FromPlayer((Player) event.getObject()).isPresent()) {
                event.addCapability(new ResourceLocation(ReHookedMod.MOD_ID, "properties"),
                        new PlayerHookCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        IPlayerHookHandler.FromPlayer(event.getEntity()).ifPresent(IPlayerHookHandler::removeAllHooks);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        event.getServer().getPlayerList().getPlayers().forEach(player -> {
            IPlayerHookHandler.FromPlayer(player).ifPresent(handler -> {
                if (handler.shouldMoveThisTick()) {
                    player.setDeltaMovement(handler.getMoveThisTick());
                    PacketHandler.sendToPlayer(new CPushPlayerPacket(handler.getMoveThisTick()), player);
                }
            });
        });
    }
}

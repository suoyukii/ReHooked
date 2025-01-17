package com.oe.rehooked.network.packets.client;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.slf4j.Logger;

public class CHookCapabilityPacket {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public final State packetType;
    public final int id;
    
    public CHookCapabilityPacket(State packetType, int id) {
        this.packetType = packetType;
        this.id = id;
    }

    public CHookCapabilityPacket(State packetType) {
        this(packetType, 0);
    }

    public CHookCapabilityPacket(FriendlyByteBuf buf) {
        packetType = State.Get(buf.readInt());
        id = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(packetType.ordinal());
        buf.writeInt(id);
    }
    
    public enum State {
        ADD_HOOK,
        RETRACT_HOOK,
        RETRACT_ALL_HOOKS,
        FORCE_UPDATE;
        
        public static State Get(int ordinal) {
            return State.values()[ordinal];
        }
    }
}

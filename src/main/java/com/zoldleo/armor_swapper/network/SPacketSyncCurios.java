package com.zoldleo.armor_swapper.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class SPacketSyncCurios {

    private int entityId;
    private CompoundTag tag;

    public SPacketSyncCurios(int entityId, CompoundTag tag) {
        this.entityId = entityId;
        this.tag = tag;
    }

    public SPacketSyncCurios(FriendlyByteBuf buff) {
        this.entityId = buff.readInt();
        this.tag = buff.readNbt();
    }

    public static void encode(SPacketSyncCurios msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeNbt(msg.tag);
    }

    public static void handle(SPacketSyncCurios msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;

            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);
                if (entity instanceof LivingEntity living) {
                    CuriosApi.getCuriosHelper().getCuriosHandler(living).ifPresent(handler -> {
                        handler.readTag(msg.tag);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
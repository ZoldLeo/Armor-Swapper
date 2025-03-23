package com.zoldleo.armor_swapper.network;

import com.zoldleo.armor_swapper.ArmorSwapperMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(ArmorSwapperMod.MOD_ID, "main"), () -> "1", (version) -> true, (version) -> true);

    public static void register() {
        INSTANCE.messageBuilder(SSetSwapperConfigPacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
        .encoder(SSetSwapperConfigPacket::encode)
        .decoder(SSetSwapperConfigPacket::new)
        .consumer(SSetSwapperConfigPacket::handle)
        .add();
        
        INSTANCE.messageBuilder(SPacketSyncCurios.class, 1, NetworkDirection.PLAY_TO_CLIENT)
        .encoder(SPacketSyncCurios::encode)
        .decoder(SPacketSyncCurios::new)
        .consumer(SPacketSyncCurios::handle)
        .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendToClient(Player player, Object msg) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), msg);
    }
}
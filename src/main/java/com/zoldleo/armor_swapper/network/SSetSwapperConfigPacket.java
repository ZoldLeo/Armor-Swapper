package com.zoldleo.armor_swapper.network;

import java.util.function.Supplier;
import java.nio.charset.Charset;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SSetSwapperConfigPacket {
    private final String flagData;
    private final boolean onoffData;

    public SSetSwapperConfigPacket(String flag, boolean onoff) {
        flagData = flag;
        onoffData = onoff;
    }

    public SSetSwapperConfigPacket(FriendlyByteBuf buff) {
        int length = buff.readInt();
        flagData = buff.readCharSequence(length, Charset.forName("UTF-8")).toString();
        onoffData = buff.readBoolean();
    }

    public void encode(FriendlyByteBuf buff) {
        buff.writeInt(flagData.length());
        buff.writeCharSequence(flagData, Charset.forName("UTF-8"));
        buff.writeBoolean(onoffData);
    }

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if(!stack.isEmpty()) {
                    if (onoffData) {
                        stack.getOrCreateTag().putBoolean(flagData, true);
                    } else {
                        stack.getOrCreateTag().remove(flagData);
                    }
                }
            }
		});
		ctx.get().setPacketHandled(true);
	}
}
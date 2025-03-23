package com.zoldleo.armor_swapper.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zoldleo.armor_swapper.item.ArmorSwapperInventory;
import com.zoldleo.armor_swapper.network.PacketHandler;
import com.zoldleo.armor_swapper.network.SSetSwapperConfigPacket;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.ModList;

@OnlyIn(Dist.CLIENT)
public class SwapperOptionsScreen extends Screen {

	private final ArmorSwapperInventory inv;
    private boolean storeArmor;
    private boolean storeOffhand;
    private boolean storeCurios;

	public SwapperOptionsScreen(ItemStack stack) {
		super(new TextComponent("Armor Swapper Settings"));
        inv = new ArmorSwapperInventory(stack, 5);
        storeArmor = !inv.getFlag("DontStoreArmor");
        storeOffhand = !inv.getFlag("DontStoreOffhand");
        storeCurios = !inv.getFlag("DontStoreCurios");
	}

	@Override
	protected void init() {
        boolean haveCuriosMod = ModList.get().isLoaded("curios");
        int armorHeight = haveCuriosMod ? (height-18)/2-22 : height/2-20;
        int offhandHeight = haveCuriosMod ? (height-18)/2 : height/2+2;

        TextComponent ArmorText = storeArmor ? new TextComponent("Store Armor: On") : new TextComponent("Store Armor: Off");
        addRenderableWidget(new ExtendedButton((width-200)/2, armorHeight, 200, 18, ArmorText, button -> {
            storeArmor = !storeArmor;
            if (storeArmor) {
                button.setMessage(new TextComponent("Store Armor: On"));
                PacketHandler.sendToServer(new SSetSwapperConfigPacket("DontStoreArmor", false));
            } else {
                button.setMessage(new TextComponent("Store Armor: Off"));
                PacketHandler.sendToServer(new SSetSwapperConfigPacket("DontStoreArmor", true));
            }
        }));
        TextComponent offhandText = storeOffhand ? new TextComponent("Store Offhand: On") : new TextComponent("Store Offhand: Off");
        addRenderableWidget(new ExtendedButton((width-200)/2, offhandHeight, 200, 18, offhandText, button -> {
            storeOffhand = !storeOffhand;
            if (storeOffhand) {
                button.setMessage(new TextComponent("Store Offhand: On"));
                PacketHandler.sendToServer(new SSetSwapperConfigPacket("DontStoreOffhand", false));
            } else {
                button.setMessage(new TextComponent("Store Offhand: Off"));
                PacketHandler.sendToServer(new SSetSwapperConfigPacket("DontStoreOffhand", true));
            }
        }));
        if (!haveCuriosMod) {
            return;
        }
        TextComponent curiosText = storeCurios ? new TextComponent("Store Curios: On") : new TextComponent("Store Curios: Off");
        addRenderableWidget(new ExtendedButton((width-200)/2, (height-18)/2+22, 200, 18, curiosText, button -> {
            storeCurios = !storeCurios;
            if (storeCurios) {
                button.setMessage(new TextComponent("Store Curios: On"));
                PacketHandler.sendToServer(new SSetSwapperConfigPacket("DontStoreCurios", false));
            } else {
                button.setMessage(new TextComponent("Store Curios: Off"));
                PacketHandler.sendToServer(new SSetSwapperConfigPacket("DontStoreCurios", true));
            }
        }));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(poseStack);
        drawCenteredString(poseStack, font, I18n.get("Armor Swapper Settings"), width/2, height/2-96, 0xffffff);
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

    //@SuppressWarnings("null")
    @Override
    public boolean keyPressed(int p_97765_, int p_97766_, int p_97767_) {
        InputConstants.Key mouseKey = InputConstants.getKey(p_97765_, p_97766_);
        if (super.keyPressed(p_97765_, p_97766_, p_97767_)) {
            return true;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return false;
    }

	@Override
	public void onClose() {
		super.onClose();
	}

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
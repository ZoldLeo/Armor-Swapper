package com.zoldleo.armor_swapper;

import com.zoldleo.armor_swapper.client.ModColorHandler;
import com.zoldleo.armor_swapper.init.EntityInit;
import com.zoldleo.armor_swapper.init.ItemInit;
import com.zoldleo.armor_swapper.init.RecipeInit;
import com.zoldleo.armor_swapper.item.ArmorSwapperItem;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArmorSwapperMod.MOD_ID)
public class ArmorSwapperMod {
    public static final String MOD_ID = "armor_swapper";

    public ArmorSwapperMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        RecipeInit.SERIALIZERS.register(bus);

        bus.addListener(ModColorHandler::registerItemColor);
        bus.addListener(this::clientSetup);
        bus.addListener(ArmorSwapperMod::registerEntityRenderers);
        
        MinecraftForge.EVENT_BUS.register(ArmorSwapperMod.class);
    }

    @SuppressWarnings("unused")
    private void clientSetup(final FMLClientSetupEvent event) {
        ItemProperties.register( ItemInit.ARMOR_SWAPPER.get(), new ResourceLocation("has_items"),
            (itemStack, clientLevel, livingEntity, numberArg) -> ArmorSwapperItem.hasItems(itemStack));
    }

	private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.SWAPPER_ITEM_ENTITY.get(), ItemEntityRenderer::new);
	}
}
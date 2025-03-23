package com.zoldleo.armor_swapper.client;

import com.zoldleo.armor_swapper.init.ItemInit;
import com.zoldleo.armor_swapper.item.ArmorSwapperItem;

import net.minecraftforge.client.event.ColorHandlerEvent;

public class ModColorHandler {
    public static void registerItemColor(ColorHandlerEvent.Item event) {
        event.getItemColors().register(ArmorSwapperItem::getItemColor, ItemInit.ARMOR_SWAPPER.get());
    }
}

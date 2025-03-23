package com.zoldleo.armor_swapper.init;

import com.google.common.base.Supplier;
import com.zoldleo.armor_swapper.ArmorSwapperMod;
import com.zoldleo.armor_swapper.item.ArmorSwapperItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorSwapperMod.MOD_ID);

    public static final RegistryObject<Item> ARMOR_SWAPPER = register("armor_swapper", () -> new ArmorSwapperItem(new Item
    .Properties()
    .tab(CreativeModeTab.TAB_COMBAT)
    .stacksTo(1)
    ));

    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }

}

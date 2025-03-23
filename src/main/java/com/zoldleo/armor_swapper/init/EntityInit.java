package com.zoldleo.armor_swapper.init;

import com.zoldleo.armor_swapper.ArmorSwapperMod;
import com.zoldleo.armor_swapper.item.ArmorSwapperEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ArmorSwapperMod.MOD_ID);

	public static final RegistryObject<EntityType<ArmorSwapperEntity>> SWAPPER_ITEM_ENTITY = ENTITIES.register(
			"armor_swapper_item", () -> EntityType.Builder.of(ArmorSwapperEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).build(""));
}

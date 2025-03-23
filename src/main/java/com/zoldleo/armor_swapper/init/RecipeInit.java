package com.zoldleo.armor_swapper.init;

import com.zoldleo.armor_swapper.ArmorSwapperMod;
import com.zoldleo.armor_swapper.recipes.CraftSwapperRecipe;
import com.zoldleo.armor_swapper.recipes.RecolorSwapperRecipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
                DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArmorSwapperMod.MOD_ID);

        public static final RegistryObject<RecipeSerializer<RecolorSwapperRecipe>> RECOLOR_SWAPPER_SERIALIZER =
                SERIALIZERS.register("recolor_armor_swapper", () -> RecolorSwapperRecipe.Serializer.INSTANCE);

        public static final RegistryObject<RecipeSerializer<CraftSwapperRecipe>> CRAFT_SWAPPER_SERIALIZER =
                SERIALIZERS.register("craft_armor_swapper", () -> CraftSwapperRecipe.Serializer.INSTANCE);
}

package com.zoldleo.armor_swapper.datagen;

import net.minecraft.core.NonNullList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

import com.zoldleo.armor_swapper.ArmorSwapperMod;
import com.zoldleo.armor_swapper.recipes.RecolorSwapperRecipe;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

	@Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        for (DyeColor color : DyeColor.values()) {
            NonNullList<Ingredient> list = NonNullList.create();
            RecolorSwapperRecipe recipe = new RecolorSwapperRecipe(new ResourceLocation(ArmorSwapperMod.MOD_ID, "recolor_armor_swapper_" + color.toString()), ArmorSwapperMod.MOD_ID + ".item.color", ItemStack.EMPTY, list, color.getId());
            DyedRecipeBuilder builder = new DyedRecipeBuilder(recipe, color);
            builder.build(consumer);
        }
    }
}
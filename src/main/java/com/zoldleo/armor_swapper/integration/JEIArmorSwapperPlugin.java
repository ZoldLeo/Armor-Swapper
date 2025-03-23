package com.zoldleo.armor_swapper.integration;

import com.zoldleo.armor_swapper.ArmorSwapperMod;
import com.zoldleo.armor_swapper.init.ItemInit;
import com.zoldleo.armor_swapper.recipes.CraftSwapperRecipe;
import com.zoldleo.armor_swapper.recipes.RecolorSwapperRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

public class JEIArmorSwapperPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ArmorSwapperMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CraftSwapperRecipe> craftR = new ArrayList<CraftSwapperRecipe>();
        craftR.add((CraftSwapperRecipe) rm.byKey(new ResourceLocation("armor_swapper:armor_swapper")).get());
        List<Recipe<?>> dyeR = rm.getRecipes().stream().filter((recipe) -> recipe instanceof RecolorSwapperRecipe).collect(Collectors.toList());
        registration.addRecipes(new RecipeType<>(new ResourceLocation("minecraft:crafting"), CraftSwapperRecipe.class), craftR);
        registration.addRecipes(new RecipeType<>(new ResourceLocation("minecraft:crafting"), RecolorSwapperRecipe.class), dyeR);
    }
    
    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ItemInit.ARMOR_SWAPPER.get());
    }
}
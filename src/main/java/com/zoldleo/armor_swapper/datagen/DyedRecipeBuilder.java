package com.zoldleo.armor_swapper.datagen;

//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.zoldleo.armor_swapper.init.RecipeInit;
import com.zoldleo.armor_swapper.recipes.RecolorSwapperRecipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
//import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.DyeColor;

public class DyedRecipeBuilder  {
    //private static final Map<RecipeType<?>, Set<ResourceLocation>> builtRecipes = new HashMap<>();
    private final RecolorSwapperRecipe recipe;
    private final DyeColor color;

    public DyedRecipeBuilder(RecolorSwapperRecipe recipe, DyeColor color) {
        this.recipe = recipe;
        this.color = color;
    }
    
    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, null);
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String directory) {
        String saveId = recipe.getId().getPath();
        if (directory != null) {
            saveId = directory + "/" + saveId;
        }
        //saveId = this.getSerializer().getRegistryName().getPath() + "/" + saveId;
        ResourceLocation id = new ResourceLocation(recipe.getId().getNamespace(), saveId);

        /*if (!builtRecipes.computeIfAbsent(recipe.getType(), type -> new HashSet<>()).add(id)) {
            throw new IllegalArgumentException("Tried to register recipe with id " + id + " twice for type " + Registry.RECIPE_TYPE.getKey(recipe.getType()));
        }*/
        consumerIn.accept(new WrappedCustomRecipe(recipe, id, color));
    }

    protected RecipeSerializer<RecolorSwapperRecipe> getSerializer() {
        return RecipeInit.RECOLOR_SWAPPER_SERIALIZER.get();
    }

    private class WrappedCustomRecipe implements FinishedRecipe {
        private final RecolorSwapperRecipe recipe;
        private final ResourceLocation id;
        private final DyeColor color;

        private WrappedCustomRecipe(RecolorSwapperRecipe recipe, ResourceLocation id, DyeColor color) {
            this.recipe = recipe;
            this.id = id;
            this.color = color;
        }

        @Override
        public void serializeRecipeData(@Nonnull JsonObject json) {
            json.addProperty("color", color.getId());
        }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject json = new JsonObject();
            String str = RecolorSwapperRecipe.NAME.toString();
            json.addProperty("type", str);
            serializeRecipeData(json);
            return json;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.recipe.getSerializer();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}